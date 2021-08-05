package com.example.viandland;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterFoodCategories extends RecyclerView.Adapter<AdapterFoodCategories.RVViewHolder>{

    private ArrayList<ModelFoodCategories> items;
    int row_index = 1;


    public AdapterFoodCategories(ArrayList<ModelFoodCategories> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item,parent,false);
        RVViewHolder rvViewHolder = new RVViewHolder(view);
        return rvViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RVViewHolder holder, final int position) {
        ModelFoodCategories currentItem = items.get(position);
        holder.imageView.setImageResource(currentItem.getImage());
        holder.textView.setText(currentItem.getText());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                row_index = position;
                notifyDataSetChanged();

            }
        });

        if (row_index == position){

            holder.linearLayout.setBackgroundResource(R.drawable.rv_bg);
        }
        else {
            holder.linearLayout.setBackgroundResource(R.drawable.rv_selectedbg);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class RVViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;

        public RVViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.breakfast_image);
            textView = itemView.findViewById(R.id.breakfast_text);
            linearLayout = itemView.findViewById(R.id.linearlayout);
        }
    }
}
