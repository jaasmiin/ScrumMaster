package com.example.scrummaster.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenOptions;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.Phrase;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.example.scrummaster.R;
import com.example.scrummaster.datamodel.PostNotes;
import com.example.scrummaster.service.PostNoteService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuswahlmenueActivity extends RobotActivity implements RobotLifecycleCallbacks {
    Button btn_modPunkte;
    Button btn_modDaily;
    Button btn_powerpoint;
    ArrayList <String> teilnehmerliste = new ArrayList<String>();
    Phrase auswahl = new Phrase(" Welche Aktion soll ich ausführen?") ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QiSDK.register(this, this);
        setContentView(R.layout.activity_auswahlmenue);
        btn_modDaily= findViewById(R.id.btn_mod_daily);
        btn_modPunkte=findViewById(R.id.btn_mod_punkte);
        btn_powerpoint=findViewById(R.id.btn_powerpoint);

        btn_modDaily.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(AuswahlmenueActivity.this,ModerationDailyScrumActivity.class));

            }
        });

        btn_modPunkte.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(AuswahlmenueActivity.this,ModerationPunkteActivity.class));

            }
        });

        btn_powerpoint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(AuswahlmenueActivity.this,PowerPointKaraokeActivity.class));

            }
        });
           //sobald die Activity gestartet wurde, wird die Teilnehmerliste an Gitlab geschickt
        sendTeilnehmer();


    }

    //Sendet die Teilnehmerliste zu Gitlab
    private void sendTeilnehmer(){

        //Diese Methode ist ausgebelendet, weil Pepper Emulator damit abstürzt--> Nullpointer
        //PostNotes liste = new PostNotes(listToString(loadTeilnehmerListe()));

        PostNotes pepperestliste = new PostNotes("Test");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://git.scc.kit.edu/api/v4/projects/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostNoteService postNoteService =retrofit.create(PostNoteService.class);

        Call<PostNotes> call = postNoteService.sendTeilnehmerListe(pepperestliste);

        call.enqueue(new Callback<PostNotes>() {
            @Override
            public void onResponse(Call<PostNotes> call, Response<PostNotes> response) {
                Toast.makeText(AuswahlmenueActivity.this,response.toString(),Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<PostNotes> call, Throwable t) {

            }
        });

    }

    //Ladet die TeilnehmerListe und gibt diese zurück
    private ArrayList<String> loadTeilnehmerListe(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("teilnehmerListe",null);
        Type type= new TypeToken<ArrayList<String>>(){}.getType();
        teilnehmerliste= gson.fromJson(json,type);

        return teilnehmerliste;
    }
    //Wandelt eine StringListe in einen String um und gibt diese aus
    private String listToString (ArrayList<String> liste){
        String ausgabeListe = String.join(" ,",liste);
        return ausgabeListe;

    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        //Phraseset für Moderation mit Punkten
        PhraseSet modMitPunkten= PhraseSetBuilder.with(qiContext)
                                                .withTexts("Starte Moderation mit Punkten", "mit Punkten", "Punkte")
                .build();

        //Phraseset für DailyScrum Moderation
        PhraseSet modDaily= PhraseSetBuilder.with(qiContext)
                .withTexts("Starte Daily Scrum", "DailyScrum", "Daily")
                .build();
        //Phraseset für Powerpoint Karaoke
        PhraseSet powerpoint= PhraseSetBuilder.with(qiContext)
                .withTexts("Starte Power Point Karaoke", "Power Point", "Karaoke","Power Point Karaoke")
                .build();


        //Auswahlfrage
        Say say = SayBuilder.with(qiContext)
                .withPhrase(auswahl)
                .build();
        say.run();

        //Zuhören was der Nutzer sich aussucht
        Listen listen = ListenBuilder.with(qiContext)
               .withPhraseSets(modMitPunkten)
              .withPhraseSets(modDaily)
                .withPhraseSets(powerpoint)
                .build();
        ListenResult listenresult= listen.run();

        // Das Gesagte in String umwandeln
        String result = listenresult.getHeardPhrase().toString();

        //Jenachdem was gesagt wurde wird die entsprechende Activity gestartet
        if (modMitPunkten.getPhrases().toString().contains(result) ) {
            startActivity(new Intent(AuswahlmenueActivity.this,ModerationPunkteActivity.class));}
        if (modDaily.getPhrases().toString().contains(result)) {
            startActivity(new Intent(AuswahlmenueActivity.this,ModerationDailyScrumActivity.class));}
        if (powerpoint.getPhrases().toString().contains(result)) {
            startActivity(new Intent(AuswahlmenueActivity.this,PowerPointKaraokeActivity.class));}


    }



    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}