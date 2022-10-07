package com.example.scrummaster.activity;

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
import com.aldebaran.qi.sdk.object.conversation.BookmarkStatus;
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.conversation.QiChatVariable;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Topic;
import com.example.scrummaster.R;
import com.example.scrummaster.datamodel.MeetingPoints;
import com.example.scrummaster.service.MeetingPointsService;
import com.example.scrummaster.service.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModerationNotesStartActivity extends RobotActivity implements RobotLifecycleCallbacks {
    private Button btn_startm;
    Chat chat;
    QiChatVariable moderationpointVariable;
    QiChatbot qiChatbot;
    Topic topic;
    private Bookmark proposalBookmark;
    BookmarkStatus bookmarkStatus;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
                QiSDK.register(this,this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderation_notes_start);
        btn_startm = findViewById(R.id.startm);
        getMeetingPoints();
        setMeetingPointDescription();
        copyParticipantList();
        //Mit klick auf den Button wird die nächste Activity geöffnet und bookmarkb auf false gesetzt,
        //sodass bei der Rückkehr in die Activity der Bookmark %Next aufgerufen wird
        btn_startm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(ModerationNotesStartActivity.this,ModerationNotesActivity.class));
                finish();

            }
        });

    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {

        // Create a topic.
        topic = TopicBuilder.with(qiContext)
                .withResource(R.raw.moderatenotes)
                .build();
        //BookmARK
        Map<String, Bookmark> bookmarks = topic.getBookmarks();
        // Create a qiChatbot
        qiChatbot = QiChatbotBuilder.with(qiContext).withTopic(topic).build();
        // Build chat with the chatbotBuilder
        chat = ChatBuilder.with(qiContext).withChatbot(qiChatbot).build();
        // Get the bookmarks from the topic.



        // Get the proposal bookmark
        Intent intent = getIntent();
        String s = intent.getStringExtra("Bookmark");
        String b = setBookmark(s);
        proposalBookmark = bookmarks.get(b);

        chat.addOnStartedListener(this::sayProposal);

        //create Variable
        moderationpointVariable = qiChatbot.variable("meetingpoint");
        moderationpointVariable.setValue("Tet");
        chat.async().run();
        chat.addOnStartedListener(() -> Log.i(TAG, "Discussion started."));




    }
    //Kopiert die Teilnehmerliste
    private void copyParticipantList (){
        ArrayList <String> participantList;
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

    public String setBookmark(String s ){
        if (s == null) {
            s = "Next";
            return s;
        }
        return s;

    }

    //Holt die zu MeetingPointListe über gitlab
    public void getMeetingPoints() {

        RetrofitService.getRetrofitInstance().create(MeetingPointsService.class).getPunkte().enqueue(new Callback<List<MeetingPoints>>() {
            @Override
            public void onResponse(Call<List<MeetingPoints>> call, Response<List<MeetingPoints>> response) {
                Log.i("Retrofit", new Gson().toJson(response.body()));
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared preferences",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                List<MeetingPoints> meetingPointsList= response.body();
                String json = gson.toJson(meetingPointsList);
                editor.putString("meetingPointList",json);
                editor.apply();


            }

            @Override
            public void onFailure(Call<List<MeetingPoints>> call, Throwable t) {
                String fail =t.getCause().toString();
                Log.e("Retrofit",fail);
            }
        });

    }
    //Speichert die Descriptions der Meetingpointlist
    private void setMeetingPointDescription (){
        ArrayList<String> description = new ArrayList<>();
        List<MeetingPoints> meetingPointsList= loadMeetingPoints();
        for (MeetingPoints meetingPoints :meetingPointsList){
            String point=  meetingPoints.getDescription();
            description.add(point);}
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String json = gson.toJson(description);
        editor.putString("meetingPointListDescription",json);
        editor.apply();
    }
    //Lädt die gespeicherte MeetingPointListe aus sharedPreferences
    private ArrayList<MeetingPoints> loadMeetingPoints(){
        ArrayList<MeetingPoints> meetingPointList;
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("meetingPointList",null);
        Type type= new TypeToken<ArrayList<MeetingPoints>>(){}.getType();
        meetingPointList = gson.fromJson(json,type);

        return meetingPointList;
    }





    @Override
    public void onRobotFocusLost() {
        if (chat != null) {
            chat.removeAllOnStartedListeners();
        }


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