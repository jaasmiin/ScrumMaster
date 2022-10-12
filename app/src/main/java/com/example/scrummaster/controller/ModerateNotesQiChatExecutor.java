package com.example.scrummaster.controller;

import android.content.Context;
import android.widget.Button;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.object.conversation.BaseQiChatExecutor;
import com.example.scrummaster.activity.ModerationNotesActivity;

import java.util.List;

public class ModerateNotesQiChatExecutor extends BaseQiChatExecutor {
private final QiContext qiContext;
Button b;
Context c;


public ModerateNotesQiChatExecutor(QiContext context){
    super(context);
    this.qiContext = context;


}



    @Override
    public void runWith(List<String> params) {
        ModerationNotesActivity.getInstance().clickButton();
    }

    @Override
    public void stop() {

    }





}
