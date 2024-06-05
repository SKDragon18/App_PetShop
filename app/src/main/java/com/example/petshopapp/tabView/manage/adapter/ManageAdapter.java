package com.example.petshopapp.tabView.manage.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.petshopapp.tabView.manage.GiongTab;
import com.example.petshopapp.tabView.manage.LoaiSanPhamTab;
import com.example.petshopapp.tabView.manage.LoaiThuCungTab;
import com.example.petshopapp.tabView.manage.SanPhamTab;
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
                return new LoaiThuCungTab();
            case 1:
                return new GiongTab();
            case 2:
                return new LoaiSanPhamTab();
            case 3:
                return new ThuCungTab();
            case 4:
                return new SanPhamTab();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 5; // trả về số item
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){

            case 0:
                return "LoaiThuCung";
            case 1:
                return "Giong";
            case 2:
                return "LoaiSanPham";
            case 3:
                return "ThuCung";
            case 4:
                return "SanPham";
            default:
                return "";
        }
    }
}
