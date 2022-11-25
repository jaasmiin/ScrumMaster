package com.example.scrummaster.activity.planning;

import android.content.Intent;
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
import com.example.scrummaster.controller.CountdownController;
import com.example.scrummaster.datamodel.MeetingPoints;
import com.example.scrummaster.service.BacklogService;
import com.example.scrummaster.service.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanningSprintBacklogItemActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private TextView title;
    private TextView description;
    private TextView timer;
    private Button back;
    private Button start_timer;
    private Button stop_timer;
    private CountdownController countdown = new CountdownController(3000,3000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QiSDK.register(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_sprint_backlog_item);
        Intent intent =getIntent();
        title = findViewById(R.id.sprintBacklog_title);
        description = findViewById(R.id.sprintBacklog_description);
        back = findViewById(R.id.sprintBacklogBack_Without_add);

        start_timer =findViewById(R.id.startCsprintBacklog);
        stop_timer= findViewById(R.id.done_sprintBacklog);
        timer= findViewById(R.id.sprintBacklog_countdown);
        title.setText(intent.getStringExtra("title"));
        description.setText(intent.getStringExtra("description"));

        start_timer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                countdown.startTimer(timer);
            }
        });

        stop_timer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                countdown.reset(timer);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlanningSprintBacklogItemActivity.this, PlanningSprintBacklogActivity.class);
                startActivity(i);
            }
        });
    }


    //Sendet den besprochenen Punkt als "closed"
    private void updateBacklogList(int i){


        //ServerDaten
        RetrofitService.getRetrofitInstance().create(BacklogService.class).setStatusSprintBoard(i).enqueue(new Callback<MeetingPoints>() {
            @Override
            public void onResponse(Call<MeetingPoints> call, Response<MeetingPoints> response) {
                Log.i("Retrofit", response.toString());
            }

            @Override
            public void onFailure(Call<MeetingPoints> call, Throwable t) {
                Log.e("Retrofit","Failed");

            }
        });

    }
    @Override
    public void onRobotFocusGained(QiContext qiContext) {

    }

    @Override
    public void onRobotFocusLost() {
        finish();
    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }

}