package com.example.viandland;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements AdapterOwnRecipes.IUserRecycler {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Object ConstraintLayout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    ImageView logoutBtn;
    Button uploadBtn;
    TextView userFullname;


    RecyclerView recyclerView;
    AdapterOwnRecipes ownRecipesAdapter;
    List<ModelOwnRecipes> modelOwnRecipesList;

    View view;

    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_profile_details, container, false);
        progressDialog = new ProgressDialog(getActivity());

        modelOwnRecipesList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.ownRecipesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        progressDialog.setMessage("Please wait while retrieving your own recipes");
        progressDialog.show();
        ShowOwnRecipes(String.valueOf(SharedPrefManager.getUid()));


        logoutBtn = view.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Log out", Toast.LENGTH_SHORT).show();
                SharedPrefManager.getInstance(getContext()).loggedOut();
                getActivity().finish();
                Intent newIntent = new Intent(getContext(), MainActivity.class);
                startActivity(newIntent);

            }
        });


        userFullname = view.findViewById(R.id.userFullNameText);
        userFullname.setText(SharedPrefManager.getFullname());

        uploadBtn = view.findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(getContext(), UploadRecipeActivity.class);
                startActivity(newIntent);
            }
        });


        return view;
    }

    private void ShowOwnRecipes(String uid) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETOWNRECIPES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            progressDialog.dismiss();
                            JSONArray places = new JSONArray(response);

                            for (int i = 0; i < places.length(); i++){
                                JSONObject recipesObject = places.getJSONObject(i);

                                int recipe_id = recipesObject.getInt("recipe_id");
                                String recipe_name = recipesObject.getString("recipe_name");
                                String recipe_image = recipesObject.getString("recipe_image");

                                modelOwnRecipesList.add(new ModelOwnRecipes(recipe_id, recipe_name,recipe_image));
                            }
                            ownRecipesAdapter = new AdapterOwnRecipes(getContext(), modelOwnRecipesList, ProfileFragment.this::showEditDialog) ;
                            recyclerView.setAdapter(ownRecipesAdapter);

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
                params.put("uid", uid);
                return params;
            }
        };

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void showEditDialog(String recipeId) {


        Toast.makeText(view.getContext(), "This one is under development " + recipeId, Toast.LENGTH_SHORT).show();


    }
}