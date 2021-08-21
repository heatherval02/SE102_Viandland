package com.example.viandland;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCategories#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCategories extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCategories() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Food_categoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCategories newInstance(String param1, String param2) {
        FragmentCategories fragment = new FragmentCategories();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
    }


    CardView lunchBtn, dinnerBtn, healthyMealsBtn, snacksBtn, breakfastBtn, kidsMealBtn;

    ///This is for the search Results RecylerView
    RecyclerView searchResultsRecyclerView;
    AdapterSearchCategory searchCategoryAdapter;
    List<ModelSearchCategoryResults> modelSearchCategoryResultsList;

    ProgressDialog progressDialog;


    Button searchBtn;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup mview = (ViewGroup) inflater.inflate(R.layout.fragment_food_categories, container, false);

        searchBtn = mview.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(mview.getContext(), SearchRecipeActivity.class);
                startActivity(newIntent);

            }
        });

        //Results RecyclerView Implementatioins

        modelSearchCategoryResultsList = new ArrayList<>();
        searchResultsRecyclerView = mview.findViewById(R.id.categoryResultsRecyclerView);
        searchResultsRecyclerView.setHasFixedSize(true);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(mview.getContext()));

        progressDialog = new ProgressDialog(mview.getContext());
        progressDialog.setMessage("Please wait");

        searchFood("");



        breakfastBtn = mview.findViewById(R.id.breakfastBtn);
        breakfastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFood("breakfast");
            }

        });
        lunchBtn = mview.findViewById(R.id.lunchBtn);
        lunchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFood("lunch");
            }
        });
        dinnerBtn = mview.findViewById(R.id.dinnerBtn);
        dinnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFood("dinner");
            }
        });
        healthyMealsBtn = mview.findViewById(R.id. healthyMealsBtn);
        healthyMealsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFood("healthy");
            }
        });
        snacksBtn = mview.findViewById(R.id.snacksBtn);
        snacksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFood("snacks");
            }
        });

        kidsMealBtn = mview.findViewById(R.id.kidsMealBtn);
        kidsMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFood("kids");
            }
        });




        //Next
        return mview;


    }

    private void searchFood(String keyword) {

        String tags = keyword.trim();

       modelSearchCategoryResultsList.clear();
       progressDialog = new ProgressDialog(getContext());
       progressDialog.setMessage("Please wait while retrieving your Recipes");

       progressDialog.show();

       StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETSEARCHRESULTS,
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
                            searchCategoryAdapter = new AdapterSearchCategory(getContext(), modelSearchCategoryResultsList);
                            searchResultsRecyclerView.setAdapter(searchCategoryAdapter);

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Kindly Check your Internet Connection", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Kindly Check your Internet Connection", Toast.LENGTH_SHORT).show();
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

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


}