package com.example.scrummaster.activity;

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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModerationNotesActivity extends RobotActivity implements RobotLifecycleCallbacks {


    Countdown countdownPunkte  = new Countdown(25000,25000);
    private TextView countdown;
    private Button btn_done;
    private TextView name;
    private TextView note;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QiSDK.register(this,this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderationnotes);
        countdown = findViewById(R.id.countdown);
        btn_done = findViewById(R.id.done);
        name= findViewById(R.id.name);
        note = findViewById(R.id.btn_mod_points);



        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdownPunkte.startTimer(countdown);
            }

        });
        getBesprechungsPunkte();



    }

    public void getBesprechungsPunkte() {
        RetrofitService.getRetrofitInstance().create(MeetingPointsService.class).getPunkte().enqueue(new Callback<List<MeetingPoints>>() {
            @Override
            public void onResponse(Call<List<MeetingPoints>> call, Response<List<MeetingPoints>> response) {
                Log.i("Retrofit", new Gson().toJson(response.body()));
                List<MeetingPoints> antwort = response.body();
                name.setText(antwort.get(0).getDescription());
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

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}