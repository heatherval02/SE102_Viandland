package com.example.viandland;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

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

    private RecyclerView recyclerView;
    private AdapterFoodCategories adapterFoodCategories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<ModelFoodCategories> item = new ArrayList<>();
        item.add(new ModelFoodCategories(R.drawable.breakfast,"Breakfast"));
        item.add(new ModelFoodCategories(R.drawable.lunch,"Lunch"));
        item.add(new ModelFoodCategories(R.drawable.dinner,"Dinner"));
        item.add(new ModelFoodCategories(R.drawable.diet_food,"Healthy Meals"));
        item.add(new ModelFoodCategories(R.drawable.snack,"Snacks"));

        // Inflate the layout for this fragment
        ViewGroup mview = (ViewGroup) inflater.inflate(R.layout.fragment_food_categories, container, false);

        recyclerView = mview.findViewById(R.id.rv_1);
        adapterFoodCategories = new AdapterFoodCategories(item, mview.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(mview.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapterFoodCategories);

        return mview;


    }
}