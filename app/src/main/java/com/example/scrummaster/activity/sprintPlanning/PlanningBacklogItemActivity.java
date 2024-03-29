package com.example.scrummaster.activity.sprintPlanning;

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
        private CountdownController countdown = new CountdownController(60000,60000);

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


    }




    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Intent intent =getIntent();
        start_timer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                countdown.startTimerplanningbacklogitem(timer,PlanningBacklogItemActivity.this);

            }
        });



        save_and_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RetrofitController.updateBacklogList(intent.getIntExtra("iid",0));
                RetrofitController.getIssues(PlanningBacklogItemActivity.this);
                Intent i = new Intent(PlanningBacklogItemActivity.this, PlanningEmptyActivity.class);
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
        stop_timer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                countdown.reset(timer);
                Intent i = new Intent(PlanningBacklogItemActivity.this, PlanningBacklogItemActivity.class);
                startActivity(i);
            }

        });
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