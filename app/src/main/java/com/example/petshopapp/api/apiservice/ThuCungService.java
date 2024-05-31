package com.example.petshopapp.api.apiservice;

import com.example.petshopapp.model.ThuCung;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ThuCungService {
    @GET("center/thucung")
    Call<List<ThuCung>> getAll();
}
