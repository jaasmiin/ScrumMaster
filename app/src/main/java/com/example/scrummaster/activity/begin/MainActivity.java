package com.example.scrummaster.activity.begin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

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

import java.util.ArrayList;

public class  MainActivity extends RobotActivity implements RobotLifecycleCallbacks {

    ImageButton btn_scan;
    ImageButton btn_selectionmnu;

    Phrase scanOrSelect = new Phrase("Hallo, du kannst entweder deinen Code scannen,oder ins Menü wechseln. Was möchtest du machen?");



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);
        setContentView(R.layout.activity_main);
        btn_scan =findViewById(R.id.btn_scan);
        btn_selectionmnu= findViewById(R.id.btn_selectionmnu);






    }

        private void scanCode() {
            ScanOptions options =  new ScanOptions();
            options.setPrompt("Herzlich Willkommen");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            options.setCaptureActivity(CaptureAct.class);
            barLauncher.launch(options);
    }

    //Prüfen ob etwas vom scanner zurückgegeben wurde,wenn ja wechseln in WelcomeActivity, gescannter Name wird per Intent übertragen
    // an WelcomeActivity
    ActivityResultLauncher<ScanOptions> barLauncher =registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            result.getContents();

          //  saveParticipantList(result.getContents());

            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            //Mit dem Intent wird der gescannte Name an die WelcomeActivity übergeben, damit man
            //dort den Teilnehmerpersönlich begrüßen kann
            Intent intent = new Intent (MainActivity.this, WelcomeActivity.class);
            intent.putExtra("participant",result.getContents());
            startActivity(intent);

            //Wenn die Liste bis zum schließen der App behalten werden soll dann muss das Shae´red Preferences hier mit rein
            //wenn man die Liste jedoch neu erschaffen möchte sobald man in die MainActivity kommt, dann muss die unere Methode
            //saveTeilnehmerliste benutzt werden und in Zeile 82 entkommentieren
          /* SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared preferences",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();

           if (!result.getContents().contains("Gast")){
            participantList.add(result.getContents());
            String json = gson.toJson(participantList);
            editor.putString("participantList",json);
            editor.apply();}*/

        }
    });

    //Methode speichert die Teilnehmerliste
       private void saveParticipantList(String participant){
           if (participant != null) {
               ArrayList <String> participantList = new ArrayList<String>();
               SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
               SharedPreferences.Editor editor = sharedPreferences.edit();
               Gson gson = new Gson();
               participantList.add(participant);
               String json = gson.toJson(participantList);
               editor.putString("participantList", json);
               editor.commit();
           }

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
        //Beim klicken auf den Button "Scan" wird der BarcodeScanner geöffnet
        btn_scan.setOnClickListener(v -> {scanCode();

        });
        //Beim klicken auf den Button "Auswahlmenü" wechselt die View zum Auswahlmenü, und die
        //Teilnehmerliste wird in Gitlab geposted

        btn_selectionmnu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra("sendData","true");
                startActivity(intent);

            }
        });

        //Auswahlfrage
        Say say = SayBuilder.with(qiContext)
                .withPhrase(scanOrSelect)
                .build();
        say.run();
        //Phraseset für Scannen
        PhraseSet scan= PhraseSetBuilder.with(qiContext)
                .withTexts("Code", "Scannen", "Hallo Pepper", "Hallo","Scanne Code")
                .build();

        //Phraseset für Auswahlmenü
        PhraseSet selectionmnu= PhraseSetBuilder.with(qiContext)
                .withTexts("menü", " Starte menü")
                .build();

        Listen listen = ListenBuilder.with(qiContext)
                .withPhraseSets()
                .withPhraseSets(selectionmnu)
                .build();
        ListenResult listenresult= listen.run();

        // Das Gesagte in String umwandeln

        String result = listenresult.getHeardPhrase().toString();

        //Jenachdem was gesagt wurde wird die entsprechende Activity gestartet
        if (scan.getPhrases().toString().contains(result) ) {
            startActivity(new Intent(MainActivity.this,CaptureAct.class));}
        if (selectionmnu.getPhrases().toString().contains(result)) {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            intent.putExtra("sendData","true");
            startActivity(intent);

            startActivity(intent);}

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
