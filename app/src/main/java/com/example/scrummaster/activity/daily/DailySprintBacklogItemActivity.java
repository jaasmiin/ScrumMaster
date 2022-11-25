package com.example.scrummaster.activity.daily;

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

public class DailySprintBacklogItemActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private TextView title;
    private TextView description;
    private Button back;
    private Button status_doing;
    private Button status_done;
    private CountdownController countdown = new CountdownController(3000,3000);

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


        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(DailySprintBacklogItemActivity.this, DailySprintBacklog.class);
                startActivity(i);
            }
        });

        status_done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setItemStatusDone(intent.getIntExtra("iid",0));
                Intent i = new Intent(DailySprintBacklogItemActivity.this, DailySprintBacklog.class);
                startActivity(i);
            }
        });

        status_doing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setItemStatusDoing(intent.getIntExtra("iid",0));
                Intent i = new Intent(DailySprintBacklogItemActivity.this, DailySprintBacklog.class);
                startActivity(i);
            }
        });



    }


    //Sendet den besprochenen Punkt als "closed"
    private void setItemStatusDone(int i){
        //ServerDaten
        RetrofitService.getRetrofitInstance().create(BacklogService.class).closeBacklogItem(i).enqueue(new Callback<MeetingPoints>() {
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

    //Sendet den besprochenen Punkt als "doing"
    private void setItemStatusDoing(int i){
        //ServerDaten
        RetrofitService.getRetrofitInstance().create(BacklogService.class).setStatusDoing(i).enqueue(new Callback<MeetingPoints>() {
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