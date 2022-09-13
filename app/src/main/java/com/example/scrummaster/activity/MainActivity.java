package com.example.scrummaster.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;


import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.Phrase;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.example.scrummaster.R;

import com.google.gson.Gson;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class  MainActivity extends RobotActivity implements RobotLifecycleCallbacks {

    Button btn_scan;
    Button btn_auswahlmnu;
    ArrayList <String> teilnehmerliste = new ArrayList<String>();
    Phrase scanOderAuswahl = new Phrase("Hallo, du kannst entweder deinen Code scannen,oder ins Auswahlmenü wechseln. Was möchtest du machen?");



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);
        setContentView(R.layout.activity_main);
        btn_scan =findViewById(R.id.btn_scan);
        btn_auswahlmnu= findViewById(R.id.btn_auswahlmnu);
        //Beim klicken auf den Button "Scan" wird der BarcodeScanner geöffnet
        btn_scan.setOnClickListener(v -> {scanCode();

        });
        //Beim klicken auf den Button "Auswahlmenü" wechselt die View zum Auswahlmenü, und die
        //Teilnehmerliste wird in Gitlab geposted


        btn_auswahlmnu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            startActivity(new Intent(MainActivity.this,AuswahlmenueActivity.class));

            }
        });


    }

        private void scanCode() {
            ScanOptions options =  new ScanOptions();
            options.setPrompt("Herzlich Willkommen");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            options.setCaptureActivity(CaptureAct.class);
            barLauncher.launch(options);
    }

    //Prüfen ob etwas vom scanner returend wurde,wenn ja wechseln in WillkommenActivity, gescannter Name wird per Intent übertragen
    // an WillkommenActivity
    ActivityResultLauncher<ScanOptions> barLauncher =registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {

            result.getContents();
            //saveTeilnehmerListe(result.getContents());

            startActivity(new Intent(MainActivity.this, WillkommenActivity.class));
            //Mit dem Intent wird der gescannte Name an die WillkommenActivity übergeben, damit man
            //dort den Teilnehmer persönlich begrüßen kann
            Intent intent = new Intent (MainActivity.this,WillkommenActivity.class);
            intent.putExtra("teilnehmer",result.getContents());
            startActivity(intent);

            //Wenn die Liste bis zum schließen der App behalten werden soll dann muss das Shae´red Preferences hier mit rein
            //wenn man die Liste jedoch neu erschaffen möchte sobald man in die MainActivity kommt, dann muss die unere Methode
            //saveTeilnehmerliste benutzt werden und in Zeile 82 entkommentieren
           SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared preferences",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            teilnehmerliste.add(result.getContents());
            String json = gson.toJson(teilnehmerliste);
            editor.putString("teilnehmerListe",json);
            editor.apply();

        }
    });

    //Methode speichert die Teilnehmerliste
       private void saveTeilnehmerListe (String teilnehmer){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        teilnehmerliste.add(teilnehmer);
        String json = gson.toJson(teilnehmerliste);
        editor.putString("teilnehmerListe",json);
        editor.apply();

      }


    @Override
    protected void onDestroy() {
        setContentView(R.layout.activity_main);
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        //Auswahlfrage
        Say say = SayBuilder.with(qiContext)
                .withPhrase(scanOderAuswahl)
                .build();
        say.run();
        //Phraseset für Scannen
        PhraseSet scannen= PhraseSetBuilder.with(qiContext)
                .withTexts("Code scannen", "scannen")
                .build();

        //Phraseset für Auswahlmenü
        PhraseSet auswahlmnu= PhraseSetBuilder.with(qiContext)
                .withTexts("Auswahlmenü", " Starte Auswahlmenü", "Menü")
                .build();

        Listen listen = ListenBuilder.with(qiContext)
                .withPhraseSets(scannen)
                .withPhraseSets(auswahlmnu)
                .build();
        ListenResult listenresult= listen.run();

        // Das Gesagte in String umwandeln
        String result = listenresult.getHeardPhrase().toString();

        //Jenachdem was gesagt wurde wird die entsprechende Activity gestartet
        if (scannen.getPhrases().toString().contains(result) ) {
            startActivity(new Intent(MainActivity.this,CaptureAct.class));}
        if (auswahlmnu.getPhrases().toString().contains(result)) {
            startActivity(new Intent(MainActivity.this,AuswahlmenueActivity.class));}

    }

    @Override
    public void onRobotFocusLost() {
        // The robot focus is lost.
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused.
    }
}
