package com.example.scrummaster.activity;

import android.content.Intent;
import android.os.Bundle;

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

public class PlanningMeeting_Start extends RobotActivity implements RobotLifecycleCallbacks {
    private Phrase meeting_select = new Phrase("Welches Meeting findet statt? Eins oder zwei?");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QiSDK.register(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_meeting_start);
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        //Auswahlfrage
        Say say = SayBuilder.with(qiContext)
                .withPhrase(meeting_select)
                .build();
        say.run();
        //Phraseset für Scannen
        PhraseSet one= PhraseSetBuilder.with(qiContext)
                .withTexts("eins", "1", "Nummer eins")
                .build();

        //Phraseset für Auswahlmenü
        PhraseSet two= PhraseSetBuilder.with(qiContext)
                .withTexts("zwei", " 2", "Nummer zwei")
                .build();

        Listen listen = ListenBuilder.with(qiContext)
                .withPhraseSets(one)
                .withPhraseSets(two)
                .build();
        ListenResult listenresult= listen.run();

        // Das Gesagte in String umwandeln

        String result = listenresult.getHeardPhrase().toString();

        //Jenachdem was gesagt wurde wird die entsprechende Activity gestartet
        if (one.getPhrases().toString().contains(result) ) {
            startActivity(new Intent(PlanningMeeting_Start.this,BacklogActivity.class));}
        if (two.getPhrases().toString().contains(result)) {
           startActivity(new Intent(PlanningMeeting_Start.this, SprintBacklogActivity.class));
        }

    }

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}