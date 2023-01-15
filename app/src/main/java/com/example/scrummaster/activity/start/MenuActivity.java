package com.example.scrummaster.activity.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

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
import com.example.scrummaster.activity.dailyScrum.DailyStartActivity;
import com.example.scrummaster.activity.retrospective.RetrospectiveMenuActivity;
import com.example.scrummaster.activity.sprintPlanning.PlanningStartActivity;
import com.example.scrummaster.activity.tools.ToolsMenuActivity;
import com.example.scrummaster.controller.ParticipantController;

import java.util.ArrayList;

public class MenuActivity extends RobotActivity implements RobotLifecycleCallbacks {
    ImageButton btn_planning;
    ImageButton btn_modDaily;
    ImageButton btn_retrospektive;
    ImageButton btn_tools;
    Phrase select = new Phrase(" Welche Aktion soll ich ausführen?") ;
    String sendParticipantList;
    Listen listen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //Laden der Besprechungs Punkte
        //RetrofitController.getMeetingPoints(this);
        //Kopieren der Besprechungs Punkte zur Nutzung in anderen Activities
        //RetrofitController.copyMeetingPointList(this);
        //Kopiert die Teilnehmerliste zur Nutzung für andere Activities
       ParticipantController.copyParticipantList(this);
        super.onCreate(savedInstanceState);
        QiSDK.register(this, this);
        setContentView(R.layout.activity_selectionmenu);
        btn_modDaily= findViewById(R.id.btn_mod_daily);
        btn_retrospektive=findViewById(R.id.btn_retrospektive);
        btn_planning=findViewById(R.id.btn_planning);
       btn_tools = findViewById(R.id.btn_tools);





        //sobald diese Activity aus dem MainActivity gestartet wurde, wird die Teilnehmerliste an Gitlab geschickt
        //wird sie aus  einer anderen Activity gestartet dann wird die Liste nicht erneut gesendet
        Intent intent = getIntent();
        sendParticipantList = intent.getStringExtra("sendData");
        if (sendParticipantList != null){
        ParticipantController.sendParticipants(this);}



