package com.example.petshopapp.tabView.manageEmployee.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.petshopapp.tabView.manageEmployee.ChiNhanhTab;
import com.example.petshopapp.tabView.manageEmployee.NhanVienTab;


public class ManageEmployeeAdapter extends FragmentStatePagerAdapter {
    public ManageEmployeeAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ChiNhanhTab();
            case 1:
                return new NhanVienTab();
            default:
                return new Fragment();
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
                return "ChiNhanh";
            case 1:
                return "NhanVien";
            default:
                return "ThuCung";
        }
    }
}
