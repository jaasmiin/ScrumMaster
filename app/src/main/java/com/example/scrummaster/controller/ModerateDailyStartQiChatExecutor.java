package com.example.scrummaster.controller;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.object.conversation.BaseQiChatExecutor;
import com.example.scrummaster.activity.ModerationDailyStartActivity;

import java.util.List;

public class ModerateDailyStartQiChatExecutor extends BaseQiChatExecutor {
    private final QiContext qiContext;

    public ModerateDailyStartQiChatExecutor(QiContext context){
        super(context);
        this.qiContext = context;
    }


    @Override
    public void runWith(List<String> params) {
        ModerationDailyStartActivity.getInstance().clickButton();

    }

    @Override
    public void stop() {

    }
}
