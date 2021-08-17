package com.example.viandland;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityViewRecipe extends AppCompatActivity {

    String recipe_id, from;
    ImageView recipe_image;
    CollapsingToolbarLayout recipeName;
    TextView recipe_category, recipe_cook, recipe_preparation, recipeDescription;
    String instructions = "";
    String ingredients = "";
    TextView recipeInstructionsText;
    TextView recipeIngredientsText;

    ProgressDialog progressDialog;
    Button addToFavoritesButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe_details);
        recipe_id = getIntent().getStringExtra("recipe_id");
        from = getIntent().getStringExtra("from");

        recipe_image = findViewById(R.id.recipe_image);
        recipeName = findViewById(R.id.recipeName);
        recipe_category = findViewById(R.id.recipeCategory);
        recipe_cook = findViewById(R.id.recipe_cook);
        recipe_preparation = findViewById(R.id.recipe_preparation);
        recipeDescription = findViewById(R.id.recipeDescription);
        recipeInstructionsText = findViewById(R.id.recipeInstructions);
        recipeIngredientsText = findViewById(R.id.recipeIngredients);

        if (from.equalsIgnoreCase("recipes")){
            getAllRecipeDataFromRecipes(recipe_id);
        }
        if (from.equalsIgnoreCase("trending_recipes")){
            getAllRecipeDataFromTrendingRecipes(recipe_id);
        }
        if (from.equalsIgnoreCase("todays_recipe")) {
            getAllRecipeDataFromTodaysRecipes(recipe_id);

        }

        addToFavoritesButton = findViewById(R.id.addToFavoritesButton);
        addToFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddToUsersFavorite(SharedPrefManager.getUid(), from, recipe_id);


            }
        });



    }

    private void AddToUsersFavorite(int uid, String from, String recipe_id) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_ADDTOFAVORITES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject messageObj= new JSONObject(response);
                            if (messageObj.getString("message").equalsIgnoreCase("Recipe is already listed as your favorite")){
                                Toast.makeText(ActivityViewRecipe.this, "This recipe is currently listed as your favorite", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ActivityViewRecipe.this, "Recipe succecssfully added to your favorites", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityViewRecipe.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uid", String.valueOf(uid));
                params.put("recipe_id", recipe_id);
                params.put("table_name", from);
                return params;
            }
        };

        RequestHandler.getInstance(ActivityViewRecipe.this).addToRequestQueue(stringRequest);
    }

    private void getAllRecipeDataFromTodaysRecipes(String recipe_id) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETRECIPEDATAFROMTODAYS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray recipes = new JSONArray(response);

                            for (int i = 0; i <  recipes.length(); i++){
                                JSONObject recipesObject =  recipes.getJSONObject(i);

                                int recipe_id_text = recipesObject.getInt("recipe_id");
                                String recipe_name_text = recipesObject.getString("recipe_name");
                                String recipe_description_text = recipesObject.getString("recipe_description");
                                String recipe_prep_time_text = recipesObject.getString("recipe_prep_time");
                                String recipe_image_text = recipesObject.getString("recipe_image");
                                String cook_name_text = recipesObject.getString("cook_name");
                                String recipe_categoryText = recipesObject.getString("recipe_category");

                                Glide.with(ActivityViewRecipe.this)
                                        .load(recipe_image_text)
                                        //.placeholder(R.drawable.loader)
                                        .into(recipe_image);
                                recipeName.setTitle(recipe_name_text);
                                recipe_category.setText(recipe_categoryText );
                                recipe_cook.setText(cook_name_text);
                                recipe_preparation.setText(recipe_prep_time_text);
                                recipeDescription.setText(recipe_description_text);

                            }

                            if (from.equalsIgnoreCase("recipes")){
                                addIngredientsFromRecipes(recipe_id);
                                addInstructionsFromRecipes(recipe_id);
                            }
                            if (from.equalsIgnoreCase("trending_recipes")){
                                addIngredientsFromTrendingRecipes(recipe_id);
                                addInstructionsFromTrendingRecipes(recipe_id);
                            }
                            if (from.equalsIgnoreCase("todays_recipe")) {
                                addIngredientsFromTodaysRecipe(recipe_id);
                                addInstructionFromTodaysRecipe(recipe_id);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityViewRecipe.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("recipe_id", recipe_id);
                return params;
            }
        };

        RequestHandler.getInstance(ActivityViewRecipe.this).addToRequestQueue(stringRequest);

    }

    private void getAllRecipeDataFromTrendingRecipes(String recipe_id) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETRECIPEDATAFROMTRENDING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray recipes = new JSONArray(response);

                            for (int i = 0; i <  recipes.length(); i++){
                                JSONObject recipesObject =  recipes.getJSONObject(i);

                                int recipe_id_text = recipesObject.getInt("recipe_id");
                                String recipe_name_text = recipesObject.getString("recipe_name");
                                String recipe_description_text = recipesObject.getString("recipe_description");
                                String recipe_prep_time_text = recipesObject.getString("recipe_prep_time");
                                String recipe_image_text = recipesObject.getString("recipe_image");
                                String cook_name_text = recipesObject.getString("cook_name");
                                String recipe_categoryText = recipesObject.getString("recipe_category");

                                Glide.with(ActivityViewRecipe.this)
                                        .load(recipe_image_text)
                                        //.placeholder(R.drawable.loader)
                                        .into(recipe_image);
                                recipeName.setTitle(recipe_name_text);
                                recipe_category.setText(recipe_categoryText );
                                recipe_cook.setText(cook_name_text);
                                recipe_preparation.setText(recipe_prep_time_text);
                                recipeDescription.setText(recipe_description_text);

                            }

                            if (from.equalsIgnoreCase("recipes")){
                                addIngredientsFromRecipes(recipe_id);
                                addInstructionsFromRecipes(recipe_id);
                            }
                            if (from.equalsIgnoreCase("trending_recipes")){
                                addIngredientsFromTrendingRecipes(recipe_id);
                                addInstructionsFromTrendingRecipes(recipe_id);
                            }
                            if (from.equalsIgnoreCase("todays_recipe")) {
                                addIngredientsFromTodaysRecipe(recipe_id);
                                addInstructionFromTodaysRecipe(recipe_id);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityViewRecipe.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("recipe_id", recipe_id);
                return params;
            }
        };

        RequestHandler.getInstance(ActivityViewRecipe.this).addToRequestQueue(stringRequest);

    }


    private void getAllRecipeDataFromRecipes(String recipe_id) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETRECIPEDATAFROMRECIPES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray recipes = new JSONArray(response);

                            for (int i = 0; i <  recipes.length(); i++){
                                JSONObject recipesObject =  recipes.getJSONObject(i);

                                int recipe_id_text = recipesObject.getInt("recipe_id");
                                String recipe_name_text = recipesObject.getString("recipe_name");
                                String recipe_description_text = recipesObject.getString("recipe_description");
                                String recipe_prep_time_text = recipesObject.getString("recipe_prep_time");
                                String recipe_image_text = recipesObject.getString("recipe_image");
                                String cook_name_text = recipesObject.getString("cook_name");
                                String recipe_categoryText = recipesObject.getString("recipe_category");

                                Glide.with(ActivityViewRecipe.this)
                                        .load(recipe_image_text)
                                        //.placeholder(R.drawable.loader)
                                        .into(recipe_image);
                                recipeName.setTitle(recipe_name_text);
                                recipe_category.setText(recipe_categoryText );
                                recipe_cook.setText(cook_name_text);
                                recipe_preparation.setText(recipe_prep_time_text);
                                recipeDescription.setText(recipe_description_text);

                            }

                            if (from.equalsIgnoreCase("recipes")){
                                addIngredientsFromRecipes(recipe_id);
                                addInstructionsFromRecipes(recipe_id);
                            }
                            if (from.equalsIgnoreCase("trending_recipes")){
                                addIngredientsFromTrendingRecipes(recipe_id);
                                addInstructionsFromTrendingRecipes(recipe_id);
                            }
                            if (from.equalsIgnoreCase("todays_recipe")) {
                                addIngredientsFromTodaysRecipe(recipe_id);
                                addInstructionFromTodaysRecipe(recipe_id);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityViewRecipe.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("recipe_id", recipe_id);
                return params;
            }
        };

        RequestHandler.getInstance(ActivityViewRecipe.this).addToRequestQueue(stringRequest);

    }


    private void addInstructionsFromRecipes(String recipe_id) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETINSTRUCTIONFROMRECIPES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray recipes = new JSONArray(response);

                            for (int i = 0; i <  recipes.length(); i++){
                                JSONObject recipesObject =  recipes.getJSONObject(i);

                                String instruction_id = recipesObject.getString("instruction_id");
                                int recipe_id_text = recipesObject.getInt("recipe_id");
                                String recipe_instructions = recipesObject.getString("recipe_instruction");

                                instructions = instructions + String.valueOf(i+1)+". " + recipe_instructions + "\n";

                            }
                            recipeInstructionsText.setText(instructions );



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityViewRecipe.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("recipe_id", recipe_id);
                return params;
            }
        };

        RequestHandler.getInstance(ActivityViewRecipe.this).addToRequestQueue(stringRequest);
    }


    private void addIngredientsFromTrendingRecipes(String recipe_id) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETINSTRUCTIONFROMTRENDING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray recipes = new JSONArray(response);

                            for (int i = 0; i <  recipes.length(); i++){
                                JSONObject recipesObject =  recipes.getJSONObject(i);

                                String instruction_id = recipesObject.getString("instruction_id");
                                int recipe_id_text = recipesObject.getInt("recipe_id");
                                String recipe_instructions = recipesObject.getString("recipe_instruction");

                                instructions = instructions + String.valueOf(i+1)+". " + recipe_instructions + "\n";

                            }
                            recipeInstructionsText.setText(instructions );



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityViewRecipe.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("recipe_id", recipe_id);
                return params;
            }
        };

        RequestHandler.getInstance(ActivityViewRecipe.this).addToRequestQueue(stringRequest);

    }


    private void addInstructionFromTodaysRecipe(String recipe_id) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETINSTRUCTIONFROMTODAYS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray recipes = new JSONArray(response);

                            for (int i = 0; i <  recipes.length(); i++){
                                JSONObject recipesObject =  recipes.getJSONObject(i);

                                String instruction_id = recipesObject.getString("instruction_id");
                                int recipe_id_text = recipesObject.getInt("recipe_id");
                                String recipe_instructions = recipesObject.getString("recipe_instruction");

                                instructions = instructions + String.valueOf(i+1)+". " + recipe_instructions + "\n";

                            }
                            recipeInstructionsText.setText(instructions );



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityViewRecipe.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("recipe_id", recipe_id);
                return params;
            }
        };

        RequestHandler.getInstance(ActivityViewRecipe.this).addToRequestQueue(stringRequest);

    }


    private void addIngredientsFromTodaysRecipe(String recipe_id) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETINGREDIENTSFROMTODAYS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray recipes = new JSONArray(response);

                            for (int i = 0; i <  recipes.length(); i++){
                                JSONObject recipesObject =  recipes.getJSONObject(i);

                                String ingredients_id = recipesObject.getString("ingredients_id");
                                int recipe_id_text = recipesObject.getInt("recipe_id");
                                String recipe_ingredients = recipesObject.getString("recipe_ingredients");

                                ingredients = ingredients + recipe_ingredients + "\n";

                            }
                            recipeIngredientsText.setText(ingredients);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityViewRecipe.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("recipe_id", recipe_id);
                return params;
            }
        };

        RequestHandler.getInstance(ActivityViewRecipe.this).addToRequestQueue(stringRequest);

    }
    private void addInstructionsFromTrendingRecipes(String recipe_id) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETINGREDIENTSFROMTRENDING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray recipes = new JSONArray(response);

                            for (int i = 0; i <  recipes.length(); i++){
                                JSONObject recipesObject =  recipes.getJSONObject(i);

                                String ingredients_id = recipesObject.getString("ingredients_id");
                                int recipe_id_text = recipesObject.getInt("recipe_id");
                                String recipe_ingredients = recipesObject.getString("recipe_ingredients");

                                ingredients =ingredients + recipe_ingredients + "\n";

                            }
                            recipeIngredientsText.setText(ingredients);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityViewRecipe.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("recipe_id", recipe_id);
                return params;
            }
        };

        RequestHandler.getInstance(ActivityViewRecipe.this).addToRequestQueue(stringRequest);
    }
    private void addIngredientsFromRecipes(String recipe_id) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETINGREDIENTSFROMRECIPES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray recipes = new JSONArray(response);

                            for (int i = 0; i <  recipes.length(); i++){
                                JSONObject recipesObject =  recipes.getJSONObject(i);

                                String ingredients_id = recipesObject.getString("ingredients_id");
                                int recipe_id_text = recipesObject.getInt("recipe_id");
                                String recipe_ingredients = recipesObject.getString("recipe_ingredients");

                               ingredients =ingredients + recipe_ingredients + "\n";

                            }
                            recipeIngredientsText.setText(ingredients);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityViewRecipe.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("recipe_id", recipe_id);
                return params;
            }
        };

        RequestHandler.getInstance(ActivityViewRecipe.this).addToRequestQueue(stringRequest);

    }


}