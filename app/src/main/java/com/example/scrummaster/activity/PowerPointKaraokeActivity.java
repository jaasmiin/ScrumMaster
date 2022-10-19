package com.example.scrummaster.activity;

import android.os.Bundle;

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

public class PowerPointKaraokeActivity extends RobotActivity implements RobotLifecycleCallbacks {
    private ViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QiSDK.register(this,this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_point_karaoke);

        viewPager2 =findViewById(R.id.viewpager);

        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.p1_f1));
        sliderItems.add(new SliderItem(R.drawable.p1_f2));
        sliderItems.add(new SliderItem(R.drawable.p1_f3));
        sliderItems.add(new SliderItem(R.drawable.p1_f4));
        sliderItems.add(new SliderItem(R.drawable.p1_f5));
        sliderItems.add(new SliderItem(R.drawable.p1_f6));
        sliderItems.add(new SliderItem(R.drawable.p1_f7));
        sliderItems.add(new SliderItem(R.drawable.p1_f8));

        viewPager2.setAdapter(new PowerPointAdapter(sliderItems,viewPager2));
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {

    }

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}