package com.example.scrummaster.activity.daily;

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
import com.example.scrummaster.begin.MeetingFinished;
import com.example.scrummaster.controller.CountdownController;
import com.example.scrummaster.controller.ModerateDailyQiChatExecutor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyQuestionsActivity extends RobotActivity implements RobotLifecycleCallbacks {
    private TextView countdown;
    private CountdownController mcountdown = new CountdownController(5000,5000);
    private Button btn_done;
    private Button btn_start;
    private Button finish;
    private TextView name;
    private TextView note;
    private Chat chat;
    private QiChatVariable nameVariable;
    private QiChatbot qiChatbot;
    private Topic topic;
    private Bookmark proposalBookmark;
    private List<String> participantList = new ArrayList<>();
    private static DailyQuestionsActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        QiSDK.register(this,this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_questions);
        countdown = findViewById(R.id.countdown2);
        btn_done = findViewById(R.id.done2);
        name= (TextView) findViewById(R.id.name2);
        note = findViewById(R.id.questionsofdailyscrum);
        btn_start = findViewById(R.id.startcdaily);
        finish = findViewById(R.id.dailyTomenu);
        instance =  this;
    }

    //Gibt die instance dieser Activity zurück
    public static DailyQuestionsActivity getInstance(){
        return instance;

    }

    //Diese Methode klickt beim Aufruf auf den Button Start
    public void clickButton () {
        btn_start.post(new Runnable() {
            @Override
            public void run() {
                btn_start.performClick();
            }

        });
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        participantList = loadParticipantListCopy();
        if (participantList.size() == 0) {
            Intent i = new Intent(DailyQuestionsActivity.this, DailySprintBacklog.class);
            startActivity(i);
        } else {
            // Create a topic.
            topic = TopicBuilder.with(qiContext)
                    .withResource(R.raw.moderatedaily)
                    .build();

            // Create a qiChatbot
            qiChatbot = QiChatbotBuilder.with(qiContext).withTopic(topic).build();

            Map<String, QiChatExecutor> executors = new HashMap<>();

            // Map the executor name from the topic to our qiChatExecutor
            executors.put("start", new ModerateDailyQiChatExecutor(qiContext));

            // Set the executors to the qiChatbot
            qiChatbot.setExecutors(executors);

            // Build chat with the chatbotBuilder
            chat = ChatBuilder.with(qiContext).withChatbot(qiChatbot).build();

            //Create variable
            nameVariable = qiChatbot.variable("namedaily");
            nameVariable.setValue(participantList.get(0));

// Get the bookmarks from the topic.
            Map<String, Bookmark> bookmarks = topic.getBookmarks();
// Get the proposal bookmark
            proposalBookmark = bookmarks.get("first");
            chat.addOnStartedListener(this::sayProposal);

            // Run an action asynchronously.
            chat.async().run();
            chat.addOnStartedListener(() -> Log.i(TAG, "Discussion started."));

        }
        btn_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                name.setText(participantList.get(0));
                mcountdown.startTimerDaily(countdown, DailyQuestionsActivity.this);
                overridePendingTransition(0, 0);
                deleteParticipantListEntry();
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mcountdown.reset(countdown);
                Intent intent = new Intent(DailyQuestionsActivity.this, DailyQuestionsActivity.class);
                startActivity(intent);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyQuestionsActivity.this, MeetingFinished.class);
                startActivity(intent);
            }
        });


    }


    private void assignVariable(String value) {
        // Set the value.
        nameVariable.async().setValue(value);}

    public void sayProposal() {
        qiChatbot.goToBookmark(proposalBookmark,
                AutonomousReactionImportance.HIGH,
                AutonomousReactionValidity.IMMEDIATE);
    }




    //Lädt die TeilnehmerListe speichert diese als Kopie in Shared Preferences und gibt die Kopie zurück
    private ArrayList<String> loadParticipantListCopy(){

        ArrayList <String> participantList;


        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("participantListCopy",null);
        Type type= new TypeToken<ArrayList<String>>(){}.getType();
        participantList = gson.fromJson(json,type);
       return participantList;

    }
    //Löscht den ersten Eintrag der gespeicherten ParticapantListCopy aus sharedPreferences
    private void deleteParticipantListEntry() {
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

    @Override
    public void onRobotFocusLost() {
        if (chat != null) {
            chat.removeAllOnStartedListeners();
            finish();
        }

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}