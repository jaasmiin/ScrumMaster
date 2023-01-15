package com.example.scrummaster.activity.dailyScrum;

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
import com.example.scrummaster.controller.RetrofitController;

public class DailySprintBacklogItemActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private TextView title;
    private TextView description;
    private Button back;
    private Button status_doing;
    private Button status_done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QiSDK.register(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_sprint_backog_item);
        Intent intent =getIntent();
        title = findViewById(R.id.dailysprintBacklogItem_title);
        description = findViewById(R.id.dailysprintBacklogItem_description);

        status_doing = findViewById(R.id.status_doing);
        status_done =findViewById(R.id.status_done);
        back = findViewById(R.id.dailySprintBacklogItem_finished);
        title.setText(intent.getStringExtra("title"));
        description.setText(intent.getStringExtra("description"));





    }






    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Intent intent =getIntent();
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(DailySprintBacklogItemActivity.this, DailySprintBacklogActivity.class);
                startActivity(i);
            }
        });

        status_done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RetrofitController.setItemStatusDone(intent.getIntExtra("iid",0));
                RetrofitController.getSprintBacklog(DailySprintBacklogItemActivity.this);
                Intent i = new Intent(DailySprintBacklogItemActivity.this, DailyEmpty.class);
                startActivity(i);
            }
        });

        status_doing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RetrofitController.setItemStatusDoing(intent.getIntExtra("iid",0));
                RetrofitController.getSprintBacklog(DailySprintBacklogItemActivity.this);
                Intent i = new Intent(DailySprintBacklogItemActivity.this, DailyEmpty.class);
                startActivity(i);

            }
        });


    }

    @Override
    public void onRobotFocusLost() {
        RetrofitController.getSprintBacklog(this);
        finish();
    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }

}