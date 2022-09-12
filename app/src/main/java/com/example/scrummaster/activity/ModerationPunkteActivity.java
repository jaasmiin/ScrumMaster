package com.example.scrummaster.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.example.scrummaster.R;
import com.example.scrummaster.controller.Countdown;

import java.util.Locale;

public class ModerationPunkteActivity extends RobotActivity implements RobotLifecycleCallbacks {


    Countdown countdownPunkte  = new Countdown(100000,50000);
    private TextView countdown;
    private Button btn_fertig;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderationpunkte);
        countdown = findViewById(R.id.countdown);
        btn_fertig= findViewById(R.id.fertig);

        btn_fertig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdownPunkte.startTimer(countdown);
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