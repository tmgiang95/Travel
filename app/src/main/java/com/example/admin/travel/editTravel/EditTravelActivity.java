package com.example.admin.travel.editTravel;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.admin.travel.R;
import com.example.admin.travel.base.BaseActivity;
import com.example.admin.travel.models.Travel;
import com.example.admin.travel.utils.MyDate;
import com.example.admin.travel.utils.MyString;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import static com.example.admin.travel.base.MyConst.REQACTION_DEL_TRAVEL;
import static com.example.admin.travel.base.MyConst.REQACTION_EDIT_TRAVEL;
import static com.example.admin.travel.base.MyConst.REQCD_PLACE_AUTOCOMPLETE;
import static com.example.admin.travel.base.MyConst.REQKEY_TRAVEL;

public class EditTravelActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private EditText etText;
    private EditText etPlace;
    private EditText etStartDt;
    private EditText etEndDt;
    private Place mPlace;
    private Travel mTravel;
    private long startDt;
    private long endDt;
    private boolean mInEditMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_travel);
        findViewByIDMultiViews();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (REQACTION_EDIT_TRAVEL.equals(getIntent().getAction())) {
            mInEditMode = true;
            setTitle(getString(R.string.title_activity_edit_travel));
            mTravel = (Travel) getIntent().getExtras().getSerializable(REQKEY_TRAVEL);
            fillData();
        }


    }

    private void findViewByIDMultiViews() {
        etText = findViewById(R.id.etText);
        etPlace = findViewById(R.id.etPlace);
        etStartDt = findViewById(R.id.etStartDt);
        etEndDt = findViewById(R.id.etEndDt);
        findViewById(R.id.placeLayout).setOnClickListener(this);
        findViewById(R.id.startDtLayout).setOnClickListener(this);
        findViewById(R.id.endDtLayout).setOnClickListener(this);
        etPlace.setOnClickListener(this);
        etStartDt.setOnClickListener(this);
        etEndDt.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(this,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        switch (v.getId()) {
            case R.id.btnAdd:
                validData(v);
                break;
            case R.id.startDtLayout:
            case R.id.etStartDt:
                dpd.getDatePicker().setTag(v.getId());
                if (endDt > 0) {
                    dpd.getDatePicker().setMaxDate(endDt);
                }
                dpd.show();
                break;
            case R.id.endDtLayout:
            case R.id.etEndDt:
                dpd.getDatePicker().setTag(v.getId());
                if (startDt > 0) {
                    dpd.getDatePicker().setMinDate(endDt);
                }
                dpd.show();
                break;
            case R.id.placeLayout:
            case R.id.etPlace:
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                            .build();
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(typeFilter)
                            .build(this);
                    startActivityForResult(intent, REQCD_PLACE_AUTOCOMPLETE);
                } catch (GooglePlayServicesRepairableException e) {
                    GoogleApiAvailability.getInstance().getErrorDialog(this,
                            e.getConnectionStatusCode(),
                            0).show();
                } catch (GooglePlayServicesNotAvailableException e) {
                    String message = " Google Play Services is not available"
                            + GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
                    Log.e("giangtm1", message);
                    Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQCD_PLACE_AUTOCOMPLETE:
                mPlace = PlaceAutocomplete.getPlace(this, data);
                Log.i("giangtm1", "onActivityResult: " + mPlace);
                etPlace.setText(mPlace.getName());
        }
    }

    public void validData(View v) {
        String title = etText.getText().toString();
        if (MyString.isEmpty(title)) {
            ((TextInputLayout) findViewById(R.id.titleLayout)).setErrorEnabled(true);
            ((TextInputLayout) findViewById(R.id.titleLayout)).setError("Travel title is Empty");
            return;
        } else {
            ((TextInputLayout) findViewById(R.id.titleLayout)).setError(null);
        }
        if (startDt == 0) {
            Snackbar.make(v, "Travel start date is Empty", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (endDt == 0) {
            Snackbar.make(v, "Travel end date is Empty", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (mTravel == null) {
            mTravel = new Travel(title);
        } else {
            mTravel.setTitle(title);
        }
        mTravel.setDateTime(startDt);
        mTravel.setEndDt(endDt);
        if (mPlace != null) {
            mTravel.setPlaceId(mPlace.getId());
            mTravel.setPlaceName(mPlace.getName().toString());
            if (mPlace.getAddress() != null) {
                mTravel.setPlaceAddr(mPlace.getAddress().toString());
            }
            mTravel.setPlaceLat(mPlace.getLatLng().latitude);
            mTravel.setPlaceLng(mPlace.getLatLng().longitude);

        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra(REQKEY_TRAVEL, mTravel);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void fillData() {
        etText.setText(mTravel.getTitle());
        etPlace.setText(mTravel.getPlaceName());
        etStartDt.setText(mTravel.getDateTimeText());
        etEndDt.setText(MyDate.getDateString(mTravel.getEndDt()));
        startDt = mTravel.getDateTimeLong();
        endDt = mTravel.getEndDtLong();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Object tag = view.getTag();
        Calendar calendar = Calendar.getInstance();
        if (tag.equals(R.id.etStartDt)) {
            calendar.set(year, month, dayOfMonth, 0, 0, 0);
            if (endDt > 0 && endDt < calendar.getTimeInMillis()) return;
            startDt = calendar.getTimeInMillis();
            etStartDt.setText(String.valueOf(MyDate.getDateString(startDt)));
        } else {
            calendar.set(year, month, dayOfMonth, 23, 59, 59);
            if (startDt > 0 && startDt > calendar.getTimeInMillis()) return;
            endDt = calendar.getTimeInMillis();
            etEndDt.setText(String.valueOf(MyDate.getDateString(endDt)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mInEditMode)
            getMenuInflater().inflate(R.menu.menu_edittravel, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mniDelete:
                createAlertDialog(R.string.title_delete_dialog
                        , R.string.message_delete_dialog
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent returnIntent = new Intent();
                                returnIntent.setAction(REQACTION_DEL_TRAVEL);
                                returnIntent.putExtra(REQKEY_TRAVEL, mTravel);
                                setResult(RESULT_OK, returnIntent);
                                finish();
                            }
                        }
                        , null);

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
