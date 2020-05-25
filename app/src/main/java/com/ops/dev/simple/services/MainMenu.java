package com.ops.dev.simple.services;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainMenu extends AppCompatActivity {

    private Fragment mFragment;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._menu_principal);
        BottomNavigationView navView = findViewById(R.id.menu);

        navView.setItemIconTintList(null);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        goHome();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    goHome();
                    return true;
                case R.id.fav:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mFragment = new Fav();
                    fragmentTransaction.replace(R.id.fragmentContainer, mFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                case R.id.profile:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mFragment = new Profile();
                    fragmentTransaction.replace(R.id.fragmentContainer, mFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    private void goHome() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragment = new Home();
        fragmentTransaction.replace(R.id.fragmentContainer, mFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}