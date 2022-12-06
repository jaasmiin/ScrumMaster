package com.example.scrummaster.activity.daily;

import android.content.Intent;
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
import com.example.scrummaster.activity.MeetingFinished;
import com.example.scrummaster.datamodel.MeetingPoints;
import com.example.scrummaster.service.BacklogService;
import com.example.scrummaster.service.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Dieses SprintBoard dient dazu die Items auf Doing und Done zu setzen
public class DailySprintBacklog extends RobotActivity implements RobotLifecycleCallbacks {
    private Button sprintStory_1;
    private Button sprintStory_2;
    private Button sprintStory_3;
    private Button sprintStory_4;
    private Button sprintStory_5;
    private Button sprintStory_6;
    private Button finished;
    private TextView headline;

    private ArrayList<MeetingPoints> backlogList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QiSDK.register(this, this);
        super.onCreate(savedInstanceState);
        backlogList = loadSprintBoard();

        setContentView(R.layout.activity_daily_sprint_backlog);
        headline = findViewById(R.id.headlindeDailyBacklog);
        sprintStory_1 = findViewById(R.id.dailysprintStory_1);
        sprintStory_2 = findViewById(R.id.dailysprintStory_2);
        sprintStory_3 = findViewById(R.id.dailysprintStory_3);
        sprintStory_4 = findViewById(R.id.dailysprintStory_4);
        sprintStory_5 = findViewById(R.id.dailysprintStory_5);
        sprintStory_6 = findViewById(R.id.dailysprintStory_6);
        finished = findViewById(R.id.sprintbacklogtofinished);


        if (backlogList.size() >= 1) {
            sprintStory_1.setText(backlogList.get(0).getTitle());
            sprintStory_1.setVisibility(View.VISIBLE);
            sprintStory_1.setBackgroundColor(buttonBackround(backlogList.get(0).getLabels()));
        }

        if (backlogList.size() >= 2) {
            sprintStory_2.setText(backlogList.get(1).getTitle());
            sprintStory_2.setVisibility(View.VISIBLE);
            sprintStory_2.setBackgroundColor(buttonBackround(backlogList.get(1).getLabels()));
        }

        if (backlogList.size() >= 3) {
            sprintStory_3.setText(backlogList.get(2).getTitle());
            sprintStory_3.setVisibility(View.VISIBLE);
            sprintStory_3.setBackgroundColor(buttonBackround(backlogList.get(2).getLabels()));
        }

        if (backlogList.size() >= 4) {
            sprintStory_4.setText(backlogList.get(3).getTitle());
            sprintStory_4.setVisibility(View.VISIBLE);
            sprintStory_4.setBackgroundColor(buttonBackround(backlogList.get(3).getLabels()));
        }

        if (backlogList.size() >= 5) {
            sprintStory_5.setText(backlogList.get(4).getTitle());
            sprintStory_5.setVisibility(View.VISIBLE);
            sprintStory_5.setBackgroundColor(buttonBackround(backlogList.get(4).getLabels()));
        }

        if (backlogList.size() >= 6) {
            sprintStory_6.setText(backlogList.get(5).getTitle());
            sprintStory_6.setVisibility(View.VISIBLE);
            sprintStory_6.setBackgroundColor(buttonBackround(backlogList.get(5).getLabels()));
        }

        finished.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DailySprintBacklog.this, MeetingFinished.class);
                startActivity(intent);

            }
        });

        sprintStory_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DailySprintBacklog.this, DailySprintBacklogItemActivity.class);
                intent.putExtra("title", backlogList.get(0).getTitle());
                intent.putExtra("description", backlogList.get(0).getDescription());
                intent.putExtra("iid", backlogList.get(0).getIid());
                startActivity(intent);

            }
        });
        sprintStory_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DailySprintBacklog.this, DailySprintBacklogItemActivity.class);
                intent.putExtra("title", backlogList.get(1).getTitle());
                intent.putExtra("description", backlogList.get(1).getDescription());
                intent.putExtra("iid", backlogList.get(1).getIid());
                startActivity(intent);

            }
        });
        sprintStory_3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DailySprintBacklog.this, DailySprintBacklogItemActivity.class);
                intent.putExtra("title", backlogList.get(2).getTitle());
                intent.putExtra("description", backlogList.get(2).getDescription());
                intent.putExtra("iid", backlogList.get(2).getIid());
                startActivity(intent);

            }
        });
        sprintStory_4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DailySprintBacklog.this, DailySprintBacklogItemActivity.class);
                intent.putExtra("title", backlogList.get(3).getTitle());
                intent.putExtra("description", backlogList.get(3).getDescription());
                intent.putExtra("iid", backlogList.get(3).getIid());
                startActivity(intent);

            }
        });
        sprintStory_5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DailySprintBacklog.this, DailySprintBacklogItemActivity.class);
                intent.putExtra("title", backlogList.get(4).getTitle());
                intent.putExtra("description", backlogList.get(4).getDescription());
                intent.putExtra("iid", backlogList.get(4).getIid());
                startActivity(intent);

            }
        });
        sprintStory_6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DailySprintBacklog.this, DailySprintBacklogItemActivity.class);
                intent.putExtra("title", backlogList.get(5).getTitle());
                intent.putExtra("description", backlogList.get(5).getDescription());
                intent.putExtra("iid", backlogList.get(5).getIid());
                startActivity(intent);

            }
        });

    }


    private int buttonBackround(List<String> list) {
        if (list.contains("In Bearbeitung")) {
            return getResources().getColor(R.color.purple_500);
        } else
            return getResources().getColor(R.color.green);

    }




    //Lädt die gespeicherte SprintBoard aus sharedPreferences
    private ArrayList<MeetingPoints> loadSprintBoard() {
        getSprintBacklog();
        ArrayList<MeetingPoints> issueList;
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("SprintBoard", null);
        Type type = new TypeToken<ArrayList<MeetingPoints>>() {
        }.getType();
        issueList = gson.fromJson(json, type);

        return issueList;
    }
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


    }

    @Override
    public void onRobotFocusLost() {
        finish();
    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}