        btn_modDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, DailyStartActivity.class));
            }
        });

        btn_planning.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Übergebe den Wert "Start" für den Bookmark in der RetrospectiveCheckinStartActivity und öffnen dieser Activity
                Intent i_ModerationNotes = new Intent(MenuActivity.this, PlanningStartActivity.class);
                i_ModerationNotes.putExtra("Bookmark","Start");
                startActivity(i_ModerationNotes);

            }
        });

        btn_retrospektive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i_PowerPoint = new Intent(MenuActivity.this, RetrospectiveMenuActivity.class);
                startActivity(i_PowerPoint);
            }
        });

        btn_tools.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(MenuActivity.this, ToolsMenuActivity.class));

            }
        });







    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {


        //Phraseset für Planning
        PhraseSet planning= PhraseSetBuilder.with(qiContext)
                                                .withTexts("Starte Planning Meeting", "Planning", "Planning Meeting","Sprint Planning")
                .build(

                );

        //Phraseset für DailyScrum Moderation
        PhraseSet modDaily= PhraseSetBuilder.with(qiContext)
                .withTexts("Starte Daily Scrum", "Daily Scrum", "Daily","Daily Meeting")
                .build();
        //Phraseset für Retrospektive
        PhraseSet retrospective= PhraseSetBuilder.with(qiContext)
                .withTexts("Starte Retrospektive Meeting", "Retrospektive Meeting", "Retrospektive")
                .build();

        //Phraseset für Tools
        PhraseSet tools= PhraseSetBuilder.with(qiContext)
                .withTexts("Starte Tools", "Tools")
                .build();

        //Auswahlfrage
        Say say = SayBuilder.with(qiContext)
                .withPhrase(select)
                .build();
        say.run();

        //Zuhören was der Nutzer sich
        listen = ListenBuilder.with(qiContext)
               .withPhraseSets(planning)
              .withPhraseSets(modDaily)
                .withPhraseSets(retrospective)
                .withPhraseSets(tools)
                .build();
        ListenResult listenresult= listen.run();

        // Das Gesagte in String umwandeln
        String result = listenresult.getHeardPhrase().toString();

        //Jenachdem was gesagt wurde wird die entsprechende Activity gestartet
        if (planning.getPhrases().toString().contains(result) ) {
            Intent i = new Intent(MenuActivity.this, PlanningStartActivity.class);

            startActivity(i);}
        if (modDaily.getPhrases().toString().contains(result)) {
            startActivity(new Intent(MenuActivity.this, DailyStartActivity.class));}
        if (retrospective.getPhrases().toString().contains(result)) {
            startActivity(new Intent(MenuActivity.this,RetrospectiveMenuActivity.class));}
        if (tools.getPhrases().toString().contains(result)) {
            startActivity(new Intent(MenuActivity.this, ToolsMenuActivity.class));}


    }



    @Override
    public void onRobotFocusLost() {

        listen.removeAllOnStartedListeners();
        finish();
    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }


    /*//Lädt die TeilnehmerListe und gibt diese zurück
    private ArrayList<String> loadParticipantList(){
        ArrayList <String> participantList;
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("participantList",null);
        Type type= new TypeToken<ArrayList<String>>(){}.getType();
        participantList = gson.fromJson(json,type);

        ArrayList <String> TEST= new ArrayList<>();
        TEST.add("jasmin");
        TEST.add("Liyana");

        return TEST;
       // return participantList;
    }*/

    //Wandelt eine StringListe in einen String um und gibt diese aus
    private String listToString (ArrayList<String> liste){
        String output = String.join(" ,",liste);
        return output;

    }

   /* //Holt die zu MeetingPointListe über gitlab
    public void getMeetingPoints() {

        RetrofitService.getRetrofitInstance().create(RetrospectiveService.class).getQuestion().enqueue(new Callback<List<Items>>() {
            @Override
            public void onResponse(Call<List<Items>> call, Response<List<Items>> response) {
                Log.i("Retrofit", new Gson().toJson(response.body()));
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared preferences",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                List<Items> meetingPointsList= response.body();
                String json = gson.toJson(meetingPointsList);
                editor.putString("meetingPointList",json);
                editor.apply();
            }

            @Override
            public void onFailure(Call<List<Items>> call, Throwable t) {
                String fail =t.getCause().toString();
                Log.e("Retrofit",fail);
            }
        });

    }*/

 /*   //Kopiert die MeetingPointList
    private void copyMeetingPointList (){
        ArrayList <Items> meetingPointList;
        //Die Origonal MeetingPointListe laden
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("meetingPointList",null);
        Type type= new TypeToken<ArrayList<Items>>(){}.getType();
        meetingPointList = gson.fromJson(json,type);
        //Die OriginalMeetingPointListe als Kopie speichern
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gsonCopy = new Gson();
        String jsonCopy = gsonCopy.toJson(meetingPointList);
        editor.putString("meetingPointListCopy",jsonCopy);
        editor.apply();

    }*/
    //Holt die komplette IssueListe mit dem Status opened komplett über gitlab
    /*public void getIssues() {

        RetrofitService.getRetrofitInstance().create(BacklogService.class).getIssues().enqueue(new Callback<List<Items>>() {
            @Override
            public void onResponse(Call<List<Items>> call, Response<List<Items>> response) {
                Log.i("Retrofit", new Gson().toJson(response.body()));
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared preferences",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                List<Items> meetingPointsList= response.body();
                String json = gson.toJson(meetingPointsList);
                editor.putString("IssueList",json);
                editor.apply();
            }

            @Override
            public void onFailure(Call<List<Items>> call, Throwable t) {
                String fail =t.getCause().toString();
                Log.e("Retrofit",fail);
            }
        });

    }*/
}