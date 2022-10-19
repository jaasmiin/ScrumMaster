package com.example.scrummaster.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.scrummaster.R;
import com.example.scrummaster.datamodel.SliderItem;

import java.util.List;

public class PowerPointAdapter extends RecyclerView.Adapter<PowerPointAdapter.SliderViewHolder> {
    private List<SliderItem> sliderItems;
    private ViewPager2 viewpager2;

    public PowerPointAdapter(List<SliderItem> sliderItem, ViewPager2 viewpager2) {
        this.sliderItems = sliderItem;
        this.viewpager2 = viewpager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item,
                        parent,
                        false
                )

        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;


         SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivImage);
        }

        void setImage(SliderItem sliderItem) {

           imageView.setImageResource(sliderItem.getImage());
        }

    }
}

