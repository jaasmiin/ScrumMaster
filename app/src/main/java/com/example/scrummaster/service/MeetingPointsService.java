package com.example.scrummaster.service;

import com.example.scrummaster.datamodel.MeetingPoints;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MeetingPointsService {

    //https://git.scc.kit.edu/api/v4/projects/35214/milestones


    @Headers("PRIVATE-TOKEN: F6JsHQc4Q7z5i4_aLHFU")
    @GET("35214/milestones/")
    Call<List<MeetingPoints>> getPunkte();

    @Headers("PRIVATE-TOKEN: F6JsHQc4Q7z5i4_aLHFU")
    @PUT("35214/milestones/{milestones}?state_event=close")

    Call<MeetingPoints>updateMilestones(@Path("milestones") String id);

}
