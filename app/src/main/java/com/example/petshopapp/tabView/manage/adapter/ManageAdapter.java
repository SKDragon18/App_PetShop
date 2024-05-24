package com.example.petshopapp.tabView.manage.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.petshopapp.tabView.manage.GiongTab;
import com.example.petshopapp.tabView.manage.LoaiSanPhamTab;
import com.example.petshopapp.tabView.manage.LoaiThuCungTab;
import com.example.petshopapp.tabView.manage.ThuCungTab;


public class ManageAdapter extends FragmentStatePagerAdapter {
    public ManageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ThuCungTab();
            case 1:
                return new LoaiThuCungTab();
            case 2:
                return new GiongTab();
            case 3:
                return new LoaiSanPhamTab();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 4; // trả về số item
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "ThuCung";
            case 1:
                return "LoaiThuCung";
            case 2:
                return "Giong";
            case 3:
                return "LoaiSanPham";
            default:
                return "ThuCung";
        }
    }
}
