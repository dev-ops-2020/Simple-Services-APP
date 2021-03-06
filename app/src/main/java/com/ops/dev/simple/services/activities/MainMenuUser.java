package com.ops.dev.simple.services.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.adapters.MenuPagerAdapterUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainMenuUser extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    ViewPager viewPager;
    MenuPagerAdapterUser menuPagerAdapterUser;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._____main_menu_user);

        viewPager = findViewById(R.id.view_pager);
        menuPagerAdapterUser = new MenuPagerAdapterUser(getSupportFragmentManager());
        bottomNavigationView = findViewById(R.id.bottom_menu);

        viewPager.setAdapter(menuPagerAdapterUser);
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
                        bottomNavigationView.getMenu().findItem(R.id.categories).setChecked(true);
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
            viewPager.setCurrentItem(1); // TODO Change to 2
            bottomNavigationView.setSelectedItemId(R.id.categories); // TODO Change to R.id.home
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
            case R.id.categories:
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