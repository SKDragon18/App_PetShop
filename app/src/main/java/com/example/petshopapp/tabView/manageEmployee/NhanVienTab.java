package com.example.petshopapp.tabView.manageEmployee;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.petshopapp.R;
import com.example.petshopapp.adapter.NhanVienManageAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.NhanVienService;
import com.example.petshopapp.model.NhanVien;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NhanVienTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NhanVienTab extends Fragment {

    private View mView;

    private Button btnThem;

    private ListView lvNhanVien;

    NhanVienService nhanVienService;

    List<NhanVien> data = new ArrayList<>();

    NhanVienManageAdapter nhanVienManageAdapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public NhanVienTab() {
        // Required empty public constructor
    }
    public static NhanVienTab newInstance(String param1, String param2) {
        NhanVienTab fragment = new NhanVienTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void openAddDialog(int gravity){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_nhanvien_add);

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

        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);
        EditText edtMaNhanVien = dialog.findViewById(R.id.edtMaNhanVien);
        EditText edtHo=dialog.findViewById(R.id.edtHo);
        EditText edtTen= dialog.findViewById(R.id.edtTen);
        EditText edtCCCD = dialog.findViewById(R.id.edtCCCD);
        EditText edtEmail=dialog.findViewById(R.id.edtEmail);
        EditText edtSDT= dialog.findViewById(R.id.edtSDT);
        Spinner spChiNhanh=dialog.findViewById(R.id.spChiNhanh);

        spChiNhanh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NhanVien nhanVien = new NhanVien();
                nhanVien.setMaNhanVien(edtMaNhanVien.getText().toString());
                nhanVien.setHo(edtHo.getText().toString());
                nhanVien.setTen(edtTen.getText().toString());
                nhanVien.setCccd(edtCCCD.getText().toString());
                nhanVien.setSoDienThoai(edtSDT.getText().toString());
                nhanVien.setEmail(edtEmail.getText().toString());
                nhanVien.setHinhAnh(null);
                nhanVien.setChiNhanh(null);
                nhanVienService.insert(nhanVien).enqueue(new Callback<NhanVien>() {
                    @Override
                    public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                        if(response.code()== 200){
                            DocDL();
                            Toast.makeText(mView.getContext(),"Thêm thành công",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(mView.getContext(),"Lỗi: "+String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<NhanVien> call, Throwable throwable) {
                        Log.e("ERROR_API","Call api fail: "+throwable.getMessage());
                        Toast.makeText(mView.getContext(),"Call api fail: "+throwable.getMessage(),Toast.LENGTH_SHORT).show();
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

    public void DocDL(){
        System.out.println("DocDLNhanVien");
        nhanVienService.getAll().enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                if (response.code() == 200) {
                    data.clear();
                    for (NhanVien x : response.body()) {
                        data.add(x);
                    }
                    nhanVienManageAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mView.getContext(), "Lỗi: " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable throwable) {
                Log.e("ERROR_API", "Call api fail: " + throwable.getMessage());
                Toast.makeText(mView.getContext(), "Call api fail: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setInit(){
        btnThem=mView.findViewById(R.id.btnThem);
        lvNhanVien=mView.findViewById(R.id.lvNhanVien);
    }

    public void setEvent(){
        nhanVienManageAdapter=new NhanVienManageAdapter(mView.getContext(),R.layout.item_nhanvien_manage,data);
        lvNhanVien.setAdapter(nhanVienManageAdapter);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDialog(Gravity.CENTER);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isVisible()){
            DocDL();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_nhan_vien_tab, container, false);

        Retrofit retrofit = ApiClient.getClient();
        nhanVienService =retrofit.create(NhanVienService.class);

        setInit();
        setEvent();
        return mView;
    }

}