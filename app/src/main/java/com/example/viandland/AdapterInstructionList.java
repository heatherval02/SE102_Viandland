package com.example.viandland;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterInstructionList extends RecyclerView.Adapter<AdapterInstructionList.AdapterInstructionListViewHolder>{

    IRecyclerViewRefresher mListener;
    Context mCtx;
    List<ModelInstructionList> modelInstructionListList;

    public AdapterInstructionList(Context mCtx, List<ModelInstructionList> modelInstructionListList, IRecyclerViewRefresher mListener) {
        this.mCtx = mCtx;
        this.modelInstructionListList = modelInstructionListList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public AdapterInstructionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_instruction_holder, null);
        return new AdapterInstructionList.AdapterInstructionListViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterInstructionListViewHolder holder, int position) {
        ModelInstructionList modelInstructionList = modelInstructionListList.get(position);
        holder.itemText.setText(modelInstructionList.instructionString);
        holder.instructionBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mCtx, "Instruction deleted", Toast.LENGTH_SHORT).show();
                deleteInstruction(String.valueOf(modelInstructionList.instructionId));
                mListener.goRefresh();
                return false;
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mCtx, "Instruction deleted", Toast.LENGTH_SHORT).show();
                deleteInstruction(String.valueOf(modelInstructionList.instructionId));
                mListener.goRefresh();
            }
        });


    }

    private void deleteInstruction(String instruction_id) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_REMOVEINSTRUCTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("message").equalsIgnoreCase("Success")){
                                //mListener.refreshIngredientsList();
                            } else {
                                Toast.makeText(mCtx, "Error Occured Please contact support", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(mCtx, "Kindly Check your Internet Connection", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mCtx, "Kindly Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("instruction_id", instruction_id);
                return params;
            }
        };

        RequestHandler.getInstance(mCtx).addToRequestQueue(stringRequest);
    }

    @Override
    public int getItemCount() {
        return modelInstructionListList.size();
    }

    class AdapterInstructionListViewHolder extends RecyclerView.ViewHolder {

        IRecyclerViewRefresher mListener;
        CardView instructionBtn;
        TextView itemText;
        ImageButton deleteButton;

        public AdapterInstructionListViewHolder(@NonNull View itemView, IRecyclerViewRefresher mListener) {
            super(itemView);
            this.mListener = mListener;
            instructionBtn = itemView.findViewById(R.id.instructionsButton);
            itemText = itemView.findViewById(R.id.instructionText);
            deleteButton = itemView.findViewById(R.id.deleteBtn);
        }
    }

    interface IRecyclerViewRefresher{
        void goRefresh();
    }
}
