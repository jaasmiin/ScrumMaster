package com.example.scrummaster.activity.tools;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.example.scrummaster.R;
import com.example.scrummaster.activity.MenuActivity;
import com.example.scrummaster.datamodel.PostNotes;
import com.example.scrummaster.service.SendCommentService;
import com.example.scrummaster.service.RetrofitService;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HappinessIndexActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private int happinessValue= 0;
    private ArrayList <Integer> happinesValuesList = new ArrayList();
    private int numberOfVoters=0;

    private ImageButton happy_1;
    private ImageButton happy_2;
    private ImageButton happy_3;
    private ImageButton happy_4;
    private ImageButton happy_5;
    private Button menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QiSDK.register(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happiness_index);
        happy_1 = findViewById(R.id.btn_happy_1);
        happy_2 = findViewById(R.id.btn_happy_2);
        happy_3 = findViewById(R.id.btn_happy_3);
        happy_4 = findViewById(R.id.btn_happy_4);
        happy_5 = findViewById(R.id.btn_happy_5);
        menu= findViewById(R.id.happiness_menu);


       menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HappinessIndexActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        //Klick auf Button und erhöhen des Happynessindexes und der Anzahl der Teilnehmer
        happy_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                happinessValue = happinessValue +1;
                numberOfVoters = numberOfVoters +1;
                happinesValuesList.add(1);
                Log.i(TAG, String.valueOf(happinessValue));
            }
        });

        //Klick auf Button und erhöhen des Happynessindexes und der Anzahl der Teilnehmer
        happy_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                happinessValue = happinessValue +2;
                numberOfVoters = numberOfVoters +1;
                happinesValuesList.add(2);
                Log.i(TAG, String.valueOf(happinessValue));
            }
        });

        //Klick auf Button und erhöhen des Happynessindexes und der Anzahl der Teilnehmer
        happy_3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                happinessValue = happinessValue +3;
                numberOfVoters = numberOfVoters +1;
                happinesValuesList.add(3);
                Log.i(TAG, String.valueOf(happinessValue));
            }
        });

        //Klick auf Button und erhöhen des Happynessindexes und der Anzahl der Teilnehmer
        happy_4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                happinessValue = happinessValue +4;
                numberOfVoters = numberOfVoters +1;
                happinesValuesList.add(4);
                Log.i(TAG, String.valueOf(happinessValue));
            }
        });

        //Klick auf Button und erhöhen des Happynessindexes und der Anzahl der Teilnehmer
        happy_5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                happinessValue = happinessValue +5;
                numberOfVoters = numberOfVoters +1;
                happinesValuesList.add(5);
                Log.i(TAG, String.valueOf(happinessValue));
            }
        });


    }
    //Sendet die Teilnehmerliste zu Gitlab
    private void sendHappinessIndex(){

        String happinessList = listToString(happinesValuesList);
        float index = (float)happinessValue/numberOfVoters;

        PostNotes happinessData = new PostNotes("Die Anzahl der Abstimmer war: " +numberOfVoters + " \n Die einzelen Abtimmungen waren:" + happinessList +  "\n Das Ergebnis ist: "+  index);

        //ServerDaten
        RetrofitService.getRetrofitInstance().create(SendCommentService.class).sendHappinessIndex(happinessData).enqueue(new Callback<PostNotes>() {
            @Override
            public void onResponse(Call<PostNotes> call, Response<PostNotes> response) {
                Log.i("Retrofit", new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<PostNotes> call, Throwable t) {
                Log.e("Retrofit","Failed");

            }
        });

    }
    //Wandelt eine StringListe in einen String um und gibt diese aus
    private String listToString (ArrayList<Integer> liste){
       return liste.toString();

    }




    @Override
    public void onRobotFocusGained(QiContext qiContext) {

    }

    @Override
    public void onRobotFocusLost() {
        sendHappinessIndex();
        finish();
    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}