package com.example.petshopapp.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petshopapp.R;
import com.example.petshopapp.api.apiservice.BangGiaService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.BangGiaSanPham;
import com.example.petshopapp.tabView.importProduct.model.ChiTietSanPham;

import java.math.BigDecimal;
import java.util.List;

public class ChiTietNhapSanPhamAdapter extends ArrayAdapter {
    Context context;
    int resource;

    View mView;

    TextView tvMaSanPham, tvTenSanPham, tvSoLuong, tvDonGia;
    Button btnDelete;
    List<ChiTietSanPham> data;
    public ChiTietNhapSanPhamAdapter(@NonNull Context context, int resource, List<ChiTietSanPham> data) {
        super(context, resource,data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }
    private String getString(BigDecimal bigDecimal){
        return bigDecimal==null? "" :bigDecimal.toString();
    }
    private BigDecimal convertBigDecimal(String text){
        if(text==null){
            SendMessage.sendCatch(mView.getContext(), "Text is null");
            return null;
        }
        try{
            if(text.isEmpty()){
                SendMessage.sendCatch(mView.getContext(), "Text is empty");
                return null;
            }
            return new BigDecimal(text);
        }
        catch (Exception e){
            SendMessage.sendCatch(mView.getContext(), e.getMessage());
        }
        return null;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        this.mView=convertView;
        ChiTietSanPham chiTietSanPham = data.get(position);

        tvMaSanPham = mView.findViewById(R.id.tvMaSanPham);
        tvTenSanPham = mView.findViewById(R.id.tvTenSanPham);
        tvDonGia = mView.findViewById(R.id.tvDonGia);
        tvSoLuong = mView.findViewById(R.id.tvSoLuong);
        btnDelete = mView.findViewById(R.id.btnDelete);

        tvMaSanPham.setText(String.valueOf(chiTietSanPham.getSanPham().getMaChiNhanh()));
        tvTenSanPham.setText(chiTietSanPham.getSanPham().getTenSanPham());
        tvSoLuong.setText(String.valueOf(chiTietSanPham.getSoLuong()));
        tvDonGia.setText(getString(chiTietSanPham.getDonGia()));

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(chiTietSanPham);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    
}
