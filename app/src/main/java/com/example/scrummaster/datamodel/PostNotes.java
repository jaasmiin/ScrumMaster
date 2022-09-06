package com.example.scrummaster.datamodel;

import com.google.gson.annotations.SerializedName;

public class PostNotes {
    private int id;
    private int issue_iid;
    @SerializedName("body")
    private String body;
    private String created_at;

    public PostNotes(String body) {

        this.body = body;


    }



    public int getId() {
        return id;
    }

    public int getIssue_iid() {
        return issue_iid;
    }

    public String getBody() {
        return body;
    }

    public String getCreated_at() {
        return created_at;
    }


}
