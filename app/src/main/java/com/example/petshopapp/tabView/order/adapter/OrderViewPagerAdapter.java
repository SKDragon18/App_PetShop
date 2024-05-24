package com.example.petshopapp.tabView.order.adapter;

//package com.example.petshopapp.fragment.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.petshopapp.tabView.order.Tab1;
import com.example.petshopapp.tabView.order.Tab2;

public class OrderViewPagerAdapter extends FragmentStatePagerAdapter {
    public OrderViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Tab1();
            case 1:
                return new Tab2();
            default:
                return new Tab1();
        }
    }

    @Override
    public int getCount() {
        return 2; // trả về 2 item
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "Tab1";
            case 1:
                return "Tab2";
            default:
                return "Tab1";
        }
    }
}
