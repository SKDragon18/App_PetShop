package com.example.petshopapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.petshopapp.R;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.DonDatHangService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.DonDat;
import com.example.petshopapp.model.HoaDonGui;
import com.example.petshopapp.tools.TimeConvert;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonDatHangManageAdapter extends ArrayAdapter {
    Context context;
    int resource;

    View mView;
    List<DonDat> data;
    DonDatHangService donDatHangService;
    String maNV;
    public DonDatHangManageAdapter(@NonNull Context context, int resource, List<DonDat> data, String maNV) {
        super(context, resource,data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.maNV =maNV;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        this.mView=convertView;
        DonDat donDat = data.get(position);

        ApiClient apiClient = ApiClient.getApiClient();
        donDatHangService = apiClient.getRetrofit().create(DonDatHangService.class);

        TextView tvSoDonDat = mView.findViewById(R.id.tvSoDonDat);
        TextView tvNgayLap = mView.findViewById(R.id.tvNgayLap);
        TextView tvMaKhachHang = mView.findViewById(R.id.tvMaKhachHang);
        TextView tvSoDienThoai = mView.findViewById(R.id.tvSoDienThoai);
        Button btnTrangThai = mView.findViewById(R.id.btnTrangThai);
        tvSoDonDat.setText(String.valueOf(donDat.getSoDonDat()));
        tvNgayLap.setText(TimeConvert.convertJavaDate(donDat.getNgayLap()));
        tvMaKhachHang.setText(donDat.getMaKhachhang());
        tvSoDienThoai.setText(donDat.getSoDienThoai());
        if(donDat.getTrangThai()){
            btnTrangThai.setEnabled(false);
            btnTrangThai.setBackgroundResource(R.drawable.allow);
        }
        else{
            btnTrangThai.setEnabled(true);
            btnTrangThai.setBackgroundResource(R.drawable.red_create);
            btnTrangThai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCreateDialog(donDat);
                }
            });
        }
        return convertView;
    }

    private void openCreateDialog(DonDat donDat){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn có chắc muốn TẠO HÓA ĐƠN cho đơn đặt này?");
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HoaDonGui hoaDonGui = new HoaDonGui();
                hoaDonGui.setMaNhanVien(maNV);
                hoaDonGui.setSoHoaDon(donDat.getSoDonDat());
                donDatHangService.taoHoaDon(hoaDonGui).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code()==200){
                            try{
                                String result = response.body().string();
                                donDat.setTrangThai(Boolean.TRUE);
                                notifyDataSetChanged();
                                Toast.makeText(mView.getContext(), result,Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e){
                                SendMessage.sendCatch(mView.getContext(), e.getMessage());
                            }
                        }
                        else{
                            try {
                                int code = response.code();
                                String message = response.message();
                                String error = response.errorBody().string();
                                SendMessage.sendMessageFail(mView.getContext(),code,error,message);
                            } catch (Exception e) {
                                SendMessage.sendCatch(mView.getContext(),e.getMessage());
                                return;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        SendMessage.sendApiFail(mView.getContext(),throwable);
                    }
                });
            }
        });
        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý khi người dùng hủy bỏ thao tác
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
