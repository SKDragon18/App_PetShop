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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petshopapp.R;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.ThuCungService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.model.Giong;
import com.example.petshopapp.model.ThuCung;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThuCungManageAdapter extends ArrayAdapter {
    //View
    private Context context;
    private int resource;
    private View mView;
    //Data
    private List<ThuCung> data;
    private List<ChiNhanh> chiNhanhList = new ArrayList<>();
    private List<Giong> giongList = new ArrayList<>();
    private List<String> tenChiNhanhList = new ArrayList<>();
    private List<String> tenGiongList = new ArrayList<>();
    //Api
    private ThuCungService thuCungService;

    public      ThuCungManageAdapter(@NonNull Context context, int resource, List<ThuCung> data,
                                     List<ChiNhanh> chiNhanhList,List<String> tenChiNhanhList,
                                     List<Giong> giongList,List<String> tenGiongList) {
        super(context, resource,data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.chiNhanhList = chiNhanhList;
        this.tenChiNhanhList = tenChiNhanhList;
        this.giongList = giongList;
        this.tenGiongList = tenGiongList;
        ApiClient apiClient = ApiClient.getApiClient();
        this.thuCungService = apiClient.getRetrofit().create(ThuCungService.class);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        mView = convertView;
        TextView tvMaThuCung = convertView.findViewById(R.id.tvMaThuCung);
        TextView tvTenThuCung=convertView.findViewById(R.id.tvTenThuCung);
        TextView tvGiaHienTai=convertView.findViewById(R.id.tvGiaHienTai);
        TextView tvSLTon = convertView.findViewById(R.id.tvSLTon);
        TextView tvTenChiNhanh = convertView.findViewById(R.id.tvTenChiNhanh);
        Button btnUpdate=convertView.findViewById(R.id.btnUpdate);
        Button btnDelete=convertView.findViewById(R.id.btnDelete);

        ThuCung x = data.get(position);
        tvMaThuCung.setText(String.valueOf(x.getMaThuCung()));
        tvTenThuCung.setText(x.getTenThuCung());
        tvTenChiNhanh.setText(x.getChiNhanh().getTenChiNhanh());
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

    private void capNhatSoLuongTon(ThuCung thuCung){
        thuCungService.updateSL(thuCung).enqueue(new Callback<ResponseBody>() {
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

    private void openUpdateDialog(int gravity, ThuCung thuCung){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thucung_update);

        Window window = dialog.getWindow();
        if(window == null)return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        EditText edtMaThuCung = dialog.findViewById(R.id.edtMaThuCung);
        EditText edtTenThuCung = dialog.findViewById(R.id.edtTenThuCung);
        EditText edtChu = dialog.findViewById(R.id.edtChu);
        EditText edtMoTa = dialog.findViewById(R.id.edtMoTa);
        EditText edtGiaHienTai = dialog.findViewById(R.id.edtGiaHienTai);
        EditText edtSLTon = dialog.findViewById(R.id.edtSLTon);
        CheckBox cbTrangThai = dialog.findViewById(R.id.cbTrangThai);
        Spinner spGiong = dialog.findViewById(R.id.spGiong);
        Spinner spChiNhanh = dialog.findViewById(R.id.spChiNhanh);

        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        edtMaThuCung.setText(String.valueOf(thuCung.getMaThuCung()));
        edtTenThuCung.setText(thuCung.getTenThuCung());
        edtChu.setText(thuCung.getChu());
        edtMoTa.setText(thuCung.getMoTa());
        edtGiaHienTai.setText(getString(thuCung.getGiaHienTai()));
        edtSLTon.setText(String.valueOf(thuCung.getSoLuongTon()));
        if(thuCung.getTrangThaiBan()==1){
            cbTrangThai.setChecked(true);
        }
        else{
            cbTrangThai.setChecked(false);
        }

        ArrayAdapter adapterDSGiong= new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenGiongList);
        spGiong.setAdapter(adapterDSGiong);
        adapterDSGiong.notifyDataSetChanged();

        ArrayAdapter adapterDSChiNhanh= new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenChiNhanhList);
        spChiNhanh.setAdapter(adapterDSChiNhanh);
        adapterDSChiNhanh.notifyDataSetChanged();

        spGiong.setSelection(tenGiongList.indexOf(thuCung.getGiong().getTengiong()));
        spChiNhanh.setSelection(tenChiNhanhList.indexOf(thuCung.getChiNhanh().getTenChiNhanh()));

        int soLuongTon = thuCung.getSoLuongTon();
        final ThuCung thuCungTemp=thuCung;

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbTrangThai.isChecked()){
                    thuCungTemp.setTrangThaiBan(1);
                }
                else{
                    thuCungTemp.setTrangThaiBan(0);
                }
                thuCungTemp.setTenThuCung(edtTenThuCung.getText().toString());
                thuCungTemp.setChu(edtChu.getText().toString());
                thuCungTemp.setMoTa(edtMoTa.getText().toString());
                thuCungTemp.setGiaHienTai(convertBigDecimal(edtGiaHienTai.getText().toString()));
                thuCungTemp.setSoLuongTon(Integer.parseInt(edtSLTon.getText().toString()));
                String tenChiNhanh= spChiNhanh.getSelectedItem().toString();
                String tenGiong = spGiong.getSelectedItem().toString();
                for(Giong x: giongList){
                    if(x.getTengiong().equals(tenGiong)){
                        thuCungTemp.setGiong(x);
                        break;
                    }
                }
                for(ChiNhanh x: chiNhanhList){
                    if(x.getTenChiNhanh().equals(tenChiNhanh)){
                        thuCungTemp.setChiNhanh(x);
                        break;
                    }
                }
                thuCungService.update(thuCungTemp).enqueue(new Callback<ThuCung>() {
                    @Override
                    public void onResponse(Call<ThuCung> call, Response<ThuCung> response) {
                        if(response.code() == 200) {
                            if(soLuongTon!=thuCungTemp.getSoLuongTon()){
                                capNhatSoLuongTon(thuCungTemp);
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
                    public void onFailure(Call<ThuCung> call, Throwable throwable) {
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

    private void openDeleteDialog(int gravity, ThuCung thuCung){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thucung_delete);

        Window window = dialog.getWindow();
        if(window == null)return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        EditText edtMaThuCung = dialog.findViewById(R.id.edtMaThuCung);
        EditText edtTenThuCung = dialog.findViewById(R.id.edtTenThuCung);
        EditText edtChu = dialog.findViewById(R.id.edtChu);
        EditText edtMoTa = dialog.findViewById(R.id.edtMoTa);
        EditText edtGiaHienTai = dialog.findViewById(R.id.edtGiaHienTai);
        EditText edtSLTon = dialog.findViewById(R.id.edtSLTon);
        EditText edtGiong = dialog.findViewById(R.id.edtGiong);
        EditText edtChiNhanh = dialog.findViewById(R.id.edtChiNhanh);

        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        edtMaThuCung.setText(String.valueOf(thuCung.getMaThuCung()));
        edtTenThuCung.setText(thuCung.getTenThuCung());
        edtChu.setText(thuCung.getChu());
        edtMoTa.setText(thuCung.getMoTa());
        edtGiaHienTai.setText(getString(thuCung.getGiaHienTai()));
        edtSLTon.setText(String.valueOf(thuCung.getSoLuongTon()));
        edtGiong.setText(thuCung.getGiong().getTengiong());
        edtChiNhanh.setText(thuCung.getChiNhanh().getTenChiNhanh());

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thuCungService.delete(thuCung.getMaThuCung()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try{
                            if(response.code() == 200){
                                String result = response.body().string();
                                data.remove(thuCung);
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
