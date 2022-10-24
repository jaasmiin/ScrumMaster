package com.example.scrummaster.controller;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.object.conversation.BaseQiChatExecutor;
import com.example.scrummaster.activity.PowerPointStartActivity;

import java.util.List;

public class PowerPointAdvancedQiChatExecutor extends BaseQiChatExecutor {
    private final QiContext qiContext;

    public PowerPointAdvancedQiChatExecutor(QiContext context) {
        super(context);
        this.qiContext = context;
    }

    @Override
    public void runWith(List<String> params) {

        PowerPointStartActivity.getInstance().clickButtonAdvanced();

    }

    @Override
    public void stop() {

    }
}
