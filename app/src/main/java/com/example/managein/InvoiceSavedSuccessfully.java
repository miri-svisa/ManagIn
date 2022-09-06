package com.example.managein;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
/////not useful!!
public class InvoiceSavedSuccessfully extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_saved_successfully);
//    }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(InvoiceSavedSuccessfully.this, homepage.class);
                InvoiceSavedSuccessfully.this.startActivity(mainIntent);
                InvoiceSavedSuccessfully.this.finish();
            }
        }, 2000);
    }}