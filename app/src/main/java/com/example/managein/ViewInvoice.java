package com.example.managein;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
//This class displays the invoice image and allows you to return to the home page by clicking a button
public class ViewInvoice extends AppCompatActivity {

    //    This function displays the activity of and also calls the functions of presenting the invoice and activating a button listener
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoice);
        viewInvoiceImage();
        buildAButtonListener();
    }



    //    --------------------------------------------------------
//    This function displays the invoice image from ImageUri
    private void viewInvoiceImage() {
        ImageView imageView=(ImageView)findViewById(R.id.Invoice_Image);
        String ImageUri= getIntent().getStringExtra("ImageUri");
        imageView.setImageURI(Uri.parse(ImageUri));
    }


    //    --------------------------------------------------------
//    This function builds a listener for an exit button that when the user presses the button he will go to the home page
    private void buildAButtonListener() {
        ImageButton logout = findViewById(R.id.LogOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewInvoice.this, homepage.class);
                startActivity(intent);
            }
        });
    };
}


