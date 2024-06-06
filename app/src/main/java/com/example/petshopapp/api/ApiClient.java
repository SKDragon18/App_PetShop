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
    private static final String API_PATH ="http://192.168.1.29:8989/";
    private static final Gson GSON =new GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            .create();
    private static ApiClient apiClient;
    private String authToken = "";
    private Retrofit retrofit = null;

    private ApiClient(){
    }

    public void setAuToken(String authToken){
        this.authToken=authToken;
    }

    public static ApiClient getApiClient(){
        if(apiClient==null){
            apiClient=new ApiClient();
        }
        return apiClient;
    }

    public Retrofit getRetrofit(){
        if(retrofit == null){
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + authToken)
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();
            retrofit= new Retrofit.Builder()
                    .baseUrl(API_PATH)
                    .addConverterFactory(GsonConverterFactory.create(GSON))
                    .build();
        }
        return retrofit;
    }
    public void deleteRetrofit(){
        retrofit=null;
    }

}

