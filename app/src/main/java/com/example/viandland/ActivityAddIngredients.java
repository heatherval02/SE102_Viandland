package com.example.viandland;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ActivityAddIngredients extends AppCompatActivity{

    Button openDialogForIngredients;
    ImageView backBtn;
    String recipeId;

    RecyclerView ingredientsRecyclerView;
    AdapterIngredientsList ingredientsListAdapter;
    List<ModelIngredientsList> modelIngredientsLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);
        //Add adapter Here
        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView);
        ingredientsRecyclerView.setHasFixedSize(true);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(ActivityAddIngredients.this));

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(ActivityAddIngredients.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View confirmDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_recipe, viewGroup, false);

        Button confirmButton = confirmDialogView.findViewById(R.id.confirmButton);
        Button cancelButton = confirmDialogView.findViewById(R.id.cancelButton);

        confirmDialog .setView(confirmDialogView);
        AlertDialog alertDialogConfirm = confirmDialog .create();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipe(getIntent().getStringExtra("temp_id"), getIntent().getStringExtra("uniq_id"), String.valueOf(SharedPrefManager.getUid()));
                alertDialogConfirm.dismiss();


            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityAddIngredients.this, "Cancelled", Toast.LENGTH_SHORT).show();
                alertDialogConfirm.dismiss();
                finish();
            }
        });

        alertDialogConfirm.show();


        openDialogForIngredients = findViewById(R.id.openDialogButton);
        openDialogForIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAddIngredients.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_add_ingredients, viewGroup, false);

                EditText ingredientsText = dialogView.findViewById(R.id.ingredientsText);
                Button addIngredientsButton = dialogView.findViewById(R.id.addIngredientsBtn);

                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                addIngredientsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmAddIngredients(recipeId.trim(), ingredientsText.getText().toString());
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

    }

    private void confirmAddIngredients(String recipeId, String recipeText) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_SETINGREDIENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                         JSONObject obj = new JSONObject(response);
                         if (obj.getString("message").equalsIgnoreCase("Success")){
                             addIngredients();
                         } else {
                             Toast.makeText(ActivityAddIngredients.this, "Error Occured Please contact support", Toast.LENGTH_SHORT).show();
                         }

                        } catch (JSONException e) {
                            Toast.makeText(ActivityAddIngredients.this, "Error on JSON : "+ e, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityAddIngredients.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ingredients_text",  recipeText);
                params.put("recipe_id", recipeId);
                return params;
            }
        };

        RequestHandler.getInstance(ActivityAddIngredients.this).addToRequestQueue(stringRequest);

    }


    private void addRecipe(String temp_id, String uniq_id, String uid) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_ADDTORECIPES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                             recipeId = obj.getString("recipe_id");
                             addIngredients();

                        } catch (JSONException e) {
                            Toast.makeText(ActivityAddIngredients.this, "Error on JSON : "+ e, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityAddIngredients.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uid", uid);
                params.put("temp_id", temp_id);
                params.put("uniq_id", uniq_id);
                return params;
            }
        };

        RequestHandler.getInstance(ActivityAddIngredients.this).addToRequestQueue(stringRequest);
    }

    private void addIngredients() {
        modelIngredientsLists = new ArrayList<>();

        // modelIngredientsLists.add(new ModelIngredientsList(1, Integer.valueOf(recipeId), "1 Egg"));

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETRECIPEINGREDIENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray ingredObj = new JSONArray(response);

                            for (int i = 0; i < ingredObj.length(); i++){
                                JSONObject recipesObject = ingredObj.getJSONObject(i);
                                int ingred_id = recipesObject.getInt("ingredients_id");
                                int recipe_id = recipesObject.getInt("recipe_id");
                                String ingridientsText = recipesObject.getString("recipe_ingredients");

                                modelIngredientsLists.add(new ModelIngredientsList(ingred_id, Integer.valueOf(recipeId), ingridientsText));
                            }
                            ingredientsListAdapter = new AdapterIngredientsList(getApplicationContext(), modelIngredientsLists,ActivityAddIngredients.this::refreshIngredientsList) ;
                            ingredientsRecyclerView.setAdapter(ingredientsListAdapter);


                        } catch (JSONException e) {
                            Toast.makeText(ActivityAddIngredients.this, "Error on JSON ACTIVITY: "+ e, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityAddIngredients.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("recipe_id", recipeId);
                return params;
            }
        };
        RequestHandler.getInstance(ActivityAddIngredients.this).addToRequestQueue(stringRequest);
    }

    private void refreshIngredientsList() {
        addIngredients();
    }


}