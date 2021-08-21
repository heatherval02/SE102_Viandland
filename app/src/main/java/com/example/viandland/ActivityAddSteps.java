package com.example.viandland;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class ActivityAddSteps extends AppCompatActivity implements AdapterInstructionList.IRecyclerViewRefresher {


    Dialog selectRecipeDialog;
    ImageButton back;
    Button openStepsDialogButton;
    String recipeId;
    Button showCategoryDialogBtn;

    //Recycler View

    RecyclerView instructionRecyclerView;
    AdapterInstructionList instructionListAdapter;
    List<ModelInstructionList> modelInstructionList;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_steps);
        recipeId = getIntent().getStringExtra("recipe_id");

        showCategoryDialogBtn = findViewById(R.id.saveAllBtn);
        showCategoryDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                selectRecipeDialog = new Dialog(ActivityAddSteps.this);
                selectRecipeDialog.setContentView(R.layout.dialog_select_category);

                EditText categoryText = selectRecipeDialog.findViewById(R.id.categoryText);
                Button publishRecipe = selectRecipeDialog.findViewById(R.id.publishButton);
                ImageButton closeDialog = selectRecipeDialog.findViewById(R.id.closeBtn);

                Button breakfastCategory, lunchCategory, dinnerCategory, healthyMealsCategory, snacksCategory, otherCategory;
                breakfastCategory = selectRecipeDialog.findViewById(R.id.breakfastCategory);
                lunchCategory = selectRecipeDialog.findViewById(R.id.lunchCategory);
                dinnerCategory = selectRecipeDialog.findViewById(R.id.dinnerCategory);
                healthyMealsCategory = selectRecipeDialog.findViewById(R.id.healthyMealsCategory);
                snacksCategory = selectRecipeDialog.findViewById(R.id.snacksCategory);
                otherCategory = selectRecipeDialog.findViewById(R.id.otherCategory);



//
//
                breakfastCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoryText.setText(categoryText.getText()+"#Breakfast ");
                    }
                });
                lunchCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoryText.setText(categoryText.getText()+"#Lunch ");
                    }
                });

                dinnerCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoryText.setText(categoryText.getText()+"#Dinner ");
                    }
                });
                healthyMealsCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoryText.setText(categoryText.getText()+"#HealthyMeals ");
                    }
                });
                snacksCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoryText.setText(categoryText.getText()+"#Snacks ");
                    }
                });

                otherCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoryText.setText(categoryText.getText()+"#Other ");
                    }
                });

                publishRecipe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                Constants.URL_SETCATEGORY,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject obj = new JSONObject(response);
                                            if (obj.getString("message").equalsIgnoreCase("Success")){
                                                selectRecipeDialog.dismiss();
                                                successDialog();
                                            } else {
                                                Toast.makeText(ActivityAddSteps.this, "Error Occured Please contact support", Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            Toast.makeText(ActivityAddSteps.this, "Error on JSON : "+ e, Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(ActivityAddSteps.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }

                        ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("recipe_id", recipeId);
                                params.put("category_text",  categoryText.getText().toString().trim());
                                return params;
                            }
                        };

                        RequestHandler.getInstance(ActivityAddSteps.this).addToRequestQueue(stringRequest);

                    }
                });
                closeDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectRecipeDialog.dismiss();
                    }
                });

                selectRecipeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                selectRecipeDialog.show();



            }
        });



        instructionRecyclerView = findViewById(R.id.instructionRecyclerView);
        instructionRecyclerView.setHasFixedSize(true);
        instructionRecyclerView.setLayoutManager(new LinearLayoutManager(ActivityAddSteps.this));
         addInstructions();





        back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




            openStepsDialogButton = findViewById(R.id.openStepsDialogButton);
            openStepsDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAddSteps.this);
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_add_instructions, viewGroup, false);

                    EditText instructionText = dialogView.findViewById(R.id.instructionText);
                    Button addIngredientsButton = dialogView.findViewById(R.id.addIInstructionBtn);
                    ImageButton closeDialog = dialogView.findViewById(R.id.closeBtn);

                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();
                    addIngredientsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmAddInstruction(recipeId.trim(), instructionText.getText().toString());
                            alertDialog.dismiss();
                        }
                    });
                    closeDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });


                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();


                }
            });

    }

    private void successDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAddSteps.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(ActivityAddSteps.this).inflate(R.layout.dialog_congratulation, viewGroup, false);

        Button okDialogButton = dialogView.findViewById(R.id.okButton);
        Button cancelDialogButton = dialogView.findViewById(R.id.cancelButton);
        ImageButton closeDialog = dialogView.findViewById(R.id.closeBtn);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        okDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(ActivityAddSteps.this, MainpageDashboard.class);
                startActivity(newIntent);
                finish();
            }
        });
        cancelDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }

    private void confirmAddInstruction(String recipe_id, String instruction_text) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_SETINSTRUCTIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("message").equalsIgnoreCase("Success")){
                                addInstructions();
                            } else {
                                Toast.makeText(ActivityAddSteps.this, "Error Occured Please contact support", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(ActivityAddSteps.this, "Error on JSON : "+ e, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityAddSteps.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("instruction_text",  instruction_text);
                params.put("recipe_id", recipeId);
                return params;
            }
        };

        RequestHandler.getInstance(ActivityAddSteps.this).addToRequestQueue(stringRequest);

    }

    private void addInstructions() {

        modelInstructionList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETRECIPEINSTRUCTIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray ingredObj = new JSONArray(response);

                            for (int i = 0; i < ingredObj.length(); i++){
                                JSONObject recipesObject = ingredObj.getJSONObject(i);
                                int instruction_id = recipesObject.getInt("instruction_id");
                                int recipe_id = recipesObject.getInt("recipe_id");
                                String instructionText = recipesObject.getString("recipe_instruction");

                                modelInstructionList.add(new ModelInstructionList(instruction_id, Integer.parseInt(recipeId), instructionText));
                            }

                            instructionListAdapter = new AdapterInstructionList(getApplicationContext(),  modelInstructionList, ActivityAddSteps.this::addInstructions) ;
                            instructionRecyclerView.setAdapter(instructionListAdapter);


                        } catch (JSONException e) {
                            Toast.makeText(ActivityAddSteps.this, "Error on JSON ACTIVITY: "+ e, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityAddSteps.this, "Error :" + error, Toast.LENGTH_SHORT).show();
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
        RequestHandler.getInstance(ActivityAddSteps.this).addToRequestQueue(stringRequest);


    }


    @Override
    public void goRefresh() {
        addInstructions();
    }
}