package com.example.scrummaster.controller;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.object.conversation.BaseQiChatExecutor;
import com.example.scrummaster.activity.retrospective.RetrospectiveCheckinActivity;

import java.util.List;

public class ModerateNotesQiChatExecutor extends BaseQiChatExecutor {
private final QiContext qiContext;



public ModerateNotesQiChatExecutor(QiContext context){
    super(context);
    this.qiContext = context;
}



    @Override
    public void runWith(List<String> params) {
        RetrospectiveCheckinActivity.getInstance().clickButton();

    }

    @Override
    public void stop() {

    }





}
