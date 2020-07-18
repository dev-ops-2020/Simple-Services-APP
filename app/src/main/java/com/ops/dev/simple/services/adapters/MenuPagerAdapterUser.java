package com.ops.dev.simple.services.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ops.dev.simple.services.activities.fragments.Categories;
import com.ops.dev.simple.services.activities.fragments.Search;
import com.ops.dev.simple.services.activities.fragments.Home;
import com.ops.dev.simple.services.activities.fragments.Cart;
import com.ops.dev.simple.services.activities.fragments.ProfileUser;

public class MenuPagerAdapterUser extends FragmentStatePagerAdapter {

    public MenuPagerAdapterUser(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Search();
            case 1:
                return new Categories();
            case 2:
                return new Home();
            case 3:
                return new Cart();
            case 4:
                return new ProfileUser();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}