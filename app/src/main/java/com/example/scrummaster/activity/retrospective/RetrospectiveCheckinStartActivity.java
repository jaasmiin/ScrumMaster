package com.example.scrummaster.activity.retrospective;

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
import com.aldebaran.qi.sdk.object.conversation.QiChatVariable;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Topic;
import com.example.scrummaster.R;
import com.example.scrummaster.controller.ParticipantController;
import com.example.scrummaster.controller.RetrofitController;

import java.util.Map;

public class RetrospectiveCheckinStartActivity extends RobotActivity implements RobotLifecycleCallbacks {
    private Button btn_startm;
    private Chat chat;
    private QiChatVariable variable;
    private QiChatbot qiChatbot;
    private Topic topic;
    private Bookmark proposalBookmark;
    private String question;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QiSDK.register(this,this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_moderation_notes_start);
        btn_startm = findViewById(R.id.startm);


        ParticipantController.copyParticipantList(this);
        //Mit klick auf den Button wird die nächste Activity geöffnet und bookmarkb auf false gesetzt,
        //sodass bei der Rückkehr in die Activity der Bookmark %Next aufgerufen wird
        btn_startm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(RetrospectiveCheckinStartActivity.this, RetrospectiveCheckinActivity.class);
                i.putExtra("question",question);
                startActivity(i);

            }
        });



    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        TextView meetingpoint = findViewById(R.id.id_meeting);
        // Create a topic.
            topic = TopicBuilder.with(qiContext)
                    .withResource(R.raw.retrospective)
                    .build();
            //BookmARK
            Map<String, Bookmark> bookmarks = topic.getBookmarks();
            // Create a qiChatbot
            qiChatbot = QiChatbotBuilder.with(qiContext).withTopic(topic).build();
            // Build chat with the chatbotBuilder
            chat = ChatBuilder
                    .with(qiContext)
                    .withChatbot(qiChatbot).build();



            proposalBookmark = bookmarks.get("Start");

            chat.addOnStartedListener(this::sayProposal);

            //create Variable

            question = RetrofitController.loadQuestion(this);

            variable = qiChatbot.variable("meetingpoint");
            variable.setValue(question);
            chat.async().run();
            chat.addOnStartedListener(() -> Log.i(TAG, "Discussion started."));


        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (question != null) {
                    meetingpoint.setText(question);
                }

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

    public void sayProposal() {
        qiChatbot.goToBookmark(proposalBookmark,
                AutonomousReactionImportance.HIGH,
                AutonomousReactionValidity.IMMEDIATE);
    }
  @Override
    protected void onDestroy() {
        QiSDK.unregister(this,this);

      super.onDestroy();

  }
}