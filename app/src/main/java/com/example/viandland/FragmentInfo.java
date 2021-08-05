
package com.example.viandland;

import android.app.AlertDialog;
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

    BlurLayout blurLayout;

    TextView todaysRecipeName, todaysRecipeCook;
    ImageView todaysRecipeImage;

    Calendar calendar = Calendar.getInstance();
    String dayToday = "";


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

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETTODAYSRECIPES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String recipeIdText = String.valueOf(jsonObject.getString("recipe_id"));
                            todaysRecipeName.setText(jsonObject.getString("recipe_name"));

                            Glide.with(getContext())
                                    .load(jsonObject.getString("recipe_img"))
                                    //.placeholder(R.drawable.favorites)
                                    .into(todaysRecipeImage);

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



        return view;

    }


}