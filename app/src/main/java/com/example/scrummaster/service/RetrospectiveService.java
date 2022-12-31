package com.example.scrummaster.service;

import com.example.scrummaster.datamodel.Items;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface RetrospectiveService {

    //https://git.scc.kit.edu/api/v4/projects/35214/milestones


    @Headers("PRIVATE-TOKEN: F6JsHQc4Q7z5i4_aLHFU")
    @GET("35214/issues?labels=Frage")
    Call<List<Items>> getQuestion();



}
