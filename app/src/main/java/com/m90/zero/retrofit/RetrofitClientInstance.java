package com.m90.zero.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit2;

    //http://avistore.in/snappy/public/api/getImgList2
    public static final String BASE_URL = "http://avistore.in/snappy/public/api/";


    public static Retrofit getRetrofitInstanceServer() {
        if (retrofit2 == null) {
            retrofit2 = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit2;
    }




}
