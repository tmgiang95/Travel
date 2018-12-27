package com.example.admin.travel.mainScreen;

import android.content.Intent;
import android.os.Bundle;

import com.example.admin.travel.R;
import com.example.admin.travel.base.BaseActivity;
import com.example.admin.travel.editTravel.EditTravelActivity;
import com.example.admin.travel.editTravel.viewmodels.TravelViewModel;
import com.example.admin.travel.models.Travel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends BaseActivity {

    private TravelViewModel mTravelViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getBaseContext(), EditTravelActivity.class), REQCD_TRAVEL_ADD);
            }
        });
        mTravelViewModel = ViewModelProviders.of(this).get(TravelViewModel.class);
        mTravelViewModel.getmAllTravels().observe(this, new Observer<List<Travel>>() {
            @Override
            public void onChanged(List<Travel> travels) {
                Log.d("giangtm1", "onChanged: "+ travels.size());
            }
        });
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQCD_TRAVEL_ADD:
                Travel travel = null;
                if (data != null && data.getExtras() != null) {
                    travel = (Travel) data.getExtras().getSerializable(REQKEY_TRAVEL);
                    Log.d("giangtm1", "onActivityResult: " + travel.toString());
                    mTravelViewModel.insert(travel);
                }
                break;
        }
    }
}
