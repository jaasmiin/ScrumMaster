package com.example.scrummaster.activity.tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.viewpager2.widget.ViewPager2;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.example.scrummaster.R;
import com.example.scrummaster.controller.PowerPointAdapter;
import com.example.scrummaster.datamodel.SliderItem;

import java.util.ArrayList;
import java.util.List;

public class PowerPointAdvancedActivity extends RobotActivity implements RobotLifecycleCallbacks {
    private ViewPager2 viewPager2;
    Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QiSDK.register(this,this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_point_advanced);

        btn_back = findViewById(R.id.advanced_done);
        viewPager2 =findViewById(R.id.viewpager_advanced);

        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.p2_f1));
        sliderItems.add(new SliderItem(R.drawable.p2_f2));
        sliderItems.add(new SliderItem(R.drawable.p2_f3));
        sliderItems.add(new SliderItem(R.drawable.p2_f4));
        sliderItems.add(new SliderItem(R.drawable.p2_f5));
        sliderItems.add(new SliderItem(R.drawable.p2_f6));
        sliderItems.add(new SliderItem(R.drawable.p2_f7));
        sliderItems.add(new SliderItem(R.drawable.p2_f8));
        sliderItems.add(new SliderItem(R.drawable.p2_f9));
        sliderItems.add(new SliderItem(R.drawable.p2_f10));
        sliderItems.add(new SliderItem(R.drawable.p2_f11));
        sliderItems.add(new SliderItem(R.drawable.p2_f12));
        sliderItems.add(new SliderItem(R.drawable.p2_f13));
        sliderItems.add(new SliderItem(R.drawable.p2_f14));
        sliderItems.add(new SliderItem(R.drawable.p2_f15));

        //Klick auf Button Anfänger geht in die Beginner Activity

        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PowerPointAdvancedActivity.this, PowerPointStartActivity.class);
                intent.putExtra("bookmark_advanced","select2");
                startActivity(intent);

            }
        });


        viewPager2.setAdapter(new PowerPointAdapter(sliderItems,viewPager2));
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {

    }

    @Override
    public void onRobotFocusLost() {
        finish();
    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}