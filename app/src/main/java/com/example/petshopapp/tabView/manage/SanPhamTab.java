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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.petshopapp.PetShopRegister;
import com.example.petshopapp.R;
import com.example.petshopapp.adapter.SanPhamManageAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.api.apiservice.LoaiSanPhamService;
import com.example.petshopapp.api.apiservice.SanPhamService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.model.LoaiSanPham;
import com.example.petshopapp.model.LoaiThuCung;
import com.example.petshopapp.model.SanPham;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SanPhamTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SanPhamTab extends Fragment {

    //View
    private View mView;

    //View
    private ListView lvSanPham;
    private Button btnThem;

    //API
    private SanPhamService sanPhamService;
    private ChiNhanhService chiNhanhService;
    private LoaiSanPhamService loaiSanPhamService;

    //Data
    private List<SanPham> data= new ArrayList<>();
    private Map<Integer,String> chiNhanhMap = new HashMap<>();
    private List<String> tenChiNhanhList;
    private List<LoaiSanPham> loaiSanPhamList = new ArrayList<>();
    private List<String> tenLoaiSanPham = new ArrayList<>();

    //Adapter
    private SanPhamManageAdapter sanPhamManageAdapter;
    private ArrayAdapter adapterDSLoaiSanPham;
    private ArrayAdapter adapterDSChiNhanh;

    public SanPhamTab() {
    }

    public static SanPhamTab newInstance(String param1, String param2) {
        SanPhamTab fragment = new SanPhamTab();
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
        ApiClient apiClient = ApiClient.getApiClient();
        sanPhamService =apiClient.getRetrofit().create(SanPhamService.class);
        chiNhanhService = apiClient.getRetrofit().create(ChiNhanhService.class);
        loaiSanPhamService = apiClient.getRetrofit().create(LoaiSanPhamService.class);
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_san_pham_tab, container, false);
        setInit();
        setEvent();
        return mView;
    }
    @Override
    public void onResume() {
        super.onResume();
        if(isVisible()){
            DocDL();
            DocDLChiNhanh();
        }
    }

    public void setInit(){
        btnThem = mView.findViewById(R.id.btnThem);
        lvSanPham = mView.findViewById(R.id.lvSanPham);
    }

    public void setEvent(){
        if(chiNhanhMap.size()==0){
            DocDLChiNhanh();
        }
        if(tenLoaiSanPham.size()==0){
            DocDLLoaiSanPham();
        }

        sanPhamManageAdapter=new SanPhamManageAdapter(mView.getContext(),R.layout.item_sanpham_manage,
                data,chiNhanhMap, loaiSanPhamList, tenLoaiSanPham);
        lvSanPham.setAdapter(sanPhamManageAdapter);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDialog(Gravity.CENTER);
            }
        });
    }

    public void DocDL(){
        System.out.println("DocDLSanPham");
        sanPhamService.getAll().enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                if (response.code() == 200) {
                    data.clear();
                    for (SanPham x : response.body()) {
                        data.add(x);
                    }
                    sanPhamManageAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<List<SanPham>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }
    public void DocDLChiNhanh(){
        System.out.println("DocDLChiNhanh");
        chiNhanhService.getAll().enqueue(new Callback<List<ChiNhanh>>() {
            @Override
            public void onResponse(Call<List<ChiNhanh>> call, Response<List<ChiNhanh>> response) {
                if (response.code() == 200) {
                    chiNhanhMap.clear();
                    for (ChiNhanh x : response.body()) {
                        chiNhanhMap.put(x.getMaChiNhanh(),x.getTenChiNhanh());
                    }
                    tenChiNhanhList = new ArrayList<>(chiNhanhMap.values());
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
    public void DocDLLoaiSanPham(){
        System.out.println("DocDLLoaiSanPham");
        loaiSanPhamService.getAll().enqueue(new Callback<List<LoaiSanPham>>() {
            @Override
            public void onResponse(Call<List<LoaiSanPham>> call, Response<List<LoaiSanPham>> response) {
                if (response.code() == 200) {
                    loaiSanPhamList.clear();
                    tenLoaiSanPham.clear();
                    for (LoaiSanPham x : response.body()) {
                        loaiSanPhamList.add(x);
                        tenLoaiSanPham.add(x.getTenLoaiSanPham());
                    }
                    if(adapterDSLoaiSanPham!=null){
                        adapterDSLoaiSanPham.notifyDataSetChanged();
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
            public void onFailure(Call<List<LoaiSanPham>> call, Throwable throwable) {
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
        dialog.setContentView(R.layout.dialog_sanpham_add);

        Window window = dialog.getWindow();
        if(window == null)return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        EditText edtTenSanPham = dialog.findViewById(R.id.edtTenSanPham);
        EditText edtGiaHienTai = dialog.findViewById(R.id.edtGiaHienTai);
        EditText edtSLTon = dialog.findViewById(R.id.edtSLTon);
        Spinner spLoaiSanPham = dialog.findViewById(R.id.spLoaiSanPham);
        Spinner spChiNhanh = dialog.findViewById(R.id.spChiNhanh);

        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);





        if(chiNhanhMap==null||chiNhanhMap.size()==0)DocDLChiNhanh();
        if(tenLoaiSanPham==null||tenLoaiSanPham.size()==0)DocDLLoaiSanPham();

        adapterDSChiNhanh= new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenChiNhanhList);
        spChiNhanh.setAdapter(adapterDSChiNhanh);


        adapterDSLoaiSanPham= new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenLoaiSanPham);
        spLoaiSanPham.setAdapter(adapterDSLoaiSanPham);




        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SanPham sanPham = new SanPham();
                String tenSanPham = edtTenSanPham.getText().toString();
                String giaHienTai = edtGiaHienTai.getText().toString();
                String soLuongTon = edtSLTon.getText().toString();
                String tenChiNhanh = spChiNhanh.getSelectedItem().toString();
                String tenLoaiSanPham = spLoaiSanPham.getSelectedItem().toString();

                if(tenSanPham.isEmpty()||giaHienTai.isEmpty()||soLuongTon.isEmpty()){
                    Toast.makeText(mView.getContext(),"Mời nhập đầy đủ thông tin",Toast.LENGTH_SHORT).show();
                    return;
                }

                sanPham.setTenSanPham(tenSanPham);
                sanPham.setGiaHienTai(convertBigDecimal(giaHienTai));
                sanPham.setSoLuongTon(Integer.parseInt(soLuongTon));
                for(Map.Entry<Integer,String> x:chiNhanhMap.entrySet()) {
                    if (x.getValue().equals(tenChiNhanh)) {
                        sanPham.setMaChiNhanh(x.getKey());
                        break;
                    }
                }
                for(LoaiSanPham x: loaiSanPhamList){
                    if(x.getTenLoaiSanPham().equals(tenLoaiSanPham)){
                        sanPham.setLoaiSanPham(x);
                        break;
                    }
                }
                System.out.println(sanPham.getMaChiNhanh());
                sanPhamService.insert(sanPham).enqueue(new Callback<SanPham>() {
                    @Override
                    public void onResponse(Call<SanPham> call, Response<SanPham> response) {
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
                    public void onFailure(Call<SanPham> call, Throwable throwable) {
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