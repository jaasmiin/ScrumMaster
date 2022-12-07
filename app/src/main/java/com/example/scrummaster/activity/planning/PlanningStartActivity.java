package com.example.scrummaster.activity.planning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import com.example.scrummaster.begin.MenuActivity;
import com.example.scrummaster.datamodel.MeetingPoints;
import com.example.scrummaster.service.BacklogService;
import com.example.scrummaster.service.RetrofitService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanningStartActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private Phrase meeting_select = new Phrase("Welches Meeting findet statt? Eins oder zwei?");
    private Button one;
    private Button two;
    private Button menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QiSDK.register(this, this);
        super.onCreate(savedInstanceState);
        getSprintBacklog();

        setContentView(R.layout.activity_planning_start);
        one = findViewById(R.id.planning_meeting_I);
        two = findViewById(R.id.planning_meeting_II);
        menu=findViewById(R.id.planningTomenu);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlanningStartActivity.this, PlanningBacklogActivity.class);
                startActivity(i);
            }
        });

        two.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(PlanningStartActivity.this, PlanningSprintBacklogActivity.class);
                startActivity(i);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlanningStartActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });
    }
    //Holt die  IssueListe mit dem Label SprintBacklog über gitlab und speichert sie in shared Preferences
    public void getSprintBacklog() {

        RetrofitService.getRetrofitInstance().create(BacklogService.class).getSprintBacklog().enqueue(new Callback<List<MeetingPoints>>() {
            @Override
            public void onResponse(Call<List<MeetingPoints>> call, Response<List<MeetingPoints>> response) {
                Log.i("Retrofit", new Gson().toJson(response.body()));
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared preferences",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                List<MeetingPoints> meetingPointsList= response.body();
                String json = gson.toJson(meetingPointsList);
                editor.putString("SprintBoard",json);
                editor.apply();
            }

            @Override
            public void onFailure(Call<List<MeetingPoints>> call, Throwable t) {
                String fail =t.getCause().toString();
                Log.e("Retrofit",fail);
            }
        });

    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        //Auswahlfrage
        Say say = SayBuilder.with(qiContext)
                .withPhrase(meeting_select)
                .build();
        say.run();
        //Phraseset für Scannen
        PhraseSet one= PhraseSetBuilder.with(qiContext)
                .withTexts("eins", "1", "Nummer eins")
                .build();

        //Phraseset für Auswahlmenü
        PhraseSet two= PhraseSetBuilder.with(qiContext)
                .withTexts("zwei", " 2", "Nummer zwei")
                .build();

        Listen listen = ListenBuilder.with(qiContext)
                .withPhraseSets(one)
                .withPhraseSets(two)
                .build();
        ListenResult listenresult= listen.run();

        // Das Gesagte in String umwandeln

        String result = listenresult.getHeardPhrase().toString();

        //Jenachdem was gesagt wurde wird die entsprechende Activity gestartet
        if (one.getPhrases().toString().contains(result) ) {
            startActivity(new Intent(PlanningStartActivity.this, PlanningBacklogActivity.class));}
        if (two.getPhrases().toString().contains(result)) {
           startActivity(new Intent(PlanningStartActivity.this, PlanningSprintBacklogActivity.class));
        }

    }

    @Override
    public void onRobotFocusLost() {
        finish();
    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}