package com.example.admin.travel.mainScreen;

import android.content.Intent;
import android.os.Bundle;

import com.example.admin.travel.R;
import com.example.admin.travel.base.BaseActivity;
import com.example.admin.travel.base.MyApplication;
import com.example.admin.travel.base.TravelSort;
import com.example.admin.travel.editTravel.EditTravelActivity;
import com.example.admin.travel.editTravel.viewmodels.TravelViewModel;
import com.example.admin.travel.mainScreen.adapters.TravelListAdapter;
import com.example.admin.travel.models.Travel;
import com.example.admin.travel.travelDetail.TravelDetailActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static com.example.admin.travel.base.MyConst.REQACTION_DEL_TRAVEL;
import static com.example.admin.travel.base.MyConst.REQACTION_EDIT_TRAVEL;
import static com.example.admin.travel.base.MyConst.REQCD_TRAVEL_ADD;
import static com.example.admin.travel.base.MyConst.REQCD_TRAVEL_EDIT;
import static com.example.admin.travel.base.MyConst.REQKEY_TRAVEL;
import static com.example.admin.travel.base.MyConst.REQKEY_TRAVEL_ID;

public class MainActivity extends BaseActivity implements TravelListAdapter.ListviewItemClickListener {

    private TravelViewModel mTravelViewModel;
    private RecyclerView recyclerView;
    private TravelListAdapter travelListAdapter;
    private List<Travel> mList = new ArrayList<>();
    private Observer<List<Travel>> listObserver = new Observer<List<Travel>>() {
        @Override
        public void onChanged(List<Travel> travels) {
            mList.clear();
            mList.addAll(travels);
            travelListAdapter.notifyDataSetChanged();
            Log.d("giangtm1", "onChanged: " + travels.size());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleView);
        travelListAdapter = new TravelListAdapter(getBaseContext(), mList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(travelListAdapter);
        travelListAdapter.setOnListViewClickListener(this);
        travelListAdapter.setOnListViewMoreClickListener(new TravelListAdapter.ListviewMoreButtonClickListener() {
            @Override
            public void onClick(Travel travel) {
                Intent intent = new Intent(getBaseContext(), EditTravelActivity.class);
                intent.putExtra(REQKEY_TRAVEL, travel);
                intent.setAction(REQACTION_EDIT_TRAVEL);
                startActivityForResult(intent, REQCD_TRAVEL_EDIT);
            }
        });
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getBaseContext(), EditTravelActivity.class), REQCD_TRAVEL_ADD);
            }
        });


        mTravelViewModel = ViewModelProviders.of(this).get(TravelViewModel.class);
        mTravelViewModel.getAllTravels().observe(this, listObserver);
        mTravelViewModel.setTravelSort(((MyApplication) getApplication()).getTravelSort());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        TravelSort travelSort = ((MyApplication) getApplication()).getTravelSort();
        switch (travelSort) {
            case DEFAULT:
                menu.findItem(R.id.action_sort_default).setChecked(true);
                break;
            case TITLE_ASC:
                menu.findItem(R.id.action_sort_travel_asc).setChecked(true);
                break;
            case TITLE_DESC:
                menu.findItem(R.id.action_sort_travel_desc).setChecked(true);
                break;
            case START_ASC:
                menu.findItem(R.id.action_sort_start_asc).setChecked(true);
                break;
            case START_DESC:
                menu.findItem(R.id.action_sort_start_desc).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_sort_default:
                mTravelViewModel.setTravelSort(TravelSort.DEFAULT);
                item.setChecked(true);
                break;
            case R.id.action_sort_travel_asc:
                mTravelViewModel.setTravelSort(TravelSort.TITLE_ASC);
                item.setChecked(true);
                break;
            case R.id.action_sort_travel_desc:
                mTravelViewModel.setTravelSort(TravelSort.TITLE_DESC);
                item.setChecked(true);
                break;
            case R.id.action_sort_start_asc:
                mTravelViewModel.setTravelSort(TravelSort.START_ASC);
                item.setChecked(true);
                break;
            case R.id.action_sort_start_desc:
                mTravelViewModel.setTravelSort(TravelSort.START_DESC);
                item.setChecked(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        Travel travel = null;
        switch (requestCode) {
            case REQCD_TRAVEL_ADD:
                if (data != null && data.getExtras() != null) {
                    travel = (Travel) data.getExtras().getSerializable(REQKEY_TRAVEL);
                    mTravelViewModel.insert(travel);
                }
                break;
            case REQCD_TRAVEL_EDIT:
                if (data != null && data.getExtras() != null) {
                    travel = (Travel) data.getExtras().getSerializable(REQKEY_TRAVEL);
                    if (REQACTION_DEL_TRAVEL.equals(data.getAction())) {
                        mTravelViewModel.delete(travel);

                    } else {
                        mTravelViewModel.update(travel);
                    }


                }
                break;
        }
    }

    @Override
    public void onClick(View v, int pos, Travel travel) {
        Intent intent = new Intent(getBaseContext(), TravelDetailActivity.class);
        intent.putExtra(REQKEY_TRAVEL_ID, travel.getId());
        startActivity(intent);
    }
}
