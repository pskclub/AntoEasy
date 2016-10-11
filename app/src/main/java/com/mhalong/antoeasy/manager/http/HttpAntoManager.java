package com.mhalong.antoeasy.manager.http;

import android.content.Context;

import com.mhalong.antoeasy.manager.Contextor;

import retrofit2.Retrofit;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class HttpAntoManager {

    private static HttpAntoManager instance;

    public static HttpAntoManager getInstance() {
        if (instance == null)
            instance = new HttpAntoManager();
        return instance;
    }

    private Context mContext;
    private ApiAnto service;

    private HttpAntoManager() {
        mContext = Contextor.getInstance().getContext();
        Retrofit retofit = new Retrofit.Builder()
                .baseUrl("https://api.anto.io/")
                .build();
        service = retofit.create(ApiAnto.class);
    }

    public ApiAnto getService() {
        return service;
    }

}
