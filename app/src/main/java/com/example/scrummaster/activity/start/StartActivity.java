package com.example.scrummaster.activity.start;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

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

import java.util.ArrayList;

public class StartActivity extends RobotActivity implements RobotLifecycleCallbacks {
        ImageButton start;
    Phrase select = new Phrase(" Hallo, heute möchte ich dir gerne etwas bei deiner Arbeit unter die Arme greifen. Sobald wir loslegen können berühre den Bildschirm oder sage Start. ") ;
    Listen listen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QiSDK.register(this,this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        start = findViewById(R.id.startApp);
       

    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        //Klick auf Button Anfänger geht in die Start Activity
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared preferences",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        //Phraseset für Planning
        PhraseSet begin= PhraseSetBuilder.with(qiContext)
                .withTexts("Starte App", "Starte", "Beginne App","Beginne", "Fange An","anfangen")
                .build( );

        //Auswahlfrage
        Say say = SayBuilder.with(qiContext)
                .withPhrase(select)
                .build();
        say.run();

        //Zuhören was der Nutzer sich
        listen = ListenBuilder.with(qiContext)
                .withPhraseSets(begin)
                .build();
        ListenResult listenresult= listen.run();

        // Das Gesagte in String umwandeln
        String result = listenresult.getHeardPhrase().toString();

        //Jenachdem was gesagt wurde wird die entsprechende Activity gestartet
        if (begin.getPhrases().toString().contains(result) ) {
            Intent i = new Intent(StartActivity.this, MainActivity.class);
            startActivity(i);}
    }
    private void saveParticipantList(){

        ArrayList<String> participantList = new ArrayList<>();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(participantList);
        editor.putString("participantList", json);
        editor.apply();


    }


    @Override
    public void onRobotFocusLost() {
        saveParticipantList();
        finish();

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}