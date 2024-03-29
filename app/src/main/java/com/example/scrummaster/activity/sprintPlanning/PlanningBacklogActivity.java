package com.example.scrummaster.activity.sprintPlanning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.conversation.Phrase;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.example.scrummaster.R;
import com.example.scrummaster.controller.RetrofitController;
import com.example.scrummaster.datamodel.Items;

import java.util.ArrayList;

public class PlanningBacklogActivity extends RobotActivity implements RobotLifecycleCallbacks {
    private Button userStory_1;
    private Button userStory_2;
    private Button userStory_3;
    private Button userStory_4;
    private Button userStory_5;
    private Button userStory_6;
    private Button finish;
    Phrase moderation = new Phrase("Hallo, willkommen bei unserem Sprint Planning eins Meeting. Ich zeige euch euer aktuelles Product Backlog." +
            "Achtet bei der Besprechung darauf die Items mit der größeren Priorisierung auszuwählen. Diese habe ich euch farblich gekennzeichnet." +
            "Die Farbe rot hat eine hohe, die Farbe orange eine mittlere und die Farbe grün eine niedrige Priorisierung. Wenn ihr die Beschreibung der Items sehen wollt, klickt einfach auf das Item.");

    private ArrayList<Items> backlogList;


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

        backlogList = RetrofitController.loadIssues(this);

        if (backlogList.size() >= 1 && ! backlogList.get(0).getLabels().contains("SprintBacklog")) {
        userStory_1.setText(backlogList.get(0).getTitle());
        userStory_1.setVisibility(View.VISIBLE);
        userStory_1.setBackgroundColor(buttonBackround(backlogList.get(0).getWeight()));
        }

        if (backlogList.size() >= 2 && ! backlogList.get(1).getLabels().contains("SprintBacklog")) {
            userStory_2.setText(backlogList.get(1).getTitle());
            userStory_2.setVisibility(View.VISIBLE);
            userStory_2.setBackgroundColor(buttonBackround(backlogList.get(1).getWeight()));
        }

        if (backlogList.size() >= 3 && ! backlogList.get(2).getLabels().contains("SprintBacklog")) {
            userStory_3.setText(backlogList.get(2).getTitle());
            userStory_3.setVisibility(View.VISIBLE);
            userStory_3.setBackgroundColor(buttonBackround(backlogList.get(2).getWeight()));
        }

        if (backlogList.size() >= 4 && ! backlogList.get(3).getLabels().contains("SprintBacklog")) {
            userStory_4.setText(backlogList.get(3).getTitle());
            userStory_4.setVisibility(View.VISIBLE);
            userStory_4.setBackgroundColor(buttonBackround(backlogList.get(3).getWeight()));
        }

        if (backlogList.size() >= 5 && ! backlogList.get(4).getLabels().contains("SprintBacklog")) {
            userStory_5.setText(backlogList.get(4).getTitle());
            userStory_5.setVisibility(View.VISIBLE);
            userStory_5.setBackgroundColor(buttonBackround(backlogList.get(4).getWeight()));
        }

        if (backlogList.size() >= 6 && ! backlogList.get(5).getLabels().contains("SprintBacklog")) {
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




    @Override
    public void onRobotFocusGained(QiContext qiContext) {

        //Auswahlfrage
        Say say = SayBuilder.with(qiContext)
                .withPhrase(moderation)
                .build();
        say.run();


    }

    @Override
    public void onRobotFocusLost() {
        finish();

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}