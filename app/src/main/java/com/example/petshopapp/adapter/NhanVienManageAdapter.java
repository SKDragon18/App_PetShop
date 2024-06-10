package com.example.petshopapp.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petshopapp.R;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.Const;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.api.apiservice.HinhAnhService;
import com.example.petshopapp.api.apiservice.NhanVienService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.model.HinhAnh;
import com.example.petshopapp.model.NhanVien;
import com.example.petshopapp.model.NhanVien;
import com.example.petshopapp.tabView.manageEmployee.NhanVienTab;
import com.example.petshopapp.tools.ImageInteract;
import com.example.petshopapp.tools.RealPathUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NhanVienManageAdapter extends ArrayAdapter {
    Context context;
    int resource;

    //View
    View mView;
    TextView tvIdNhanVien,tvTenNhanVien;
    Button btnUpdate, btnDelete;

    //Api
    NhanVienService nhanVienService;
    ChiNhanhService chiNhanhService;
    HinhAnhService hinhAnhService;

    //Data
    List<NhanVien> data;
    Map<Integer,String> chiNhanhMap;

    //Adapter
    ArrayAdapter adapterDSChiNhanh;

    //Avatar
    private static final int MY_REQUEST_CODE = 123;



    public NhanVienManageAdapter(@NonNull Context context, int resource, List<NhanVien> data,
                                 Map<Integer,String> chiNhanhMap) {
        super(context, resource,data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.chiNhanhMap = chiNhanhMap;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        this.mView=convertView;

        TextView tvHoTen = convertView.findViewById(R.id.tvHoTen);
        TextView tvMaNhanVien = convertView.findViewById(R.id.tvMaNhanVien);
        TextView tvSDT= convertView.findViewById(R.id.tvSDT);
        TextView tvEmail=convertView.findViewById(R.id.tvEmail);
        Button btnUpdate = convertView.findViewById(R.id.btnUpdate);
        Button btnDelete=convertView.findViewById(R.id.btnDelete);

        NhanVien nhanVien = data.get(position);
        tvHoTen.setText(nhanVien.getHo()+" "+nhanVien.getTen());
        tvMaNhanVien.setText(nhanVien.getMaNhanVien());
        tvEmail.setText(nhanVien.getEmail());
        tvSDT.setText(nhanVien.getSoDienThoai());

        ApiClient apiClient = ApiClient.getApiClient();
        nhanVienService =apiClient.getRetrofit().create(NhanVienService.class);
        chiNhanhService=apiClient.getRetrofit().create(ChiNhanhService.class);
        hinhAnhService=apiClient.getRetrofit().create(HinhAnhService.class);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUpdateDialog(Gravity.CENTER, nhanVien);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteDialog(Gravity.CENTER, nhanVien);
            }
        });

        return convertView;
    }

    private void openUpdateDialog(int gravity, NhanVien nhanVien){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_nhanvien_update);

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

        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);
        EditText edtHo=dialog.findViewById(R.id.edtHo);
        EditText edtTen= dialog.findViewById(R.id.edtTen);
        EditText edtCCCD = dialog.findViewById(R.id.edtCCCD);
        EditText edtEmail=dialog.findViewById(R.id.edtEmail);
        EditText edtSDT= dialog.findViewById(R.id.edtSDT);
        EditText edtChucVu = dialog.findViewById(R.id.edtChucVu);
        EditText edtMaNhanVien=dialog.findViewById(R.id.edtMaNhanVien);
        Spinner spChiNhanh=dialog.findViewById(R.id.spChiNhanh);
        ImageView ivAvatar = dialog.findViewById(R.id.ivAvatar);
        
        edtMaNhanVien.setText(nhanVien.getMaNhanVien());
        edtHo.setText(nhanVien.getHo());
        edtTen.setText(nhanVien.getTen());
        edtChucVu.setText(nhanVien.getChucVu());
        edtCCCD.setText(nhanVien.getCccd());
        edtEmail.setText(nhanVien.getEmail());
        edtSDT.setText(nhanVien.getSoDienThoai());
        getImage(nhanVien,ivAvatar);

        List<String> tenChiNhanhList = new ArrayList<>(chiNhanhMap.values());
        adapterDSChiNhanh = new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1,tenChiNhanhList );
        spChiNhanh.setAdapter(adapterDSChiNhanh);
        DocDLChiNhanh();
        final NhanVien nhanVienTemp=nhanVien;
        
        spChiNhanh.setSelection(tenChiNhanhList.indexOf(chiNhanhMap.get(nhanVienTemp.getMaChiNhanh())));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nhanVienTemp.setHo(edtHo.getText().toString());
                nhanVienTemp.setTen(edtTen.getText().toString());
                nhanVienTemp.setChucVu(edtChucVu.getText().toString());
                nhanVienTemp.setCccd(edtCCCD.getText().toString());
                nhanVienTemp.setEmail(edtEmail.getText().toString());
                nhanVienTemp.setSoDienThoai(edtSDT.getText().toString());
                String ten = spChiNhanh.getSelectedItem().toString();
                for(Map.Entry<Integer,String> x:chiNhanhMap.entrySet()){
                    if(x.getValue().equals(ten)){
                        nhanVienTemp.setMaChiNhanh(x.getKey());
                        break;
                    }
                }
                nhanVienService.update(nhanVienTemp).enqueue(new Callback<NhanVien>() {
                    @Override
                    public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                        if(response.code() == 200) {
                            NhanVien nhanVienTemp = response.body();
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
                    public void onFailure(Call<NhanVien> call, Throwable throwable) {
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

    private void openDeleteDialog(int gravity, NhanVien nhanVien){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_nhanvien_delete);

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

        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        EditText edtMaNhanVien=dialog.findViewById(R.id.edtMaNhanVien);
        EditText edtHo=dialog.findViewById(R.id.edtHo);
        EditText edtTen= dialog.findViewById(R.id.edtTen);
        EditText edtCCCD = dialog.findViewById(R.id.edtCCCD);
        EditText edtEmail=dialog.findViewById(R.id.edtEmail);
        EditText edtSDT= dialog.findViewById(R.id.edtSDT);
        EditText edtChucVu = dialog.findViewById(R.id.edtChucVu);
        EditText edtChiNhanh = dialog.findViewById(R.id.edtChiNhanh);
        Spinner spChiNhanh=dialog.findViewById(R.id.spChiNhanh);

        edtMaNhanVien.setText(nhanVien.getMaNhanVien());
        edtHo.setText(nhanVien.getHo());
        edtTen.setText(nhanVien.getTen());
        edtChucVu.setText(nhanVien.getChucVu());
        edtCCCD.setText(nhanVien.getCccd());
        edtEmail.setText(nhanVien.getEmail());
        edtSDT.setText(nhanVien.getSoDienThoai());
        edtChiNhanh.setText(chiNhanhMap.get(nhanVien.getMaChiNhanh()));

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nhanVienService.delete(nhanVien.getMaNhanVien()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try{
                            if(response.code() == 200){
                                String result = response.body().string();
                                data.remove(nhanVien);
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

    public void DocDLChiNhanh(){
        chiNhanhService.getAll().enqueue(new Callback<List<ChiNhanh>>() {
            @Override
            public void onResponse(Call<List<ChiNhanh>> call, Response<List<ChiNhanh>> response) {
                if (response.code() == 200) {
                    chiNhanhMap.clear();
                    for (ChiNhanh x : response.body()) {
                        chiNhanhMap.put(x.getMaChiNhanh(),x.getTenChiNhanh());
                    }
                    adapterDSChiNhanh.notifyDataSetChanged();
                } else {
                    Toast.makeText(mView.getContext(), "Lỗi: " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChiNhanh>> call, Throwable throwable) {
                Log.e("ERROR_API", "Call api fail: " + throwable.getMessage());
                Toast.makeText(mView.getContext(), "Call api fail: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getImage(NhanVien nhanVien,ImageView ivAvatar){
        if(nhanVien!=null && nhanVien.getHinhAnh()!=null&&nhanVien.getHinhAnh().size()!=0){
            long idHinh = nhanVien.getHinhAnh().get(0);
            hinhAnhService.getImage(new long[]{idHinh}).enqueue(new Callback<List<HinhAnh>>() {
                @Override
                public void onResponse(Call<List<HinhAnh>> call, Response<List<HinhAnh>> response) {
                    try{
                        if(response.code() == 200){
                            List<HinhAnh> list = response.body();
                            String source = list.get(0).getSource();
                            Bitmap bitmap;
                            bitmap= ImageInteract.convertStringToBitmap(source);
                            if(bitmap == null){
                                Toast.makeText(getContext(),"Bitmap null",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            ivAvatar.setImageBitmap(bitmap);
                        }
                        else{
                            String message="Lỗi: "+String.valueOf(response.code())
                                    +"\n"+"Chi tiết: "+ response.errorBody().string();
                            Log.e("ERROR","Call api fail: "+message);
                        }
                    }
                    catch (Exception e){
                        SendMessage.sendCatch(mView.getContext(),e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<List<HinhAnh>> call, Throwable throwable) {
                    SendMessage.sendApiFail(mView.getContext(),throwable);
                }
            });
        }
    }

}
