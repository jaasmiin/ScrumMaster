package com.example.scrummaster.controller;

import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class Countdown {

    private long START_TIME_IN_MILLIS;
    private CountDownTimer countDownTimer;
    private boolean timerstatus;
    private long restzeit;

    public Countdown(long START_TIME_IN_MILLIS,  long restzeit) {
        this.START_TIME_IN_MILLIS =START_TIME_IN_MILLIS;
        this.restzeit = restzeit;
    }

   public void startTimer(TextView textView){
        countDownTimer = new CountDownTimer(restzeit,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                restzeit = millisUntilFinished;
                updateCountdownText(textView);
            }

            @Override
            public void onFinish() {
                timerstatus = false;
                //Nächster Name aus der Liste und Timer starten

            }


        }.start();
        timerstatus = true;

    }

    public void  updateCountdownText(TextView textView){
        int minuten = (int) (restzeit / 1000) /60;
        int sekunden = (int) (restzeit / 1000) % 60;

        String restzeitAnzeige= String.format(Locale.getDefault(),"%02d:%02d",minuten,sekunden);
        textView.setText(restzeitAnzeige);

    }

}
