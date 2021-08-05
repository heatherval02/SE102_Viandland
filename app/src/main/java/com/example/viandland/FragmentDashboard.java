package com.example.viandland;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentDashboard extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDashboard newInstance(String param1, String param2) {
        FragmentDashboard fragment = new FragmentDashboard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentDashboard() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ViewPager trendingRecipesViewPager;
    AdapterTrendingRecipes trendingRecipesAdapter;
    ArrayList<ModelTrendingRecipes> modelTrendingRecipesArrayList;


   //Recently Added Recipes

    RecyclerView recyclerView;
    AdapterWhatsNew whatsNewAdapter;
    List<ModelWhatsNew> modelWhatsNewList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);



        trendingRecipesViewPager = view.findViewById(R.id.trendingRecipesViewPager);
        loadTrendingRecipesCards();
        trendingRecipesViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // No action yet
            }
    
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recentlyAddedRecipesRecylerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        loadRecentlyAddedCards();


    // For Floating Cutton Function

        return view;
    }



    private void loadRecentlyAddedCards() {

        modelWhatsNewList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GETWHATSNEWRECIPES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray recipes = new JSONArray(response);

                            for (int i = 0; i <  recipes.length(); i++){
                                JSONObject recipesObject =  recipes.getJSONObject(i);

                                int recipe_id = recipesObject.getInt("recipe_id");
                                String recipe_name = recipesObject.getString("recipe_name");
                                String recipe_date_added = recipesObject.getString("recipe_date_added");
                                String recipe_description = recipesObject.getString("recipe_id");
                                String recipe_prep_time = recipesObject.getString("recipe_description");
                                String recipe_image = recipesObject.getString("recipe_image");
                                String cook_name = recipesObject.getString("cook_name");
                                String cook_image = recipesObject.getString("cook_image");

                                modelWhatsNewList.add(new ModelWhatsNew(recipe_id,recipe_name,recipe_date_added,recipe_description,recipe_image,cook_image,cook_name));

                            }
                            whatsNewAdapter = new AdapterWhatsNew(modelWhatsNewList, getContext());
                            recyclerView.setAdapter(whatsNewAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        Volley.newRequestQueue(getContext()).add(stringRequest);


    }

    private void loadTrendingRecipesCards() {
        modelTrendingRecipesArrayList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GETTRENDINGRECIPES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray recipes = new JSONArray(response);

                            for (int i = 0; i <  recipes.length(); i++){
                                JSONObject recipesObject =  recipes.getJSONObject(i);

                                int recipe_id = recipesObject.getInt("recipe_id");
                                String recipe_name = recipesObject.getString("recipe_name");
                                String recipe_date_added = recipesObject.getString("recipe_date_added");
                                String recipe_description = recipesObject.getString("recipe_id");
                                String recipe_prep_time = recipesObject.getString("recipe_description");
                                String recipe_image = recipesObject.getString("recipe_image");
                                String cook_name = recipesObject.getString("cook_name");
                                String cook_image = recipesObject.getString("cook_image");

                                modelTrendingRecipesArrayList.add(new ModelTrendingRecipes(recipe_id,recipe_name,recipe_date_added,recipe_description,recipe_image,cook_image,cook_name));
                            }
                            trendingRecipesAdapter = new AdapterTrendingRecipes(getContext(), modelTrendingRecipesArrayList);
                            trendingRecipesViewPager.setAdapter(trendingRecipesAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        Volley.newRequestQueue(getContext()).add(stringRequest);

        trendingRecipesViewPager.setPadding(10, 0, 10,0);

    }

}