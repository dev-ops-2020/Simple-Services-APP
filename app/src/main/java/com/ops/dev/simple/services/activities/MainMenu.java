package com.ops.dev.simple.services.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.adapters.MenuPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ops.dev.simple.services.models.CategoriesIconModel;
import com.ops.dev.simple.services.models.CategoriesModel;

public class MainMenu extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    ViewPager viewPager;
    MenuPagerAdapter menuPagerAdapter;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._____main_menu);

        viewPager = findViewById(R.id.view_pager);
        menuPagerAdapter = new MenuPagerAdapter(getSupportFragmentManager());
        bottomNavigationView = findViewById(R.id.bottom_menu);

        viewPager.setAdapter(menuPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.search).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.businesses).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.cart).setChecked(true);
                        break;
                    case 4:
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
            viewPager.setCurrentItem(2);
            bottomNavigationView.setSelectedItemId(R.id.home);
        }

        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.search:
                viewPager.setCurrentItem(0);
                break;
            case R.id.businesses:
                viewPager.setCurrentItem(1);
                break;
            case R.id.home:
                viewPager.setCurrentItem(2);
                break;
            case R.id.cart:
                viewPager.setCurrentItem(3);
                break;
            case R.id.profile:
                viewPager.setCurrentItem(4);
                break;
        }
        return true;
    }
}