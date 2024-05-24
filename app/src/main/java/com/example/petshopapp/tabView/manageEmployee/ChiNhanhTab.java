package com.example.petshopapp.tabView.manageEmployee;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.petshopapp.R;
import com.example.petshopapp.adapter.ChiNhanhManageAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.model.ChiNhanh;

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
 * Use the {@link ChiNhanhTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChiNhanhTab extends Fragment {

    private View mView;

    private Button btnThem;

    private ListView lvChiNhanh;

    ChiNhanhService chiNhanhService;

    List<ChiNhanh> data = new ArrayList<>();

    ChiNhanhManageAdapter chiNhanhManageAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChiNhanhTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChiNhanhTab.
     */
    // TODO: Rename and change types and number of parameters
    public static ChiNhanhTab newInstance(String param1, String param2) {
        ChiNhanhTab fragment = new ChiNhanhTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void openAddDialog(int gravity){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chinhanh_add);

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

        EditText edtTenChiNhanh = dialog.findViewById(R.id.edtTenChiNhanh);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"),edtTenChiNhanh.getText().toString());

                chiNhanhService.insert(requestBody).enqueue(new Callback<ChiNhanh>() {
                    @Override
                    public void onResponse(Call<ChiNhanh> call, Response<ChiNhanh> response) {
                        if(response.code()== 200){
                            DocDL();
                            Toast.makeText(mView.getContext(),"Thêm thành công",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(mView.getContext(),"Lỗi: "+String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ChiNhanh> call, Throwable throwable) {
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
        System.out.println("DocDLLoaiSP");
        chiNhanhService.getAll().enqueue(new Callback<List<ChiNhanh>>() {
            @Override
            public void onResponse(Call<List<ChiNhanh>> call, Response<List<ChiNhanh>> response) {
                if (response.code() == 200) {
                    data.clear();
                    for (ChiNhanh x : response.body()) {
                        data.add(x);
                    }
                    chiNhanhManageAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mView.getContext(), "Lỗi: " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChiNhanh>> call, Throwable throwable) {
                Log.e("ERROR_API", "Call api fail: " + throwable.getMessage());
                Toast.makeText(mView.getContext(), "Call api fail: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setInit(){
        btnThem=mView.findViewById(R.id.btnThem);
        lvChiNhanh=mView.findViewById(R.id.lvChiNhanh);
    }

    public void setEvent(){
        chiNhanhManageAdapter=new ChiNhanhManageAdapter(mView.getContext(),R.layout.item_chinhanh_manage,data);
        lvChiNhanh.setAdapter(chiNhanhManageAdapter);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_chi_nhanh_tab, container, false);

        Retrofit retrofit = ApiClient.getClient();
        chiNhanhService =retrofit.create(ChiNhanhService.class);

        setInit();
        setEvent();
        return mView;
    }
}