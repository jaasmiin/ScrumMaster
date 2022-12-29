package com.example.scrummaster.controller;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.example.scrummaster.activity.dailyScrum.DailyQuestionsActivity;
import com.example.scrummaster.activity.retrospective.RetrospectiveActivity;

import java.util.Locale;

public class CountdownController {

    private long START_TIME_IN_MILLIS;
    private CountDownTimer countDownTimer;
    private boolean timerstatus;
    private long timeleft;
    int i =0;
    int j = 0;

    public CountdownController(long START_TIME_IN_MILLIS, long timeleft) {
        this.START_TIME_IN_MILLIS =START_TIME_IN_MILLIS;
        this.timeleft = timeleft;
    }


   public void startTimer(TextView countdownDisplay){

        countDownTimer = new CountDownTimer(timeleft,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleft = millisUntilFinished;
                updateCountdownText(countdownDisplay);


            }

            @Override
            public void onFinish() {
                timerstatus = false;
                timeleft = START_TIME_IN_MILLIS;
                       }


        }.start();
        timerstatus = true;

    }

    public void startTimerDaily(TextView countdownDisplay, Context c){


        countDownTimer = new CountDownTimer(timeleft,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleft = millisUntilFinished;
                updateCountdownText(countdownDisplay);



            }

            @Override
            public void onFinish() {
                timerstatus = false;
                timeleft = START_TIME_IN_MILLIS;
                Intent intent = new Intent(c, DailyQuestionsActivity.class);
                c.startActivity(intent);
            }

        }.start();
        timerstatus = true;

    }

    public void startTimerNotes(TextView countdownDisplay, Context c){

       
        countDownTimer = new CountDownTimer(timeleft,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleft = millisUntilFinished;
                updateCountdownText(countdownDisplay);



                     }

            @Override
            public void onFinish() {
                timerstatus = false;
                timeleft = START_TIME_IN_MILLIS;
                Intent intent = new Intent(c, RetrospectiveActivity.class);
                c.startActivity(intent);
            }

        }.start();
        timerstatus = true;

    }







    public void reset(TextView textView){
        timeleft = START_TIME_IN_MILLIS;
        updateCountdownText(textView);

    }

    public void  updateCountdownText(TextView textView){
        int minuten = (int) (timeleft / 1000) /60;
        int sekunden = (int) (timeleft / 1000) % 60;

        String showLeftTime= String.format(Locale.getDefault(),"%02d:%02d",minuten,sekunden);
        textView.setText(showLeftTime);

    }



}
