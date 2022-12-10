package com.example.scrummaster.activity.planning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.example.scrummaster.R;
import com.example.scrummaster.controller.CountdownController;
import com.example.scrummaster.controller.RetrofitController;
//
public class PlanningBacklogItemActivity extends RobotActivity implements RobotLifecycleCallbacks {

        private TextView title;
        private TextView description;
        private TextView timer;
        private Button  back;
        private Button  save_and_back;
        private Button start_timer;
        private Button stop_timer;
        private CountdownController countdown = new CountdownController(3000,3000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QiSDK.register(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_backlog_item);
        Intent intent =getIntent();
        title = findViewById(R.id.backlog_title);
        description = findViewById(R.id.backlog_description);
        back = findViewById(R.id.back_Without_add);
        save_and_back = findViewById(R.id.backlog_addUserStory);
        start_timer =findViewById(R.id.startCbacklog);
        stop_timer= findViewById(R.id.done_backlog);
        timer= findViewById(R.id.backlog_countdown);

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

        save_and_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              RetrofitController.updateBacklogList(intent.getIntExtra("iid",0));

                Intent i = new Intent(PlanningBacklogItemActivity.this, PlanningBacklogActivity.class);
                startActivity(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlanningBacklogItemActivity.this, PlanningBacklogActivity.class);
                startActivity(i);
            }
        });
    }


   /* //Sendet den besprochenen Punkt als "closed"
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

    }*/
  /*  //Holt die komplette IssueListe mit dem Status opened komplett über gitlab
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

    }*/

    @Override
    public void onRobotFocusGained(QiContext qiContext) {

    }

    @Override
    public void onRobotFocusLost() {
        RetrofitController.getIssues(this);
        finish();
    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}