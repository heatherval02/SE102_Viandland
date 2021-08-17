package com.example.viandland;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterOwnRecipes extends RecyclerView.Adapter<AdapterOwnRecipes.AdapterOwnRecipesViewHolder>{


    Context mCtx;
    List<ModelOwnRecipes> modelOwnRecipesList;
    IUserRecycler mListener;

    public AdapterOwnRecipes(Context mCtx, List<ModelOwnRecipes> modelOwnRecipesList, IUserRecycler mListener) {
        this.mCtx = mCtx;
        this.modelOwnRecipesList = modelOwnRecipesList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public AdapterOwnRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_own_recipes, null);
        return new AdapterOwnRecipes.AdapterOwnRecipesViewHolder(view, mListener);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOwnRecipesViewHolder holder, int position) {
        ModelOwnRecipes modelOwnRecipes = modelOwnRecipesList.get(position);
        holder.recipeName.setText(modelOwnRecipes.recipe_name);

        Glide.with(mCtx)
                .load(modelOwnRecipes.recipe_image)
                //.placeholder(R.drawable.loader)
                .into(holder.recipeImage);

        holder.recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(mCtx, ActivityViewRecipe.class);
                newIntent.putExtra("recipe_id", String.valueOf(modelOwnRecipes.recipe_id));
                newIntent.putExtra("from", "recipes");
                mCtx.startActivity(newIntent);

            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.showEditDialog(String.valueOf(modelOwnRecipes.recipe_id));
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelOwnRecipesList.size();
    }

    class AdapterOwnRecipesViewHolder extends RecyclerView.ViewHolder {

        ImageView recipeImage;
        TextView recipeName;
        ImageView editButton;
        IUserRecycler mListener;

        public AdapterOwnRecipesViewHolder(@NonNull View itemView,  IUserRecycler mListener) {
            super(itemView);
            this.mListener = mListener;
            recipeImage = itemView.findViewById(R.id.recipeImage);
            recipeName = itemView.findViewById(R.id.recipeName);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }

    interface IUserRecycler{
        void showEditDialog(String recipeId);
    }


}
