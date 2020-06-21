package com.ops.dev.simple.services.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ops.dev.simple.services.activities.fragments.Car;
import com.ops.dev.simple.services.activities.fragments.Fav;
import com.ops.dev.simple.services.activities.fragments.Home;
import com.ops.dev.simple.services.activities.fragments.Profile;
import com.ops.dev.simple.services.R;

public class MainMenu extends AppCompatActivity {

    Fragment mFragment;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.___main_menu);
        BottomNavigationView navView = findViewById(R.id.bottom_menu);
        navView.setItemIconTintList(null);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        goView(new Home());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    goView(new Home());
                    return true;
                case R.id.fav:
                    goView(new Fav());
                    return true;
                case R.id.car:
                    goView(new Car());
                    return true;
                case R.id.profile:
                    goView(new Profile());
                    return true;
            }
            return false;
        }
    };

    private void goView(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragment = fragment;
        fragmentTransaction.replace(R.id.fragmentContainer, mFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}