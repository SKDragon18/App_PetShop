package com.example.petshopapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshopapp.R;
import com.example.petshopapp.adapter.GioHangSanPhamAdapter;
import com.example.petshopapp.adapter.GioHangThuCungAdapter;
import com.example.petshopapp.adapter.MuaThuCungAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.api.apiservice.DonDatHangService;
import com.example.petshopapp.api.apiservice.GioHangService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.BangGiaThuCung;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.model.DonDat;
import com.example.petshopapp.model.DonDatSanPhamGui;
import com.example.petshopapp.model.DonDatThuCungGui;
import com.example.petshopapp.model.GioHangGui;
import com.example.petshopapp.model.HoaDonGui;
import com.example.petshopapp.model.LoaiSanPham;
import com.example.petshopapp.model.SanPham;
import com.example.petshopapp.model.ThuCung;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PaymentButtonContainer;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartScreen extends Fragment {
    //View
    View mView;
    ListView lvCTThuCung, lvCTSanPham;
    Button btnDatHang;
    //Dialog
    Button btnCreate;
    LinearLayout llThanhToan;
    Dialog dialog;
    EditText edtSoDienThoai, edtDiaChi;
    private PaymentButtonContainer paymentButtonContainer ;
    //Api
    private Spinner spChiNhanh;
    private ChiNhanhService chiNhanhService;
    private GioHangService gioHangService;
    private DonDatHangService donDatHangService;
    //Adapter
    private GioHangThuCungAdapter gioHangThuCungAdapter;
    private GioHangSanPhamAdapter gioHangSanPhamAdapter;
    private ArrayAdapter adapterDSChiNhanh;

    //Data
    SharedPreferences sharedPreferences;
    private String maKhachHang;
    private List<ChiNhanh> chiNhanhList = new ArrayList<>();
    private List<String> tenChiNhanhList = new ArrayList<>();
    private List<ThuCung> thuCungList = new ArrayList<>();
    private List<SanPham> sanPhamList = new ArrayList<>();
    private List<DonDatSanPhamGui> donDatSanPhamGuiList = new ArrayList<>();
    private List<DonDatThuCungGui> donDatThuCungGuiList = new ArrayList<>();

    public CartScreen() {
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

        mView = inflater.inflate(R.layout.fragment_cart_screen, container, false);
        ApiClient apiClient = ApiClient.getApiClient();
        chiNhanhService = apiClient.getRetrofit().create(ChiNhanhService.class);
        gioHangService = apiClient.getRetrofit().create(GioHangService.class);
        donDatHangService= apiClient.getRetrofit().create(DonDatHangService.class);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);
        maKhachHang= sharedPreferences.getString("username","");

        setInit();
        setEvent();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        DocDLChiNhanh();
    }

    private void setInit(){
        spChiNhanh = mView.findViewById(R.id.spChiNhanh);
        lvCTSanPham = mView.findViewById(R.id.lvCTSanPham);
        lvCTThuCung = mView.findViewById(R.id.lvCTThuCung);
        btnDatHang = mView.findViewById(R.id.btnDatHang);
    }
    private void setEvent(){
        gioHangThuCungAdapter=new GioHangThuCungAdapter(mView.getContext(),R.layout.item_thucunggiohang_manage,thuCungList, donDatThuCungGuiList, maKhachHang);
        lvCTThuCung.setAdapter(gioHangThuCungAdapter);
        gioHangSanPhamAdapter=new GioHangSanPhamAdapter(mView.getContext(),R.layout.item_sanphamgiohang_manage,sanPhamList, donDatSanPhamGuiList, maKhachHang);
        lvCTSanPham.setAdapter(gioHangSanPhamAdapter);

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
                DocDL(maChiNhanh,maKhachHang);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(Gravity.CENTER);
            }
        });
    }

    private void DocDLChiNhanh(){
        System.out.println("DocDLChiNhanh");
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
                        if(sanPhamList.size()!=0){
                            String tenChiNhanh = spChiNhanh.getSelectedItem().toString();
                            int maChiNhanh=-1;
                            for(ChiNhanh x: chiNhanhList){
                                if(x.getTenChiNhanh().equals(tenChiNhanh)){
                                    maChiNhanh = x.getMaChiNhanh();
                                    break;
                                }
                            }
                            if(maChiNhanh==-1)return;
                            DocDL(maChiNhanh,maKhachHang);
                        }
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
    private void DocDL(int maChiNhanh, String maKhachHang){
        GioHangGui gioHangGui = new GioHangGui();
        gioHangGui.setMaKhachHang(maKhachHang);
        gioHangGui.setMaChiNhanh(maChiNhanh);
        gioHangService.getSanPham(gioHangGui).enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                if(response.code()==200){
                    sanPhamList.clear();
                    donDatSanPhamGuiList.clear();
                    for(SanPham x: response.body()){
                        sanPhamList.add(x);
                        DonDatSanPhamGui donDatSanPhamGui = new DonDatSanPhamGui();
                        donDatSanPhamGui.setMaSanPham(x.getMaSanPham());
                        donDatSanPhamGui.setMaChiNhanh(maChiNhanh);
                        donDatSanPhamGui.setSoLuong(1);
                        BigDecimal giaKM, giaHienThoi;
                        giaKM = x.getGiaKM();
                        giaHienThoi = x.getGiaHienTai();
                        if(giaKM!=null){
                            donDatSanPhamGui.setDonGia(giaKM);
                        }
                        else{
                            donDatSanPhamGui.setDonGia(giaHienThoi);
                        }
                        //chưa set ma don, soluong
                        donDatSanPhamGuiList.add(donDatSanPhamGui);
                    }
                    if(gioHangSanPhamAdapter!=null)gioHangSanPhamAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<List<SanPham>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
        gioHangService.getThuCung(gioHangGui).enqueue(new Callback<List<ThuCung>>() {
            @Override
            public void onResponse(Call<List<ThuCung>> call, Response<List<ThuCung>> response) {
                if(response.code()==200){
                    thuCungList.clear();
                    donDatThuCungGuiList.clear();
                    for(ThuCung x: response.body()){
                        thuCungList.add(x);
                        DonDatThuCungGui donDatThuCungGui = new DonDatThuCungGui();
                        donDatThuCungGui.setMaThuCung(x.getMaThuCung());
                        donDatThuCungGui.setSoLuong(1);
                        BigDecimal giaKM = x.getGiaKM();
                        BigDecimal giaHienThoi = x.getGiaHienTai();
                        if(giaKM!=null){
                            donDatThuCungGui.setDonGia(giaKM);
                        }
                        else{
                            donDatThuCungGui.setDonGia(giaHienThoi);
                        }
                        //chưa set madon, soluong
                        donDatThuCungGuiList.add(donDatThuCungGui);
                    }
                    if(gioHangThuCungAdapter!=null)gioHangThuCungAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<List<ThuCung>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }
    private void openDialog(int gravity){
        dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dathang);

        Window window = dialog.getWindow();
        if(window == null)return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);

        edtSoDienThoai = dialog.findViewById(R.id.edtSoDienThoai);
        edtDiaChi = dialog.findViewById(R.id.edtDiaChi);
        llThanhToan = dialog.findViewById(R.id.llThanhToan);

        btnCreate = dialog.findViewById(R.id.btnCreate);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);
        paymentButtonContainer = dialog.findViewById(R.id.payment_button_container);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String diaChi = edtDiaChi.getText().toString();
                String soDienThoai = edtSoDienThoai.getText().toString();
                taoDonDat(diaChi,soDienThoai);

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

    public String convertVNDToUSD(String soTien){
        final BigDecimal EXCHANGE_RATE = new BigDecimal("23000");
        BigDecimal moneyVND = new BigDecimal(soTien);
        BigDecimal moneyUSD = moneyVND.divide(EXCHANGE_RATE,2,BigDecimal.ROUND_HALF_UP);
        System.out.println(moneyVND);
        System.out.println(moneyUSD);
        return String.valueOf(moneyUSD);
    }
    private void requestPaypal(String gia, long maDonDat){
        paymentButtonContainer.setup(
                new CreateOrder() {
                    @Override
                    public void create(@NotNull CreateOrderActions createOrderActions) {
                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.USD)
                                                        .value(gia)
                                                        .build()
                                        )
                                        .build()
                        );
                        OrderRequest order = new OrderRequest(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }
                },
                new OnApprove() {
                    @Override
                    public void onApprove(@NotNull Approval approval) {
                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                taoHoaDon(maDonDat);//Tạo hóa đơn
                                Log.i("CaptureOrder", String.format("CaptureOrderResult: %s", result));
                                if(dialog!=null)dialog.dismiss();
                            }
                        });
                    }
                }
        );
    }
    private void taoDonDat(String diaChi, String soDienThoai){
        DonDat donDat = new DonDat();
        donDat.setMaKhachhang(maKhachHang);
        donDat.setDiaChi(diaChi);
        donDat.setSoDienThoai(soDienThoai);
        String tenChiNhanh = spChiNhanh.getSelectedItem().toString();
        for(ChiNhanh x: chiNhanhList){
            if(x.getTenChiNhanh().equals(tenChiNhanh)){
                donDat.setMaChiNhanh(x.getMaChiNhanh());
                break;
            }
        }
        donDat.setNgayLap(new Date());
        donDatHangService.insert(donDat).enqueue(new Callback<DonDat>() {
            @Override
            public void onResponse(Call<DonDat> call, Response<DonDat> response) {
                if(response.code()== 200){
                    DonDat donDat1 = response.body();
                    long maDonDat= donDat1.getSoDonDat();
                    themSP(maDonDat);
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
            public void onFailure(Call<DonDat> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }
    private void themTC(long maDonDat){
        if(donDatThuCungGuiList.size()!=0){
        for(int i = 0;i<donDatThuCungGuiList.size();i++){
            donDatThuCungGuiList.get(i).setMaDonDat(maDonDat);
        }
        donDatHangService.themTC(donDatThuCungGuiList).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    if(response.code() == 200){
                        String result = response.body().string();
                        xoaGioHang(maDonDat);

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
                catch (Exception e){
                    SendMessage.sendCatch(mView.getContext(),e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });}
        else{
            xoaGioHang(maDonDat);
        }
    }
    private void themSP(long maDonDat){
        if(donDatSanPhamGuiList.size()!=0) {
            for (int i = 0; i < donDatSanPhamGuiList.size(); i++) {
                donDatSanPhamGuiList.get(i).setMaDonDat(maDonDat);
            }
            donDatHangService.themSP(donDatSanPhamGuiList).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.code() == 200) {
                            String result = response.body().string();
                            themTC(maDonDat);
                        } else {
                            try {
                                int code = response.code();
                                String message = response.message();
                                String error = response.errorBody().string();
                                SendMessage.sendMessageFail(mView.getContext(), code, error, message);
                            } catch (Exception e) {
                                SendMessage.sendCatch(mView.getContext(), e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        SendMessage.sendCatch(mView.getContext(), e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    SendMessage.sendApiFail(mView.getContext(), throwable);
                }
            });
        }
        else{
            themTC(maDonDat);
        }
    }
    private void getThanhTien(long maDonDat){
        Toast.makeText(mView.getContext(),"Tạo đơn hàng thành công. Xác nhận thanh toán?",Toast.LENGTH_SHORT).show();
        donDatHangService.thanhTien(maDonDat).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    if(response.code() == 200){
                        String result = response.body().string();//Thành tiền
                        try{
                            String money = convertVNDToUSD(result);
                            System.out.println("Money: "+money);
                            requestPaypal(money, maDonDat);
                        }catch(Exception e){
                            SendMessage.sendCatch(mView.getContext(),e.getMessage());
                        }

                        llThanhToan.setVisibility(View.VISIBLE);

                        btnCreate.setEnabled(false);
                        edtDiaChi.setEnabled(false);
                        edtSoDienThoai.setEnabled(false);
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

    private void taoHoaDon(long maDonDat){
        HoaDonGui hoaDonGui = new HoaDonGui();
        hoaDonGui.setSoHoaDon(maDonDat);
        hoaDonGui.setMaNhanVien(null);
        donDatHangService.taoHoaDon(hoaDonGui).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    if(response.code() == 200){
                        String result = response.body().string();
                        Toast.makeText(mView.getContext(),"Thanh toán thành công",Toast.LENGTH_SHORT).show();
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

    private void xoaGioHang(long maDonDat){
        GioHangGui gioHangGui = new GioHangGui();
        gioHangGui.setMaKhachHang(maKhachHang);
        String tenChiNhanh = spChiNhanh.getSelectedItem().toString();
        for(ChiNhanh x: chiNhanhList){
            if(x.getTenChiNhanh().equals(tenChiNhanh)){
                gioHangGui.setMaChiNhanh(x.getMaChiNhanh());
                break;
            }
        }
        gioHangService.xoaGioHang(gioHangGui).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    if(response.code() == 200){
                        String result = response.body().string();
                        onResume();
                        getThanhTien(maDonDat);
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