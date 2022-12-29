package com.example.scrummaster.activity.dailyScrum;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scrummaster.R;

public class DailyEmpty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_empty);
        Intent i = new Intent(DailyEmpty.this,DailySprintBacklog.class);
        startActivity(i);
    }
}