package com.example.petshopapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petshopapp.R;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.BangGiaService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.BangGia;
import com.example.petshopapp.model.HoaDon;
import com.example.petshopapp.tools.TimeConvert;
import com.example.petshopapp.widget.CalendarDialog;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HoaDonViewAdapter extends ArrayAdapter {
    Context context;
    int resource;

    View mView;

    TextView tvMaHoaDon, tvNgayLap, tvTongHoaDon, tvHinhThucThanhToan;
    List<HoaDon> data;
    private BangGiaService bangGiaService;
    public HoaDonViewAdapter(@NonNull Context context, int resource, List<HoaDon> data) {
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
        HoaDon hoaDon = data.get(position);

        tvMaHoaDon = mView.findViewById(R.id.tvMaHoaDon);
        tvNgayLap = mView.findViewById(R.id.tvNgayLap);
        tvTongHoaDon = mView.findViewById(R.id.tvTongHoaDon);
        tvHinhThucThanhToan = mView.findViewById(R.id.tvHinhThucThanhToan);

        tvMaHoaDon.setText(String.valueOf(hoaDon.getMaHoaDon()));
        tvNgayLap.setText(TimeConvert.convertJavaDatetime(hoaDon.getNgayLap()));
        tvTongHoaDon.setText(String.valueOf(hoaDon.getTongHoaDon()));
        String maNhanVien = hoaDon.getMaNhanVien();
        if(maNhanVien==null||maNhanVien.equals("")){
            tvHinhThucThanhToan.setText("Online");
        }
        else{
            tvHinhThucThanhToan.setText("NV nhận tiền mặt: "+maNhanVien);
        }


        return convertView;
    }
}
