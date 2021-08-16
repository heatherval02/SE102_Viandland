package com.example.viandland;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterFavoriteRecipes extends RecyclerView.Adapter<AdapterFavoriteRecipes.AdapterFavoriteRecipesViewHolder>{

    Context mCtx;
    List<ModelFavoriteRecipes> modelFavoriteRecipesList;

    public AdapterFavoriteRecipes(Context mCtx, List<ModelFavoriteRecipes> modelFavoriteRecipesList) {
        this.mCtx = mCtx;
        this.modelFavoriteRecipesList = modelFavoriteRecipesList;
    }

    @NonNull
    @Override
    public AdapterFavoriteRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_favorites_recipes, null);
        return new AdapterFavoriteRecipes.AdapterFavoriteRecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFavoriteRecipesViewHolder holder, int position) {
       ModelFavoriteRecipes modelFavoriteRecipes = modelFavoriteRecipesList.get(position);

        Glide.with(mCtx)
                .load(modelFavoriteRecipes.getRecipe_image())
                //.placeholder(R.drawable.loader)
                .into(holder.recipeImage);
        holder.recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(mCtx, ActivityViewRecipe.class);
                newIntent.putExtra("recipe_id", String.valueOf(modelFavoriteRecipes.recipe_id));
                newIntent.putExtra("from", String.valueOf(modelFavoriteRecipes.table_name));
                mCtx.startActivity(newIntent);
            }
        });

        holder.removeFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                Toast.makeText(mCtx, "Delete CLick on favorite id : " + String.valueOf(modelFavoriteRecipes.favorites_id), Toast.LENGTH_SHORT).show();
            }
        });

        holder.recipeTitle.setText(modelFavoriteRecipes.getRecipe_name());
        holder.recipeCook.setText(modelFavoriteRecipes.getRecipe_cook());
        holder.recipeDateAdded.setText(modelFavoriteRecipes.getRecipe_date_added());

    }

    @Override
    public int getItemCount() {
        return modelFavoriteRecipesList.size();
    }

    class AdapterFavoriteRecipesViewHolder extends RecyclerView.ViewHolder {

        ImageView recipeImage;
        TextView recipeTitle,recipeCook, recipeDateAdded;
        ImageButton removeFavoriteButton;


        public AdapterFavoriteRecipesViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeImage = itemView.findViewById(R.id.recipeImage);
            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            recipeCook = itemView.findViewById(R.id.recipeCook);
            recipeDateAdded = itemView.findViewById(R.id.recipeDateAdded);
            removeFavoriteButton = itemView.findViewById(R.id.removeFavoritesButton);

        }
    }
}
