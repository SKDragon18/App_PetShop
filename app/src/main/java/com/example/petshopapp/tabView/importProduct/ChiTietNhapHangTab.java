package com.example.petshopapp.tabView.importProduct;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.petshopapp.R;
import com.example.petshopapp.adapter.ChiTietNhapSanPhamAdapter;
import com.example.petshopapp.adapter.ChiTietNhapThuCungAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.api.apiservice.NhanVienService;
import com.example.petshopapp.api.apiservice.NhapHangService;
import com.example.petshopapp.api.apiservice.SanPhamService;
import com.example.petshopapp.api.apiservice.ThuCungService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.model.ChiTietNhapSanPham;
import com.example.petshopapp.model.ChiTietNhapThuCung;
import com.example.petshopapp.model.DonNhapHang;
import com.example.petshopapp.model.LoaiSanPham;
import com.example.petshopapp.model.NhanVien;
import com.example.petshopapp.model.SanPham;
import com.example.petshopapp.model.ThuCung;
import com.example.petshopapp.tabView.importProduct.model.ChiTietSanPham;
import com.example.petshopapp.tabView.importProduct.model.ChiTietThuCung;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChiTietNhapHangTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChiTietNhapHangTab extends Fragment {


    //View
    private View mView;
    private Button btnBack, btnSave, btnAddTC, btnAddSP;
    private ListView lvCTThuCung, lvCTSanPham;

    //Api
    private NhapHangService nhapHangService;
    private NhanVienService nhanVienService;
    private ChiNhanhService chiNhanhService;
    private SanPhamService sanPhamService;
    private ThuCungService thuCungService;

    //Data
    private List<ThuCung> thuCungList = new ArrayList<>();
    private List<SanPham> sanPhamList = new ArrayList<>();
    private List<ChiTietThuCung> chiTietThuCungList = new ArrayList<>();
    private List<ChiTietSanPham> chiTietSanPhamList = new ArrayList<>();
    private List<String> tenThuCungList = new ArrayList<>();
    private List<String> tenSanPhamList = new ArrayList<>();
    private String maNhanVien;
    private int maChiNhanh = -1;
    private ChiNhanh chiNhanh;
    private long maDonNhap;

    //Adapter
    private ChiTietNhapThuCungAdapter chiTietNhapThuCungAdapter;
    private ChiTietNhapSanPhamAdapter chiTietNhapSanPhamAdapter;

    //Share
    SharedPreferences sharedPreferences;

    public ChiTietNhapHangTab() {
        // Required empty public constructor
    }

    public static ChiTietNhapHangTab newInstance(String param1, String param2) {
        ChiTietNhapHangTab fragment = new ChiTietNhapHangTab();
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
        mView= inflater.inflate(R.layout.fragment_chi_tiet_nhap_hang, container, false);
        Bundle bundle =getArguments();
        if(bundle!=null){
        }
        ApiClient apiClient = ApiClient.getApiClient();
        nhapHangService=apiClient.getRetrofit().create(NhapHangService.class);
        sanPhamService=apiClient.getRetrofit().create(SanPhamService.class);
        thuCungService=apiClient.getRetrofit().create(ThuCungService.class);
        chiNhanhService=apiClient.getRetrofit().create(ChiNhanhService.class);
        nhanVienService=apiClient.getRetrofit().create(NhanVienService.class);
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
        DocDLNhanVien();
    }

    private void setInit(){
        btnBack = mView.findViewById(R.id.btnBack);
        btnSave = mView.findViewById(R.id.btnSave);
        btnAddSP = mView.findViewById(R.id.btnAddSP);
        btnAddTC = mView.findViewById(R.id.btnAddTC);
        lvCTSanPham=mView.findViewById(R.id.lvCTSanPham);
        lvCTThuCung=mView.findViewById(R.id.lvCTThuCung);
    }
    private void setEvent(){
        chiTietNhapSanPhamAdapter = new ChiTietNhapSanPhamAdapter(mView.getContext(),R.layout.item_chitietnhapsanpham_manage,chiTietSanPhamList);
        lvCTSanPham.setAdapter(chiTietNhapSanPhamAdapter);
        chiTietNhapThuCungAdapter = new ChiTietNhapThuCungAdapter(mView.getContext(),R.layout.item_chitietnhapthucung_manage,chiTietThuCungList);
        lvCTThuCung.setAdapter(chiTietNhapThuCungAdapter);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if(fragmentManager.getBackStackEntryCount()>0){
                    fragmentManager.popBackStack();
                }
            }
        });
        btnAddSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddSPDialog(Gravity.CENTER);
            }
        });
        btnAddTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddTCDialog(Gravity.CENTER);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taoDon();
            }
        });

    }

    private String getString(BigDecimal bigDecimal){
        return bigDecimal==null? "" :bigDecimal.toString();
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

    private void openAddTCDialog(int gravity){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_nhaphang_addtc);

        Window window = dialog.getWindow();
        if(window == null)return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        EditText edtGiaNhap = dialog.findViewById(R.id.edtGiaNhap);
        EditText edtSoLuong =dialog.findViewById(R.id.edtSoLuong);
        Spinner spThuCung = dialog.findViewById(R.id.spThuCung);

        ArrayAdapter adapterDSThuCung = new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenThuCungList);
        spThuCung.setAdapter(adapterDSThuCung);

        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChiTietThuCung chiTietThuCung = new ChiTietThuCung();
                String tenThuCung = spThuCung.getSelectedItem().toString();
                for(ThuCung x: thuCungList){
                    if(tenThuCung.equals(x.getTenThuCung())){
                        chiTietThuCung.setThuCung(x);
                        break;
                    }
                }
                chiTietThuCung.setMaDonNhap(-1);
                int soLuong = Integer.parseInt(edtSoLuong.getText().toString());
                BigDecimal giaNhap = convertBigDecimal(edtGiaNhap.getText().toString());
                chiTietThuCung.setSoLuong(soLuong);
                chiTietThuCung.setGiaNhap(giaNhap);
                chiTietThuCungList.add(chiTietThuCung);
                chiTietNhapThuCungAdapter.notifyDataSetChanged();
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

    private void openAddSPDialog(int gravity){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_nhaphang_addsp);

        Window window = dialog.getWindow();
        if(window == null)return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        EditText edtDonGia = dialog.findViewById(R.id.edtDonGia);
        EditText edtSoLuong =dialog.findViewById(R.id.edtSoLuong);
        Spinner spSanPham = dialog.findViewById(R.id.spSanPham);

        ArrayAdapter adapterDSSanPham = new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenSanPhamList);
        spSanPham.setAdapter(adapterDSSanPham);

        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChiTietSanPham chiTietSanPham  = new ChiTietSanPham();
                String tenSanPham  = spSanPham.getSelectedItem().toString();
                for(SanPham x: sanPhamList){
                    if(tenSanPham.equals(x.getTenSanPham())){
                        chiTietSanPham.setSanPham(x);
                        break;
                    }
                }
                chiTietSanPham.setMaDonNhap(-1);
                int soLuong = Integer.parseInt(edtSoLuong.getText().toString());
                BigDecimal donGia = convertBigDecimal(edtDonGia.getText().toString());
                chiTietSanPham.setSoLuong(soLuong);
                chiTietSanPham.setDonGia(donGia);
                chiTietSanPhamList.add(chiTietSanPham);
                chiTietNhapSanPhamAdapter.notifyDataSetChanged();
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

    private void DocDLSP(){

        sanPhamService.getAll().enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                if (response.code() == 200) {
                    sanPhamList.clear();
                    tenSanPhamList.clear();
                    for (SanPham x : response.body()) {
                        if(x.getMaChiNhanh()==maChiNhanh){
                            sanPhamList.add(x);
                            tenSanPhamList.add(x.getTenSanPham());
                        }
                    }
                    if(chiTietNhapSanPhamAdapter!=null)chiTietNhapSanPhamAdapter.notifyDataSetChanged();
                    System.out.println("DocDLSP");
                    DocDLTC();
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
    private void DocDLTC(){
        thuCungService.getAll().enqueue(new Callback<List<ThuCung>>() {
            @Override
            public void onResponse(Call<List<ThuCung>> call, Response<List<ThuCung>> response) {
                if (response.code() == 200) {
                    thuCungList.clear();
                    tenThuCungList.clear();
                    for (ThuCung x : response.body()) {
                        if(x.getChiNhanh().getMaChiNhanh()==maChiNhanh){
                            thuCungList.add(x);
                            tenThuCungList.add(x.getTenThuCung());
                        }
                    }
                    if(chiTietNhapThuCungAdapter!=null)chiTietNhapThuCungAdapter.notifyDataSetChanged();
                    System.out.println("DocDLTC");
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
            public void onFailure(Call<List<ThuCung>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }
    private void DocDLNhanVien(){

        nhanVienService.getOneById(maNhanVien).enqueue(new Callback<NhanVien>() {
            @Override
            public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                if (response.code() == 200) {
                    NhanVien nhanVien = response.body();
                    maChiNhanh=nhanVien.getMaChiNhanh();
                    System.out.println("DocDLNV");
                    DocDLChiNhanh();

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
    private void DocDLChiNhanh(){
        chiNhanhService.getOneById(maChiNhanh).enqueue(new Callback<ChiNhanh>() {
            @Override
            public void onResponse(Call<ChiNhanh> call, Response<ChiNhanh> response) {
                if (response.code() == 200) {
                    chiNhanh = response.body();
                    System.out.println("DocDLChiNhanh");
                    DocDLSP();

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
            public void onFailure(Call<ChiNhanh> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }

    private void taoDon(){
        DonNhapHang donNhapHang = new DonNhapHang();
        donNhapHang.setChiNhanhDTO(chiNhanh);
        donNhapHang.setMaNhanVien(maNhanVien);
        nhapHangService.insert(donNhapHang).enqueue(new Callback<DonNhapHang>() {
            @Override
            public void onResponse(Call<DonNhapHang> call, Response<DonNhapHang> response) {
                if(response.code()==200){
                    DonNhapHang donNhapHang1 = response.body();
                    maDonNhap = donNhapHang1.getMaDonNhapHang();
                    Toast.makeText(mView.getContext(),"Tạo hóa đơn thành công", Toast.LENGTH_SHORT).show();
                    themChiTietSanPham();

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
            public void onFailure(Call<DonNhapHang> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }
    private void themChiTietSanPham(){
        if(chiTietSanPhamList.size()!=0){
            List<ChiTietNhapSanPham> chiTietNhapSanPhamList =new ArrayList<>();
            for(ChiTietSanPham x: chiTietSanPhamList){
                x.setMaDonNhap(maDonNhap);
                chiTietNhapSanPhamList.add(x.convertDTO());
            }
            nhapHangService.addSP(chiTietNhapSanPhamList).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try{
                        if(response.code() == 200){
                            String result = response.body().string();
                            Toast.makeText(mView.getContext(),result,Toast.LENGTH_SHORT).show();
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
                    catch (Exception e){
                        SendMessage.sendCatch(mView.getContext(),e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    SendMessage.sendApiFail(mView.getContext(),throwable);
                }
            });
        }
        themChiTietThuCung();
    }

    private void themChiTietThuCung(){
        if(chiTietThuCungList.size()!=0){
            List<ChiTietNhapThuCung> chiTietNhapThuCungList = new ArrayList<>();
            for(ChiTietThuCung x: chiTietThuCungList){
                x.setMaDonNhap(maDonNhap);
                chiTietNhapThuCungList.add(x.convertDTO());
            }
            nhapHangService.addTC(chiTietNhapThuCungList).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try{
                        if(response.code() == 200){
                            String result = response.body().string();
                            Toast.makeText(mView.getContext(),result,Toast.LENGTH_SHORT).show();
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
                    catch (Exception e){
                        SendMessage.sendCatch(mView.getContext(),e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    SendMessage.sendApiFail(mView.getContext(),throwable);
                }
            });
        }
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount()>0){
            fragmentManager.popBackStack();
        }
    }
}