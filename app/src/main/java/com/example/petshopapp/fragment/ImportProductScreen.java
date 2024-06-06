package com.example.petshopapp.fragment;

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
import com.example.petshopapp.adapter.NhapHangAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.NhapHangService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.DonNhapHang;
import com.example.petshopapp.tabView.importProduct.ChiTietNhapHangTab;
import com.example.petshopapp.tabView.manageFinance.BangGiaChiTietTab;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImportProductScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImportProductScreen extends Fragment {

    private View mView;

    private Button btnThem;

    private ListView lvDonNhapHang;
    private LinearLayout llND;

    NhapHangService nhapHangService;

    List<DonNhapHang> data = new ArrayList<>();
    NhapHangAdapter nhapHangAdapter;
    public ImportProductScreen() {
        // Required empty public constructor
    }
    
    public static ImportProductScreen newInstance(String param1, String param2) {
        ImportProductScreen fragment = new ImportProductScreen();
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
        mView =  inflater.inflate(R.layout.fragment_import_product_screen, container, false);

        ApiClient apiClient = ApiClient.getApiClient();
        nhapHangService =apiClient.getRetrofit().create(NhapHangService.class);

        setInit();
        setEvent();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isVisible()){
            DocDL();
        }
    }
    
    private void DocDL(){
        System.out.println("DocDLNhapHang");
        nhapHangService.getAll().enqueue(new Callback<List<DonNhapHang>>() {
            @Override
            public void onResponse(Call<List<DonNhapHang>> call, Response<List<DonNhapHang>> response) {
                if (response.code() == 200) {
                    data.clear();
                    for (DonNhapHang x : response.body()) {
                        data.add(x);
                    }
                    nhapHangAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<List<DonNhapHang>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }

    public void setInit(){
        btnThem=mView.findViewById(R.id.btnThem);
        lvDonNhapHang=mView.findViewById(R.id.lvDonNhapHang);
        llND = mView.findViewById(R.id.llND);
    }

    public void setEvent(){
        nhapHangAdapter=new NhapHangAdapter(mView.getContext(),R.layout.item_donnhaphang_manage,data);
        lvDonNhapHang.setAdapter(nhapHangAdapter);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llND.setVisibility(View.GONE);
                ChiTietNhapHangTab newFragment = new ChiTietNhapHangTab();
                Bundle bundle = new Bundle();
                newFragment.setArguments(bundle);
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    private int previousCount = fragmentManager.getBackStackEntryCount();
                    @Override
                    public void onBackStackChanged() {
                        int currentCount=fragmentManager.getBackStackEntryCount();
                        if(currentCount<previousCount){
                            Toast.makeText(mView.getContext(),"Trở về thành công",Toast.LENGTH_SHORT).show();
                            DocDL();
                            close();
                        }
                        previousCount=currentCount;
                    }
                });
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction
                        .replace(R.id.flContainerImport,newFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }
    private void close(){
        llND.setVisibility(View.VISIBLE);
    }
}