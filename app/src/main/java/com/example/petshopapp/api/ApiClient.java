package com.example.petshopapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String API_PATH ="http://192.168.1.243:8080/";
    private static Gson gson =new GsonBuilder().setLenient().create();

    public static Retrofit getClient(){
        return new Retrofit.Builder()
                .baseUrl(API_PATH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}

//Use:
//Retrofit retrofit = ApiClient.getClient(token);
//iApiService = retrofit.create(iApiService.class);
