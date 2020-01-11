package com.nasande.nasande;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    public ApiService ObtenirInstance(){
        String API_BASE_URL = "http://nasande.cg/";
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(new OkHttpClient.Builder().addInterceptor(interceptor).build());

        Retrofit retrofit = builder.build();


        ApiService client =  retrofit.create(ApiService.class);
        return client;
    }
}
