package com.example.viandland;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class MainpageDashboard extends AppCompatActivity {

    MeowBottomNavigation bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage_dashboard);

        // Assign Variable

        bottomNavigation = findViewById(R.id.bottom_nav_menu);

        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.dashboard));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.food_categories));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.food_info));
        bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.favorites));
        bottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.profile));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

                Fragment fragment = null;

                switch (item.getId()){

                    case 1:
                        fragment = new FragmentDashboard();
                        break;

                    case 2:
                        fragment = new FragmentCategories();
                        break;

                    case 3:
                        fragment = new FragmentInfo();
                        break;

                    case 4:
                        fragment = new FragmentFavorites();
                        break;

                    case 5:

                        fragment = new ProfileFragment();
                        break;

                }
                loadFragment(fragment);

            }
        });

        bottomNavigation.show(1,true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                // display toast

            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });

    }

    private void loadFragment(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,fragment)
                .commit();
    }
}