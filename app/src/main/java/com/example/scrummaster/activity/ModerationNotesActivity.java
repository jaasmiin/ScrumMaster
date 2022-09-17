package com.example.scrummaster.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.example.scrummaster.R;
import com.example.scrummaster.controller.Countdown;
import com.example.scrummaster.datamodel.MeetingPoints;
import com.example.scrummaster.service.MeetingPointsService;
import com.example.scrummaster.service.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModerationNotesActivity extends RobotActivity implements RobotLifecycleCallbacks {

     Countdown modcountdown = new Countdown(1000,1000);
    private TextView countdown;
    private Button btn_done;
    private TextView name;
    private TextView note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QiSDK.register(this,this);
        getMeetingPoints();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderationnotes);
        countdown = findViewById(R.id.countdown);
        btn_done = findViewById(R.id.done);
        name= findViewById(R.id.name);
        note = findViewById(R.id.notes);



        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modcountdown.startTimer(countdown,name,note,ModerationNotesActivity.this);
            }


        });




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
                Log.e("Retrofit","Failed");
            }
        });

    }




    @Override
    public void onRobotFocusGained(QiContext qiContext) {



    }
    private void moderateMeeting (){
       List<MeetingPoints> meetingPointsList= loadMeetingPoints();
        ArrayList<String> participantList = loadTeilnehmerListe();

        for (int i = 0; i< meetingPointsList.size();i++ ){
            note.setText(meetingPointsList.get(i).getDescription());
                for(i=0;i<participantList.size();i++){
                    name.setText(participantList.get(i));
                   // modcountdown.startTimer(countdown);


            }
        }


    }

    //Lädt die TeilnehmerListe und gibt diese zurück
    private ArrayList<String> loadTeilnehmerListe(){
        ArrayList <String> participantList ;
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("participantList",null);
        Type type= new TypeToken<ArrayList<String>>(){}.getType();
        participantList = gson.fromJson(json,type);

        return participantList;
    }


    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}