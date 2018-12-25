package com.example.admin.travel;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.example.admin.travel.base.BaseActivity;
import com.example.admin.travel.utils.MyString;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;

public class EditTravelActivity extends BaseActivity implements View.OnClickListener {
    EditText etText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_travel);
        etText = findViewById(R.id.etText);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAdd:
                String title = etText.getText().toString();
                if(MyString.isEmpty(title)){
                    Snackbar.make(v,"Travel title is Empty",Snackbar.LENGTH_LONG).show();
                    return;
                }
                finish();
                break;
        }
    }
}
