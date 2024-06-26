package com.example.petshopapp.tabView.order;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.petshopapp.R;
import com.example.petshopapp.adapter.BangGiaManageAdapter;
import com.example.petshopapp.adapter.MuaSanPhamAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.BangGiaService;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.api.apiservice.GioHangService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.BangGiaSanPham;
import com.example.petshopapp.model.BangGiaThuCung;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.model.GioHangSanPhamGui;
import com.example.petshopapp.tabView.manageFinance.BangGiaChiTietTab;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MuaSanPhamTab extends Fragment {
    //View
    private View mView;
    private GridView gvSanPham;
    private Spinner spChiNhanh;
    private LinearLayout llND;
    private SearchView svSearch;

    //Api
    private BangGiaService bangGiaService;
    //Adapter
    private MuaSanPhamAdapter muaSanPhamAdapter;
    private ArrayAdapter adapterDSChiNhanh;

    //Data
    SharedPreferences sharedPreferences;
    private String maKhachHang;
    private List<BangGiaSanPham> data = new ArrayList<>();
    private List<BangGiaSanPham> all_data = new ArrayList<>();
    private List<String> tenChiNhanhList = new ArrayList<>();
    public MuaSanPhamTab() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_muasanpham_tab, container, false);

        ApiClient apiClient = ApiClient.getApiClient();
        bangGiaService = apiClient.getRetrofit().create(BangGiaService.class);

        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);

        //Lấy thông tin
        maKhachHang= sharedPreferences.getString("username","");

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

    private void setInit(){
        llND = mView.findViewById(R.id.llND);
        gvSanPham = mView.findViewById(R.id.gvSanPham);
        spChiNhanh = mView.findViewById(R.id.spChiNhanh);
        svSearch = mView.findViewById(R.id.svSearch);
    }
    private void setEvent(){
        muaSanPhamAdapter=new MuaSanPhamAdapter(mView.getContext(),R.layout.item_muasanpham,data);
        gvSanPham.setAdapter(muaSanPhamAdapter);
        adapterDSChiNhanh = new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenChiNhanhList);
        spChiNhanh.setAdapter(adapterDSChiNhanh);
        spChiNhanh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tenChiNhanh = spChiNhanh.getSelectedItem().toString();
                data.clear();
                for(BangGiaSanPham x: all_data){
                    if(x.getTenChiNhanh().equals(tenChiNhanh))data.add(x);
                }
                muaSanPhamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gvSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BangGiaSanPham bangGiaSanPham = data.get(position);
                openChiTietTab(bangGiaSanPham);
            }
        });
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String tenChiNhanh = spChiNhanh.getSelectedItem().toString();
                if(newText.equals("")){
                    data.clear();
                    for(BangGiaSanPham x: all_data){
                        if(x.getTenChiNhanh().equals(tenChiNhanh))data.add(x);
                    }
                }
                else{
                    data.clear();
                    for(BangGiaSanPham x :all_data){
                        if(x.getTenChiNhanh().equals(tenChiNhanh)){
                            if(x.getTenSanPham().contains(newText)){
                                data.add(x);
                            }
                        }
                    }
                }
                muaSanPhamAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void DocDL(){
        bangGiaService.getAllSP().enqueue(new Callback<List<BangGiaSanPham>>() {
            @Override
            public void onResponse(Call<List<BangGiaSanPham>> call, Response<List<BangGiaSanPham>> response) {
                if (response.code() == 200) {
                    data.clear();
                    all_data.clear();
                    tenChiNhanhList.clear();
                    for (BangGiaSanPham x : response.body()) {
                        data.add(x);
                        all_data.add(x);
                        if(!tenChiNhanhList.contains(x.getTenChiNhanh()))tenChiNhanhList.add(x.getTenChiNhanh());
                    }
                    if(muaSanPhamAdapter!=null){
                        muaSanPhamAdapter.notifyDataSetChanged();
                    }
                    if(adapterDSChiNhanh!=null){
                        adapterDSChiNhanh.notifyDataSetChanged();
                    }
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
            public void onFailure(Call<List<BangGiaSanPham>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }

    private void openChiTietTab(BangGiaSanPham bangGiaSanPham){
        llND.setVisibility(View.GONE);
        ChiTietThongTinSanPham newFragment = new ChiTietThongTinSanPham();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bangGiaSanPham",bangGiaSanPham);
        bundle.putString("maKhachHang", maKhachHang);
        newFragment.setArguments(bundle);
        FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            private int previousCount = fragmentManager.getBackStackEntryCount();
            @Override
            public void onBackStackChanged() {
                int currentCount=fragmentManager.getBackStackEntryCount();
                if(currentCount<previousCount){
                    Toast.makeText(mView.getContext(),"Trở về thành công",Toast.LENGTH_SHORT).show();
                    close();
                }
                previousCount=currentCount;
            }
        });

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .replace(R.id.flContainerMuaSanPham,newFragment)
                .addToBackStack(null)
                .commit();
    }
    private void close(){
        llND.setVisibility(View.VISIBLE);
    }

}