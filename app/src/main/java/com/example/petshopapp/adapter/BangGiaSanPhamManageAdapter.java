package com.example.petshopapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petshopapp.R;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.BangGiaService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.BangGiaSanPham;
import com.example.petshopapp.tools.TimeConvert;
import com.example.petshopapp.widget.CalendarDialog;

import java.math.BigDecimal;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BangGiaSanPhamManageAdapter extends ArrayAdapter {
    Context context;
    int resource;

    View mView;

    TextView tvMaSanPham, tvTenSanPham, tvTenLoaiSanPham, tvGiaHienTai;
    EditText edtGiaKM;
    List<BangGiaSanPham> data;
    private BangGiaService bangGiaSanPhamService;
    public BangGiaSanPhamManageAdapter(@NonNull Context context, int resource, List<BangGiaSanPham> data) {
        super(context, resource,data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }
    private String getString(BigDecimal bigDecimal){
        return bigDecimal==null? "" :bigDecimal.toString();
    }
    private BigDecimal convertBigDecimal(String text){
        if(text==null)return null;
        try{
            return new BigDecimal(text);
        }
        catch (Exception e){
            SendMessage.sendCatch(mView.getContext(), e.getMessage());
        }
        return null;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        this.mView=convertView;
        BangGiaSanPham bangGiaSanPham = data.get(position);

        tvMaSanPham = mView.findViewById(R.id.tvMaSanPham);
        tvTenSanPham = mView.findViewById(R.id.tvTenSanPham);
        tvTenLoaiSanPham = mView.findViewById(R.id.tvTenLoaiSanPham);
        tvGiaHienTai = mView.findViewById(R.id.tvGiaHienTai);

        edtGiaKM = mView.findViewById(R.id.edtGiaKhuyenMai);

        tvMaSanPham.setText(String.valueOf(bangGiaSanPham.getMaSanPham()));
        tvTenSanPham.setText(bangGiaSanPham.getTenSanPham());
        tvTenLoaiSanPham.setText(bangGiaSanPham.getTenLoaiSanPham());

        tvGiaHienTai.setText(getString(bangGiaSanPham.getGiaHienTai()));

        edtGiaKM.setText(getString(bangGiaSanPham.getGiaKhuyenMai()));
        edtGiaKM.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if((actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_GO ||
                        actionId == EditorInfo.IME_ACTION_NEXT ||
                        actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_SEND ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN))){
                    if(edtGiaKM.getText()==null || edtGiaKM.getText().toString().isEmpty()){
                        edtGiaKM.setError("Mời nhập số");
                    }
                    data.get(position).setGiaKhuyenMai(convertBigDecimal(edtGiaKM.getText().toString()));
                    return true;
                }
                return false;
            }
        });

        return convertView;
    }

    
}
