package com.example.petshopapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petshopapp.R;
import com.example.petshopapp.model.DonNhapHang;
import com.example.petshopapp.tools.TimeConvert;

import java.util.List;


public class NhapHangAdapter extends ArrayAdapter {
    Context context;
    int resource;

    View mView;

    TextView tvMaDonNhapHang, tvNgayLap;
    List<DonNhapHang> data;
    public NhapHangAdapter(@NonNull Context context, int resource, List<DonNhapHang> data) {
        super(context, resource,data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        this.mView=convertView;
        DonNhapHang donNhapHang = data.get(position);

        tvMaDonNhapHang = mView.findViewById(R.id.tvMaDonNhapHang);
        tvNgayLap = mView.findViewById(R.id.tvNgayLap);


        tvMaDonNhapHang.setText(String.valueOf(donNhapHang.getMaDonNhapHang()));
        tvNgayLap.setText(TimeConvert.convertJavaDatetime(donNhapHang.getNgayLap()));

        return convertView;
    }
}
