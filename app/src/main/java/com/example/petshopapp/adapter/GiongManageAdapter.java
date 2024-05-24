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

    protected void DocDLLoaiThuCung(){
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
                }
                else{
                    Toast.makeText(mView.getContext(),"Lỗi: "+String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<LoaiThuCung>> call, Throwable throwable) {
                Log.e("ERROR_API","Call api fail: "+throwable.getMessage());
                Toast.makeText(mView.getContext(),"Call api fail: "+throwable.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openUpdateDialog(int gravity, Giong giong){
        try{
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

            DocDLLoaiThuCung();

            ArrayAdapter adapterDSLoaiThuCung = new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenLoaiThuCungList);
            spLoaiThuCung.setAdapter(adapterDSLoaiThuCung);

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
            for(String x: tenLoaiThuCungList){
                System.out.println(x);
            }
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    giong.setTengiong(edtTenGiong.getText().toString());
                    giongService.update(giong).enqueue(new Callback<Giong>() {
                        @Override
                        public void onResponse(Call<Giong> call, Response<Giong> response) {
                            if(response.code() == 200) {
                                Giong giong = response.body();
                                notifyDataSetChanged();
                                Toast.makeText(mView.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(mView.getContext(),"Lỗi: "+String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Giong> call, Throwable throwable) {
                            Log.e("ERROR_API","Call api fail: "+throwable.getMessage());
                            Toast.makeText(mView.getContext(),"Call api fail: "+throwable.getMessage(),Toast.LENGTH_SHORT).show();
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
        catch(Exception e){
            System.out.println(e.getMessage().toString());
        }

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
                                data.remove(giong);
                                notifyDataSetChanged();
                                Toast.makeText(mView.getContext(),"Xóa thành công",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String message="Lỗi: "+String.valueOf(response.code())
                                        +"\n"+"Chi tiết: "+ response.errorBody().string();
                                Toast.makeText(mView.getContext(),"Tồn tại trong database",Toast.LENGTH_SHORT).show();
                                Log.e("ERROR","Call api fail: "+message);
                            }
                        }
                        catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        Log.e("ERROR_API","Call api fail: "+throwable.getMessage());
                        Toast.makeText(mView.getContext(),"Call api fail: "+throwable.getMessage(),Toast.LENGTH_SHORT).show();
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
