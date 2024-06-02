package com.example.petshopapp.tabView.manageFinance.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.petshopapp.tabView.manageFinance.BangGiaTab;

public class ManageFinanceAdapter extends FragmentStatePagerAdapter {
    public ManageFinanceAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new BangGiaTab();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 1; // trả về 1 item
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "BangGia";
            default:
                return "ThuCung";
        }
    }
}
