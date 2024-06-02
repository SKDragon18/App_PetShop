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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petshopapp.R;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.BangGiaService;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.BangGia;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.tools.TimeConvert;
import com.example.petshopapp.widget.CalendarDialog;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BangGiaManageAdapter extends ArrayAdapter {
    Context context;
    int resource;

    View mView;

    TextView tvMaBangGia, tvTGBatDau, tvTGKetThuc, tvChiNhanh;
    Button btnUpdate, btnDelete;
    List<BangGia> data;
    private BangGiaService bangGiaService;
    public BangGiaManageAdapter(@NonNull Context context, int resource, List<BangGia> data) {
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
        BangGia bangGia = data.get(position);

        tvMaBangGia = mView.findViewById(R.id.tvMaBangGia);
        tvChiNhanh = mView.findViewById(R.id.tvChiNhanh);
        tvTGBatDau=mView.findViewById(R.id.tvTGBatDau);
        tvTGKetThuc=mView.findViewById(R.id.tvTGKetThuc);

        btnUpdate=mView.findViewById(R.id.btnUpdate);
        btnDelete=mView.findViewById(R.id.btnDelete);


        tvMaBangGia.setText(String.valueOf(bangGia.getMaBangGia()));
        tvChiNhanh.setText(bangGia.getChiNhanh().getTenChiNhanh());
        tvTGBatDau.setText(TimeConvert.convertJavaDatetime(bangGia.getThoiGianBatDau()));
        tvTGKetThuc.setText(TimeConvert.convertJavaDatetime(bangGia.getThoiGianKetThuc()));

        Retrofit retrofit = ApiClient.getClient();
        bangGiaService =retrofit.create(BangGiaService.class);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUpdateDialog(Gravity.CENTER, bangGia);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteDialog(Gravity.CENTER, bangGia);
            }
        });

        return convertView;
    }

    private void openUpdateDialog(int gravity, BangGia bangGia){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_banggia_update);

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

        EditText edtMaBangGia = dialog.findViewById(R.id.edtMaBangGia);
        EditText edtNoiDung = dialog.findViewById(R.id.edtNoiDung);
        EditText edtTGBatDau = dialog.findViewById(R.id.edtTGBatDau);
        EditText edtTGKetThuc = dialog.findViewById(R.id.edtTGKetThuc);
        EditText edtChiNhanh = dialog.findViewById(R.id.edtChiNhanh);

        edtMaBangGia.setText(String.valueOf(bangGia.getMaBangGia()));
        edtNoiDung.setText(bangGia.getNoiDung());
        edtChiNhanh.setText(bangGia.getChiNhanh().getTenChiNhanh());
        edtTGBatDau.setText(TimeConvert.convertJavaDatetime(bangGia.getThoiGianBatDau()));
        edtTGKetThuc.setText(TimeConvert.convertJavaDatetime(bangGia.getThoiGianKetThuc()));

        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnCalendarBD = dialog.findViewById(R.id.btnCalendarBD);
        Button btnCalendarKT = dialog.findViewById(R.id.btnCalendarKT);
        CalendarDialog calendarDialogBD = new CalendarDialog();
        CalendarDialog calendarDialogKT = new CalendarDialog();

        btnCalendarBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarDialogBD.open(mView.getContext(),edtTGBatDau);
            }
        });

        btnCalendarKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarDialogKT.open(mView.getContext(), edtTGKetThuc);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bangGia.setNoiDung(edtNoiDung.getText().toString());
                bangGia.setThoiGianBatDau(calendarDialogBD.getTimestamp());
                bangGia.setThoiGianKetThuc(calendarDialogKT.getTimestamp());
                bangGiaService.update(bangGia).enqueue(new Callback<BangGia>() {
                    @Override
                    public void onResponse(Call<BangGia> call, Response<BangGia> response) {
                        if(response.code() == 200) {
                            BangGia bangGiaTemp = response.body();
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
                    public void onFailure(Call<BangGia> call, Throwable throwable) {
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

    private void openDeleteDialog(int gravity, BangGia bangGia){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_banggia_delete);

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

        EditText edtMaBangGia = dialog.findViewById(R.id.edtMaBangGia);
        EditText edtNoiDung = dialog.findViewById(R.id.edtNoiDung);
        EditText edtTGBatDau = dialog.findViewById(R.id.edtTGBatDau);
        EditText edtTGKetThuc = dialog.findViewById(R.id.edtTGKetThuc);
        EditText edtChiNhanh = dialog.findViewById(R.id.edtChiNhanh);

        edtMaBangGia.setText(String.valueOf(bangGia.getMaBangGia()));
        edtNoiDung.setText(bangGia.getNoiDung());
        edtChiNhanh.setText(bangGia.getChiNhanh().getTenChiNhanh());
        edtTGBatDau.setText(TimeConvert.convertJavaDatetime(bangGia.getThoiGianBatDau()));
        edtTGKetThuc.setText(TimeConvert.convertJavaDatetime(bangGia.getThoiGianKetThuc()));

        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bangGiaService.delete(bangGia.getMaBangGia()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try{
                            if(response.code() == 200){
                                String result = response.body().string();
                                data.remove(bangGia);
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

    
}
