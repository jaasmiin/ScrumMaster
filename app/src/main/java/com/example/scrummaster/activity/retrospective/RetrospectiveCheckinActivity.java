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
import com.aldebaran.qi.sdk.object.conversation.QiChatExecutor;
import com.aldebaran.qi.sdk.object.conversation.QiChatVariable;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Topic;
import com.example.scrummaster.R;
import com.example.scrummaster.controller.CountdownController;
import com.example.scrummaster.controller.ModerateNotesQiChatExecutor;
import com.example.scrummaster.controller.ParticipantController;
import com.example.scrummaster.controller.RetrofitController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RetrospectiveCheckinActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private TextView countdown;
    private CountdownController mcountdown = new CountdownController(10000,5000);
   public Button btn_start;
    private Button btn_stop;
    private TextView name;
    private TextView note;
    private Chat chat;
    private QiChatVariable nameVariable;
    private QiChatbot qiChatbot;
    public Topic topic;
    public Bookmark proposalBookmark;
    private ArrayList<String> participantList = new ArrayList<>();
    private static RetrospectiveCheckinActivity instance;
    private String question;
    private Button btn_finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QiSDK.register(this,this);
        super.onCreate(savedInstanceState);
        question= RetrofitController.loadQuestion(this);
        setContentView(R.layout.activity_moderationnotes);
        countdown = findViewById(R.id.backlog_countdown);
        btn_stop = findViewById(R.id.done);
        btn_finish =findViewById(R.id.donecheckin);
        btn_start= findViewById(R.id.startc);
        name= (TextView) findViewById(R.id.name);
        note = (TextView)findViewById(R.id.notes);
        Intent i = getIntent();
        note.setText(question);
        instance= this;

    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {


        participantList = ParticipantController.loadParticipantListCopy(this);
        if (participantList.size()==0){

            Intent i = new Intent(RetrospectiveCheckinActivity.this, RetrospectiveMenuActivity.class);
            startActivity(i);
        } else{
        // Create a topic.
        topic = TopicBuilder.with(qiContext)
                .withResource(R.raw.retrospective)
                .build();

        // Create a qiChatbot
        qiChatbot = QiChatbotBuilder.with(qiContext).withTopic(topic).build();

        //Create executor
        Map<String, QiChatExecutor> executors = new HashMap<>();

        // Map the executor name from the topic to our qiChatExecutor
        executors.put("myExecutor", new ModerateNotesQiChatExecutor(qiContext));

        // Set the executors to the qiChatbot
        qiChatbot.setExecutors(executors);

        // Build chat with the chatbotBuilder
        chat = ChatBuilder.with(qiContext).withChatbot(qiChatbot).build();

        //Create variable
        nameVariable = qiChatbot.variable("name");
        nameVariable.setValue(participantList.get(0));

        // Get the bookmarks from the topic.
        Map<String, Bookmark> bookmarks = topic.getBookmarks();
        // Get the proposal bookmark
        proposalBookmark = bookmarks.get("first");
        chat.addOnStartedListener(this::sayProposal);

        // Run an action asynchronously.
        chat.async().run();
        chat.addOnStartedListener(() -> Log.i(TAG, "Discussion started."));}

        btn_finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(RetrospectiveCheckinActivity.this, RetrospectiveMenuActivity.class);
                startActivity(i);

            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ParticipantController.deleteParticipantListEntry(RetrospectiveCheckinActivity.this);

                mcountdown.startTimerNotes(countdown, RetrospectiveCheckinActivity.this);
                overridePendingTransition(0, 0);
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mcountdown.reset(countdown);
                //overridePendingTransition(0, 0);
                Intent intent = new Intent(RetrospectiveCheckinActivity.this, RetrospectiveCheckinActivity.class);
                startActivity(intent);
            }
        });
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (participantList.size()!=0) {
                    name.setText(participantList.get(0));
                }
            }
        });


    }

    //Gibt die instance dieser Activity zur??ck
    public static RetrospectiveCheckinActivity getInstance(){
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

   /* //L??scht den ersten Eintrag der gespeicherten MeetingListCopy aus sharedPreferences
    private void deleteMeetingPointListEntry() {
        ArrayList<Items> l = new ArrayList<>();
        l = RetrofitController.loadMeetingPointListCopy(this);
        l.remove(0);
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(l);
        editor.putString("meetingPointListCopy",json);
        editor.apply();

    }*/

    // Ruft den Bookmark auf
    public void sayProposal() {
        qiChatbot.goToBookmark(proposalBookmark,
                AutonomousReactionImportance.HIGH,
                AutonomousReactionValidity.IMMEDIATE);
    }


   /* //L??dt die Besprechungspunkte speichert diese als Kopie in Shared Preferences und gibt die Kopie zur??ck
    private ArrayList<Items> loadMeetingPointListCopy(){

        ArrayList <Items> meetingPointListCopy;

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("meetingPointListCopy",null);
        Type type= new TypeToken<ArrayList<Items>>(){}.getType();
        meetingPointListCopy = gson.fromJson(json,type);
        return meetingPointListCopy;
    }*/

   /* //L??scht den ersten Eintrag der gespeicherten MeetingPointListeDescription aus sharedPreferences
    private void deleteMeetingPointsDescription() {
        List<String> l = new ArrayList<>();
        l = getMeetingPointsDescription();
        l.remove(0);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(l);
        editor.putString("meetingPointListDescription",json);
        editor.commit();
        }


    //L??scht den ersten Eintrag der gespeicherten ParticapantListCopy aus sharedPreferences
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


/*
    // L??dt die MeetingPointDescription Liste aus sharedPreferences und gibt diese zur??ck
    private List<String> getMeetingPointsDescription(){
        List<String> l = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("meetingPointListDescription",null);
        Type type= new TypeToken<List<String>>(){}.getType();
        l = gson.fromJson(json,type);
        return l;
    }*/

   /* //L??dt die TeilnehmerListe speichert iese als Kopie in Shared Preferences und gibt die Kopie zur??ck
    private ArrayList<String> loadParticipantListCopy(){

        ArrayList <String> participantList;

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("participantListCopy",null);
        Type type= new TypeToken<ArrayList<String>>(){}.getType();
        participantList = gson.fromJson(json,type);
        return participantList;
    }*/

  /*  private void copyParticipantList (){
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

    }*/


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