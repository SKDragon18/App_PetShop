package com.example.petshopapp.fragment.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.petshopapp.factory.FragmentFactoryCustom;
import com.example.petshopapp.fragment.CartScreen;
import com.example.petshopapp.fragment.HomeScreen;
import com.example.petshopapp.fragment.UserScreen;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    List<String>fragmentNameList;
    FragmentFactoryCustom fragmentFactory;
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, List<String>fragmentNameList) {
        super(fm, behavior);
        this.fragmentNameList=fragmentNameList;
        fragmentFactory=new FragmentFactoryCustom();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentFactory.createFragment(fragmentNameList.get(position));//Trả về fragment screen
    }

    @Override
    public int getCount() {
        return fragmentNameList.size(); // trả về số item
    }
}
