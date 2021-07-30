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


    ViewPager recentlyAddedViewPager;
    AdapterWhatsNew whatsNewAdapter;
    ArrayList<ModelWhatsNew> modelWhatsNewArrayList;

    FloatingActionButton todaysRecipeButton;


    Calendar calendar = Calendar.getInstance();
    String dayToday = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //Get the Day of the week
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                dayToday = "Sunday";
                break;
            case Calendar.MONDAY:
                dayToday = "Monday";
                break;
            case Calendar.TUESDAY:
                dayToday = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayToday = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayToday = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayToday = "Friday";
                break;
            case Calendar.SATURDAY:
                dayToday = "Saturday";
                break;

        }


        recentlyAddedViewPager = view.findViewById(R.id.recentlyAddedRecipesViewPager);
        loadRecentlyAddedCards();
        recentlyAddedViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });





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

    // For Floating Cutton Function

        todaysRecipeButton = view.findViewById(R.id.todaysRecipeButton);
        todaysRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView closeDialogButton, todaysRecipeName, todaysRecipeCook;
                ImageView todaysRecipeImage;

                AlertDialog.Builder addFundDialog = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.dialog_todays_recipes, null);

                closeDialogButton = mView.findViewById(R.id.closeDialogButton);
                todaysRecipeName = mView.findViewById(R.id.todaysRecipeName);
                todaysRecipeCook = mView.findViewById(R.id.todaysRecipeCook);
                todaysRecipeImage = mView.findViewById(R.id.todaysRecipeImage);


                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        Constants.URL_GETTODAYSRECIPES,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String recipeId = String.valueOf(jsonObject.getString("recipe_id"));
                                    todaysRecipeName.setText(jsonObject.getString("recipe_name"));
                                    todaysRecipeCook.setText(jsonObject.getString("recicpe_cook"));
                                    Glide.with(getContext())
                                            .load(jsonObject.getString("recipe_img"))
                                            .placeholder(R.drawable.favorites)
                                            .into(todaysRecipeImage);

                                    todaysRecipeImage.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(getContext(), recipeId, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "Error in Json" + e, Toast.LENGTH_LONG).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<>();
                        params.put("dayToday", dayToday);

                        return params;
                    }
                };

                RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);



                addFundDialog.setView(mView);
                AlertDialog dialog = addFundDialog.create();

                closeDialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });



        return view;
    }

    private void loadRecentlyAddedCards() {
        modelWhatsNewArrayList = new ArrayList<>();

       // modelWhatsNewArrayList.add(new ModelWhatsNew(1,"Adobo","07/22/2021","This is a recipe post for Filipino Pork Adobo. It is a dish composed of pork slices cooked in soy sauce, vinegar, and garlic. There are version wherein onions are also added. Adobo is a popular dish in the Philippines, along with Sinigang.","https://panlasangpinoy.com/wp-content/uploads/2009/08/Best-Pork-Adobo-Recipe.jpg"," https://www.google.com/imgres?imgurl=https%3A%2F%2Fthumbs.dreamstime.com%2Fb%2Fadmin-sign-laptop-icon-stock-vector-166205404.jpg&imgrefurl=https%3A%2F%2Fwww.dreamstime.com%2Fillustration%2Fadmin-sign.html&tbnid=KgDkgGArNaGaCM&vet=12ahUKEwjAgZvz6frxAhUCYZQKHZ7PBS4QMygCegUIARDRAQ..i&docid=PxITScl98joWHM&w=800&h=800&q=admin&ved=2ahUKEwjAgZvz6frxAhUCYZQKHZ7PBS4QMygCegUIARDRAQ","Juan Dela Cruz"));

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

                                modelWhatsNewArrayList.add(new ModelWhatsNew(recipe_id,recipe_name,recipe_date_added,recipe_description,recipe_image,cook_image,cook_name));

                            }
                            whatsNewAdapter = new AdapterWhatsNew(getContext(), modelWhatsNewArrayList);
                            recentlyAddedViewPager.setAdapter(whatsNewAdapter);


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

        recentlyAddedViewPager.setPadding(10, 0, 10,0);

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