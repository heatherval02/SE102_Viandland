package com.example.viandland;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchRecipeActivity extends AppCompatActivity {

    RecyclerView searchResultsRecyclerView;
    AdapterSearchCategory searchCategoryAdapter;
    List<ModelSearchCategoryResults> modelSearchCategoryResultsList;

    ImageButton back;

    ProgressDialog progressDialog;

    androidx.appcompat.widget.SearchView searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);

        back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        modelSearchCategoryResultsList = new ArrayList<>();
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclcerView);
        searchResultsRecyclerView.setHasFixedSize(true);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");


        searchText = findViewById(R.id.search_view);
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressDialog = new ProgressDialog(SearchRecipeActivity.this);
                progressDialog.setMessage("Please wait while retrieving your Recipes");
                progressDialog.show();
                searchFood(searchText.getQuery().toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void searchFood(String query) {

        String tags = query.trim();

        modelSearchCategoryResultsList.clear();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETFOODNAMESEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try {
                            JSONArray places = new JSONArray(response);

                            for (int i = 0; i < places.length(); i++){
                                JSONObject recipesObject = places.getJSONObject(i);

                                int recipe_id = recipesObject.getInt("recipe_id");
                                String recipe_name = recipesObject.getString("recipe_name");
                                String recipe_date_added = recipesObject.getString("recipe_date_added");
                                String recipe_description = recipesObject.getString("recipe_id");
                                String recipe_prep_time = "Date Added : "+recipesObject.getString("recipe_description");
                                String recipe_image = recipesObject.getString("recipe_image");
                                String cook_name = "Cook : "+recipesObject.getString("cook_name");
                                String category = recipesObject.getString("category");

                                modelSearchCategoryResultsList.add(new ModelSearchCategoryResults(recipe_id, recipe_name,recipe_description,recipe_date_added,cook_name ,recipe_prep_time,recipe_image,category));

                            }
                            searchCategoryAdapter = new AdapterSearchCategory(SearchRecipeActivity.this, modelSearchCategoryResultsList);
                            searchResultsRecyclerView.setAdapter(searchCategoryAdapter);

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(SearchRecipeActivity.this, "Error JSON "+ e, Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(SearchRecipeActivity.this, "Error : "+  error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("tags", tags);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }


}