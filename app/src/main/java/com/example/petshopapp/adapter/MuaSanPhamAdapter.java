package com.example.petshopapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.example.petshopapp.model.BangGiaSanPham;
import com.example.petshopapp.model.ThuCung;
import com.example.petshopapp.tools.ImageInteract;

import java.math.BigDecimal;
import java.util.List;

public class MuaSanPhamAdapter extends ArrayAdapter {
    Context context;
    int resource;
    List<BangGiaSanPham> data;
    public MuaSanPhamAdapter(@NonNull Context context, int resource, List<BangGiaSanPham> data) {
        super(context, resource,data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        ImageView ivAvatarSanPham=convertView.findViewById(R.id.ivAvatarSanPham);
        TextView tvTenSanPham=convertView.findViewById(R.id.tvTenSanPham);
        TextView tvGiaSanPham=convertView.findViewById(R.id.tvGiaSanPham);

        BangGiaSanPham x = data.get(position);
        tvTenSanPham.setText(x.getTenSanPham());
        BigDecimal gia=x.getGiaHienTai();
        BigDecimal giaKhuyenMai = x.getGiaKhuyenMai();
        if(giaKhuyenMai!=null){
            tvGiaSanPham.setText(String.valueOf(giaKhuyenMai)+" VND (Sale off)");
            tvGiaSanPham.setTextColor(Color.RED);
        }
        else if(gia != null){
            tvGiaSanPham.setText(String.valueOf(gia)+" VND");
        }
        else{
            tvGiaSanPham.setText("Liên hệ");
            tvGiaSanPham.setTextColor(Color.RED);
        }
        if(x.getHinhAnh()!=null){
            Bitmap bitmap= ImageInteract.convertStringToBitmap(x.getHinhAnh());
            ivAvatarSanPham.setImageBitmap(bitmap);
        }

        return convertView;
    }
}