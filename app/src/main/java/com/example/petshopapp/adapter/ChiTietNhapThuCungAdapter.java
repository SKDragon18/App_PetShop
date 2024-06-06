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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petshopapp.R;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.BangGiaThuCung;
import com.example.petshopapp.tabView.importProduct.model.ChiTietThuCung;

import java.math.BigDecimal;
import java.util.List;

public class ChiTietNhapThuCungAdapter extends ArrayAdapter {
    Context context;
    int resource;

    View mView;

    TextView tvMaThuCung, tvTenThuCung, tvSoLuong, tvDonGia;
    Button btnDelete;
    List<ChiTietThuCung> data;
    public ChiTietNhapThuCungAdapter(@NonNull Context context, int resource, List<ChiTietThuCung> data) {
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
        ChiTietThuCung chiTietThuCung = data.get(position);

        tvMaThuCung = mView.findViewById(R.id.tvMaThuCung);
        tvTenThuCung = mView.findViewById(R.id.tvTenThuCung);
        tvSoLuong =mView.findViewById(R.id.tvSoLuong);
        tvDonGia= mView.findViewById(R.id.tvDonGia);

        btnDelete = mView.findViewById(R.id.btnDelete);

        tvMaThuCung.setText(String.valueOf(chiTietThuCung.getThuCung().getMaThuCung()));
        tvTenThuCung.setText(chiTietThuCung.getThuCung().getTenThuCung());
        tvSoLuong.setText(String.valueOf(chiTietThuCung.getSoLuong()));
        tvDonGia.setText(getString(chiTietThuCung.getGiaNhap()));

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(chiTietThuCung);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
