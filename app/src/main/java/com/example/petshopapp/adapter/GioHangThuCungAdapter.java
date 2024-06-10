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
import com.example.petshopapp.model.DonDatThuCungGui;
import com.example.petshopapp.model.ThuCung;

import java.util.List;

public class GioHangThuCungAdapter extends ArrayAdapter {
    Context context;
    int resource;

    View mView;

    TextView tvMaThuCung, tvTenThuCung, tvTenGiong, tvSoLuong;
    Button btnPlus, btnSub;
    List<ThuCung> data;
    List<DonDatThuCungGui> donDatThuCungGuiList;
    public GioHangThuCungAdapter(@NonNull Context context, int resource, List<ThuCung> data,
                                 List<DonDatThuCungGui> donDatThuCungGuiList) {
        super(context, resource,data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.donDatThuCungGuiList = donDatThuCungGuiList;
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
        ThuCung thuCung = data.get(position);
        DonDatThuCungGui donDatThuCungGui = donDatThuCungGuiList.get(position);
        tvMaThuCung = mView.findViewById(R.id.tvMaThuCung);
        tvTenThuCung = mView.findViewById(R.id.tvTenThuCung);
        tvTenGiong = mView.findViewById(R.id.tvTenGiong);
        tvSoLuong = mView.findViewById(R.id.tvSoLuong);
        btnPlus = mView.findViewById(R.id.btnPlus);
        btnSub = mView.findViewById(R.id.btnSub);

        tvMaThuCung.setText(String.valueOf(thuCung.getMaThuCung()));
        tvTenThuCung.setText(thuCung.getTenThuCung());
        tvTenGiong.setText(thuCung.getGiong().getTengiong());

        tvSoLuong.setText(String.valueOf(donDatThuCungGui.getSoLuong()));
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soLuong = donDatThuCungGui.getSoLuong();
                soLuong -= 1;
                if(soLuong<1)return;
                donDatThuCungGui.setSoLuong(soLuong);
                notifyDataSetChanged();
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soLuong = donDatThuCungGui.getSoLuong();
                int soLuongMax = thuCung.getSoLuongTon();
                soLuong += 1;
                if(soLuong>Integer.MAX_VALUE)return;
                if(soLuong>soLuongMax){
                    Toast.makeText(mView.getContext(), "Vượt quá số lượng tồn",Toast.LENGTH_SHORT).show();
                    return;
                }
                donDatThuCungGui.setSoLuong(soLuong);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    
}
