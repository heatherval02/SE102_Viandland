package com.example.viandland;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdapterWhatsNew extends PagerAdapter {

    private Context context;
    private ArrayList<ModelWhatsNew> modelWhatsNewArrayList;

    public AdapterWhatsNew(Context context, ArrayList<ModelWhatsNew> modelWhatsNewArrayList) {
        this.context = context;
        this.modelWhatsNewArrayList = modelWhatsNewArrayList;
    }

    @Override
    public int getCount() {
        return modelWhatsNewArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_whats_new_card, container, false);

        ImageView trendingRecipeImage = view.findViewById(R.id.trendingRecipeImage);
        ImageView ownerImage = view.findViewById(R.id.ownerImage);
        TextView recipeName = view.findViewById(R.id.recipeName);
        TextView recipeCook = view.findViewById(R.id.recipeCook);
        TextView recipeDateAdded = view.findViewById(R.id.recipeDateAdded);

        ModelWhatsNew whatsNewModel = modelWhatsNewArrayList.get(position);

        Glide.with(context)
                .load(whatsNewModel.getRecipe_image())
                .placeholder(R.drawable.favorites)
                .into(trendingRecipeImage);

        Glide.with(context)
                .load(whatsNewModel.getCook_image())
                    .placeholder(R.drawable.favorites)
                .into(ownerImage);

        recipeName.setText(whatsNewModel.getRecipe_name());

        recipeCook.setText(whatsNewModel.getCook_name());

        recipeDateAdded.setText(whatsNewModel.getRecipe_date_added());


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You Clicked: " + whatsNewModel.getRecipe_id(), Toast.LENGTH_SHORT).show();
            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
