
package com.example.viandland;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.alterac.blurkit.BlurLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Food_infoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentInfo newInstance(String param1, String param2) {
        FragmentInfo fragment = new FragmentInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    String recipe_id;
    BlurLayout blurLayout;

    TextView todaysRecipeName, todaysRecipeCook, todaysDescriptionText;
    ImageView todaysRecipeImage;

    Calendar calendar = Calendar.getInstance();
    String dayToday = "";

    TextView todaysText;

    ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait while retrieving recipes");
        progressDialog.show();

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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_info, container, false);
        blurLayout = view.findViewById(R.id.blurLayout);
        blurLayout.startBlur();



        todaysRecipeName = view.findViewById(R.id.recipeName);
        todaysRecipeImage = view.findViewById(R.id.recipeImage);

        todaysDescriptionText = view.findViewById(R.id.recipeDescriptionText);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETTODAYSRECIPES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            recipe_id = jsonObject.getString("recipe_id");
                            todaysRecipeName.setText(dayToday+"\n"+jsonObject.getString("recipe_name"));
                            todaysDescriptionText.setText(jsonObject.getString("recipe_description"));

                            Glide.with(getContext())
                                    .load(jsonObject.getString("recipe_img"))
                                    //.placeholder(R.drawable.favorites)
                                    .into(todaysRecipeImage);

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
                params.put("dayToday", dayToday);

                return params;
            }
        };

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

        todaysRecipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(view.getContext(), ActivityViewRecipe.class);
                newIntent.putExtra("recipe_id", String.valueOf(recipe_id));
                newIntent.putExtra("from", "todays_recipe");
                startActivity(newIntent);

            }
        });
        return view;

    }


}