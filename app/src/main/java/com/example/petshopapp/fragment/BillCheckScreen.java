package com.example.petshopapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.petshopapp.R;
import com.example.petshopapp.adapter.DonDatHangManageAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.api.apiservice.DonDatHangService;
import com.example.petshopapp.api.apiservice.NhanVienService;
import com.example.petshopapp.api.apiservice.NhapHangService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.DonDat;
import com.example.petshopapp.model.DonNhapHang;
import com.example.petshopapp.model.NhanVien;
import com.example.petshopapp.tabView.importProduct.ChiTietNhapHangTab;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillCheckScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillCheckScreen extends Fragment {
    private View mView;

    private ListView lvDonDatHang;

    List<DonDat> data = new ArrayList<>();
    DonDatHangManageAdapter donDatHangManageAdapter;

    //Api
    NhanVienService nhanVienService;
    DonDatHangService donDatHangService;
    //Share
    SharedPreferences sharedPreferences;
    String maNhanVien;
    int maChiNhanh=-1;
    public BillCheckScreen() {

    }


    public static BillCheckScreen newInstance(String param1, String param2) {
        BillCheckScreen fragment = new BillCheckScreen();
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
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_bill_check_screen, container, false);

        ApiClient apiClient = ApiClient.getApiClient();
        donDatHangService =apiClient.getRetrofit().create(DonDatHangService.class);
        nhanVienService = apiClient.getRetrofit().create(NhanVienService.class);

        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);
        //Lấy thông tin
        maNhanVien= sharedPreferences.getString("username","");
        setInit();
        setEvent();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isVisible()){
            DocDLNhanVien();
        }
    }

    public void setInit(){
        lvDonDatHang=mView.findViewById(R.id.lvDonDatHang);
    }

    public void setEvent(){
        donDatHangManageAdapter=new DonDatHangManageAdapter(mView.getContext(),R.layout.item_dondat_manage,data, maNhanVien);
        lvDonDatHang.setAdapter(donDatHangManageAdapter);
    }

    private void DocDL(){
        if(maChiNhanh==-1)return;
        System.out.println("DocDLDonDatHang");
        donDatHangService.getAllDonDat().enqueue(new Callback<List<DonDat>>() {
            @Override
            public void onResponse(Call<List<DonDat>> call, Response<List<DonDat>> response) {
                if(response.code()==200){
                    data.clear();
                    for(DonDat x: response.body()){
                        if(x.getMaChiNhanh()==maChiNhanh)data.add(x);
                    }
                    donDatHangManageAdapter.notifyDataSetChanged();
                }
                else {
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
            public void onFailure(Call<List<DonDat>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }

    private void DocDLNhanVien(){
        if(maNhanVien == null)return;
        nhanVienService.getOneById(maNhanVien).enqueue(new Callback<NhanVien>() {
            @Override
            public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                if (response.code() == 200) {
                    NhanVien nhanVien = response.body();
                    maChiNhanh=nhanVien.getMaChiNhanh();
                    DocDL();
                } else {
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
            public void onFailure(Call<NhanVien> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }
}