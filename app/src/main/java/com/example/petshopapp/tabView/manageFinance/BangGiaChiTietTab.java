package com.example.petshopapp.tabView.manageFinance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.petshopapp.R;
import com.example.petshopapp.adapter.BangGiaSanPhamManageAdapter;
import com.example.petshopapp.adapter.BangGiaThuCungManageAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.BangGiaService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.BangGiaSanPham;
import com.example.petshopapp.model.BangGiaThuCung;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BangGiaChiTietTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BangGiaChiTietTab extends Fragment {
    //View
    private View mView;
    private Button btnBack;
    private ListView lvCTThuCung, lvCTSanPham;

    //Api
    private BangGiaService bangGiaService;

    //Data
    private List<BangGiaThuCung> bangGiaThuCungList = new ArrayList<>();
    private List<BangGiaSanPham> bangGiaSanPhamList = new ArrayList<>();
    private long idBangGia;

    //Adapter
    private BangGiaThuCungManageAdapter bangGiaThuCungManageAdapter;
    private BangGiaSanPhamManageAdapter bangGiaSanPhamManageAdapter;

    public BangGiaChiTietTab() {
    }

    public static BangGiaChiTietTab newInstance() {
        BangGiaChiTietTab fragment = new BangGiaChiTietTab();
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
        mView= inflater.inflate(R.layout.fragment_bang_gia_chi_tiet_tab, container, false);
        Bundle bundle =getArguments();
        if(bundle!=null){
            idBangGia = bundle.getLong("idBangGia");
//            Toast.makeText(mView.getContext(),String.valueOf(idBangGia),Toast.LENGTH_SHORT).show();
        }
        Retrofit retrofit = ApiClient.getClient();
        bangGiaService=retrofit.create(BangGiaService.class);
        setInit();
        setEvent();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        DocDL();
    }

    private void setInit(){
        btnBack = mView.findViewById(R.id.btnBack);
        lvCTSanPham=mView.findViewById(R.id.lvCTSanPham);
        lvCTThuCung=mView.findViewById(R.id.lvCTThuCung);
    }
    private void setEvent(){
        DocDL();
        bangGiaSanPhamManageAdapter = new BangGiaSanPhamManageAdapter(mView.getContext(),R.layout.item_banggiasanpham_manage,bangGiaSanPhamList);
        lvCTSanPham.setAdapter(bangGiaSanPhamManageAdapter);
        bangGiaThuCungManageAdapter = new BangGiaThuCungManageAdapter(mView.getContext(),R.layout.item_banggiathucung_manage,bangGiaThuCungList);
        lvCTThuCung.setAdapter(bangGiaThuCungManageAdapter);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if(fragmentManager.getBackStackEntryCount()>0){
                    fragmentManager.popBackStack();
                }
            }
        });
    }

    private void DocDL(){
        Toast.makeText(mView.getContext(),"DocDl",Toast.LENGTH_SHORT).show();
        bangGiaService.getAllSP().enqueue(new Callback<List<BangGiaSanPham>>() {
            @Override
            public void onResponse(Call<List<BangGiaSanPham>> call, Response<List<BangGiaSanPham>> response) {
                if(response.code()==200){
                    List<BangGiaSanPham> dsTemp = response.body();
                    bangGiaSanPhamList.clear();
                    for(BangGiaSanPham x: dsTemp){
                        if(x.getMaBangGia()==idBangGia)bangGiaSanPhamList.add(x);
                    }
                    bangGiaSanPhamManageAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<List<BangGiaSanPham>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
        bangGiaService.getAllTC().enqueue(new Callback<List<BangGiaThuCung>>() {
            @Override
            public void onResponse(Call<List<BangGiaThuCung>> call, Response<List<BangGiaThuCung>> response) {
                if(response.code()==200){
                    List<BangGiaThuCung> dsTemp = response.body();
                    bangGiaThuCungList.clear();
                    for(BangGiaThuCung x:dsTemp){
                        if(x.getMaBangGia()==idBangGia)bangGiaThuCungList.add(x);
                    }
                    bangGiaThuCungManageAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<List<BangGiaThuCung>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }
}