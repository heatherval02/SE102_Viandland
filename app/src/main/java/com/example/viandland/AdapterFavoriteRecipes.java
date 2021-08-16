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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterFavoriteRecipes extends RecyclerView.Adapter<AdapterFavoriteRecipes.AdapterFavoriteRecipesViewHolder>{

    Context mCtx;
    List<ModelFavoriteRecipes> modelFavoriteRecipesList;
    IUserRecycler mListener;

    public AdapterFavoriteRecipes(Context mCtx, List<ModelFavoriteRecipes> modelFavoriteRecipesList, IUserRecycler mListener) {
        this.mCtx = mCtx;
        this.modelFavoriteRecipesList = modelFavoriteRecipesList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public AdapterFavoriteRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_favorites_recipes, null);
        return new AdapterFavoriteRecipes.AdapterFavoriteRecipesViewHolder(view, mListener);
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
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        Constants.URL_REMOVEFAVORITES,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if (obj.getString("message").equalsIgnoreCase("Success")){
                                        Toast.makeText(mCtx, "Recipe deleted successfully", Toast.LENGTH_SHORT).show();
                                        mListener.refreshFavoritesList();

                                    } else {
                                        Toast.makeText(mCtx, "Error Occured Please contact support", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    Toast.makeText(mCtx, "Error on JSON HERE: "+ e, Toast.LENGTH_SHORT).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mCtx, "Error :" + error, Toast.LENGTH_SHORT).show();
                            }
                        }

                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("uid", String.valueOf(SharedPrefManager.getUid()));
                        params.put("favorites_id", modelFavoriteRecipes.favorites_id);
                        return params;
                    }
                };

                RequestHandler.getInstance(mCtx).addToRequestQueue(stringRequest);



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
        IUserRecycler mListener;

        public AdapterFavoriteRecipesViewHolder(@NonNull View itemView, IUserRecycler mListener) {
            super(itemView);
            this.mListener = mListener;
            recipeImage = itemView.findViewById(R.id.recipeImage);
            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            recipeCook = itemView.findViewById(R.id.recipeCook);
            recipeDateAdded = itemView.findViewById(R.id.recipeDateAdded);
            removeFavoriteButton = itemView.findViewById(R.id.removeFavoritesButton);

        }
    }


    interface IUserRecycler{
        void refreshFavoritesList();
    }
}
