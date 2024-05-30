package com.example.petshopapp.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.petshopapp.R;
import com.example.petshopapp.adapter.TaiKhoanManageAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.api.apiservice.HinhAnhService;
import com.example.petshopapp.api.apiservice.TaiKhoanService;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.model.TaiKhoan;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageUserScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageUserScreen extends Fragment {

    //Đối tượng view
    private View mView;
    private Button btnThem;
    private ListView lvTaiKhoan;

    //API
    TaiKhoanService taiKhoanService;

    //Data
    List<TaiKhoan> data = new ArrayList<>();

    //Adapter
    TaiKhoanManageAdapter taiKhoanManageAdapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ManageUserScreen() {
    }

    public static ManageUserScreen newInstance(String param1, String param2) {
        ManageUserScreen fragment = new ManageUserScreen();
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

    public void DocDL(){
        System.out.println("DocDLTaiKhoan");
        taiKhoanService.getAll().enqueue(new Callback<List<TaiKhoan>>() {
            @Override
            public void onResponse(Call<List<TaiKhoan>> call, Response<List<TaiKhoan>> response) {
                if (response.code() == 200) {
                    data.clear();
                    for (TaiKhoan x : response.body()) {
                        data.add(x);
                    }
                    taiKhoanManageAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mView.getContext(), "Lỗi: " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<TaiKhoan>> call, Throwable throwable) {
                Log.e("ERROR_API", "Call api fail: " + throwable.getMessage());
                Toast.makeText(mView.getContext(), "Call api fail: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setInit(){
        btnThem=mView.findViewById(R.id.btnThem);
        lvTaiKhoan=mView.findViewById(R.id.lvTaiKhoan);
    }

    public void setEvent(){
        taiKhoanManageAdapter=new TaiKhoanManageAdapter(mView.getContext(),R.layout.item_taikhoan_manage,data);
        lvTaiKhoan.setAdapter(taiKhoanManageAdapter);
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
        mView =  inflater.inflate(R.layout.fragment_manage_user, container, false);
        Retrofit retrofit = ApiClient.getClient();
        taiKhoanService =retrofit.create(TaiKhoanService.class);
        setInit();
        setEvent();
        return mView;
    }
}