package com.example.scrummaster.activity;



import android.content.Intent;
import android.os.Bundle;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.conversation.Phrase;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.example.scrummaster.R;

public class WelcomeActivity extends RobotActivity implements RobotLifecycleCallbacks {






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);
        setContentView(R.layout.activity_willkommen);


    }


    @Override
    public void onRobotFocusGained(QiContext qiContext) {
  //Dieses Intent ruft den gescannten Namen aus der MainActivity auf
        Intent intent = getIntent();

        //String test = "Jasmin";
        String name = intent.getStringExtra("participant");
        //So soll der ParticipantController begrüßt werden.
        Phrase welcome = new Phrase ("Hallo, " + name +". Schön das du da bist. Bitte nehme Platz.");
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

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}