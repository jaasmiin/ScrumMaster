package com.example.scrummaster.activity.tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.example.scrummaster.activity.start.MenuActivity;

public class ToolsMenuActivity extends RobotActivity implements RobotLifecycleCallbacks {
    private TextView tools_headline;
    private Button tools_menu;
    private Button powerpoint;
    private Button happiness;
    private Phrase select = new Phrase(" Was kann ich für dich tun?") ;
    private Listen listen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_menu);
        QiSDK.register(this, this);

        tools_headline = findViewById(R.id.tools_headline);
        tools_menu = findViewById(R.id.tools_menu);
        powerpoint = findViewById(R.id.tools_powerpoint);
        happiness = findViewById(R.id.tools_happiness);

        tools_menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Übergebe den Wert "Start" für den Bookmark in der RetrospectiveCheckinStartActivity und öffnen dieser Activity
                Intent i_ModerationNotes = new Intent(ToolsMenuActivity.this, MenuActivity.class);
                startActivity(i_ModerationNotes);

            }
        });
        powerpoint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Übergebe den Wert "Start" für den Bookmark in der RetrospectiveCheckinStartActivity und öffnen dieser Activity
                Intent i = new Intent(ToolsMenuActivity.this, PowerPointStartActivity.class);
                i.putExtra("Bookmark","Start");
                startActivity(i);}

        });
        happiness.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Übergebe den Wert "Start" für den Bookmark in der RetrospectiveCheckinStartActivity und öffnen dieser Activity
                Intent i_ModerationNotes = new Intent(ToolsMenuActivity.this,HappinessIndexActivity.class);

                startActivity(i_ModerationNotes);

            }
        });
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        //Menü
        PhraseSet menu= PhraseSetBuilder.with(qiContext)
                .withTexts("Gehe zurück zum Menü", "zurück zum Menü","Menü")
                .build(

                );

        //Phraseset für Powerpoint
        PhraseSet powerpointPhrase= PhraseSetBuilder.with(qiContext)
                .withTexts("Starte Powerpoint Karaoke", "Powerpoint Karaoke", "Karaoke" ,"Powerpoint")
                .build();
        //Phraseset für Happiness
        PhraseSet happinessPhrase= PhraseSetBuilder.with(qiContext)
                .withTexts("Starte Happiness Index", "Happiness Index", "Happiness", "Index")
                .build();


        //Auswahlfrage
        Say say = SayBuilder.with(qiContext)
                .withPhrase(select)
                .build();
        say.run();

        //Zuhören was der Nutzer sich
        listen = ListenBuilder.with(qiContext)
                .withPhraseSets(powerpointPhrase)
                .withPhraseSets(happinessPhrase)
                .withPhraseSets(menu)
                .build();
        ListenResult listenresult= listen.run();

        // Das Gesagte in String umwandeln
        String result = listenresult.getHeardPhrase().toString();

        //Jenachdem was gesagt wurde wird die entsprechende Activity gestartet
        if (powerpointPhrase.getPhrases().toString().contains(result) ) {
            Intent i = new Intent(ToolsMenuActivity.this, PowerPointStartActivity.class);
            i.putExtra("Bookmark","Start");
            startActivity(i);}
        if (menu.getPhrases().toString().contains(result)) {
            startActivity(new Intent(ToolsMenuActivity.this, MenuActivity.class));}
        if (happinessPhrase.getPhrases().toString().contains(result)) {
            startActivity(new Intent(ToolsMenuActivity.this, HappinessIndexActivity.class));}



    }

    @Override
    public void onRobotFocusLost() {
        finish();
    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}