package com.example.admin.travel.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.stetho.Stetho;

public class MyApplication extends Application {
    private static final String TRAVEL_SORT_NAME = "TRAVEL_SORT_NAME";
    private static final String TRAVEL_SORT_KEY = "TRAVEL_SORT_KEY";

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        Log.d("giangtm1", "onCreate");
    }

    public void setTravelSort(TravelSort travelSort) {
        SharedPreferences sharedPreferences = getSharedPreferences(TRAVEL_SORT_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TRAVEL_SORT_KEY, travelSort.name());
        editor.apply();
    }

    public TravelSort getTravelSort() {
        SharedPreferences sharedPreferences = getSharedPreferences(TRAVEL_SORT_NAME, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(TRAVEL_SORT_KEY, TravelSort.DEFAULT.name());
        TravelSort travelSort = TravelSort.DEFAULT;
        try {
            travelSort = TravelSort.valueOf(name);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return travelSort;
    }
}
