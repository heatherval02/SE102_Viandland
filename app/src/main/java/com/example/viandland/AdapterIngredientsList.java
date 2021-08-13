package com.example.viandland;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterIngredientsList extends RecyclerView.Adapter<AdapterIngredientsList.AdapterIngredientsViewHolder>{


    Context mCtx;
    List<ModelIngredientsList> modelIngredientsListList;
    IUserRecycler mListener;

    public AdapterIngredientsList(Context mCtx, List<ModelIngredientsList> modelIngredientsListList, IUserRecycler mListener) {
        this.mCtx = mCtx;
        this.modelIngredientsListList = modelIngredientsListList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public AdapterIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_data_holder, null);
        return new AdapterIngredientsList.AdapterIngredientsViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterIngredientsViewHolder holder, int position) {
        ModelIngredientsList modelIngredientsList = modelIngredientsListList.get(position);
        holder.itemText.setText(modelIngredientsList.ingredientsString);
        holder.ingredientsBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteIngredients(String.valueOf(modelIngredientsList.ingredientsId));
                return false;
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteIngredients(String.valueOf(modelIngredientsList.ingredientsId));
            }
        });
    }

    private void deleteIngredients(String ingredientsId) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_REMOVEINGREDIENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("message").equalsIgnoreCase("Success")){
                               mListener.refreshIngredientsList();
                            } else {
                                Toast.makeText(mCtx, "Error Occured Please contact support", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(mCtx, "Error on JSON HERE: "+ e, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mCtx, "Error :" + error, Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ingredients_id", ingredientsId);
                return params;
            }
        };

        RequestHandler.getInstance(mCtx).addToRequestQueue(stringRequest);
    }

    @Override
    public int getItemCount() {
        return modelIngredientsListList.size();
    }

    class AdapterIngredientsViewHolder extends RecyclerView.ViewHolder{

        CardView ingredientsBtn;
        TextView itemText;
        ImageButton deleteButton;
        IUserRecycler mListener;

        public AdapterIngredientsViewHolder(@NonNull View itemView, IUserRecycler mListener) {
            super(itemView);
            this.mListener = mListener;
            deleteButton = itemView.findViewById(R.id.deleteBtn);
            itemText = itemView.findViewById(R.id.ingredientsText);
            ingredientsBtn = itemView.findViewById(R.id.ingredientButton);
        }
    }


    interface IUserRecycler{
        void refreshIngredientsList();
    }
}

