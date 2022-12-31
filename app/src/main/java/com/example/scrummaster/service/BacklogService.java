package com.example.scrummaster.service;

import com.example.scrummaster.datamodel.Items;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BacklogService {

    //https://git.scc.kit.edu/api/v4/projects/35214/


    @Headers("PRIVATE-TOKEN: F6JsHQc4Q7z5i4_aLHFU")
    @GET("35214/issues?labels=ProductBacklog&state=opened")
    Call<List<Items>> getIssues();

    @Headers("PRIVATE-TOKEN: F6JsHQc4Q7z5i4_aLHFU")
    @PUT("35214/issues/{iid}?add_labels=SprintBacklog")
    Call<Items> setStatusSprintBoard(@Path("iid") int iid);

    @Headers("PRIVATE-TOKEN: F6JsHQc4Q7z5i4_aLHFU")
    @PUT("35214/issues/{iid}?add_labels=In Bearbeitung")
    Call<Items> setStatusDoing(@Path("iid") int iid);

    @Headers("PRIVATE-TOKEN: F6JsHQc4Q7z5i4_aLHFU")
    @PUT("35214/issues/{iid}?state_event=close")
    Call<Items>closeBacklogItem(@Path("iid") int iid);

    @Headers("PRIVATE-TOKEN: F6JsHQc4Q7z5i4_aLHFU")
    @GET("35214/issues?labels=SprintBacklog&state=opened")
    Call<List<Items>> getSprintBacklog();



}
