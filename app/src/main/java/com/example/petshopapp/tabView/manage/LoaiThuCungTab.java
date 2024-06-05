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
import com.example.petshopapp.adapter.LoaiThuCungManageAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.LoaiThuCungService;
import com.example.petshopapp.message.SendMessage;
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
 * Use the {@link LoaiThuCungTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoaiThuCungTab extends Fragment {

    private View mView;

    private Button btnThem;

    private ListView lvLoaiThuCung;

    LoaiThuCungService loaiThuCungService;

    List<LoaiThuCung> data = new ArrayList<>();

    LoaiThuCungManageAdapter loaiThuCungManageAdapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public LoaiThuCungTab() {
        // Required empty public constructor
    }
    public static LoaiThuCungTab newInstance(String param1, String param2) {
        LoaiThuCungTab fragment = new LoaiThuCungTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void openAddDialog(int gravity){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loaithucung_add);

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

        EditText edtTenLoaiThucCung = dialog.findViewById(R.id.edtTenLoaiThuCung);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"),edtTenLoaiThucCung.getText().toString());

                loaiThuCungService.insert(requestBody).enqueue(new Callback<LoaiThuCung>() {
                    @Override
                    public void onResponse(Call<LoaiThuCung> call, Response<LoaiThuCung> response) {
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
                                return;
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<LoaiThuCung> call, Throwable throwable) {
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
        System.out.println("DocDLLoaiThuCung");
        loaiThuCungService.getAll().enqueue(new Callback<List<LoaiThuCung>>() {
            @Override
            public void onResponse(Call<List<LoaiThuCung>> call, Response<List<LoaiThuCung>> response) {
                if (response.code() == 200) {
                    data.clear();
                    for (LoaiThuCung x : response.body()) {
                        data.add(x);
                    }
                    loaiThuCungManageAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<List<LoaiThuCung>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }

    public void setInit(){
        btnThem=mView.findViewById(R.id.btnThem);
        lvLoaiThuCung=mView.findViewById(R.id.lvLoaiThuCung);
    }

    public void setEvent(){
        loaiThuCungManageAdapter=new LoaiThuCungManageAdapter(mView.getContext(),R.layout.item_loaithucung_manage,data);
        lvLoaiThuCung.setAdapter(loaiThuCungManageAdapter);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDialog(Gravity.CENTER);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isVisible()){
            DocDL();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_loai_thu_cung_tab, container, false);

        ApiClient apiClient = ApiClient.getApiClient();
        loaiThuCungService =apiClient.getRetrofit().create(LoaiThuCungService.class);

        setInit();
        setEvent();
        return mView;
    }

}