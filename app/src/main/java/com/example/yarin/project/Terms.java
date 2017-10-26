package com.example.yarin.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Terms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
    }
    public void approve(View view) {
        Intent responseIntent = getIntent();
        //Intent responseIntent = new Intent();//getIntent();

        // the result code inidicates the user approved
        // the intent contains the data to pass
        setResult(RESULT_OK, responseIntent);
        finish();
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    }

