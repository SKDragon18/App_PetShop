package com.example.petshopapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String API_PATH ="http://192.168.1.243:8989/";
    private static Gson gson =new GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            .create();
    private static String authToken = "";
    public static void setAuToken(String authTokenNew){
        authToken=authTokenNew;
    }
    public static Retrofit getClient(){
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + authToken)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();
        return new Retrofit.Builder()
                .baseUrl(API_PATH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

}

