package com.example.scrummaster.activity.sprintPlanning;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scrummaster.R;

public class PlanningEmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_empty);
        Intent i = new Intent(PlanningEmptyActivity.this,PlanningBacklogActivity.class);
        startActivity(i);
    }
}