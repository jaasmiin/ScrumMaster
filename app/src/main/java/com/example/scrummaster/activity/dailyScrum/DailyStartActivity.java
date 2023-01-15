package com.example.scrummaster.activity.dailyScrum;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
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
import com.example.scrummaster.activity.start.MenuActivity;
import com.example.scrummaster.controller.ModerateDailyStartQiChatExecutor;
import com.example.scrummaster.controller.ParticipantController;
import com.example.scrummaster.controller.RetrofitController;

import java.util.HashMap;
import java.util.Map;

public class DailyStartActivity extends RobotActivity implements RobotLifecycleCallbacks {
    Topic topic;
    QiChatbot qiChatbot;
    Chat chat;
    QiChatVariable variable;
    private Bookmark proposalBookmark;
    Button btn_start;
    Button menu;
    TextView welcome;
    private static DailyStartActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_start);
        QiSDK.register(this, this);
        btn_start= findViewById(R.id.startmdaily);
        welcome = findViewById(R.id.welcomedaily);
        menu =findViewById(R.id.dailyTomenu2);
        instance = this;
        ParticipantController.copyParticipantList(this);
        RetrofitController.getSprintBacklog(this);

    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        //Ceate Topic
        topic = TopicBuilder.with(qiContext)
                .withResource(R.raw.dailyscrum)
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

                Intent intent = new Intent(DailyStartActivity.this, DailyQuestionsActivity.class);
                startActivity(intent);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DailyStartActivity.this, MenuActivity.class);
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
    public static DailyStartActivity getInstance(){
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