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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petshopapp.R;
import com.example.petshopapp.model.BangGiaThuCung;
import com.example.petshopapp.model.ThuCung;
import com.example.petshopapp.tools.ImageInteract;

import java.math.BigDecimal;
import java.util.List;

public class MuaThuCungAdapter extends ArrayAdapter {
    Context context;
    int resource;
    List<BangGiaThuCung> data;
    public MuaThuCungAdapter(@NonNull Context context, int resource, List<BangGiaThuCung> data) {
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

        BangGiaThuCung x = data.get(position);
        tvTenThuCung.setText(x.getTenThuCung());
        BigDecimal gia=x.getGiaHienTai();
        BigDecimal giaKhuyenMai = x.getGiaKhuyenMai();
        if(giaKhuyenMai!=null){
            tvGiaThuCung.setText(String.valueOf(giaKhuyenMai)+" VND (Sale off)");
            tvGiaThuCung.setTextColor(Color.RED);
        }
        else if(gia != null){
            tvGiaThuCung.setText(String.valueOf(gia)+" VND");
        }
        else{
            tvGiaThuCung.setText("Liên hệ");
            tvGiaThuCung.setTextColor(Color.RED);
        }
        if(x.getHinhAnh()!=null){
            Bitmap bitmap= ImageInteract.convertStringToBitmap(x.getHinhAnh());
            ivAvatarThuCung.setImageBitmap(bitmap);
        }


        return convertView;
    }
}