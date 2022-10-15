package com.example.scrummaster.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.example.scrummaster.controller.ModerateDailyStartQiChatExecutor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModerationDailyStartActivity extends RobotActivity implements RobotLifecycleCallbacks {
    Topic topic;
    QiChatbot qiChatbot;
    Chat chat;
    QiChatVariable variable;
    private Bookmark proposalBookmark;
    Button btn_start;
    TextView welcome;
    private static ModerationDailyStartActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderation_daily_start_activity);
        QiSDK.register(this, this);
        btn_start= findViewById(R.id.startmdaily);
        welcome = findViewById(R.id.welcomedaily);
        instance = this;
        copyParticipantList();

    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        //Ceate Topic
        topic = TopicBuilder.with(qiContext)
                .withResource(R.raw.moderatedaily)
                .build();

        //Create QiChatbot
        qiChatbot = QiChatbotBuilder.with(qiContext)
                .withTopic(topic)
                .build();

// Build chat with the chatbotBuilder
        chat = ChatBuilder
                .with(qiContext)
                .withChatbot(qiChatbot).build();

        //BookmARK
        Map<String, Bookmark> bookmarks = topic.getBookmarks();
        proposalBookmark = bookmarks.get("Startdaily");
        chat.addOnStartedListener(this::sayProposal);



        //Create executor
        Map<String, QiChatExecutor> executors = new HashMap<>();

        // Map the executor name from the topic to our qiChatExecutor
        executors.put("myExecutor", new ModerateDailyStartQiChatExecutor(qiContext));

        // Set the executors to the qiChatbot
        qiChatbot.setExecutors(executors);

        //Start Chat
        chat.async().run();
        chat.addOnStartedListener(() -> Log.i(TAG, "Discussion started."));

        btn_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ModerationDailyStartActivity.this, ModerationDailyScrumActivity.class);
                startActivity(intent);
            }
        });
    }


    public void sayProposal() {
        qiChatbot.goToBookmark(proposalBookmark,
                AutonomousReactionImportance.HIGH,
                AutonomousReactionValidity.IMMEDIATE);
    }
    //Gibt die instance dieser Activity zurück
    public static  ModerationDailyStartActivity getInstance(){
        return instance;

    }

    //Diese Methode klickt beim Aufruf auf den Button Start
    public void clickButton ()
    { btn_start.post(new Runnable(){
        @Override
        public void run() {
            btn_start.performClick();
        }

    });

    }
    //Kopiert die Teilnehmerliste
    private void copyParticipantList (){
        ArrayList<String> participantList;
        //Die Origonal TeilnehmerListe laden
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("participantList",null);
        Type type= new TypeToken<ArrayList<String>>(){}.getType();
        participantList = gson.fromJson(json,type);
        //Die OriginalTeilnehmerListe als Kopie speichern
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gsonCopy = new Gson();
        String jsonCopy = gsonCopy.toJson(participantList);
        editor.putString("participantListCopy",jsonCopy);
        editor.apply();

    }


    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}