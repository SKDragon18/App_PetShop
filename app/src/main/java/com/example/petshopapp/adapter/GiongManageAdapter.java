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
import com.example.petshopapp.api.apiservice.GiongService;
import com.example.petshopapp.api.apiservice.LoaiThuCungService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.Giong;
import com.example.petshopapp.model.LoaiThuCung;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GiongManageAdapter extends ArrayAdapter {
    Context context;
    int resource;

    View mView;

    TextView tvMaGiong, tvTenGiong, tvLoaiThuCung;
    Button btnUpdate, btnDelete;
    List<Giong> data;
    GiongService giongService;

    LoaiThuCungService loaiThuCungService;

    List<LoaiThuCung> loaiThuCungList= new ArrayList<>();

    List<String> tenLoaiThuCungList=new ArrayList<>();
    public GiongManageAdapter(@NonNull Context context, int resource, List<Giong> data) {
        super(context, resource,data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    protected void DocDLLoaiThuCung(ArrayAdapter adapterDSLoaiThuCung){
        loaiThuCungService.getAll().enqueue(new Callback<List<LoaiThuCung>>() {
            @Override
            public void onResponse(Call<List<LoaiThuCung>> call, Response<List<LoaiThuCung>> response) {
                if(response.code()==200){
                    loaiThuCungList.clear();
                    tenLoaiThuCungList.clear();
                    for(LoaiThuCung x: response.body()){
                        loaiThuCungList.add(x);
                        tenLoaiThuCungList.add(x.getTenLoaiThuCung());
                    }
                    adapterDSLoaiThuCung.notifyDataSetChanged();
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
            public void onFailure(Call<List<LoaiThuCung>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }

    private void openUpdateDialog(int gravity, Giong giong){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_giong_update);

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

        //Init

        EditText edtTenGiong = dialog.findViewById(R.id.edtTenGiong);
        Spinner spLoaiThuCung = dialog.findViewById(R.id.spLoaiThuCung);

        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        edtTenGiong.setText(giong.getTengiong());



        ArrayAdapter adapterDSLoaiThuCung = new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenLoaiThuCungList);
        spLoaiThuCung.setAdapter(adapterDSLoaiThuCung);

        DocDLLoaiThuCung(adapterDSLoaiThuCung);

        spLoaiThuCung.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tenLoaiThuCung = spLoaiThuCung.getSelectedItem().toString();
                for(LoaiThuCung x: loaiThuCungList){
                    if(x.getTenLoaiThuCung().equals(tenLoaiThuCung)){
                        giong.setLoaiThuCung(x);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spLoaiThuCung.setSelection(tenLoaiThuCungList.indexOf(giong.getLoaiThuCung().getTenLoaiThuCung()));
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giong.setTengiong(edtTenGiong.getText().toString());
                giongService.update(giong).enqueue(new Callback<Giong>() {
                    @Override
                    public void onResponse(Call<Giong> call, Response<Giong> response) {
                        if(response.code() == 200) {
                            Giong giong = response.body();
                            Toast.makeText(mView.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();

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
                    public void onFailure(Call<Giong> call, Throwable throwable) {
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

    private void openDeleteDialog(int gravity, Giong giong){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_giong_delete);

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

        EditText edtTenGiong = dialog.findViewById(R.id.edtTenGiong);
        EditText edtTenLoai = dialog.findViewById(R.id.edtTenLoaiThuCung);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        edtTenGiong.setText(giong.getTengiong());
        edtTenLoai.setText(giong.getLoaiThuCung().getTenLoaiThuCung());

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giongService.delete(giong.getMaGiong()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try{
                            if(response.code() == 200){
                                String result = response.body().string();
                                Toast.makeText(mView.getContext(),"Xóa thành công",Toast.LENGTH_SHORT).show();
                                data.remove(giong);
                                notifyDataSetChanged();

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
        Giong giong = data.get(position);

        tvMaGiong= mView.findViewById(R.id.tvMaGiong);
        tvTenGiong = mView.findViewById(R.id.tvTenGiong);
        tvLoaiThuCung=mView.findViewById(R.id.tvLoaiThuCung);
        btnUpdate=mView.findViewById(R.id.btnUpdate);
        btnDelete=mView.findViewById(R.id.btnDelete);

        tvMaGiong.setText(String.valueOf(giong.getMaGiong()));
        tvTenGiong.setText(giong.getTengiong());
        tvLoaiThuCung.setText(giong.getLoaiThuCung().getTenLoaiThuCung());

        Retrofit retrofit = ApiClient.getClient();
        giongService =retrofit.create(GiongService.class);
        loaiThuCungService=retrofit.create(LoaiThuCungService.class);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUpdateDialog(Gravity.CENTER, giong);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteDialog(Gravity.CENTER, giong);
            }
        });

        return convertView;
    }
}
