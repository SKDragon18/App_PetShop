package com.example.petshopapp.tabView.manageFinance;

import android.graphics.Color;
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
import com.example.petshopapp.model.BangGiaSanPhamGui;
import com.example.petshopapp.model.BangGiaThuCung;
import com.example.petshopapp.model.BangGiaThuCungGui;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
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
    private Button btnSave, btnUploadTC, btnUploadSP;

    //Api
    private BangGiaService bangGiaService;

    //Data
    private List<BangGiaThuCung> bangGiaThuCungList = new ArrayList<>();
    private List<BangGiaSanPham> bangGiaSanPhamList = new ArrayList<>();
    private long idBangGia;
    private int maChiNhanh;

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
            maChiNhanh = bundle.getInt("maChiNhanh");
        }
        ApiClient apiClient = ApiClient.getApiClient();
        bangGiaService=apiClient.getRetrofit().create(BangGiaService.class);
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
        btnSave = mView.findViewById(R.id.btnSave);
        btnUploadSP = mView.findViewById(R.id.btnUploadSP);
        btnUploadTC = mView.findViewById(R.id.btnUploadTC);
        lvCTSanPham=mView.findViewById(R.id.lvCTSanPham);
        lvCTThuCung=mView.findViewById(R.id.lvCTThuCung);
    }
    private void setEvent(){
        bangGiaSanPhamManageAdapter = new BangGiaSanPhamManageAdapter(mView.getContext(),R.layout.item_banggiasanpham_manage,bangGiaSanPhamList, btnSave);
        lvCTSanPham.setAdapter(bangGiaSanPhamManageAdapter);
        bangGiaThuCungManageAdapter = new BangGiaThuCungManageAdapter(mView.getContext(),R.layout.item_banggiathucung_manage,bangGiaThuCungList, btnSave);
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
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<BangGiaThuCungGui> bangGiaThuCungGuiList = getBangGiaThuCungGui();
                List<BangGiaSanPhamGui> bangGiaSanPhamGuiList = getBangGiaSanPhamGui();
                if(bangGiaThuCungGuiList==null){
                    SendMessage.sendCatch(mView.getContext(),"Bảng giá thú cưng rỗng");
                    return;
                }
                if(bangGiaSanPhamGuiList==null){
                    SendMessage.sendCatch(mView.getContext(),"Bảng giá sản phẩm rỗng");
                    return;
                }
                updateBangGiaThuCung(bangGiaThuCungGuiList,bangGiaSanPhamGuiList);
            }
        });
        if(bangGiaThuCungList.size()==0){
            btnUploadTC.setVisibility(View.VISIBLE);
            btnUploadTC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadTC();
                }
            });
        }
        if(bangGiaSanPhamList.size()==0){
            btnUploadSP.setVisibility(View.VISIBLE);
            btnUploadSP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadSP();
                }
            });
        }
    }

    private void updateBangGiaThuCung(List<BangGiaThuCungGui> bangGiaThuCungGuiList, List<BangGiaSanPhamGui> bangGiaSanPhamGuiList){
        bangGiaService.updateTC(bangGiaThuCungGuiList).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    try{
                        String result = response.body().string();
                        bangGiaThuCungManageAdapter.notifyDataSetChanged();
                        updateBangGiaSanPham(bangGiaSanPhamGuiList);
                    }
                    catch(Exception e){
                        SendMessage.sendCatch(mView.getContext(),e.getMessage());
                    }
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
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }

    private void updateBangGiaSanPham(List<BangGiaSanPhamGui> bangGiaSanPhamGuiList){
        bangGiaService.updateSP(bangGiaSanPhamGuiList).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    try{
                        String result = response.body().string();
                        bangGiaSanPhamManageAdapter.notifyDataSetChanged();
                        Toast.makeText(mView.getContext(),result,Toast.LENGTH_SHORT).show();
                        btnSave.setBackgroundColor(Color.GREEN);
                    }
                    catch(Exception e){
                        SendMessage.sendCatch(mView.getContext(),e.getMessage());
                    }
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
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }

    private List<BangGiaSanPhamGui> getBangGiaSanPhamGui(){
        if(bangGiaSanPhamList==null || bangGiaSanPhamList.size()==0){
            return null;
        }
        List<BangGiaSanPhamGui> bangGiaSanPhamGuiList =new ArrayList<>();
        for(BangGiaSanPham x: bangGiaSanPhamList){
            BangGiaSanPhamGui bangGiaSanPhamGui = new BangGiaSanPhamGui();
            bangGiaSanPhamGui.setMaBangGia(idBangGia);
            bangGiaSanPhamGui.setMaSanPham(x.getMaSanPham());
            bangGiaSanPhamGui.setDonGia(x.getGiaKhuyenMai());
            bangGiaSanPhamGuiList.add(bangGiaSanPhamGui);
        }
        return bangGiaSanPhamGuiList;
    }

    public List<BangGiaThuCungGui> getBangGiaThuCungGui(){
        if(bangGiaThuCungList==null || bangGiaThuCungList.size()==0){
            return null;
        }
        List<BangGiaThuCungGui> bangGiaThuCungGuiList = new ArrayList<>();
        for(BangGiaThuCung x: bangGiaThuCungList){
            BangGiaThuCungGui bangGiaThuCungGui =new BangGiaThuCungGui();
            bangGiaThuCungGui.setMaBangGia(idBangGia);
            bangGiaThuCungGui.setMaThuCung(x.getMaThuCung());
            bangGiaThuCungGui.setDonGia(x.getGiaKhuyenMai());
            bangGiaThuCungGuiList.add(bangGiaThuCungGui);
        }
        return bangGiaThuCungGuiList;
    }
    private void DocDLCTSP(){
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
                    if(bangGiaSanPhamList.size()!=0){
                        if(btnUploadSP!=null&&btnUploadSP.getVisibility()==View.VISIBLE){
                            btnUploadSP.setVisibility(View.GONE);
                        }
                    }
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
    }
    private void DocDLCTTC(){
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
                    if(bangGiaThuCungList.size()!=0){
                        if(btnUploadTC!=null&&btnUploadTC.getVisibility()==View.VISIBLE){
                            btnUploadTC.setVisibility(View.GONE);
                        }
                    }
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
    private void DocDL(){
        System.out.println("DocDLBangGiaChiTiet");
        DocDLCTSP();
        DocDLCTTC();
    }
    private void uploadTC(){
        bangGiaService.uploadTC(idBangGia).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    try{
                        String result = response.body().string();
                        DocDLCTTC();
                        Toast.makeText(mView.getContext(), result,Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){
                        SendMessage.sendCatch(mView.getContext(), e.getMessage());
                    }
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
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }
    private void uploadSP(){
        bangGiaService.uploadSP(idBangGia).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    try{
                        String result = response.body().string();
                        DocDLCTSP();
                        Toast.makeText(mView.getContext(), result,Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){
                        SendMessage.sendCatch(mView.getContext(), e.getMessage());
                    }
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
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }
}