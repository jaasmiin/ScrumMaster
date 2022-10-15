package com.example.scrummaster.controller;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.scrummaster.datamodel.PostNotes;
import com.example.scrummaster.service.PostNoteService;
import com.example.scrummaster.service.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//Versuch die Methoden auszulagrn, wenn nicht klappt löschen
public class ParticipantController {

    //Lädt die TeilnehmerListe und gibt diese zurück
    public ArrayList<String> loadParticipantList(Context c){
        ArrayList <String> participantList;
        SharedPreferences sharedPreferences = c.getSharedPreferences("shared preferences",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("participantList",null);
        Type type= new TypeToken<ArrayList<String>>(){}.getType();
        participantList = gson.fromJson(json,type);

        return participantList;
    }
   public void sendParticipants(Context c, PostNotes liste){

        //Diese Methode ist ausgebelendet, weil Pepper Emulator damit abstürzt--> Nullpointer
        //PostNotes liste = new PostNotes(listToString(loadParticipantList(c)));
        //Test Liste für Emulator
        //  ArrayList<String> test = new ArrayList<>();
        //test.add("Peter");
        //test.add("Bla");
        //PostNotes liste = new PostNotes(listToString(test));

        //ServerDaten
        RetrofitService.getRetrofitInstance().create(PostNoteService.class).sendTeilnehmerListe(liste).enqueue(new Callback<PostNotes>() {
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
    public String listToString (ArrayList<String> liste){
        String output = String.join(" ,",liste);
        return output;

    }



}
