package com.mhalong.antoeasy;

import android.app.Application;

import com.mhalong.antoeasy.manager.Contextor;

/**
 * Created by passa on 10/10/2559.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Contextor.getInstance().init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
