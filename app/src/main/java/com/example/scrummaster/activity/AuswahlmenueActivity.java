package com.example.scrummaster.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.example.scrummaster.R;
import com.example.scrummaster.datamodel.PostNotes;
import com.example.scrummaster.service.PostNoteService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuswahlmenueActivity extends RobotActivity implements RobotLifecycleCallbacks {

    ArrayList <String> teilnehmerliste = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auswahlmenue);
           //sobald die Activity gestartet wurde, wird die Teilnehmerliste an Gitlab geschickt
        sendTeilnehmer();


    }

    //Sendet die Teilnehmerliste zu Gitlab
    private void sendTeilnehmer(){

        //Diese Methode ist ausgebelendet, weil Pepper Emulator damit abstürzt--> Nullpointer
        //PostNotes liste = new PostNotes(listToString(loadTeilnehmerListe()));

        PostNotes pepperestliste = new PostNotes("Test");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://git.scc.kit.edu/api/v4/projects/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostNoteService postNoteService =retrofit.create(PostNoteService.class);

        Call<PostNotes> call = postNoteService.sendTeilnehmerListe(pepperestliste);

        call.enqueue(new Callback<PostNotes>() {
            @Override
            public void onResponse(Call<PostNotes> call, Response<PostNotes> response) {
                Toast.makeText(AuswahlmenueActivity.this,response.toString(),Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<PostNotes> call, Throwable t) {

            }
        });
    }

    //Ladet die TeilnehmerListe und gibt diese zurück
    private ArrayList<String> loadTeilnehmerListe(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("teilnehmerListe",null);
        Type type= new TypeToken<ArrayList<String>>(){}.getType();
        teilnehmerliste= gson.fromJson(json,type);

        return teilnehmerliste;
    }
    //Wandelt eine StringListe in einen String um und gibt diese aus
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