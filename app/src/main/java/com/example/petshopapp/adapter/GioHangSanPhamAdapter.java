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
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.BangGiaService;
import com.example.petshopapp.api.apiservice.GioHangService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.BangGiaSanPham;
import com.example.petshopapp.model.DonDatSanPhamGui;
import com.example.petshopapp.model.DonDatSanPhamGui;
import com.example.petshopapp.model.GioHangSanPhamGui;
import com.example.petshopapp.model.SanPham;
import com.example.petshopapp.model.SanPham;

import java.math.BigDecimal;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GioHangSanPhamAdapter extends ArrayAdapter {
    Context context;
    int resource;

    View mView;

    TextView tvMaSanPham, tvTenSanPham, tvTenLoaiSanPham, tvSoLuong;
    Button btnPlus, btnSub;
    List<SanPham> data;
    List<DonDatSanPhamGui> donDatSanPhamGuiList;
    String maKhachHang;
    GioHangService gioHangService;
    public GioHangSanPhamAdapter(@NonNull Context context, int resource, List<SanPham> data,
                                 List<DonDatSanPhamGui> donDatSanPhamGuiList, String maKhachHang) {
        super(context, resource,data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.donDatSanPhamGuiList = donDatSanPhamGuiList;
        ApiClient apiClient = ApiClient.getApiClient();
        gioHangService = apiClient.getRetrofit().create(GioHangService.class);
        this.maKhachHang = maKhachHang;
    }

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
                if(soLuong<1){
                    boGioHang(sanPham,donDatSanPhamGui);
                    return;
                }
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

        return convertView;
    }

    private void boGioHang(SanPham sanPham, DonDatSanPhamGui donDatSanPhamGui){
        GioHangSanPhamGui gioHangSanPhamGui = new GioHangSanPhamGui();
        gioHangSanPhamGui.setMaSanPham(sanPham.getMaSanPham());
        gioHangSanPhamGui.setMaKhachHang(maKhachHang);
        gioHangSanPhamGui.setMaChiNhanh(sanPham.getMaChiNhanh());
        gioHangService.boSanPham(gioHangSanPhamGui).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    if(response.code() == 200){
                        String result = response.body().string();
                        data.remove(sanPham);
                        donDatSanPhamGuiList.remove(donDatSanPhamGui);
                        notifyDataSetChanged();
                        Toast.makeText(mView.getContext(),result, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        try {
                            int code = response.code();
                            String message = response.message();
                            String error = response.errorBody().string();
                            SendMessage.sendMessageFail(mView.getContext(),code,error,message);
                        } catch (Exception e) {
                            SendMessage.sendCatch(mView.getContext(),e.getMessage());
                        }
                    }
                }
                catch (Exception e){
                    SendMessage.sendCatch(mView.getContext(),e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }
    
}
