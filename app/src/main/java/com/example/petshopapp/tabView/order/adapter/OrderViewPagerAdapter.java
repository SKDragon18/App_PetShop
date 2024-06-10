package com.example.petshopapp.tabView.order.adapter;

//package com.example.petshopapp.fragment.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.petshopapp.tabView.order.MuaThuCungTab;
import com.example.petshopapp.tabView.order.MuaSanPhamTab;

public class OrderViewPagerAdapter extends FragmentStatePagerAdapter {
    public OrderViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MuaThuCungTab();
            case 1:
                return new MuaSanPhamTab();
            default:
                return new MuaThuCungTab();
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
                return "Thú cưng";
            case 1:
                return "Sản phẩm";
            default:
                return "Tab1";
        }
    }
}
