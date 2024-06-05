package com.example.petshopapp.tabView.manage;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.petshopapp.PetShopRegister;
import com.example.petshopapp.R;
import com.example.petshopapp.adapter.ThuCungManageAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.api.apiservice.GiongService;
import com.example.petshopapp.api.apiservice.ThuCungService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.model.Giong;
import com.example.petshopapp.model.LoaiThuCung;
import com.example.petshopapp.model.ThuCung;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThuCungTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThuCungTab extends Fragment {

    //View
    private View mView;

    //View
    ListView lvThuCung;
    Button btnThem;

    //API
    ThuCungService thuCungService;
    ChiNhanhService chiNhanhService;
    GiongService giongService;

    //Data
    List<ThuCung> data= new ArrayList<>();
    ThuCungManageAdapter thuCungManageAdapter;

    List<ChiNhanh> chiNhanhList = new ArrayList<>();
    List<Giong> giongList = new ArrayList<>();
    List<String> tenChiNhanhList = new ArrayList<>();
    List<String> tenGiongList = new ArrayList<>();
    public ThuCungTab() {
        // Required empty public constructor
    }

    public static ThuCungTab newInstance(String param1, String param2) {
        ThuCungTab fragment = new ThuCungTab();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ApiClient apiClient = ApiClient.getApiClient();
        thuCungService =apiClient.getRetrofit().create(ThuCungService.class);
        chiNhanhService = apiClient.getRetrofit().create(ChiNhanhService.class);
        giongService = apiClient.getRetrofit().create(GiongService.class);
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_thu_cung_tab, container, false);
        setInit();
        setEvent();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isVisible()){
            DocDL();
            DocDlChiNhanh();
            DocDLGiong();
        }
    }

    private void setInit(){
        lvThuCung = mView.findViewById(R.id.lvThuCung);
        btnThem = mView.findViewById(R.id.btnThem);
    }

    private void setEvent(){
        if(tenChiNhanhList==null||tenChiNhanhList.size()==0)DocDlChiNhanh();
        if(tenGiongList==null||tenGiongList.size()==0)DocDLGiong();
        thuCungManageAdapter=new ThuCungManageAdapter(mView.getContext(),R.layout.item_thucung_manage,data,
                chiNhanhList,tenChiNhanhList,giongList,tenGiongList);
        lvThuCung.setAdapter(thuCungManageAdapter);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDialog(Gravity.CENTER);
            }
        });
    }

    private void DocDL(){
        System.out.println("DocDLThuCung");
        thuCungService.getAll().enqueue(new Callback<List<ThuCung>>() {
            @Override
            public void onResponse(Call<List<ThuCung>> call, Response<List<ThuCung>> response) {
                if (response.code() == 200) {
                    data.clear();
                    for (ThuCung x : response.body()) {
                        data.add(x);
                    }
                    thuCungManageAdapter.notifyDataSetChanged();
                } else {
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
            public void onFailure(Call<List<ThuCung>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }

    private void DocDlChiNhanh(){
        chiNhanhService.getAll().enqueue(new Callback<List<ChiNhanh>>() {
            @Override
            public void onResponse(Call<List<ChiNhanh>> call, Response<List<ChiNhanh>> response) {
                if (response.code() == 200) {
                    chiNhanhList.clear();
                    tenChiNhanhList.clear();
                    for (ChiNhanh x : response.body()) {
                        chiNhanhList.add(x);
                        tenChiNhanhList.add(x.getTenChiNhanh());
                    }
                } else {
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
            public void onFailure(Call<List<ChiNhanh>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }

    private void DocDLGiong(){
        giongService.getAll().enqueue(new Callback<List<Giong>>() {
            @Override
            public void onResponse(Call<List<Giong>> call, Response<List<Giong>> response) {
                if (response.code() == 200) {
                    giongList.clear();
                    tenGiongList.clear();
                    for (Giong x : response.body()) {
                        giongList.add(x);
                        tenGiongList.add(x.getTengiong());
                    }
                } else {
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
            public void onFailure(Call<List<Giong>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
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

    private void openAddDialog(int gravity){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thucung_add);

        Window window = dialog.getWindow();
        if(window == null)return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        EditText edtTenThuCung = dialog.findViewById(R.id.edtTenThuCung);
        EditText edtChu = dialog.findViewById(R.id.edtChu);
        EditText edtMoTa = dialog.findViewById(R.id.edtMoTa);
        EditText edtGiaHienTai = dialog.findViewById(R.id.edtGiaHienTai);
        EditText edtSLTon = dialog.findViewById(R.id.edtSLTon);
        Spinner spGiong = dialog.findViewById(R.id.spGiong);
        Spinner spChiNhanh = dialog.findViewById(R.id.spChiNhanh);

        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        if(tenGiongList==null||tenGiongList.size()==0)DocDLGiong();
        if(tenChiNhanhList==null||tenChiNhanhList.size()==0)DocDlChiNhanh();

        ArrayAdapter adapterDSGiong= new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenGiongList);
        spGiong.setAdapter(adapterDSGiong);
        adapterDSGiong.notifyDataSetChanged();
        ArrayAdapter adapterDSChiNhanh= new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenChiNhanhList);
        spChiNhanh.setAdapter(adapterDSChiNhanh);
        adapterDSChiNhanh.notifyDataSetChanged();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThuCung thuCung = new ThuCung();
                thuCung.setTenThuCung(edtTenThuCung.getText().toString());
                thuCung.setChu(edtChu.getText().toString());
                thuCung.setMoTa(edtMoTa.getText().toString());
                thuCung.setGiaHienTai(convertBigDecimal(edtGiaHienTai.getText().toString()));
                thuCung.setSoLuongTon(Integer.parseInt(edtSLTon.getText().toString()));
                String chiNhanh = spChiNhanh.getSelectedItem().toString();
                String giong = spGiong.getSelectedItem().toString();
                for(ChiNhanh x : chiNhanhList){
                    if(x.getTenChiNhanh().equals(chiNhanh)){
                        thuCung.setChiNhanh(x);
                        break;
                    }
                }
                for(Giong x: giongList){
                    if(x.getTengiong().equals(giong)){
                        thuCung.setGiong(x);
                        break;
                    }
                }


                thuCungService.insert(thuCung).enqueue(new Callback<ThuCung>() {
                    @Override
                    public void onResponse(Call<ThuCung> call, Response<ThuCung> response) {
                        if(response.code()== 200){
                            DocDL();
                            Toast.makeText(mView.getContext(),"Thêm thành công",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
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