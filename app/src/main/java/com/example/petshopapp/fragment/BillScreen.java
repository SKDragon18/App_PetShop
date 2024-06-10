package com.example.petshopapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.petshopapp.R;
import com.example.petshopapp.adapter.HoaDonViewAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.api.apiservice.DonDatHangService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.model.HoaDon;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BillScreen extends Fragment {

    private View mView;
    private ListView lvHoaDon;
    private Spinner spChiNhanh;
    private SharedPreferences sharedPreferences;

    //API
    private DonDatHangService donDatHangService;
    private ChiNhanhService chiNhanhService;

    //Data
    private List<HoaDon> data = new ArrayList<>();
    private List<ChiNhanh> chiNhanhList = new ArrayList<>();
    private List<String> tenChiNhanhList = new ArrayList<>();
    String maKhachHang;

    //Adapter
    private HoaDonViewAdapter hoaDonViewAdapter;
    private ArrayAdapter adapterDSChiNhanh;

    public BillScreen() {
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

        mView = inflater.inflate(R.layout.fragment_bill_screen, container, false);
        ApiClient apiClient = ApiClient.getApiClient();
        donDatHangService =apiClient.getRetrofit().create(DonDatHangService.class);
        chiNhanhService = apiClient.getRetrofit().create(ChiNhanhService.class);

        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);
        maKhachHang =sharedPreferences.getString("username","");
        setInit();
        setEvent();
        
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isVisible()){
            DocDLChiNhanh();
        }
    }

    private void setInit(){
        spChiNhanh = mView.findViewById(R.id.spChiNhanh);
        lvHoaDon = mView.findViewById(R.id.lvHoaDon);
    }

    private void setEvent(){
        hoaDonViewAdapter=new HoaDonViewAdapter(mView.getContext(),R.layout.item_hoadon_view,data);
        lvHoaDon.setAdapter(hoaDonViewAdapter);

        adapterDSChiNhanh = new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenChiNhanhList);
        spChiNhanh.setAdapter(adapterDSChiNhanh);
        spChiNhanh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tenChiNhanh = spChiNhanh.getSelectedItem().toString();
                int maChiNhanh=-1;
                for(ChiNhanh x: chiNhanhList){
                    if(x.getTenChiNhanh().equals(tenChiNhanh)){
                        maChiNhanh = x.getMaChiNhanh();
                        break;
                    }
                }
                if(maChiNhanh==-1)return;
                DocDL(maChiNhanh);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void DocDL(int maChiNhanh){
        System.out.println("DocDLDonDatHang");
        donDatHangService.getAllHoaDon().enqueue(new Callback<List<HoaDon>>() {
            @Override
            public void onResponse(Call<List<HoaDon>> call, Response<List<HoaDon>> response) {
                if (response.code() == 200) {
                    data.clear();
                    for (HoaDon x : response.body()) {
                        if(x.getDonDat()!=null&&x.getDonDat().getMaKhachhang().equals(maKhachHang)){
                            if(x.getDonDat().getMaChiNhanh()==maChiNhanh){
                                data.add(x);
                            }
                        }
                    }
                    hoaDonViewAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<List<HoaDon>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }
    private void DocDLChiNhanh(){
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
            public void onFailure(Call<List<ChiNhanh>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }
}