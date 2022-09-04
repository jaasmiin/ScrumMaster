package com.example.scrummaster.activity;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.example.scrummaster.R;
import com.example.scrummaster.datamodel.MyList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class WillkommenActivity extends RobotActivity implements RobotLifecycleCallbacks {

    ArrayList <String> teilnehmerliste=new ArrayList<String>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);
        setContentView(R.layout.activity_willkommen);

        //Testen ob das Laden und speichern der Liste geklappt hat in einem TextView
       // TextView text = (TextView) findViewById(R.id.bla);
       // text.setText(listToString(loadTeilnehmerListe()));

    }


    //Methode wandelt die gespeicherte Teilnehmerliste in einen einzelnen String um und gibt diesen aus
    private ArrayList<String> loadTeilnehmerListe(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("teilnehmerListe",null);
        Type type= new TypeToken<ArrayList<String>>(){}.getType();
        teilnehmerliste= gson.fromJson(json,type);

              return teilnehmerliste;
        }

        private String listToString (ArrayList<String> liste){
            String ausgabeListe = String.join(" ,",liste);
            return ausgabeListe;
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