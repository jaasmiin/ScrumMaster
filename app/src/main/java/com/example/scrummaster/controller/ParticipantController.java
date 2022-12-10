package com.example.scrummaster.controller;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.scrummaster.datamodel.PostNotes;
import com.example.scrummaster.service.RetrofitService;
import com.example.scrummaster.service.SendCommentService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Kopiert die Teilnehmerliste für weiteren gebrauch
       public class ParticipantController {


        //Methode sendet die Teilnehmerliste zu GitLab
            static public void sendParticipants(Context c){

                //Diese Methode ist ausgebelendet, weil Pepper Emulator damit abstürzt--> Nullpointer
                PostNotes liste = new PostNotes(listToString(loadParticipantList(c)));
                //Test Liste für Emulator
                //  ArrayList<String> test = new ArrayList<>();
                //test.add("Peter");
                //test.add("Bla");
                //PostNotes liste = new PostNotes(listToString(test));

                //ServerDaten
                RetrofitService.getRetrofitInstance().create(SendCommentService.class).sendTeilnehmerListe(liste).enqueue(new Callback<PostNotes>() {
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

        //Methode speichert die Teilnehmerliste
            static public void saveParticipantList(String participant,Context c){
        if (participant != null) {
            ArrayList<String> participantList = loadParticipantList(c);
            SharedPreferences sharedPreferences =c.getSharedPreferences("shared preferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            participantList.add(participant);
            String json = gson.toJson(participantList);
            editor.putString("participantList", json);
            editor.apply();
        }

    }
        //Lädt die TeilnehmerListe und gibt diese zurück
            static public ArrayList<String> loadParticipantList(Context c){
                ArrayList <String> participantList;
                SharedPreferences sharedPreferences = c.getSharedPreferences("shared preferences",MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString("participantList",null);
                Type type= new TypeToken<ArrayList<String>>(){}.getType();
                participantList = gson.fromJson(json,type);

                ArrayList <String> TEST= new ArrayList<>();
                TEST.add("jasmin");
                TEST.add("Liyana");

                return TEST;
                // return participantList;
            }
        //Wandelt eine StringListe in einen String um und gibt diese aus
            static public String listToString (ArrayList<String> liste){
                String output = String.join(" ,",liste);
                return output;

            }
        //Kopiert die Teilnehmerliste
             static public void copyParticipantList (Context c){

        ArrayList<String> participantList;
        ArrayList<String> TEST = new ArrayList<>();
        TEST.add("Jasmin");
        TEST.add("Aliyah");
        //Die Origonal TeilnehmerListe laden
        SharedPreferences sharedPreferences = c.getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("participantList",null);
        Type type= new TypeToken<ArrayList<String>>(){}.getType();
        participantList = gson.fromJson(json,type);
        //Die OriginalTeilnehmerListe als Kopie speichern
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gsonCopy = new Gson();
        String jsonCopy = gsonCopy.toJson(TEST);
        editor.putString("participantListCopy",jsonCopy);
        editor.apply();

    }
       //Löscht den ersten Eintrag der gespeicherten ParticapantListCopy aus sharedPreferences
            static public void deleteParticipantListEntry(Context c) {
        ArrayList<String> l = new ArrayList<>();
        l = loadParticipantListCopy(c);
        l.remove(0);
        SharedPreferences sharedPreferences = c.getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(l);
        editor.putString("participantListCopy",json);
        editor.apply();

    }
       //Lädt die TeilnehmerListe speichert diese als Kopie in Shared Preferences und gibt die Kopie zurück
            static public ArrayList<String> loadParticipantListCopy(Context c){
        ArrayList <String> participantList;
        SharedPreferences sharedPreferences = c.getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("participantListCopy",null);
        Type type= new TypeToken<ArrayList<String>>(){}.getType();
        participantList = gson.fromJson(json,type);
        return participantList;

    }

}
