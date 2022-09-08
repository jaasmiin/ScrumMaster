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

public class WillkommenActivity extends RobotActivity implements RobotLifecycleCallbacks {
    //Dieses Intent ruft den gescannten Namen aus der MainActivity auf
    Intent intent = getIntent();

   String test = "Jasmin";
//    String name = intent.getStringExtra("teilnehmer");

    //So soll der Teilnehmer begrüßt werden.
    Phrase willkommen = new Phrase ("Hallo, " + test +". Schön das du da bist. Bitte nehme Platz.");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);
        setContentView(R.layout.activity_willkommen);


    }


    @Override
    public void onRobotFocusGained(QiContext qiContext) {

        //Begrüßung
        Say say = SayBuilder.with(qiContext)
                .withPhrase(willkommen)
                .build();
        say.run();

        //Nach der Begrüßung wechselt Pepper in die MainActivity
        startActivity(new Intent(WillkommenActivity.this, AuswahlmenueActivity.class));



    }

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}