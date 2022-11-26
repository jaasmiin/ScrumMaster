package com.example.scrummaster.controller;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.object.conversation.BaseQiChatExecutor;
import com.example.scrummaster.activity.tools.PowerPointStartActivity;

import java.util.List;

public class PowerPointBeginnerQiChatExecutor extends BaseQiChatExecutor {
    private final QiContext qiContext;

    public PowerPointBeginnerQiChatExecutor(QiContext context) {
        super(context);
        this.qiContext = context;
    }

    @Override
    public void runWith(List<String> params) {

        PowerPointStartActivity.getInstance().clickButtonBeginner();

    }

    @Override
    public void stop() {

    }
}
