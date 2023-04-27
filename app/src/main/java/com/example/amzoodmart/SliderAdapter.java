package com.example.amzoodmart;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    public SliderAdapter(Context context){
        this.context =context;
    }
    int imagesArray[] ={
            R.drawable.onboardscreen1,
            R.drawable.onboardscreen2,
            R.drawable.onboardscreen3
    };

    int descriptionArray[] ={
           R.string.description,
            R.string.description,
            R.string.description
    };

    int headingArray[] = {
            R.string.first_slide,
            R.string.second_slide,
            R.string.third_slide
    };

    @Override
    public int getCount() {
        return headingArray.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout)object;
    }

}
