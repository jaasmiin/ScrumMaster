package com.example.scrummaster.controller;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.scrummaster.datamodel.MeetingPoints;
import com.example.scrummaster.service.BacklogService;
import com.example.scrummaster.service.RetrofitService;
import com.example.scrummaster.service.RetrospectiveService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitController  {
 static public void getQuestion(Context c) {


     //Holt die Frage von GitLab
        RetrofitService.getRetrofitInstance().create(RetrospectiveService.class).getQuestion().enqueue(new Callback<List<MeetingPoints>>() {
            @Override
            public void onResponse(Call<List<MeetingPoints>> call, Response<List<MeetingPoints>> response) {
                Log.i("Retrofit", new Gson().toJson(response.body()));
                SharedPreferences sharedPreferences = c.getSharedPreferences("shared preferences",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                List<MeetingPoints> meetingPointsList= response.body();
                String json = gson.toJson(meetingPointsList);
                editor.putString("question",json);
                editor.apply();

            }
            @Override
            public void onFailure(Call<List<MeetingPoints>> call, Throwable t) {
                String fail =t.getCause().toString();
                Log.e("Retrofit",fail);
            }
        });

    }

    //Holt die Issues von GitLab
   static public void getIssues(Context c) {

        RetrofitService.getRetrofitInstance().create(BacklogService.class).getIssues().enqueue(new Callback<List<MeetingPoints>>() {
            @Override
            public void onResponse(Call<List<MeetingPoints>> call, Response<List<MeetingPoints>> response) {
                Log.i("Retrofit", new Gson().toJson(response.body()));
                SharedPreferences sharedPreferences = c.getSharedPreferences("shared preferences",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                List<MeetingPoints> meetingPointsList= response.body();
                String json = gson.toJson(meetingPointsList);
                editor.putString("IssueList",json);
                editor.apply();
            }

            @Override
            public void onFailure(Call<List<MeetingPoints>> call, Throwable t) {
                String fail =t.getCause().toString();
                Log.e("Retrofit",fail);
            }
        });

    }

    //Holt die zu Beprechendne Punkte aus GitLab
    static public void getMeetingPoints(Context c) {

        RetrofitService.getRetrofitInstance().create(RetrospectiveService.class).getQuestion().enqueue(new Callback<List<MeetingPoints>>() {
            @Override
            public void onResponse(Call<List<MeetingPoints>> call, Response<List<MeetingPoints>> response) {
                Log.i("Retrofit", new Gson().toJson(response.body()));
                SharedPreferences sharedPreferences = c.getSharedPreferences("shared preferences",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                List<MeetingPoints> meetingPointsList= response.body();
                String json = gson.toJson(meetingPointsList);
                editor.putString("meetingPointList",json);
                editor.apply();
            }

            @Override
            public void onFailure(Call<List<MeetingPoints>> call, Throwable t) {
                String fail =t.getCause().toString();
                Log.e("Retrofit",fail);
            }
        });

    }

    //Kopiert die MeetingPointList
    static public void copyMeetingPointList (Context c){
        ArrayList<MeetingPoints> meetingPointList;
        //Die Origonal MeetingPointListe laden
        SharedPreferences sharedPreferences = c.getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("meetingPointList",null);
        Type type= new TypeToken<ArrayList<MeetingPoints>>(){}.getType();
        meetingPointList = gson.fromJson(json,type);
        //Die OriginalMeetingPointListe als Kopie speichern
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gsonCopy = new Gson();
        String jsonCopy = gsonCopy.toJson(meetingPointList);
        editor.putString("meetingPointListCopy",jsonCopy);
        editor.apply();

    }

    //Lädt die gespeicherte SprintBoard aus sharedPreferences
    static public ArrayList<MeetingPoints> loadSprintBoard(Context c) {
        getSprintBacklog(c);
        ArrayList<MeetingPoints> issueList;
        SharedPreferences sharedPreferences = c.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("SprintBoard", null);
        Type type = new TypeToken<ArrayList<MeetingPoints>>() {
        }.getType();
        issueList = gson.fromJson(json, type);

        return issueList;
    }

    //Holt Das Sprint Backlog aus GitLab nud speichert sie
    static public void getSprintBacklog(Context c) {

        RetrofitService.getRetrofitInstance().create(BacklogService.class).getSprintBacklog().enqueue(new Callback<List<MeetingPoints>>() {
            @Override
            public void onResponse(Call<List<MeetingPoints>> call, Response<List<MeetingPoints>> response) {
                Log.i("Retrofit", new Gson().toJson(response.body()));
                SharedPreferences sharedPreferences = c.getSharedPreferences("shared preferences",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                List<MeetingPoints> meetingPointsList= response.body();
                String json = gson.toJson(meetingPointsList);
                editor.putString("SprintBoard",json);
                editor.apply();
            }

            @Override
            public void onFailure(Call<List<MeetingPoints>> call, Throwable t) {
                String fail =t.getCause().toString();
                Log.e("Retrofit",fail);
            }
        });

    }

    //Sendet den besprochenen Punkt als "closed"
    static public void setItemStatusDone(int i){
        //ServerDaten
        RetrofitService.getRetrofitInstance().create(BacklogService.class).closeBacklogItem(i).enqueue(new Callback<MeetingPoints>() {
            @Override
            public void onResponse(Call<MeetingPoints> call, Response<MeetingPoints> response) {
                Log.i("Retrofit", response.toString());

            }

            @Override
            public void onFailure(Call<MeetingPoints> call, Throwable t) {
                Log.e("Retrofit","Failed");

            }
        });

    }

    //Sendet den besprochenen Punkt als "doing"
    static public void setItemStatusDoing(int i){
        //ServerDaten
        RetrofitService.getRetrofitInstance().create(BacklogService.class).setStatusDoing(i).enqueue(new Callback<MeetingPoints>() {
            @Override
            public void onResponse(Call<MeetingPoints> call, Response<MeetingPoints> response) {
                Log.i("Retrofit", response.toString());


            }

            @Override
            public void onFailure(Call<MeetingPoints> call, Throwable t) {
                Log.e("Retrofit","Failed");

            }
        });


    }

    //Lädt die gespeicherte Backlog Items aus sharedPreferences
   static public ArrayList<MeetingPoints> loadIssues(Context c){
        ArrayList<MeetingPoints> issueList;
        SharedPreferences sharedPreferences = c.getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("IssueList",null);
        Type type= new TypeToken<ArrayList<MeetingPoints>>(){}.getType();
        issueList = gson.fromJson(json,type);

        return issueList;
    }

    //Sendet den besprochenen Punkt als "closed"
    static public void updateBacklogList(int i){


        //ServerDaten
        RetrofitService.getRetrofitInstance().create(BacklogService.class).setStatusSprintBoard(i).enqueue(new Callback<MeetingPoints>() {
            @Override
            public void onResponse(Call<MeetingPoints> call, Response<MeetingPoints> response) {
                Log.i("Retrofit", response.toString());

            }

            @Override
            public void onFailure(Call<MeetingPoints> call, Throwable t) {
                Log.e("Retrofit","Failed");

            }

        });

    }

    //Lädt die Besprechungspunkte speichert diese als Kopie in Shared Preferences und gibt die Kopie zurück
    static public ArrayList<MeetingPoints> loadMeetingPointListCopy(Context c){

        ArrayList <MeetingPoints> meetingPointListCopy;

        SharedPreferences sharedPreferences = c.getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("meetingPointListCopy",null);
        Type type= new TypeToken<ArrayList<MeetingPoints>>(){}.getType();
        meetingPointListCopy = gson.fromJson(json,type);
        return meetingPointListCopy;
    }

    //Lädt die aktuelle Frage aus sharedPreferences
    static public String loadQuestion(Context c){
        ArrayList <MeetingPoints> questions;
        SharedPreferences sharedPreferences = c.getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("question",null);
        Type type= new TypeToken<ArrayList<MeetingPoints>>(){}.getType();
        questions = gson.fromJson(json,type);
        return questions.get(0).getTitle();

    }












}
