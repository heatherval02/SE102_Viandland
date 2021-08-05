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

public class AdapterSearchCategoryResults extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context mCtx;
    List<ModelSearchCategoryResults> modelSearchCategoryResultsList;

    public AdapterSearchCategoryResults(Context mCtx, List<ModelSearchCategoryResults> modelSearchCategoryResultsList) {
        this.mCtx = mCtx;
        this.modelSearchCategoryResultsList = modelSearchCategoryResultsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_categories_results, null);
        return new AdapterSearchCategoryResults.AdapterSearchCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ModelSearchCategoryResults searchResults = modelSearchCategoryResultsList.get(position);
        Glide.with(mCtx)
                .load(searchResults.getRecipe_image())
                //.placeholder(R.drawable.loader)
                .into(holder.recipeImage);

        holder.recipeTitle.setText(modelrecentlyAdded.getRecipe_name());
        holder.recipeCook.setText(modelrecentlyAdded.getCook_name());
        holder.recipeDateAdded.setText(modelrecentlyAdded.getRecipe_date_added());


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
