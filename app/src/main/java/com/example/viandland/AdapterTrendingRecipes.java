package com.example.viandland;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterTrendingRecipes extends PagerAdapter {

    private Context context;
    private ArrayList<ModelTrendingRecipes> modelTrendingRecipesArrayList;

    public AdapterTrendingRecipes(Context context, ArrayList<ModelTrendingRecipes> modelTrendingRecipesArrayList) {
        this.context = context;
        this.modelTrendingRecipesArrayList = modelTrendingRecipesArrayList;
    }

    @Override
    public int getCount() {
        return modelTrendingRecipesArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_trending_recipes_card, container, false);

        ImageView trendingRecipeImage = view.findViewById(R.id.trendingRecipeImage);
        ImageView ownerImage = view.findViewById(R.id.ownerImage);
        TextView recipeName = view.findViewById(R.id.recipeName);
        TextView recipeCook = view.findViewById(R.id.recipeCook);
        TextView recipeDateAdded = view.findViewById(R.id.recipeDateAdded);

        ModelTrendingRecipes trendingRecipesModel = modelTrendingRecipesArrayList.get(position);

        Glide.with(context)
                .load(trendingRecipesModel.getRecipe_image())
                //.placeholder(R.drawable.loader)
                .into(trendingRecipeImage);

        Glide.with(context)
                .load(trendingRecipesModel.getCook_image())
                //.placeholder(R.drawable.loader)
                .into(ownerImage);

        recipeName.setText(trendingRecipesModel.getRecipe_name());

        recipeCook.setText(trendingRecipesModel.getCook_name());

        recipeDateAdded.setText(trendingRecipesModel.getRecipe_date_added());


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You Clicked: " + trendingRecipesModel.getRecipe_id(), Toast.LENGTH_SHORT).show();
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
