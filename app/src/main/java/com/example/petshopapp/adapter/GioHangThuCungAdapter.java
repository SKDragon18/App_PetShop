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
import com.example.petshopapp.api.apiservice.GioHangService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.DonDatThuCungGui;
import com.example.petshopapp.model.GioHangThuCungGui;
import com.example.petshopapp.model.ThuCung;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GioHangThuCungAdapter extends ArrayAdapter {
    Context context;
    int resource;

    View mView;

    TextView tvMaThuCung, tvTenThuCung, tvTenGiong, tvSoLuong;
    Button btnPlus, btnSub;
    List<ThuCung> data;
    List<DonDatThuCungGui> donDatThuCungGuiList;
    GioHangService gioHangService;
    String maKhachHang;

    public GioHangThuCungAdapter(@NonNull Context context, int resource, List<ThuCung> data,
                                 List<DonDatThuCungGui> donDatThuCungGuiList, String maKhachHang) {
        super(context, resource,data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.donDatThuCungGuiList = donDatThuCungGuiList;
        ApiClient apiClient = ApiClient.getApiClient();
        this.gioHangService = apiClient.getRetrofit().create(GioHangService.class);
        this.maKhachHang = maKhachHang;
    }

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
                if(soLuong<1){
                    boGioHang(thuCung, donDatThuCungGui);
                    return;
                }
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

    private void boGioHang(ThuCung thuCung, DonDatThuCungGui donDatThuCungGui){
        GioHangThuCungGui gioHangThuCungGui = new GioHangThuCungGui();
        gioHangThuCungGui.setMaThuCung(thuCung.getMaThuCung());
        gioHangThuCungGui.setMaKhachHang(maKhachHang);
        gioHangService.boThuCung(gioHangThuCungGui).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    if(response.code() == 200){
                        String result = response.body().string();
                        data.remove(thuCung);
                        donDatThuCungGuiList.remove(donDatThuCungGui);
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
