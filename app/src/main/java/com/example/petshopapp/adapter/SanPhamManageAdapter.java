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
import com.example.petshopapp.api.apiservice.SanPhamService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.model.Giong;
import com.example.petshopapp.model.LoaiSanPham;
import com.example.petshopapp.model.SanPham;
import com.example.petshopapp.model.SanPham;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SanPhamManageAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private View mView;
    //Data
    private List<SanPham> data;
    private Map<Integer,String> chiNhanhMap;
    private List<LoaiSanPham> loaiSanPhamList = new ArrayList<>();
    private List<String> tenLoaiSanPham = new ArrayList<>();

    //Api
    private SanPhamService sanPhamService;
    public SanPhamManageAdapter(@NonNull Context context, int resource, List<SanPham> data,
                                Map<Integer,String> chiNhanhMap,
                                List<LoaiSanPham> loaiSanPhamList, List<String> tenLoaiSanPham) {
        super(context, resource,data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.chiNhanhMap = chiNhanhMap;
        this.loaiSanPhamList = loaiSanPhamList;
        this.tenLoaiSanPham = tenLoaiSanPham;
        ApiClient apiClient = ApiClient.getApiClient();
        this.sanPhamService = apiClient.getRetrofit().create(SanPhamService.class);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        mView = convertView;
        TextView tvMaSanPham = convertView.findViewById(R.id.tvMaSanPham);
        TextView tvTenSanPham=convertView.findViewById(R.id.tvTenSanPham);
        TextView tvGiaHienTai=convertView.findViewById(R.id.tvGiaHienTai);
        TextView tvSLTon = convertView.findViewById(R.id.tvSLTon);
        TextView tvTenChiNhanh = convertView.findViewById(R.id.tvTenChiNhanh);

        Button btnUpdate=convertView.findViewById(R.id.btnUpdate);
        Button btnDelete=convertView.findViewById(R.id.btnDelete);

        SanPham x = data.get(position);
        tvMaSanPham.setText(String.valueOf(x.getMaSanPham()));
        tvTenSanPham.setText(x.getTenSanPham());
        tvTenChiNhanh.setText(chiNhanhMap.get(x.getMaChiNhanh()));
        if(x.getGiaHienTai()!=null)tvGiaHienTai.setText(String.valueOf(x.getGiaHienTai()));
        else{
            tvGiaHienTai.setText("Liên hệ");
        }
        tvSLTon.setText(String.valueOf(x.getSoLuongTon()));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUpdateDialog(Gravity.CENTER, x);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteDialog(Gravity.CENTER, x);
            }
        });

        return convertView;
    }

    private String getString(BigDecimal bigDecimal){
        return bigDecimal==null? "" :bigDecimal.toString();
    }
    private BigDecimal convertBigDecimal(String text){
        if(text==null){
            SendMessage.sendCatch(mView.getContext(), "Text is null");
            return null;
        }
        try{
            if(text.isEmpty()){
                SendMessage.sendCatch(mView.getContext(), "Text is empty");
                return null;
            }
            return new BigDecimal(text);
        }
        catch (Exception e){
            SendMessage.sendCatch(mView.getContext(), e.getMessage());
        }
        return null;
    }

    private void capNhatSoLuongTon(SanPham sanPham){
        sanPhamService.updateSL(sanPham).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    if(response.code() == 200){
                        String result = response.body().string();
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

    private void openUpdateDialog(int gravity, SanPham sanPham){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sanpham_update);

        Window window = dialog.getWindow();
        if(window == null)return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        EditText edtMaSanPham = dialog.findViewById(R.id.edtMaSanPham);
        EditText edtTenSanPham = dialog.findViewById(R.id.edtTenSanPham);
        EditText edtGiaHienTai = dialog.findViewById(R.id.edtGiaHienTai);
        EditText edtSLTon = dialog.findViewById(R.id.edtSLTon);
        Spinner spLoaiSanPham = dialog.findViewById(R.id.spLoaiSanPham);
        Spinner spChiNhanh = dialog.findViewById(R.id.spChiNhanh);

        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        edtMaSanPham.setText(String.valueOf(sanPham.getMaSanPham()));
        edtTenSanPham.setText(sanPham.getTenSanPham());
        edtGiaHienTai.setText(getString(sanPham.getGiaHienTai()));
        edtSLTon.setText(String.valueOf(sanPham.getSoLuongTon()));
        List<String> tenChiNhanhList= new ArrayList<>(chiNhanhMap.values());

        ArrayAdapter adapterDSLoaiSanPham= new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenLoaiSanPham);
        spLoaiSanPham.setAdapter(adapterDSLoaiSanPham);
        adapterDSLoaiSanPham.notifyDataSetChanged();

        ArrayAdapter adapterDSChiNhanh= new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenChiNhanhList);
        spChiNhanh.setAdapter(adapterDSChiNhanh);
        spChiNhanh.setEnabled(false);//Khóa chức năng
        adapterDSChiNhanh.notifyDataSetChanged();

        spLoaiSanPham.setSelection(tenLoaiSanPham.indexOf(sanPham.getLoaiSanPham().getTenLoaiSanPham()));
        spChiNhanh.setSelection(tenChiNhanhList.indexOf(chiNhanhMap.get(sanPham.getMaChiNhanh())));

        long soLuongTon = sanPham.getSoLuongTon();
        final SanPham sanPhamTemp=sanPham;

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sanPhamTemp.setTenSanPham(edtTenSanPham.getText().toString());
                sanPhamTemp.setGiaHienTai(convertBigDecimal(edtGiaHienTai.getText().toString()));
                sanPhamTemp.setSoLuongTon(Integer.parseInt(edtSLTon.getText().toString()));
                String tenChiNhanh= spChiNhanh.getSelectedItem().toString();
                String tenLoaiSanPham = spLoaiSanPham.getSelectedItem().toString();
                for(Map.Entry<Integer,String> x: chiNhanhMap.entrySet()){
                    if(x.getValue().equals(tenChiNhanh)){
                        sanPhamTemp.setMaChiNhanh(x.getKey());
                        break;
                    }
                }
                for(LoaiSanPham x: loaiSanPhamList){
                    if(x.getTenLoaiSanPham().equals(tenLoaiSanPham)){
                        sanPhamTemp.setLoaiSanPham(x);
                        break;
                    }
                }
                sanPhamService.update(sanPhamTemp).enqueue(new Callback<SanPham>() {
                    @Override
                    public void onResponse(Call<SanPham> call, Response<SanPham> response) {
                        if(response.code() == 200) {
                            if(soLuongTon!=sanPhamTemp.getSoLuongTon()){
                                capNhatSoLuongTon(sanPhamTemp);
                            }
                            else{
                                notifyDataSetChanged();
                                Toast.makeText(mView.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
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
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SanPham> call, Throwable throwable) {
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

    private void openDeleteDialog(int gravity, SanPham sanPham){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sanpham_delete);

        Window window = dialog.getWindow();
        if(window == null)return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        EditText edtMaSanPham = dialog.findViewById(R.id.edtMaSanPham);
        EditText edtTenSanPham = dialog.findViewById(R.id.edtTenSanPham);
        EditText edtGiaHienTai = dialog.findViewById(R.id.edtGiaHienTai);
        EditText edtSLTon = dialog.findViewById(R.id.edtSLTon);
        EditText edtLoaiSanPham = dialog.findViewById(R.id.edtLoaiSanPham);
        EditText edtChiNhanh = dialog.findViewById(R.id.edtChiNhanh);

        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        edtMaSanPham.setText(String.valueOf(sanPham.getMaSanPham()));
        edtTenSanPham.setText(sanPham.getTenSanPham());
        edtGiaHienTai.setText(getString(sanPham.getGiaHienTai()));
        edtSLTon.setText(String.valueOf(sanPham.getSoLuongTon()));
        edtLoaiSanPham.setText(sanPham.getLoaiSanPham().getTenLoaiSanPham());
        edtChiNhanh.setText(chiNhanhMap.get(sanPham.getMaChiNhanh()));

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sanPhamService.delete(sanPham.getMaSanPham(),sanPham.getMaChiNhanh()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try{
                            if(response.code() == 200){
                                String result = response.body().string();
                                data.remove(sanPham);
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
