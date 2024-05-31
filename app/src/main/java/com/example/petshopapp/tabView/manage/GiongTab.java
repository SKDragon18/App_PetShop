package com.example.petshopapp.tabView.manage;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.petshopapp.adapter.GiongManageAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.GiongService;
import com.example.petshopapp.api.apiservice.LoaiThuCungService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.Giong;
import com.example.petshopapp.model.LoaiThuCung;

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
 * Use the {@link GiongTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GiongTab extends Fragment {


    private View mView;

    private Button btnThem;

    private ListView lvGiong;

    GiongService giongService;
    LoaiThuCungService loaiThuCungService;

    List<Giong> data = new ArrayList<>();

    List<LoaiThuCung> loaiThuCungList= new ArrayList<>();

    List<String> tenLoaiThuCungList=new ArrayList<>();

    GiongManageAdapter giongManageAdapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GiongTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GiongTab.
     */
    // TODO: Rename and change types and number of parameters
    public static GiongTab newInstance(String param1, String param2) {
        GiongTab fragment = new GiongTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    protected void DocDLLoaiThuCung(ArrayAdapter adapterDSLoaiThuCung){
        System.out.println("DocDLLoaiThuCungCombobox");
        loaiThuCungService.getAll().enqueue(new Callback<List<LoaiThuCung>>() {
            @Override
            public void onResponse(Call<List<LoaiThuCung>> call, Response<List<LoaiThuCung>> response) {
                if(response.code()==200){
                    loaiThuCungList.clear();
                    tenLoaiThuCungList.clear();
                    for(LoaiThuCung x: response.body()){
                        loaiThuCungList.add(x);
                        tenLoaiThuCungList.add(x.getTenLoaiThuCung());
                    }
                    adapterDSLoaiThuCung.notifyDataSetChanged();
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
            public void onFailure(Call<List<LoaiThuCung>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }

    private void openAddDialog(int gravity){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_giong_add);

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

        //Init
        Giong giong = new Giong();

        EditText edtTenGiong=dialog.findViewById(R.id.edtTenGiong);
        Spinner spLoaiThuCung=dialog.findViewById(R.id.spLoaiThuCung);

        ArrayAdapter adapterDSLoaiThuCung = new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenLoaiThuCungList);
        spLoaiThuCung.setAdapter(adapterDSLoaiThuCung);

        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        DocDLLoaiThuCung(adapterDSLoaiThuCung);

        //Event

        spLoaiThuCung.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tenLoaiThuCung = spLoaiThuCung.getSelectedItem().toString();
                for(LoaiThuCung x: loaiThuCungList){
                    if(x.getTenLoaiThuCung().equals(tenLoaiThuCung)){
                        giong.setLoaiThuCung(x);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    giong.setTengiong(edtTenGiong.getText().toString());
                    giongService.insert(giong).enqueue(new Callback<Giong>() {
                        @Override
                        public void onResponse(Call<Giong> call, Response<Giong> response) {
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
                        public void onFailure(Call<Giong> call, Throwable throwable) {
                            SendMessage.sendApiFail(mView.getContext(),throwable);
                        }
                    });

                    dialog.dismiss();
                }
                catch (Exception e){
                    SendMessage.sendCatch(mView.getContext(),e.getMessage());
                }

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
        System.out.println("DocDLGiong");
        giongService.getAll().enqueue(new Callback<List<Giong>>() {
            @Override
            public void onResponse(Call<List<Giong>> call, Response<List<Giong>> response) {
                if(response.code()==200){
                    data.clear();
                    for(Giong x: response.body()){
                        data.add(x);
                    }
                    giongManageAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<List<Giong>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }

    public void setInit(){
        btnThem=mView.findViewById(R.id.btnThem);
        lvGiong=mView.findViewById(R.id.lvGiong);
    }

    public void setEvent(){


        giongManageAdapter=new GiongManageAdapter(mView.getContext(),R.layout.item_giong_manage,data);
        lvGiong.setAdapter(giongManageAdapter);
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
        mView =  inflater.inflate(R.layout.fragment_giong_tab, container, false);

        Retrofit retrofit = ApiClient.getClient();
        giongService =retrofit.create(GiongService.class);
        loaiThuCungService=retrofit.create(LoaiThuCungService.class);

        setInit();
        setEvent();
        return mView;
    }


}