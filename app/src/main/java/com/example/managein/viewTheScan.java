package com.example.managein;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
/////not useful!!
public class viewTheScan extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_the_scan);
        Button newScanButton;
        ImageButton okScanButton;
        newScanButton = (Button) findViewById(R.id.addNewScan);
        newScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewTheScan.this, Scan.class);
                startActivity(intent);
            }
        });

        okScanButton = (ImageButton) findViewById(R.id.okScan);
        okScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewTheScan.this,LengthToSave.class );
                startActivity(intent);
            }
        });
    }
}