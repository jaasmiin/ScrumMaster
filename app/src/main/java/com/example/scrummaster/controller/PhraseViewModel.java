package com.example.scrummaster.controller;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aldebaran.qi.sdk.object.conversation.Phrase;

public class PhraseViewModel extends ViewModel {

    private MutableLiveData<Phrase> speaker;

    public MutableLiveData<Phrase> getSpeaker() {
        if (speaker == null) {
            speaker= new MutableLiveData<Phrase>();
        }
        return speaker;
    }
}
