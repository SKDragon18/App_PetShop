package com.example.petshopapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.telephony.PhoneNumberFormattingTextWatcher;
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
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.BangGiaThuCung;
import com.example.petshopapp.model.BangGiaThuCung;
import com.example.petshopapp.tools.TimeConvert;
import com.example.petshopapp.widget.CalendarDialog;

import java.math.BigDecimal;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BangGiaThuCungManageAdapter extends ArrayAdapter {
    Context context;
    int resource;

    View mView;

    TextView tvMaThuCung, tvTenThuCung, tvTenGiong, tvGiaHienTai;
    EditText edtGiaKM;
    List<BangGiaThuCung> data;
    public BangGiaThuCungManageAdapter(@NonNull Context context, int resource, List<BangGiaThuCung> data) {
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
        BangGiaThuCung bangGiaThuCung = data.get(position);

        tvMaThuCung = mView.findViewById(R.id.tvMaThuCung);
        tvTenThuCung = mView.findViewById(R.id.tvTenThuCung);
        tvTenGiong = mView.findViewById(R.id.tvTenGiong);
        tvGiaHienTai = mView.findViewById(R.id.tvGiaHienTai);

        edtGiaKM = mView.findViewById(R.id.edtGiaKhuyenMai);

        tvMaThuCung.setText(String.valueOf(bangGiaThuCung.getMaThuCung()));
        tvTenThuCung.setText(bangGiaThuCung.getTenThuCung());
        tvTenGiong.setText(bangGiaThuCung.getTenGiong());
        tvGiaHienTai.setText(getString(bangGiaThuCung.getGiaHienTai()));

        edtGiaKM.setText(getString(bangGiaThuCung.getGiaKhuyenMai()));
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
//        edtGiaKM.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(!hasFocus){
//                    Toast.makeText(mView.getContext(),"ss",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        return convertView;
    }
}
