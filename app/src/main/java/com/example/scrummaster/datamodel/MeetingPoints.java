package com.example.scrummaster.datamodel;

import android.util.Log;

import com.example.scrummaster.service.MeetingPointsService;
import com.example.scrummaster.service.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MeetingPoints {

    @SerializedName("id")
   private String id;
    @SerializedName("state")
    private String state;
    @SerializedName("title")

    private String title;
    @SerializedName("description")
    private String description;

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public MeetingPoints(String id, String state, String title, String description) {
        this.id = id;
        this.state = state;
        this.title = title;
        this.description = description;
    }
}


