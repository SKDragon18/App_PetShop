package com.example.petshopapp.tabView.manageFinance;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.Toast;

import com.example.petshopapp.PetShopMain;
import com.example.petshopapp.PetShopRegister;
import com.example.petshopapp.R;
import com.example.petshopapp.adapter.BangGiaManageAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.BangGiaService;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.fragment.CartScreen;
import com.example.petshopapp.fragment.HomeScreen;
import com.example.petshopapp.fragment.UserScreen2;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.BangGia;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.widget.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BangGiaTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BangGiaTab extends Fragment {

    private Button btnThem;
    private View mView;
    private ListView lvBangGia;
    private LinearLayout llND;

    private BangGiaService bangGiaService;
    private ChiNhanhService chiNhanhService;
    //Data
    private List<ChiNhanh> chiNhanhList= new ArrayList<>();
    private List<String> tenChiNhanhList=new ArrayList<>();
    private List<BangGia> data = new ArrayList<>();

    //Adapter
    private BangGiaManageAdapter bangGiaManageAdapter;
    private ArrayAdapter adapterDSChiNhanh = null;



    public BangGiaTab() {
        // Required empty public constructor
    }
    public static BangGiaTab newInstance() {
        BangGiaTab fragment = new BangGiaTab();
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
        mView = inflater.inflate(R.layout.fragment_bang_gia_tab, container, false);
        ApiClient apiClient = ApiClient.getApiClient();
        bangGiaService =apiClient.getRetrofit().create(BangGiaService.class);
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
            DocDLChiNhanh();
        }
    }

    private void setInit(){
        btnThem = mView.findViewById(R.id.btnThem);
        lvBangGia=mView.findViewById(R.id.lvBangGia);
        llND = mView.findViewById(R.id.llND);
    }
    private void setEvent(){
        bangGiaManageAdapter=new BangGiaManageAdapter(mView.getContext(),R.layout.item_banggia_manage,data);
        lvBangGia.setAdapter(bangGiaManageAdapter);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDialog(Gravity.CENTER);
            }
        });
        lvBangGia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openChiTietTab(data.get(position).getMaBangGia());
            }
        });
    }

    private void openChiTietTab(long idBangGia){
        llND.setVisibility(View.GONE);
        BangGiaChiTietTab newFragment = new BangGiaChiTietTab();
        Bundle bundle = new Bundle();
        bundle.putLong("idBangGia",idBangGia);
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
                .replace(R.id.flContainer,newFragment)
                .addToBackStack(null)
                .commit();


    }

    private void close(){
        llND.setVisibility(View.VISIBLE);
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
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ChiNhanh>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }

    private void DocDL(){
        System.out.println("DocDLBangGia");
        bangGiaService.getAll().enqueue(new Callback<List<BangGia>>() {
            @Override
            public void onResponse(Call<List<BangGia>> call, Response<List<BangGia>> response) {
                if (response.code() == 200) {
                    data.clear();
                    for (BangGia x : response.body()) {
                        data.add(x);
                    }
                    bangGiaManageAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<List<BangGia>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }

    private void openAddDialog(int gravity){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_banggia_add);

        Window window = dialog.getWindow();
        if(window == null)return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.BOTTOM == gravity){
            dialog.setCancelable(false);
        }
        else{
            dialog.setCancelable(true);
        }
        BangGia bangGia = new BangGia();

        EditText edtNoiDung = dialog.findViewById(R.id.edtNoiDung);
        EditText edtTGBatDau = dialog.findViewById(R.id.edtTGBatDau);
        EditText edtTGKetThuc = dialog.findViewById(R.id.edtTGKetThuc);
        Spinner spChiNhanh = dialog.findViewById(R.id.spChiNhanh);

        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        Button btnCalendarBD = dialog.findViewById(R.id.btnCalendarBD);
        Button btnCalendarKT = dialog.findViewById(R.id.btnCalendarKT);
        CalendarDialog calendarDialogBD = new CalendarDialog();
        CalendarDialog calendarDialogKT = new CalendarDialog();

        adapterDSChiNhanh = new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenChiNhanhList);
        spChiNhanh.setAdapter(adapterDSChiNhanh);
        DocDLChiNhanh();

        spChiNhanh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ten = spChiNhanh.getSelectedItem().toString();
                for(ChiNhanh x: chiNhanhList){
                    if(x.getTenChiNhanh().equals(ten)){
                        bangGia.setChiNhanh(x);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnCalendarBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarDialogBD.open(mView.getContext(),edtTGBatDau);
            }
        });

        btnCalendarKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarDialogKT.open(mView.getContext(), edtTGKetThuc);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bangGia.setNoiDung(edtNoiDung.getText().toString());

                bangGia.setTrangThai(true);
                bangGia.setThoiGianBatDau(calendarDialogBD.getTimestamp());
                bangGia.setThoiGianKetThuc(calendarDialogKT.getTimestamp());
                bangGiaService.insert(bangGia).enqueue(new Callback<BangGia>() {
                    @Override
                    public void onResponse(Call<BangGia> call, Response<BangGia> response) {
                        if(response.code()== 200){
                            DocDL();
                            Toast.makeText(mView.getContext(),"Thêm thành công",Toast.LENGTH_SHORT).show();
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

                    @Override
                    public void onFailure(Call<BangGia> call, Throwable throwable) {
                        SendMessage.sendApiFail(mView.getContext(),throwable);
                    }
                });

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


}