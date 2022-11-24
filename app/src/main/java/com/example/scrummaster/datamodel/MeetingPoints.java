package com.example.scrummaster.datamodel;

import com.google.gson.annotations.SerializedName;


public class MeetingPoints {

    @SerializedName("id")
   private String id;

    @SerializedName("state_event")
    private String state;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("weight")
    private int weight;

    @SerializedName("iid")
    private int iid;

    public int getIid() {
        return iid;
    }

    public int getWeight() {
        return weight;
    }

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

    public MeetingPoints(String state) {
        this.id = id;
        this.state = state;
        this.title = title;
        this.description = description;
    }

}


