package com.example.scrummaster.activity;

import android.content.Intent;
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
import com.example.scrummaster.activity.begin.MenuActivity;
import com.example.scrummaster.activity.tools.HappinessIndexActivity;

public class MeetingFinishedActivity extends RobotActivity implements RobotLifecycleCallbacks {
    Listen listen;
    ImageButton btn_happinesIndex;
    ImageButton btn_menu;
    String goodbye = "Wir sind am Ende unseres heutigen Meetings angekommen. Ich bedanke mich bei allen für die Teilnahme" +
            "Ihr könnt entweder ins Haupmenü zurück oder den Stimmungsbarometer starten. Was sollen wir machen?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QiSDK.register(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_finished);
        btn_happinesIndex = findViewById(R.id.btn_happinessIndex);
        btn_menu = findViewById(R.id.btn_menu);


        btn_happinesIndex.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MeetingFinishedActivity.this, HappinessIndexActivity.class);
                startActivity(intent);
            }
        });

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MeetingFinishedActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Phrase meetingFinished = new Phrase(goodbye);
        //Phraseset für Happinessindex
        PhraseSet happinessIndex= PhraseSetBuilder.with(qiContext)
                .withTexts("happinexx Index", "Index", "Starte happiness index", "Starte Stimmungsbarometer")
                .build(

                );

        //Phraseset für Menü
        PhraseSet menu= PhraseSetBuilder.with(qiContext)
                .withTexts("Starte Menü", "Menü")
                .build();

        //Verabschiedung
        Say say = SayBuilder.with(qiContext)
                .withPhrase(meetingFinished)
                .build();

        say.run();

        //Zuhören was der Nutzer sich aussucht
        listen = ListenBuilder.with(qiContext)
                .withPhraseSets(happinessIndex)
                .withPhraseSets(menu)
                .build();
        ListenResult listenresult= listen.run();

        // Das Gesagte in String umwandeln
        String result = listenresult.getHeardPhrase().toString();

        //Jenachdem was gesagt wurde wird die entsprechende Activity gestartet
        if (menu.getPhrases().toString().contains(result) ) {
            Intent i = new Intent(MeetingFinishedActivity.this, MenuActivity.class);
            startActivity(i);}
        if (happinessIndex.getPhrases().toString().contains(result)) {
            startActivity(new Intent(MeetingFinishedActivity.this,HappinessIndexActivity.class));}

    }

    @Override
    public void onRobotFocusLost() {
listen.removeAllOnStartedListeners();
finish();
    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}