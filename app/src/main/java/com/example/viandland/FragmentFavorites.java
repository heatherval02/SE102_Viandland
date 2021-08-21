package com.example.viandland;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentFavorites#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFavorites extends Fragment implements AdapterFavoriteRecipes.IUserRecycler{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentFavorites() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFavorites newInstance(String param1, String param2) {
        FragmentFavorites fragment = new FragmentFavorites();
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

    RecyclerView favoritesRecyclerView;
    AdapterFavoriteRecipes favoriteRecipesAdapter;
    List<ModelFavoriteRecipes> modelFavoriteRecipesList;

    ProgressDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        progressDialog = new ProgressDialog(getActivity());

        modelFavoriteRecipesList = new ArrayList<>();
        favoritesRecyclerView = view.findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setHasFixedSize(true);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


        DisplayFavoritesFromRecipes(SharedPrefManager.getUid());
        DisplayFavoritesFromTrending(SharedPrefManager.getUid());
        DisplayFavoritesFromTodaysRecipe(SharedPrefManager.getUid());


        return view;
    }

    private void DisplayFavoritesFromRecipes(int uid) {

        //modelFavoriteRecipesList.add(new ModelFavoriteRecipes(1, "Kare-Kare","Yummy","","Jay Cabasag","","https://panlasangpinoy.com/wp-content/uploads/2019/12/kare-kare-recipe-beef-tripe-500x485.jpg","","recipes"));
        progressDialog.setMessage("Retrieving your favorites");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETFAVORITESDATAFROMRECIPES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray places = new JSONArray(response);

                            for (int i = 0; i < places.length(); i++){
                                JSONObject recipesObject = places.getJSONObject(i);

                                int recipe_id = recipesObject.getInt("recipe_id");
                                String favorite_id = recipesObject.getString("favorite_id");
                                String recipe_name = recipesObject.getString("recipe_name");
                                String recipe_date_added = recipesObject.getString("recipe_date_added");
                                String recipe_description = recipesObject.getString("recipe_description");
                                String recipe_prep_time = recipesObject.getString("recipe_prep_time");
                                String recipe_image = recipesObject.getString("recipe_image");
                                String cook_name = "Cook : "+recipesObject.getString("cook_name");
                                String category = recipesObject.getString("category");
                                String table_name = recipesObject.getString("table_name");

                                modelFavoriteRecipesList.add(new ModelFavoriteRecipes(recipe_id, favorite_id, recipe_name,recipe_description,recipe_date_added,cook_name,recipe_prep_time,recipe_image, recipe_prep_time ,table_name));

                            }

                            favoriteRecipesAdapter = new AdapterFavoriteRecipes(getContext(), modelFavoriteRecipesList, FragmentFavorites.this::refreshFavoritesList);
                            favoritesRecyclerView.setAdapter(favoriteRecipesAdapter);


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
                params.put("uid", String.valueOf(uid));
                return params;
            }
        };

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    private void DisplayFavoritesFromTodaysRecipe(int uid) {

        //modelFavoriteRecipesList.add(new ModelFavoriteRecipes(1, "Kare-Kare","Yummy","","Jay Cabasag","","https://panlasangpinoy.com/wp-content/uploads/2019/12/kare-kare-recipe-beef-tripe-500x485.jpg","","recipes"));

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETFAVORITESDATAFROMTODAYS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONArray places = new JSONArray(response);

                            for (int i = 0; i < places.length(); i++){
                                JSONObject recipesObject = places.getJSONObject(i);

                                int recipe_id = recipesObject.getInt("recipe_id");
                                String favorite_id = recipesObject.getString("favorite_id");
                                String recipe_name = recipesObject.getString("recipe_name");
                                String recipe_date_added = recipesObject.getString("recipe_date_added");
                                String recipe_description = recipesObject.getString("recipe_description");
                                String recipe_prep_time = recipesObject.getString("recipe_prep_time");
                                String recipe_image = recipesObject.getString("recipe_image");
                                String cook_name = "Cook : "+recipesObject.getString("cook_name");
                                String category = recipesObject.getString("category");
                                String table_name = recipesObject.getString("table_name");

                                modelFavoriteRecipesList.add(new ModelFavoriteRecipes(recipe_id, favorite_id, recipe_name,recipe_description,recipe_date_added,cook_name,recipe_prep_time,recipe_image, recipe_prep_time ,table_name));

                            }

                            favoriteRecipesAdapter = new AdapterFavoriteRecipes(getContext(), modelFavoriteRecipesList, FragmentFavorites.this::refreshFavoritesList);
                            favoritesRecyclerView.setAdapter(favoriteRecipesAdapter);







                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Error JSON "+ e, Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("uid", String.valueOf(uid));
                return params;
            }
        };

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    private void DisplayFavoritesFromTrending(int uid) {

        //modelFavoriteRecipesList.add(new ModelFavoriteRecipes(1, "Kare-Kare","Yummy","","Jay Cabasag","","https://panlasangpinoy.com/wp-content/uploads/2019/12/kare-kare-recipe-beef-tripe-500x485.jpg","","recipes"));

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETFAVORITESDATAFROMTRENDING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try {
                            JSONArray places = new JSONArray(response);

                            for (int i = 0; i < places.length(); i++){
                                JSONObject recipesObject = places.getJSONObject(i);

                                int recipe_id = recipesObject.getInt("recipe_id");
                                String favorite_id = recipesObject.getString("favorite_id");
                                String recipe_name = recipesObject.getString("recipe_name");
                                String recipe_date_added = recipesObject.getString("recipe_date_added");
                                String recipe_description = recipesObject.getString("recipe_description");
                                String recipe_prep_time = recipesObject.getString("recipe_prep_time");
                                String recipe_image = recipesObject.getString("recipe_image");
                                String cook_name = "Cook : "+recipesObject.getString("cook_name");
                                String category = recipesObject.getString("category");
                                String table_name = recipesObject.getString("table_name");

                                modelFavoriteRecipesList.add(new ModelFavoriteRecipes(recipe_id, favorite_id, recipe_name,recipe_description,recipe_date_added,cook_name,recipe_prep_time,recipe_image, recipe_prep_time ,table_name));

                            }

                            favoriteRecipesAdapter = new AdapterFavoriteRecipes(getContext(), modelFavoriteRecipesList, FragmentFavorites.this::refreshFavoritesList);
                            favoritesRecyclerView.setAdapter(favoriteRecipesAdapter);


                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Error JSON "+ e, Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("uid", String.valueOf(uid));
                return params;
            }
        };

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    @Override
    public void refreshFavoritesList() {
        modelFavoriteRecipesList.clear();
        DisplayFavoritesFromRecipes(SharedPrefManager.getUid());
        DisplayFavoritesFromTrending(SharedPrefManager.getUid());
        DisplayFavoritesFromTodaysRecipe(SharedPrefManager.getUid());
    }
}