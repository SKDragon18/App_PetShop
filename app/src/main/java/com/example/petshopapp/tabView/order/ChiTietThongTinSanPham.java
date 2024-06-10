package com.example.petshopapp.tabView.order;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshopapp.R;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.GioHangService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.BangGiaSanPham;
import com.example.petshopapp.model.GioHangSanPhamGui;
import com.example.petshopapp.tools.ImageInteract;

import java.math.BigDecimal;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietThongTinSanPham extends Fragment {
    private View mView;
    private TextView tvTenSanPham, tvGia, tvTenLoai;
    private ImageView ivHinhAnh;
    private Button btnBack, btnAdd;

    private BangGiaSanPham bangGiaSanPham;
    private String maKhachHang;
    private GioHangService gioHangService;
    public ChiTietThongTinSanPham() {
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
        mView= inflater.inflate(R.layout.fragment_chi_tiet_thong_tin_san_pham, container, false);
        Bundle bundle =getArguments();
        if(bundle!=null){
            bangGiaSanPham =(BangGiaSanPham) bundle.getSerializable("bangGiaSanPham");
            maKhachHang = bundle.getString("maKhachHang");
        }
        ApiClient apiClient = ApiClient.getApiClient();
        gioHangService = apiClient.getRetrofit().create(GioHangService.class);

        setInit();
        setEvent();
        return mView;
    }

    private void setInit(){
        btnBack = mView.findViewById(R.id.btnBack);
        btnAdd = mView.findViewById(R.id.btnAdd);
        tvTenSanPham = mView.findViewById(R.id.tvTenSanPham);
        tvGia = mView.findViewById(R.id.tvGia);
        tvTenLoai = mView.findViewById(R.id.tvTenLoai);
        ivHinhAnh = mView.findViewById(R.id.ivHinhAnh);
        if(bangGiaSanPham!=null){
            tvTenSanPham.setText(bangGiaSanPham.getTenSanPham());
            tvTenLoai.setText(bangGiaSanPham.getTenLoaiSanPham());
            BigDecimal giaHT = bangGiaSanPham.getGiaHienTai();
            BigDecimal giaKM = bangGiaSanPham.getGiaKhuyenMai();
            if(giaKM!=null){
                tvGia.setText(String.valueOf(giaKM));
            }
            else if(giaHT!=null){
                tvGia.setText(String.valueOf(giaHT));
            }
            else{
                //Trường hợp sản phẩm đang có giá hoàn toàn null
                tvGia.setText("Liên hệ");
                btnAdd.setEnabled(false);
                btnAdd.setText("Không thể đặt");
            }
            String hinhAnh = bangGiaSanPham.getHinhAnh();
            if(hinhAnh!=null&&!hinhAnh.equals("")){
                Bitmap bitmap = ImageInteract.convertStringToBitmap(hinhAnh);
                ivHinhAnh.setImageBitmap(bitmap);
            }
            if(maKhachHang.contains("NV")){
                btnAdd.setEnabled(false);
                btnAdd.setText("Không thể đặt");
            }
        }
        else{
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            if(fragmentManager.getBackStackEntryCount()>0){
                fragmentManager.popBackStack();
            }
        }
    }

    private void setEvent(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themVaoGio(bangGiaSanPham.getMaSanPham(), bangGiaSanPham.getMaChiNhanh());
            }
        });
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
    private void themVaoGio(long maSanPham, int maChiNhanh){
        GioHangSanPhamGui gioHangSanPhamGui = new GioHangSanPhamGui();
        gioHangSanPhamGui.setMaKhachHang(maKhachHang);
        gioHangSanPhamGui.setMaSanPham(maSanPham);
        gioHangSanPhamGui.setMaChiNhanh(maChiNhanh);
        gioHangService.themSanPham(gioHangSanPhamGui).enqueue(new Callback<ResponseBody>() {
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
}