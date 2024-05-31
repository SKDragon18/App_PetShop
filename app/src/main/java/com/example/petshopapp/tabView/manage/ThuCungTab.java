package com.example.petshopapp.tabView.manage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.petshopapp.R;
import com.example.petshopapp.adapter.ThuCungManageAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.ThuCungService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.ThuCung;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThuCungTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThuCungTab extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //
    private View mView;

    //View
    GridView gvThuCung;

    //API
    ThuCungService thuCungService;

    //Data
    List<ThuCung> data= new ArrayList<>();

    ThuCungManageAdapter thuCungManageAdapter;


    public ThuCungTab() {
        // Required empty public constructor
    }


    public void setInit(){
        gvThuCung = mView.findViewById(R.id.gvThuCung);
    }

    public void setEvent(){
        thuCungManageAdapter=new ThuCungManageAdapter(mView.getContext(),R.layout.item_thucung_manage,data);
        gvThuCung.setAdapter(thuCungManageAdapter);
    }

    public void DocDL(){
        System.out.println("DocDLThuCung");
        thuCungService.getAll().enqueue(new Callback<List<ThuCung>>() {
            @Override
            public void onResponse(Call<List<ThuCung>> call, Response<List<ThuCung>> response) {
                if (response.code() == 200) {
                    data.clear();
                    for (ThuCung x : response.body()) {
                        data.add(x);
                    }
                    thuCungManageAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<List<ThuCung>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }

    public static ThuCungTab newInstance(String param1, String param2) {
        ThuCungTab fragment = new ThuCungTab();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Retrofit retrofit = ApiClient.getClient();
        thuCungService =retrofit.create(ThuCungService.class);
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_thu_cung_tab, container, false);
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
    //sau khi create view
}