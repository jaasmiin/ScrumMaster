package com.example.scrummaster.controller;

import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class Countdown {

    private long START_TIME_IN_MILLIS;
    private CountDownTimer countDownTimer;
    private boolean timerstatus;
    private long timeleft;

    public Countdown(long START_TIME_IN_MILLIS,  long timeleft) {
        this.START_TIME_IN_MILLIS =START_TIME_IN_MILLIS;
        this.timeleft = timeleft;
    }


   public void startTimer(TextView textView){
        countDownTimer = new CountDownTimer(timeleft,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleft = millisUntilFinished;
                updateCountdownText(textView);
            }

            @Override
            public void onFinish() {
                timerstatus = false;
                timeleft = START_TIME_IN_MILLIS;
                //Nächster Name aus der Liste und Timer starten

            }


        }.start();
        timerstatus = true;

    }

    public void  updateCountdownText(TextView textView){
        int minuten = (int) (timeleft / 1000) /60;
        int sekunden = (int) (timeleft / 1000) % 60;

        String showLeftTime= String.format(Locale.getDefault(),"%02d:%02d",minuten,sekunden);
        textView.setText(showLeftTime);

    }

}
