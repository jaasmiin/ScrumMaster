package com.example.scrummaster.activity.tools;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.ChatBuilder;
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder;
import com.aldebaran.qi.sdk.builder.TopicBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.conversation.AutonomousReactionImportance;
import com.aldebaran.qi.sdk.object.conversation.AutonomousReactionValidity;
import com.aldebaran.qi.sdk.object.conversation.Bookmark;
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.conversation.QiChatExecutor;
import com.aldebaran.qi.sdk.object.conversation.QiChatVariable;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Topic;
import com.example.scrummaster.R;
import com.example.scrummaster.activity.MeetingFinished;
import com.example.scrummaster.activity.MenuActivity;
import com.example.scrummaster.controller.PowerPointAdvancedQiChatExecutor;
import com.example.scrummaster.controller.PowerPointBeginnerQiChatExecutor;
import com.example.scrummaster.controller.PowerPointSelecttQiChatExecutor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PowerPointStartActivity extends RobotActivity implements RobotLifecycleCallbacks {
    public Button btn_beginner;
    public Button btn_advanced;
    private Button done;
    private Chat chat;
    private QiChatVariable variable;
    public QiChatbot qiChatbot;
    public Topic topic;
    public Bookmark proposalBookmark;
    private List<String> participantList = new ArrayList<>();
    private static PowerPointStartActivity instance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QiSDK.register(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_point_start);
        instance= this;
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        btn_advanced= findViewById(R.id.btn_advanced);
        btn_beginner= findViewById(R.id.btn_beginner);
        done = findViewById(R.id.powerpointDone);
        participantList = loadParticipantListCopy();
        if (participantList.size()==0){
            Intent i = new Intent(PowerPointStartActivity.this, MeetingFinished.class);
            startActivity(i);}
        // Create a topic.
        topic = TopicBuilder.with(qiContext)
                .withResource(R.raw.powerpoint_karaoke)
                .build();
        // Create a qiChatbot
        qiChatbot = QiChatbotBuilder.with(qiContext).withTopic(topic).build();
        //BookmARK
        Map<String, Bookmark> bookmarks = topic.getBookmarks();

        //Create executor
        Map<String, QiChatExecutor> executors = new HashMap<>();

        // Map the executor name from the topic to our qiChatExecutor
        executors.put("myExecutor", new PowerPointSelecttQiChatExecutor(qiContext));
        executors.put("beginner", new PowerPointBeginnerQiChatExecutor( qiContext));
        executors.put("advanced", new PowerPointAdvancedQiChatExecutor( qiContext));
        // Set the executors to the qiChatbot
        qiChatbot.setExecutors(executors);

        // Build chat with the chatbotBuilder
        chat = ChatBuilder
                .with(qiContext)
                .withChatbot(qiChatbot).build();

        // Get the proposal bookmark
        Intent intent = getIntent();
        String s = intent.getStringExtra("Bookmark");
        String s2 = intent.getStringExtra("bookmark_beginner");
        String s3 = intent.getStringExtra("bookmark_advanced");
        String b = setBookmark(s,s2,s3);
        // Get the proposal bookmark
        proposalBookmark = bookmarks.get(b);

        chat.addOnStartedListener(this::sayProposal);

        //create Variable

        variable = qiChatbot.variable("name");
        variable.setValue(participantList.get(0));
        chat.async().run();
        chat.addOnStartedListener(() -> Log.i(TAG, "Discussion started."));

        //Klick auf Button Anfänger geht in die Beginner Activity
        btn_beginner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PowerPointStartActivity.this, PowerPointBeginnerActivity.class);
                startActivity(intent);
                deleteParticipantListEntry();
            }
        });

        //Klick auf Button Fortgeschritten geht in die Advanced Activity
        btn_advanced.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PowerPointStartActivity.this, PowerPointAdvancedActivity.class);
                startActivity(intent);
                deleteParticipantListEntry();
            }
        });

        //Klick auf Button Fortgeschritten geht in die Advanced Activity
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PowerPointStartActivity.this, MenuActivity.class);
                startActivity(intent);
                deleteParticipantListEntry();
            }
        });

    }





    public void sayProposal() {
        qiChatbot.goToBookmark(proposalBookmark,
                AutonomousReactionImportance.HIGH,
                AutonomousReactionValidity.IMMEDIATE);
    }

    //Gibt die instance dieser Activity zurück
    public static  PowerPointStartActivity getInstance(){
        return instance;

    }
    //Lädt die TeilnehmerListe speichert diese als Kopie in Shared Preferences und gibt die Kopie zurück
    private ArrayList<String> loadParticipantListCopy(){
        ArrayList <String> participantList;
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("participantListCopy",null);
        Type type= new TypeToken<ArrayList<String>>(){}.getType();
        participantList = gson.fromJson(json,type);
        ArrayList <String> TEST= new ArrayList<>();
        TEST.add("jasmin");
        TEST.add("Liyana");

        return TEST;
        //return participantList;
    }


    //Löscht den ersten Eintrag der gespeicherten ParticapantListCopy aus sharedPreferences
    public void deleteParticipantListEntry() {
        ArrayList<String> l = new ArrayList<>();
        l = loadParticipantListCopy();
        l.remove(0);
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(l);
        editor.putString("participantListCopy",json);
        editor.apply();

    }

    //Diese Methode setzt den Bookmark die Activity neu gestartet wurde
    public String setBookmark(String s,String s2,String s3){
        if (s == null & s2 == null && s3 ==null) {
            s = "select";
            return s;
    } else if (s2 != null ) {
            return s2;
        }
        else if (s3 != null ) {
            return s3;
        }
        return s;

    }

    //Diese Methode klickt beim Aufruf auf den Button Anfänger
    public void clickButtonBeginner ()
    { btn_beginner.post(new Runnable(){
        @Override
        public void run() {
            btn_beginner.performClick();
        }

    });
    }

    //Diese Methode klickt beim Aufruf auf den Button Fortgeschtitten
    public void clickButtonAdvanced()
    { btn_advanced.post(new Runnable(){
        @Override
        public void run() {
            btn_advanced.performClick();
        }

    });
    }

    @Override
    public void onRobotFocusLost() {
        if (chat != null) {
            chat.removeAllOnStartedListeners();
        }
        finish();
    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }


}