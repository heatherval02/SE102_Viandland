package com.example.viandland;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterSearchCategory extends RecyclerView.Adapter<AdapterSearchCategory.AdapterSearchCategoryViewHolder> {

    Context mCtx;
    List<ModelSearchCategoryResults> modelSearchCategoryResultsList;

    public AdapterSearchCategory(Context mCtx, List<ModelSearchCategoryResults> modelSearchCategoryResultsList) {
        this.mCtx = mCtx;
        this.modelSearchCategoryResultsList = modelSearchCategoryResultsList;
    }


    @NonNull
    @Override
    public AdapterSearchCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_categories_results, null);
        return new AdapterSearchCategory.AdapterSearchCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSearchCategoryViewHolder holder, int position) {

        ModelSearchCategoryResults modelCategoryResults = modelSearchCategoryResultsList.get(position);

        Glide.with(mCtx)
                .load( modelCategoryResults.getRecipe_image())
                //.placeholder(R.drawable.loader)
                .into(holder.recipeImage);
        holder.recipeTitle.setText( modelCategoryResults.getRecipe_name());
        holder.recipeCook.setText( modelCategoryResults.getRecipe_cook());
        holder.recipeDateAdded.setText( modelCategoryResults.getRecipe_date_added());

    }

    @Override
    public int getItemCount() {
        return modelSearchCategoryResultsList.size();
    }

    class AdapterSearchCategoryViewHolder extends RecyclerView.ViewHolder{
        ImageView recipeImage;
        TextView recipeTitle,recipeCook, recipeDateAdded;

        public AdapterSearchCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            recipeCook = itemView.findViewById(R.id.recipeCook);
            recipeDateAdded = itemView.findViewById(R.id.recipeDateAdded);

        }

    }
}