package com.example.scrummaster.controller;

import static android.content.Context.MODE_PRIVATE;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import com.example.scrummaster.activity.MainActivity;
import com.example.scrummaster.activity.MeetingFinished;
import com.example.scrummaster.activity.ModerationNotesActivity;
import com.example.scrummaster.activity.SelectionMenuActivity;
import com.example.scrummaster.datamodel.MeetingPoints;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Countdown {

    private long START_TIME_IN_MILLIS;
    private CountDownTimer countDownTimer;
    private boolean timerstatus;
    private long timeleft;
    int i =0;
    int j = 0;

    public Countdown(long START_TIME_IN_MILLIS,  long timeleft) {
        this.START_TIME_IN_MILLIS =START_TIME_IN_MILLIS;
        this.timeleft = timeleft;
    }


   public void startTimer(TextView countdownDisplay, TextView name, TextView point, Context c){
       List<MeetingPoints> meetingPointsList= loadMeetingPoints(c);
       //ArrayList<String> participantList = loadTeilnehmerListe(c);
       ArrayList<String> participantList = new ArrayList<>();
       participantList.add("Name1");
       participantList.add("Name2");

       int sizeofParticipantList = participantList.size();
       int sizeofMeetingPointList = meetingPointsList.size();

        countDownTimer = new CountDownTimer(timeleft,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleft = millisUntilFinished;
                updateCountdownText(countdownDisplay);


                if (i<sizeofParticipantList){
                    point.setText(meetingPointsList.get(j).getDescription());
                    name.setText(participantList.get(i));

                }
                        else { if (j < sizeofMeetingPointList-1)
                                { i=0;
                                    j = j + 1;
                                        } else {

                                                Intent intent = new Intent(c, MeetingFinished.class);
                                                c.startActivity(intent);
                }
                }

            }



            @Override
            public void onFinish() {
                timerstatus = false;
                timeleft = START_TIME_IN_MILLIS;
                i= i+1;


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






    //Lädt die TeilnehmerListe und gibt diese zurück
    private ArrayList<String> loadTeilnehmerListe(Context c){


        ArrayList <String> participantList ;
        SharedPreferences sharedPreferences = c.getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("participantList",null);
        Type type= new TypeToken<ArrayList<String>>(){}.getType();
        participantList = gson.fromJson(json,type);

        return participantList;
    }

    //Lädt die gespeicherte MeetingPointListe aus sharedPreferences
    private ArrayList<MeetingPoints> loadMeetingPoints(Context c) {
        ArrayList<MeetingPoints> meetingPointList;
        SharedPreferences sharedPreferences = c.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("meetingPointList", null);
        Type type = new TypeToken<ArrayList<MeetingPoints>>() {
        }.getType();
        meetingPointList = gson.fromJson(json, type);

        return meetingPointList;

    }


}
