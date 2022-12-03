package com.example.scrummaster.activity.retrospective;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

public class RetrospectiveMenuActivity extends RobotActivity implements RobotLifecycleCallbacks {
    private Button checkin;
    private Phrase select = new Phrase(" Welche Aktion soll ich ausführen?") ;
    Listen listen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QiSDK.register(this,this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrospective_menu);


        checkin = findViewById(R.id.retro_checkIn);

       checkin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RetrospectiveMenuActivity.this, RetrospectiveStartActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        //Phraseset für Tools
        PhraseSet checkin= PhraseSetBuilder.with(qiContext)
                .withTexts("Starte Check in", "Check in")
                .build();

        //Auswahlfrage
        Say say = SayBuilder.with(qiContext)
                .withPhrase(select)
                .build();
        say.run();

        //Zuhören was der Nutzer sich
        listen = ListenBuilder.with(qiContext)
                .withPhraseSets(checkin)
                .build();
        ListenResult listenresult= listen.run();

        // Das Gesagte in String umwandeln
        String result = listenresult.getHeardPhrase().toString();

        //Jenachdem was gesagt wurde wird die entsprechende Activity gestartet
        if (checkin.getPhrases().toString().contains(result) ) {
            Intent i = new Intent(RetrospectiveMenuActivity.this, RetrospectiveStartActivity.class);
            startActivity(i);
        }

        }

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}