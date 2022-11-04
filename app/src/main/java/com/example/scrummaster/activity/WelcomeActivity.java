package com.example.scrummaster.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.conversation.Phrase;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.example.scrummaster.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class WelcomeActivity extends RobotActivity implements RobotLifecycleCallbacks {






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);
        setContentView(R.layout.activity_willkommen);


    }
    //Methode speichert die Teilnehmerliste
    private void saveParticipantList(String participant){
        if (participant != null) {
            ArrayList<String> participantList = loadParticipantList();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            participantList.add(participant);
            String json = gson.toJson(participantList);
            editor.putString("participantList", json);
            editor.apply();
        }

    }
    //Methode speichert die Teilnehmerliste
    private ArrayList<String> loadParticipantList() {

        ArrayList<String> participantList;
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("participantList",null);
        Type type= new TypeToken<ArrayList<String>>(){}.getType();
        participantList = gson.fromJson(json,type);
        return participantList;

    }

        @Override
    public void onRobotFocusGained(QiContext qiContext) {
  //Dieses Intent ruft den gescannten Namen aus der MainActivity auf
        Intent intent = getIntent();
        //String test = "Jasmin";
        String name = intent.getStringExtra("participant");
        saveParticipantList(name);
        Phrase welcome;
        //So soll der ParticipantController begrüßt werden. Es wird unterschieden zwischen Gast und Teilnehmer.
        if (name.contains("Gast")) {
            welcome = new Phrase ("Hallo, schön dass du da bist. Bitte denke daran, ass du heute passiver Teilehmer bist." +
                    "daher bitte ich dich zuzuhören, falls du fragen oder anmerkungen hast teile diese bitte nach dem Meeting mit. Danke für" +
                    "dein Verständis. Nehme bitte Platz.");

        } else {
        welcome = new Phrase ("Hallo, " + name + " schön das du da bist. Bitte nehme Platz.");
        }
        //Begrüßung
        Say say = SayBuilder.with(qiContext)
                .withPhrase(welcome)
                .build();
        say.run();

        //Nach der Begrüßung wechselt Pepper in die MainActivity
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));



    }

    @Override
    public void onRobotFocusLost() {
        finish();

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}