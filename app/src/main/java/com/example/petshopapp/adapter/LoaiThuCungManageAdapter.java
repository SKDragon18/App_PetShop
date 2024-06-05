package com.example.petshopapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petshopapp.R;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.LoaiThuCungService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.LoaiThuCung;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoaiThuCungManageAdapter extends ArrayAdapter {
    Context context;
    int resource;

    View mView;

    TextView tvIdLoaiThuCung,tvTenLoaiThuCung;
    Button btnUpdate, btnDelete;
    List<LoaiThuCung> data;
    LoaiThuCungService loaiThuCungService;
    public LoaiThuCungManageAdapter(@NonNull Context context, int resource, List<LoaiThuCung> data) {
        super(context, resource,data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    private void openUpdateDialog(int gravity, LoaiThuCung loaiThuCung){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loaithucung_update);

        Window window = dialog.getWindow();
        if(window == null)return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.BOTTOM == gravity){
            dialog.setCancelable(false);
        }
        else{
            dialog.setCancelable(true);
        }

        EditText edtTenLoaiThucCung = dialog.findViewById(R.id.edtTenLoaiThuCung);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);
        edtTenLoaiThucCung.setText(loaiThuCung.getTenLoaiThuCung());

        final LoaiThuCung loaiThuCungTemp=loaiThuCung;

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loaiThuCungTemp.setTenLoaiThuCung(edtTenLoaiThucCung.getText().toString());
                loaiThuCungService.update(loaiThuCungTemp).enqueue(new Callback<LoaiThuCung>() {
                    @Override
                    public void onResponse(Call<LoaiThuCung> call, Response<LoaiThuCung> response) {
                        if(response.code() == 200) {
                            LoaiThuCung loaiThuCungTemp = response.body();
                            notifyDataSetChanged();
                            Toast.makeText(mView.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
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
                    public void onFailure(Call<LoaiThuCung> call, Throwable throwable) {
                        SendMessage.sendApiFail(mView.getContext(),throwable);
                    }
                });

                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openDeleteDialog(int gravity, LoaiThuCung loaiThuCung){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loaithucung_delete);

        Window window = dialog.getWindow();
        if(window == null)return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.BOTTOM == gravity){
            dialog.setCancelable(false);
        }
        else{
            dialog.setCancelable(true);
        }

        EditText edtTenLoaiThucCung = dialog.findViewById(R.id.edtTenLoaiThuCung);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);
        edtTenLoaiThucCung.setText(loaiThuCung.getTenLoaiThuCung());

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loaiThuCungService.delete(loaiThuCung.getMaLoaiThuCung()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try{
                            if(response.code() == 200){
                                String result = response.body().string();
                                data.remove(loaiThuCung);
                                notifyDataSetChanged();
                                Toast.makeText(mView.getContext(),result,Toast.LENGTH_SHORT).show();
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
                        catch (Exception e){
                            SendMessage.sendCatch(mView.getContext(),e.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        SendMessage.sendApiFail(mView.getContext(),throwable);
                    }
                });

                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        this.mView=convertView;
        LoaiThuCung loaiThuCung = data.get(position);

        tvIdLoaiThuCung = mView.findViewById(R.id.tvIdLoaiThuCung);
        tvTenLoaiThuCung = mView.findViewById(R.id.tvTenLoaiThuCung);
        btnUpdate=mView.findViewById(R.id.btnUpdate);
        btnDelete=mView.findViewById(R.id.btnDelete);


        tvIdLoaiThuCung.setText(String.valueOf(loaiThuCung.getMaLoaiThuCung()));
        tvTenLoaiThuCung.setText(loaiThuCung.getTenLoaiThuCung());

        ApiClient apiClient = ApiClient.getApiClient();
        loaiThuCungService =apiClient.getRetrofit().create(LoaiThuCungService.class);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUpdateDialog(Gravity.CENTER, loaiThuCung);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteDialog(Gravity.CENTER, loaiThuCung);
            }
        });

        return convertView;
    }
}
