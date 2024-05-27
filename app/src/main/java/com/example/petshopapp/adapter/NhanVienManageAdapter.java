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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petshopapp.R;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.NhanVienService;
import com.example.petshopapp.model.NhanVien;
import com.example.petshopapp.model.NhanVien;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NhanVienManageAdapter extends ArrayAdapter {
    Context context;
    int resource;
    List<NhanVien> data;
    View mView;

    TextView tvIdNhanVien,tvTenNhanVien;
    Button btnUpdate, btnDelete;
    NhanVienService nhanVienService;
    public NhanVienManageAdapter(@NonNull Context context, int resource, List<NhanVien> data) {
        super(context, resource,data);
        this.context = context;
        this.resource = resource;
        this.data = data;
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

        edtMaNhanVien.setText(nhanVien.getMaNhanVien());
        edtHo.setText(nhanVien.getHo());
        edtTen.setText(nhanVien.getTen());
        edtChucVu.setText(nhanVien.getChucVu());
        edtCCCD.setText(nhanVien.getCccd());
        edtEmail.setText(nhanVien.getEmail());
        edtSDT.setText(nhanVien.getSoDienThoai());

        final NhanVien nhanVienTemp=nhanVien;

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nhanVienTemp.setHo(edtHo.getText().toString());
                nhanVienTemp.setTen(edtTen.getText().toString());
                nhanVienTemp.setChucVu(edtChucVu.getText().toString());
                nhanVienTemp.setCccd(edtCCCD.getText().toString());
                nhanVienTemp.setEmail(edtEmail.getText().toString());
                nhanVienTemp.setSoDienThoai(edtSDT.getText().toString());
                nhanVienService.update(nhanVienTemp).enqueue(new Callback<NhanVien>() {
                    @Override
                    public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                        if(response.code() == 200) {
                            NhanVien nhanVienTemp = response.body();
                            notifyDataSetChanged();
                            Toast.makeText(mView.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(mView.getContext(),"Lỗi: "+String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<NhanVien> call, Throwable throwable) {
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
        edtChiNhanh.setText(nhanVien.getChiNhanh().getTenChiNhanh());

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
                                String message="Lỗi: "+String.valueOf(response.code())
                                        +"\n"+"Chi tiết: "+ response.errorBody().string();
                                Toast.makeText(mView.getContext(),"Tồn tại trong database",Toast.LENGTH_SHORT).show();
                                Log.e("ERROR","Call api fail:Ư "+message);
                                System.out.println(message);
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

        Retrofit retrofit = ApiClient.getClient();
        nhanVienService =retrofit.create(NhanVienService.class);
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
}
