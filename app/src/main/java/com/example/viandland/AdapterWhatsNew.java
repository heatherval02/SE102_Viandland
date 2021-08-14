package com.example.viandland;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdapterWhatsNew extends RecyclerView.Adapter<AdapterWhatsNew.AdapaterWhatsNewViewHolder>{

    List<ModelWhatsNew> modelWhatsNewList;
    Context mCtx;

    public AdapterWhatsNew(List<ModelWhatsNew> modelWhatsNewList, Context mCtx) {
        this.modelWhatsNewList = modelWhatsNewList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public AdapaterWhatsNewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_recentlly_added_recipe, null);
        return new AdapterWhatsNew.AdapaterWhatsNewViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapaterWhatsNewViewHolder holder, int position) {
        ModelWhatsNew modelrecentlyAdded = modelWhatsNewList.get(position);

        Glide.with(mCtx)
                .load(modelrecentlyAdded.getRecipe_image())
                //.placeholder(R.drawable.loader)
                .into(holder.recipeImage);
        holder.recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(mCtx, ActivityViewRecipe.class);
                newIntent.putExtra("recipe_id", String.valueOf(modelrecentlyAdded.recipe_id));
                newIntent.putExtra("from", "recipes");
                mCtx.startActivity(newIntent);
            }
        });

        holder.recipeTitle.setText(modelrecentlyAdded.getRecipe_name());
        holder.recipeCook.setText(modelrecentlyAdded.getCook_name());
        holder.recipeDateAdded.setText(modelrecentlyAdded.getRecipe_date_added());


    }

    @Override
    public int getItemCount() {
        return modelWhatsNewList.size();
    }

    class AdapaterWhatsNewViewHolder extends RecyclerView.ViewHolder {

        ImageView recipeImage;
        TextView recipeTitle;
        TextView recipeCook;
        TextView recipeDateAdded;

        public AdapaterWhatsNewViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeImage = itemView.findViewById(R.id.recipeImage);
            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            recipeCook = itemView.findViewById(R.id.recipeCook);
            recipeDateAdded = itemView.findViewById(R.id.recipeDateAdded);

        }
    }

}
