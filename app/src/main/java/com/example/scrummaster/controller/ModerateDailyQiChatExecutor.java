package com.example.scrummaster.controller;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.object.conversation.BaseQiChatExecutor;
import com.example.scrummaster.activity.daily.DailyQuestionsActivity;

import java.util.List;

public class ModerateDailyQiChatExecutor extends BaseQiChatExecutor {
    private final QiContext qiContext;

    public ModerateDailyQiChatExecutor(QiContext context){
        super(context);
        this.qiContext = context;
    }


    @Override
    public void runWith(List<String> params) {
        DailyQuestionsActivity.getInstance().clickButton();
    }

    @Override
    public void stop() {

    }
}
