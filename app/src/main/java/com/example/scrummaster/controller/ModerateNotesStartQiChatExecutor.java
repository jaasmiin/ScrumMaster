package com.example.scrummaster.controller;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.object.conversation.BaseQiChatExecutor;

import java.util.List;

public class ModerateNotesStartQiChatExecutor extends BaseQiChatExecutor {
    private final QiContext qiContext;

    public ModerateNotesStartQiChatExecutor(QiContext context){
        super(context);
        this.qiContext = context;
    }


    @Override
    public void runWith(List<String> params) {

    }

    @Override
    public void stop() {

    }
}


