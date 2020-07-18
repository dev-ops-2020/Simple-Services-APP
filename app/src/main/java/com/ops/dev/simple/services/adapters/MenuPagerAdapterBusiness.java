package com.ops.dev.simple.services.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ops.dev.simple.services.activities.fragments.Catalog;
import com.ops.dev.simple.services.activities.fragments.Entries;
import com.ops.dev.simple.services.activities.fragments.Orders;
import com.ops.dev.simple.services.activities.fragments.ProfileBusiness;
import com.ops.dev.simple.services.activities.fragments.ProfileUser;

public class MenuPagerAdapterBusiness extends FragmentStatePagerAdapter {

    public MenuPagerAdapterBusiness(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Catalog();
            case 1:
                return new Entries();
            case 2:
                return new Orders();
            case 3:
                return new ProfileBusiness();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}