package com.example.scrummaster.datamodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Items {

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

    @SerializedName("labels")
    List<String> labels;

    public List<String> getLabels() {
        return labels;
    }

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

    public Items(String state) {
        this.id = id;
        this.state = state;
        this.title = title;
        this.description = description;
    }

}


