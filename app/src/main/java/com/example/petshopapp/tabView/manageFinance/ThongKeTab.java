package com.example.petshopapp.tabView.manageFinance;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.petshopapp.R;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.api.apiservice.DonDatHangService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.BangGia;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.model.HoaDon;
import com.example.petshopapp.tools.TimeConvert;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThongKeTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThongKeTab extends Fragment {
    //View
    private View mView;
    private Button btnThongKe;
    private PieChart chartThongKe;
    private EditText edtThang,edtNam, edtDoanhThu;

    //Data
    private List<HoaDon> data = new ArrayList<>();
    private List<Integer> chiNhanhList = new ArrayList<>();
    private Map<Integer,String> tenChiNhanhList = new HashMap<>();
    private int thang=0,nam=0;

    //Api
    private DonDatHangService donDatHangService;
    private ChiNhanhService chiNhanhService;
    public ThongKeTab() {
        // Required empty public constructor
    }
    public static ThongKeTab newInstance(String param1, String param2) {
        ThongKeTab fragment = new ThongKeTab();
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
        mView = inflater.inflate(R.layout.fragment_thong_ke_tab, container, false);
        ApiClient apiClient = ApiClient.getApiClient();
        donDatHangService = apiClient.getRetrofit().create(DonDatHangService.class);
        chiNhanhService = apiClient.getRetrofit().create(ChiNhanhService.class);
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
        btnThongKe = mView.findViewById(R.id.btnThongKe);
        chartThongKe = mView.findViewById(R.id.chartThongKe);
        edtNam = mView.findViewById(R.id.edtNam);
        edtThang = mView.findViewById(R.id.edtThang);
        edtDoanhThu = mView.findViewById(R.id.edtTongDoanhThu);
    }

    private void checkThangNam(){
        if(edtThang==null||edtNam==null){
            nam=0;
            thang=0;
            return;
        }
        String textNam = edtNam.getText().toString();
        String textThang = edtThang.getText().toString();

        if(textNam.isEmpty()){
            nam=0;
        }
        else{
            int numberNam = Integer.parseInt(textNam);
            if(numberNam<2000){
                nam =0;
                Toast.makeText(mView.getContext(),"Năm không phù hợp",Toast.LENGTH_SHORT).show();
                return;
            }
            nam = numberNam;
        }
        if(textThang.isEmpty()){
            thang=0;
        }
        else{
            int numberThang = Integer.parseInt(textThang);
            if(numberThang<1||numberThang>12){
                thang =0;
                Toast.makeText(mView.getContext(),"Tháng không phù hợp",Toast.LENGTH_SHORT).show();
                return;
            }
            thang = numberThang;
        }
    }
    private void setEvent(){
        btnThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.size()!=0){
                    chartThongKe.setVisibility(View.VISIBLE);
                    checkThangNam();
                    List<HoaDon> hoaDonList = getLocHoaDon();
                    List<PieEntry> entries = getPieEntry(hoaDonList);
                    viewChart(entries);
                }
                else{
                    chartThongKe.setVisibility(View.GONE);
                    Toast.makeText(mView.getContext(), "Dữ liệu rỗng!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private List<HoaDon> getLocHoaDon(){
        if(data.size()==0){
            return new ArrayList<>();
        }
        if(nam==0&&thang==0)return data;
        List<HoaDon> dataNew = new ArrayList<>();
        if(nam!=0&&thang==0){
            for(HoaDon x: data){
                int []date = TimeConvert.getDayMonthYearTimestamp(x.getNgayLap());
                if(nam==date[2])dataNew.add(x);
            }
        }
        else if(nam==0&&thang!=0){
            for(HoaDon x: data){
                int []date = TimeConvert.getDayMonthYearTimestamp(x.getNgayLap());
                if(thang==date[1])dataNew.add(x);
            }
        }
        else{
            for(HoaDon x: data){
                int []date = TimeConvert.getDayMonthYearTimestamp(x.getNgayLap());
                int thangHoaDon = date[1];
                int namHoaDon = date[2];
                if(thang==thangHoaDon&&nam==namHoaDon)dataNew.add(x);
            }
        }
        return dataNew;
    }
    public List<PieEntry> getPieEntry(List<HoaDon> hoaDonList) {
        Map<Integer,BigDecimal> doanhThuChiNhanh = new HashMap<>();
        for(Integer x: chiNhanhList){
            doanhThuChiNhanh.put(x,new BigDecimal(0));
        }
        BigDecimal tongDoanhThu = new BigDecimal(0);
        List<PieEntry> entries = new ArrayList<>();
        for (HoaDon x : hoaDonList) {
            if (x == null) {
                return entries;
            }
            BigDecimal tongHoaDon = x.getTongHoaDon();
            if(tongHoaDon.equals(new BigDecimal(0)))continue;
            tongDoanhThu = tongDoanhThu.add(tongHoaDon);

            int maChiNhanh = x.getDonDat().getMaChiNhanh();

            BigDecimal giaTriHienTai = doanhThuChiNhanh.get(maChiNhanh);
            BigDecimal giaTriMoi = giaTriHienTai.add(tongHoaDon);
            doanhThuChiNhanh.remove(maChiNhanh);
            doanhThuChiNhanh.put(maChiNhanh,giaTriMoi);
        }

        for(Integer x: chiNhanhList){
            System.out.println(String.valueOf(x)+"-"+doanhThuChiNhanh.get(x).toString());
            if(tenChiNhanhList.size()==0){
                entries.add(new PieEntry(doanhThuChiNhanh.get(x).floatValue(),"Mã chi nhánh: "+String.valueOf(x)));
            }
            else{
                entries.add(new PieEntry(doanhThuChiNhanh.get(x).floatValue(),tenChiNhanhList.get(x)));
            }
        }

        if(edtDoanhThu!=null)edtDoanhThu.setText(tongDoanhThu.toString()+".VNĐ");

        return entries;
    }

        public void viewChart(List<PieEntry> entries) {

        PieDataSet dataSet = new PieDataSet(entries, "Chi nhánh");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(dataSet);
        chartThongKe.setData(data);
        Description description = new Description();
        description.setText("Biểu đồ doanh thu");
        description.setTextAlign(Paint.Align.CENTER);
        chartThongKe.setDescription(description);
        chartThongKe.invalidate(); // refresh biểu đồ
    }
    private void DocDL(){
        donDatHangService.getAllHoaDon().enqueue(new Callback<List<HoaDon>>() {
            @Override
            public void onResponse(Call<List<HoaDon>> call, Response<List<HoaDon>> response) {
                if (response.code() == 200) {
                    data.clear();
                    chiNhanhList.clear();
                    for (HoaDon x : response.body()) {
                        data.add(x);
                        int maChiNhanh = x.getDonDat().getMaChiNhanh();
                        if(!chiNhanhList.contains(maChiNhanh))chiNhanhList.add(maChiNhanh);
                    }
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
                    tenChiNhanhList.clear();
                    for (ChiNhanh x : response.body()) {
                        tenChiNhanhList.put(x.getMaChiNhanh(),x.getTenChiNhanh());
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