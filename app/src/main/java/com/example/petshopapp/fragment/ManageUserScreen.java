package com.example.petshopapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.petshopapp.R;
import com.example.petshopapp.adapter.TaiKhoanManageAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.api.apiservice.HinhAnhService;
import com.example.petshopapp.api.apiservice.TaiKhoanService;
import com.example.petshopapp.factory.FragmentFactoryCustom;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.model.TaiKhoan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageUserScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageUserScreen extends Fragment {

    //Đối tượng view
    private View mView;
    private Button btnThem;
    private ListView lvTaiKhoan;
    private SharedPreferences sharedPreferences;

    //API
    private TaiKhoanService taiKhoanService;

    //Data
    private List<TaiKhoan> data = new ArrayList<>();

    //Adapter
    private TaiKhoanManageAdapter taiKhoanManageAdapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public ManageUserScreen() {
    }

    public static ManageUserScreen newInstance(String param1, String param2) {
        ManageUserScreen fragment = new ManageUserScreen();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mView =  inflater.inflate(R.layout.fragment_manage_user, container, false);
        ApiClient apiClient = ApiClient.getApiClient();
        taiKhoanService =apiClient.getRetrofit().create(TaiKhoanService.class);
        setInit();
        setEvent();
        return mView;
    }
    public void setInit(){
        btnThem=mView.findViewById(R.id.btnThem);
        lvTaiKhoan=mView.findViewById(R.id.lvTaiKhoan);
    }

    public void setEvent(){
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);
        String maNVAdmin =sharedPreferences.getString("username","");
        taiKhoanManageAdapter=new TaiKhoanManageAdapter(mView.getContext(),R.layout.item_taikhoan_manage,data,maNVAdmin);
        lvTaiKhoan.setAdapter(taiKhoanManageAdapter);
        lvTaiKhoan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TaiKhoan taiKhoan = data.get(position);
                if(taiKhoan.getTenDangNhap().equals(maNVAdmin)){
                    return false;
                }
                openUpdateDialog(Gravity.CENTER,taiKhoan);
                return true;
            }
        });
    }

    public void DocDL(){
        System.out.println("DocDLTaiKhoan");
        taiKhoanService.getAll().enqueue(new Callback<List<TaiKhoan>>() {
            @Override
            public void onResponse(Call<List<TaiKhoan>> call, Response<List<TaiKhoan>> response) {
                if (response.code() == 200) {
                    data.clear();
                    for (TaiKhoan x : response.body()) {
                        data.add(x);
                    }
                    taiKhoanManageAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<List<TaiKhoan>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }


    private void openUpdateDialog(int gravity,TaiKhoan taiKhoan){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_usermanage_update);

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

        Spinner spPhanQuyen = dialog.findViewById(R.id.spPhanQuyen);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        FragmentFactoryCustom fragmentFactoryCustom = new FragmentFactoryCustom();
        List<String> roleList = fragmentFactoryCustom.getRoleList();
        List<String> phanQuyenList=  new ArrayList<>();
        for(int i = 1; i< roleList.size();i++){
            phanQuyenList.add(roleList.get(i));
        }
        ArrayAdapter adapterPhanQuyen = new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, phanQuyenList);
        spPhanQuyen.setAdapter(adapterPhanQuyen);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quyen = spPhanQuyen.getSelectedItem().toString();
                if(quyen.equals(taiKhoan.getQuyen())){
                    Toast.makeText(mView.getContext(),"Tài khoản đang thuộc quyền này",Toast.LENGTH_SHORT).show();
                    return;
                }
                taiKhoan.setQuyen(quyen);
                taiKhoanService.update(taiKhoan).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try{
                            if(response.code() == 200){
                                String result = response.body().string();
                                taiKhoanManageAdapter.notifyDataSetChanged();
                                Toast.makeText(mView.getContext(),result,Toast.LENGTH_SHORT).show();
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