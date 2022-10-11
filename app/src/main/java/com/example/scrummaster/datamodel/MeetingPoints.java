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


