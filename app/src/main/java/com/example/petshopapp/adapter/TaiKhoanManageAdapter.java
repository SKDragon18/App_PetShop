package com.example.petshopapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
import androidx.appcompat.app.AlertDialog;

import com.example.petshopapp.R;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.TaiKhoanService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.TaiKhoan;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaiKhoanManageAdapter extends ArrayAdapter {
    Context context;
    int resource;

    View mView;

    TextView tvTenDangNhap, tvMatKhau, tvQuyen, tvTrangThai;
    Button btnReset, btnClock;
    List<TaiKhoan> data;
    TaiKhoanService taiKhoanService;
    public TaiKhoanManageAdapter(@NonNull Context context, int resource, List<TaiKhoan> data) {
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
        TaiKhoan taiKhoan = data.get(position);

        tvTenDangNhap = mView.findViewById(R.id.tvTenDangNhap);
        tvMatKhau=mView.findViewById(R.id.tvMatKhau);
        tvQuyen=mView.findViewById(R.id.tvQuyen);
        tvTrangThai=mView.findViewById(R.id.tvTrangThai);

        btnReset=mView.findViewById(R.id.btnReset);
        btnClock=mView.findViewById(R.id.btnClock);

        tvTenDangNhap.setText(taiKhoan.getTenDangNhap());
        tvMatKhau.setText("*****");
        tvQuyen.setText(taiKhoan.getQuyen());

        if(taiKhoan.getTrangThai()){
            tvTrangThai.setText("Hoạt động");
            btnClock.setBackgroundResource(R.drawable.ban);
        }
        else{
            tvTrangThai.setText("Đã khóa");
            btnClock.setBackgroundResource(R.drawable.allow);
        }

        Retrofit retrofit = ApiClient.getClient();
        taiKhoanService =retrofit.create(TaiKhoanService.class);

        btnClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClockDialog(taiKhoan);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResetDialog(taiKhoan);
            }
        });

        return convertView;
    }

    private void openClockDialog(TaiKhoan taiKhoan){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận");
        if(taiKhoan.getTrangThai()){
            builder.setMessage("Bạn có chắc muốn KHÓA tài khoản này?");
        }
        else{
            builder.setMessage("Bạn có chắc muốn MỞ KHÓA tài khoản này?");
        }

        taiKhoan.setTrangThai(!taiKhoan.getTrangThai());

        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                taiKhoanService.update(taiKhoan).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code()==200){
                            try {
                                String result = response.body().string();
                                Toast.makeText(getContext(),result, Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                            } catch (IOException e) {
                                SendMessage.sendCatch(mView.getContext(),e.getMessage());
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
                // Xử lý khi người dùng hủy bỏ thao tác xóa
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void openResetDialog(TaiKhoan taiKhoan){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn có chắc muốn RESET MẬT KHẨU tài khoản này?");
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                taiKhoanService.reset(taiKhoan).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code()==200){
                            try {
                                String result = response.body().string();
                                System.out.println(result);
                                Toast.makeText(getContext(),"Thành công reset", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                            } catch (IOException e) {
                                SendMessage.sendCatch(mView.getContext(),e.getMessage());
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
                // Xử lý khi người dùng hủy bỏ thao tác xóa
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
