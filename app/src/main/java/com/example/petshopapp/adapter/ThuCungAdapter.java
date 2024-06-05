package com.example.petshopapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petshopapp.R;
import com.example.petshopapp.model.ThuCung;

import java.math.BigDecimal;
import java.util.List;

public class ThuCungAdapter extends ArrayAdapter {
    Context context;
    int resource;
    List<ThuCung> data;
    public ThuCungAdapter(@NonNull Context context, int resource, List<ThuCung> data) {
        super(context, resource,data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        ImageView ivAvatarThuCung=convertView.findViewById(R.id.ivAvatarThuCung);
        TextView tvTenThuCung=convertView.findViewById(R.id.tvTenThuCung);
        TextView tvGiaThuCung=convertView.findViewById(R.id.tvGiaThuCung);

        ThuCung x = data.get(position);
        tvTenThuCung.setText(x.getTenThuCung());
        BigDecimal gia=x.getGiaHienTai();
        if(gia==null){
            tvGiaThuCung.setText("Liên hệ");
            tvGiaThuCung.setTextColor(Color.RED);
        }
        else{
            tvGiaThuCung.setText(String.valueOf(gia)+" VND");
        }
        return convertView;
    }
}