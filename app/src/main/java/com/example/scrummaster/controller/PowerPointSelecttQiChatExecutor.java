package com.example.scrummaster.controller;

import android.content.Context;
import android.content.Intent;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.object.conversation.BaseQiChatExecutor;
import com.example.scrummaster.activity.tools.PowerPointStartActivity;

import java.util.List;

public class PowerPointSelecttQiChatExecutor extends BaseQiChatExecutor {
    private final QiContext qiContext;

    public PowerPointSelecttQiChatExecutor(QiContext context) {
        super(context);
        this.qiContext = context;
    }

    @Override
    public void runWith(List<String> params) {
        Context c;
        c= PowerPointStartActivity.getInstance();
        Intent intent = new Intent(c, PowerPointStartActivity.class);
        PowerPointStartActivity.getInstance().deleteParticipantListEntry();
        PowerPointStartActivity.getInstance().startActivity(intent);
    }

    @Override
    public void stop() {

    }
}
