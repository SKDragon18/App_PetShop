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
import com.example.petshopapp.api.apiservice.BangGiaService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.BangGiaSanPham;
import com.example.petshopapp.model.DonDatSanPhamGui;
import com.example.petshopapp.model.SanPham;

import java.math.BigDecimal;
import java.util.List;

public class GioHangSanPhamAdapter extends ArrayAdapter {
    Context context;
    int resource;

    View mView;

    TextView tvMaSanPham, tvTenSanPham, tvTenLoaiSanPham, tvSoLuong;
    Button btnPlus, btnSub;
    List<SanPham> data;
    List<DonDatSanPhamGui> donDatSanPhamGuiList;
    public GioHangSanPhamAdapter(@NonNull Context context, int resource, List<SanPham> data,
                                 List<DonDatSanPhamGui> donDatSanPhamGuiList) {
        super(context, resource,data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.donDatSanPhamGuiList = donDatSanPhamGuiList;
    }
//    private String getString(BigDecimal bigDecimal){
//        return bigDecimal==null? "" :bigDecimal.toString();
//    }
//    private BigDecimal convertBigDecimal(String text){
//        if(text==null){
//            SendMessage.sendCatch(mView.getContext(), "Text is null");
//            return null;
//        }
//        try{
//            if(text.isEmpty()){
//                SendMessage.sendCatch(mView.getContext(), "Text is empty");
//                return null;
//            }
//            return new BigDecimal(text);
//        }
//        catch (Exception e){
//            SendMessage.sendCatch(mView.getContext(), e.getMessage());
//        }
//        return null;
//    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        this.mView=convertView;
        SanPham sanPham = data.get(position);
        DonDatSanPhamGui donDatSanPhamGui = donDatSanPhamGuiList.get(position);
        tvMaSanPham = mView.findViewById(R.id.tvMaSanPham);
        tvTenSanPham = mView.findViewById(R.id.tvTenSanPham);
        tvTenLoaiSanPham = mView.findViewById(R.id.tvTenLoaiSanPham);
        tvSoLuong = mView.findViewById(R.id.tvSoLuong);
        btnPlus = mView.findViewById(R.id.btnPlus);
        btnSub = mView.findViewById(R.id.btnSub);

        tvMaSanPham.setText(String.valueOf(sanPham.getMaSanPham()));
        tvTenSanPham.setText(sanPham.getTenSanPham());
        tvTenLoaiSanPham.setText(sanPham.getLoaiSanPham().getTenLoaiSanPham());
        tvSoLuong.setText(String.valueOf(donDatSanPhamGui.getSoLuong()));
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soLuong = donDatSanPhamGui.getSoLuong();
                soLuong -= 1;
                if(soLuong<1)return;
                donDatSanPhamGui.setSoLuong(soLuong);
                notifyDataSetChanged();
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soLuong = donDatSanPhamGui.getSoLuong();
                long soLuongMax = sanPham.getSoLuongTon();
                soLuong += 1;
                if(soLuong>Integer.MAX_VALUE)return;
                if(soLuong>soLuongMax){
                    Toast.makeText(mView.getContext(), "Vượt quá số lượng tồn",Toast.LENGTH_SHORT).show();
                    return;
                }
                donDatSanPhamGui.setSoLuong(soLuong);
                notifyDataSetChanged();
            }
        });

//        edtSoLuong.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(s.toString().equals("")){
//                    edtSoLuong.setError("Không thể rỗng");
//                    return;
//                }
//                int soLuong = Integer.parseInt(s.toString());
//                if(soLuong<1){
//                    edtSoLuong.setError("Không nhập số bé hơn 1");
//                    return;
//                }
//                donDatSanPhamGui.setSoLuong(soLuong);
//                Toast.makeText(mView.getContext(), String.valueOf(donDatSanPhamGui.getSoLuong()), Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });

        return convertView;
    }

    
}
