package com.example.scrummaster.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.example.scrummaster.activity.daily.DailyStartActivity;
import com.example.scrummaster.activity.planning.PlanningStartActivity;
import com.example.scrummaster.datamodel.MeetingPoints;
import com.example.scrummaster.datamodel.PostNotes;
import com.example.scrummaster.service.BacklogService;
import com.example.scrummaster.service.MeetingPointsService;
import com.example.scrummaster.service.PostNoteService;
import com.example.scrummaster.service.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends RobotActivity implements RobotLifecycleCallbacks {
    ImageButton btn_planning;
    ImageButton btn_modDaily;
    ImageButton btn_retrospektive;
    ImageButton btn_review;
    Phrase select = new Phrase(" Welche Aktion soll ich ausführen?") ;
    String sendParticipantList;
    Listen listen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Laden der Besprechungs Punkte
        getMeetingPoints();
        //Kopieren der Besprechungs Punkte zur Nutzung in anderen Activities
        copyMeetingPointList();
        //Kopiert die Teilnehmerliste zur Nutzung für andere Activities
        copyParticipantList();
        super.onCreate(savedInstanceState);
        QiSDK.register(this, this);
        setContentView(R.layout.activity_selectionmenu);
        btn_modDaily= findViewById(R.id.btn_mod_daily);
        btn_retrospektive=findViewById(R.id.btn_retrospektive);
        btn_planning=findViewById(R.id.btn_planning);
       btn_review= findViewById(R.id.btn_review);

        getIssues();



        //sobald diese Activity aus dem MainActivity gestartet wurde, wird die Teilnehmerliste an Gitlab geschickt
        //wird sie aus  einer anderen Activity gestartet dann wird die Liste nicht erneut gesendet
        Intent intent = getIntent();
        sendParticipantList = intent.getStringExtra("sendData");
        if (sendParticipantList != null){
        sendParticipants();}      



        btn_modDaily.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(MenuActivity.this, DailyStartActivity.class));

            }
        });

        btn_planning.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Übergebe den Wert "Start" für den Bookmark in der ModerationNotesStartActivity und öffnen dieser Activity
                Intent i_ModerationNotes = new Intent(MenuActivity.this, PlanningStartActivity.class);
                i_ModerationNotes.putExtra("Bookmark","Start");
                startActivity(i_ModerationNotes);

            }
        });

        btn_retrospektive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Übergebe den Wert "Start" für den Bookmark in der PowerPointStartActivity und öffnen dieser Activity
                Intent i_PowerPoint = new Intent(MenuActivity.this, PowerPointStartActivity.class);
                i_PowerPoint.putExtra("Bookmark","start");
                startActivity(i_PowerPoint);

            }
        });







    }
    //Kopiert die Teilnehmerliste
    private void copyParticipantList (){


        ArrayList<String> participantList;
        ArrayList<String> TEST = new ArrayList<>();
        TEST.add("Jasmin");
        TEST.add("Aliyah");
        //Die Origonal TeilnehmerListe laden
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("participantList",null);
        Type type= new TypeToken<ArrayList<String>>(){}.getType();
        participantList = gson.fromJson(json,type);
        //Die OriginalTeilnehmerListe als Kopie speichern
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gsonCopy = new Gson();
        String jsonCopy = gsonCopy.toJson(TEST);
        editor.putString("participantListCopy",jsonCopy);
        editor.apply();

    }


    //Sendet die Teilnehmerliste zu Gitlab
    private void sendParticipants(){

        //Diese Methode ist ausgebelendet, weil Pepper Emulator damit abstürzt--> Nullpointer
       PostNotes liste = new PostNotes(listToString(loadParticipantList()));
        //Test Liste für Emulator
      //  ArrayList<String> test = new ArrayList<>();
        //test.add("Peter");
        //test.add("Bla");
        //PostNotes liste = new PostNotes(listToString(test));

        //ServerDaten
        RetrofitService.getRetrofitInstance().create(PostNoteService.class).sendTeilnehmerListe(liste).enqueue(new Callback<PostNotes>() {
            @Override
            public void onResponse(Call<PostNotes> call, Response<PostNotes> response) {
                Log.i("Retrofit", new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<PostNotes> call, Throwable t) {
                Log.e("Retrofit","Failed");

            }
        });

    }


    @Override
    public void onRobotFocusGained(QiContext qiContext) {


        //Phraseset für Moderation mit Punkten
        PhraseSet modNotes= PhraseSetBuilder.with(qiContext)
                                                .withTexts("Starte Moderation mit Punkten", "mit Punkten", "Punkte")
                .build(

                );

        //Phraseset für DailyScrum Moderation
        PhraseSet modDaily= PhraseSetBuilder.with(qiContext)
                .withTexts("Starte Daily Scrum", "DailyScrum", "Daily")
                .build();
        //Phraseset für Powerpoint Karaoke
        PhraseSet powerpoint= PhraseSetBuilder.with(qiContext)
                .withTexts("Starte Power Point Karaoke", "Power Point", "Karaoke","Power Point Karaoke")
                .build();


        //Auswahlfrage
        Say say = SayBuilder.with(qiContext)
                .withPhrase(select)
                .build();
        say.run();

        //Zuhören was der Nutzer sich
        listen = ListenBuilder.with(qiContext)
               .withPhraseSets(modNotes)
              .withPhraseSets(modDaily)
                .withPhraseSets(powerpoint)
                .build();
        ListenResult listenresult= listen.run();

        // Das Gesagte in String umwandeln
        String result = listenresult.getHeardPhrase().toString();

        //Jenachdem was gesagt wurde wird die entsprechende Activity gestartet
        if (modNotes.getPhrases().toString().contains(result) ) {
            Intent i = new Intent(MenuActivity.this, ModerationNotesStartActivity.class);
            i.putExtra("Bookmark","Start");
            startActivity(i);}
        if (modDaily.getPhrases().toString().contains(result)) {
            startActivity(new Intent(MenuActivity.this, DailyStartActivity.class));}
        if (powerpoint.getPhrases().toString().contains(result)) {
            startActivity(new Intent(MenuActivity.this,PowerPointStartActivity.class));}


    }



    @Override
    public void onRobotFocusLost() {

        listen.removeAllOnStartedListeners();
        finish();
    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }


    //Lädt die TeilnehmerListe und gibt diese zurück
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
    }

    //Wandelt eine StringListe in einen String um und gibt diese aus
    private String listToString (ArrayList<String> liste){
        String output = String.join(" ,",liste);
        return output;

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

    //Kopiert die MeetingPointList
    private void copyMeetingPointList (){
        ArrayList <MeetingPoints> meetingPointList;
        //Die Origonal MeetingPointListe laden
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("meetingPointList",null);
        Type type= new TypeToken<ArrayList<MeetingPoints>>(){}.getType();
        meetingPointList = gson.fromJson(json,type);
        //Die OriginalMeetingPointListe als Kopie speichern
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gsonCopy = new Gson();
        String jsonCopy = gsonCopy.toJson(meetingPointList);
        editor.putString("meetingPointListCopy",jsonCopy);
        editor.apply();

    }
    //Holt die komplette IssueListe mit dem Status opened komplett über gitlab
    public void getIssues() {

        RetrofitService.getRetrofitInstance().create(BacklogService.class).getIssues().enqueue(new Callback<List<MeetingPoints>>() {
            @Override
            public void onResponse(Call<List<MeetingPoints>> call, Response<List<MeetingPoints>> response) {
                Log.i("Retrofit", new Gson().toJson(response.body()));
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared preferences",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                List<MeetingPoints> meetingPointsList= response.body();
                String json = gson.toJson(meetingPointsList);
                editor.putString("IssueList",json);
                editor.apply();
            }

            @Override
            public void onFailure(Call<List<MeetingPoints>> call, Throwable t) {
                String fail =t.getCause().toString();
                Log.e("Retrofit",fail);
            }
        });

    }
}