package com.example.scrummaster.service;

import com.example.scrummaster.datamodel.PostNotes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PostNoteService {

    @Headers("PRIVATE-TOKEN: F6JsHQc4Q7z5i4_aLHFU")
    @POST("35214/issues/1/notes/")
    Call<PostNotes> sendTeilnehmerListe(@Body PostNotes postnotes);

   // Call<PostNotes> sendTeilnehmerListe(@Header ("PRIVATE-TOKEN:<F6JsHQc4Q7z5i4_aLHFU>")@Body PostNotes postNotes);



}
