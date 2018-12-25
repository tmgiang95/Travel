package com.example.admin.travel.base;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("giangtm1", "onCreate");
    }
}
