package com.example.petshopapp.tabView.manage;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.petshopapp.R;
import com.example.petshopapp.adapter.LoaiSanPhamManageAdapter;
import com.example.petshopapp.adapter.LoaiThuCungManageAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.LoaiSanPhamService;
import com.example.petshopapp.api.apiservice.LoaiThuCungService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.LoaiSanPham;
import com.example.petshopapp.model.LoaiThuCung;

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
 * Use the {@link LoaiSanPhamTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoaiSanPhamTab extends Fragment {
    private View mView;

    private Button btnThem;

    private ListView lvLoaiSanPham;

    LoaiSanPhamService loaiSanPhamService;

    List<LoaiSanPham> data = new ArrayList<>();

    LoaiSanPhamManageAdapter loaiSanPhamManageAdapter;

    public LoaiSanPhamTab() {
        // Required empty public constructor
    }

    public static LoaiSanPhamTab newInstance(String param1, String param2) {
        LoaiSanPhamTab fragment = new LoaiSanPhamTab();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onResume() {
        super.onResume();
        if(isVisible()){
            DocDL();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_loai_san_pham_tab, container, false);

        ApiClient apiClient = ApiClient.getApiClient();
        loaiSanPhamService =apiClient.getRetrofit().create(LoaiSanPhamService.class);

        setInit();
        setEvent();
        return mView;
    }

    private void openAddDialog(int gravity){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loaisanpham_add);

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

        EditText edtTenLoaiSanPham = dialog.findViewById(R.id.edtTenLoaiSanPham);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"),edtTenLoaiSanPham.getText().toString());

                loaiSanPhamService.insert(requestBody).enqueue(new Callback<LoaiSanPham>() {
                    @Override
                    public void onResponse(Call<LoaiSanPham> call, Response<LoaiSanPham> response) {
                        if(response.code()== 200){
                            DocDL();
                            Toast.makeText(mView.getContext(),"Thêm thành công",Toast.LENGTH_SHORT).show();
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
                    public void onFailure(Call<LoaiSanPham> call, Throwable throwable) {
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

    public void DocDL(){
        System.out.println("DocDLLoaiSP");
        loaiSanPhamService.getAll().enqueue(new Callback<List<LoaiSanPham>>() {
            @Override
            public void onResponse(Call<List<LoaiSanPham>> call, Response<List<LoaiSanPham>> response) {
                if (response.code() == 200) {
                    data.clear();
                    for (LoaiSanPham x : response.body()) {
                        data.add(x);
                    }
                    loaiSanPhamManageAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<List<LoaiSanPham>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }


    public void setInit(){
        btnThem=mView.findViewById(R.id.btnThem);
        lvLoaiSanPham=mView.findViewById(R.id.lvLoaiSanPham);
    }

    public void setEvent(){
        loaiSanPhamManageAdapter=new LoaiSanPhamManageAdapter(mView.getContext(),R.layout.item_loaisanpham_manage,data);
        lvLoaiSanPham.setAdapter(loaiSanPhamManageAdapter);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDialog(Gravity.CENTER);
            }
        });
    }
}