package com.example.managein;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Objects;
//This department manages the home page and assigns listeners to buttons (add invoice, invoice search) that activate functions that perform the appropriate actions at the click of a button
public class homepage extends AppCompatActivity {


    //    This function displays the activity of the homepage and sets the buttons of that page
    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        HelperUtil.isChangeDetails=false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        setButtons();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();//
        DatabaseReference ref2 = database.getReference("users/" + FirebaseAuth.getInstance().getUid());
    }


    //    ----------------------------------------------------------------------------
//    This function is responsible for the buttons
    private void setButtons(){
        Button newInvoiceButton,searchInvoiceButton;
        newInvoiceButton = (Button) findViewById(R.id.addNewInvoice);
        searchInvoiceButton = (Button) findViewById(R.id.invoiceSearch);
        addListenerToNewInvoiceButton(newInvoiceButton);
        addListenerToSearchInvoiceButton(searchInvoiceButton);
    }


    //    ----------------------------------------------------------------------------
    //    This function activates a listener on the button newInvoiceButton
    private void addListenerToNewInvoiceButton(Button newInvoiceButton){
        newInvoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                NewInvoice n = new NewInvoice();
                n.execute(homepage.this);
            }
        });
    }


    //    ----------------------------------------------------------------------------
    //    This function activates a listener on the button newInvoiceButton searchInvoiceButton
    private void addListenerToSearchInvoiceButton(Button searchInvoiceButton){
        searchInvoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchInvoice n=new SearchInvoice();
                n.execute(homepage.this);
            }
        });
    }

}