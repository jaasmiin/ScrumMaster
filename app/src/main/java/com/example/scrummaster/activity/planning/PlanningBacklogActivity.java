package com.example.scrummaster.activity.planning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.example.scrummaster.R;
import com.example.scrummaster.activity.MeetingFinished;
import com.example.scrummaster.datamodel.MeetingPoints;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PlanningBacklogActivity extends RobotActivity implements RobotLifecycleCallbacks {
    private Button userStory_1;
    private Button userStory_2;
    private Button userStory_3;
    private Button userStory_4;
    private Button userStory_5;
    private Button userStory_6;
    private Button finish;

    private ArrayList<MeetingPoints> backlogList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QiSDK.register(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_backlog);

        userStory_1 = findViewById(R.id.userStory_1);
        userStory_2 = findViewById(R.id.userStory_2);
        userStory_3 = findViewById(R.id.userStory_3);
        userStory_4 = findViewById(R.id.userStory_4);
        userStory_5 = findViewById(R.id.userStory_5);
        userStory_6 = findViewById(R.id.userStory_6);
        finish= findViewById(R.id.backlog_back);

        backlogList = loadIssues();

        if (backlogList.size() >= 1) {
        userStory_1.setText(backlogList.get(0).getTitle());
        userStory_1.setVisibility(View.VISIBLE);
        userStory_1.setBackgroundColor(buttonBackround(backlogList.get(0).getWeight()));
        }

        if (backlogList.size() >= 2) {
            userStory_2.setText(backlogList.get(1).getTitle());
            userStory_2.setVisibility(View.VISIBLE);
            userStory_2.setBackgroundColor(buttonBackround(backlogList.get(1).getWeight()));
        }

        if (backlogList.size() >= 3) {
            userStory_3.setText(backlogList.get(2).getTitle());
            userStory_3.setVisibility(View.VISIBLE);
            userStory_3.setBackgroundColor(buttonBackround(backlogList.get(2).getWeight()));
        }

        if (backlogList.size() >= 4) {
            userStory_4.setText(backlogList.get(3).getTitle());
            userStory_4.setVisibility(View.VISIBLE);
            userStory_4.setBackgroundColor(buttonBackround(backlogList.get(3).getWeight()));
        }

        if (backlogList.size() >= 5) {
            userStory_5.setText(backlogList.get(4).getTitle());
            userStory_5.setVisibility(View.VISIBLE);
            userStory_5.setBackgroundColor(buttonBackround(backlogList.get(4).getWeight()));
        }

        if (backlogList.size() >= 6) {
            userStory_6.setText(backlogList.get(5).getTitle());
            userStory_6.setVisibility(View.VISIBLE);
            userStory_6.setBackgroundColor(buttonBackround(backlogList.get(5).getWeight()));
        }

       userStory_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PlanningBacklogActivity.this, PlanningBacklogItemActivity.class);
                intent.putExtra("title",backlogList.get(0).getTitle());
                intent.putExtra("description",backlogList.get(0).getDescription());
                intent.putExtra("iid",backlogList.get(0).getIid());
                startActivity(intent);

            }
        });
        userStory_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PlanningBacklogActivity.this, PlanningBacklogItemActivity.class);
                intent.putExtra("title",backlogList.get(1).getTitle());
                intent.putExtra("description",backlogList.get(1).getDescription());
                intent.putExtra("iid",backlogList.get(1).getIid());
                startActivity(intent);

            }
        });
        userStory_3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PlanningBacklogActivity.this, PlanningBacklogItemActivity.class);
                intent.putExtra("title",backlogList.get(2).getTitle());
                intent.putExtra("description",backlogList.get(2).getDescription());
                intent.putExtra("iid",backlogList.get(2).getIid());
                startActivity(intent);

            }
        });
        userStory_4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PlanningBacklogActivity.this, PlanningBacklogItemActivity.class);
                intent.putExtra("title",backlogList.get(3).getTitle());
                intent.putExtra("description",backlogList.get(3).getDescription());
                intent.putExtra("iid",backlogList.get(3).getIid());
                startActivity(intent);

            }
        });
        userStory_5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PlanningBacklogActivity.this, PlanningBacklogItemActivity.class);
                intent.putExtra("title",backlogList.get(4).getTitle());
                intent.putExtra("description",backlogList.get(4).getDescription());
                intent.putExtra("iid",backlogList.get(4).getIid());
                startActivity(intent);

            }
        });
        userStory_6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PlanningBacklogActivity.this, PlanningBacklogItemActivity.class);
                intent.putExtra("title",backlogList.get(5).getTitle());
                intent.putExtra("description",backlogList.get(5).getDescription());
                intent.putExtra("iid",backlogList.get(5).getIid());
                startActivity(intent);

            }
        });
        finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PlanningBacklogActivity.this, PlanningStartActivity.class);

                startActivity(intent);

            }
        });

    }


    //Farbe je nach Gewichtung
    private int buttonBackround( int i) {
      if (i>=0 && i<= 3 ) {
          return getResources().getColor(R.color.green);
      }else if (i>3 && i<= 6 ) {
          return getResources().getColor(R.color.orange);
      } else
          return getResources().getColor(R.color.red);

    }



    //Lädt die gespeicherte Backlog Items aus sharedPreferences
    private ArrayList<MeetingPoints> loadIssues(){
        ArrayList<MeetingPoints> issueList;
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("IssueList",null);
        Type type= new TypeToken<ArrayList<MeetingPoints>>(){}.getType();
        issueList = gson.fromJson(json,type);

        return issueList;
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