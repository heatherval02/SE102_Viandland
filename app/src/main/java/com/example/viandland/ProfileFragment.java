package com.example.viandland;

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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile_details, container, false);


        modelOwnRecipesList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.ownRecipesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        modelOwnRecipesList.add(new ModelOwnRecipes(1, "Kare-Kare","https://upload.wikimedia.org/wikipedia/commons/thumb/2/2d/Kare-kare_oxtail_stew_1.jpg/1200px-Kare-kare_oxtail_stew_1.jpg"));
        modelOwnRecipesList.add(new ModelOwnRecipes(2, "Kaldereta","http://images.summitmedia-digital.com/yummyph/images/2017/11/23/beef-kaldereta.jpg"));
        modelOwnRecipesList.add(new ModelOwnRecipes(3, "Lechon Kawali","https://www.seriouseats.com/thmb/3XcY0M1U6dM8uH17eEvFopQtHmE=/1500x1125/filters:fill(auto,1)/20210508-lechon-kawali-melissa-hom-2-inchChunks-seriouseats-1d53c12cee234305b921362e2106bf29.jpg"));

        ownRecipesAdapter = new AdapterOwnRecipes(view.getContext(), modelOwnRecipesList) ;
        recyclerView.setAdapter(ownRecipesAdapter);


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
}