package com.ops.dev.simple.services.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.adapters.MenuPagerAdapterBusiness;

public class MainMenuBusiness extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    ViewPager viewPager;
    MenuPagerAdapterBusiness menuPagerAdapterBusiness;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._____main_menu_business);

        viewPager = findViewById(R.id.view_pager);
        menuPagerAdapterBusiness = new MenuPagerAdapterBusiness(getSupportFragmentManager());
        bottomNavigationView = findViewById(R.id.bottom_menu);

        viewPager.setAdapter(menuPagerAdapterBusiness);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.catalog).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.entries).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.orders).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.profile).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        try {
            int number = getIntent().getExtras().getInt("number");
            int screen = getIntent().getExtras().getInt("screen");
            viewPager.setCurrentItem(number);
            bottomNavigationView.setSelectedItemId(screen);
        } catch (Exception ex) {
            viewPager.setCurrentItem(0);
            bottomNavigationView.setSelectedItemId(R.id.catalog);
        }
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.catalog:
                viewPager.setCurrentItem(0);
                break;
            case R.id.entries:
                viewPager.setCurrentItem(1);
                break;
            case R.id.orders:
                viewPager.setCurrentItem(2);
                break;
            case R.id.profile:
                viewPager.setCurrentItem(3);
                break;
        }
        return true;
    }
}