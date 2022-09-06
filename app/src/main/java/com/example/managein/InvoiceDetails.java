package com.example.managein;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Objects;
//--------------------------------------------------------------------------------------------------
public class InvoiceDetails extends AppCompatActivity {

    public EditText store;
    public EditText price;
    public EditText date;
    public EditText Category;
    private static final String TAG = "InvoiceDetails";
    private int index;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref2 = database.getReference("users/" + FirebaseAuth.getInstance().getUid());
    String ImageUrl = getIntent().getStringExtra("ImageUri");
    //----------------------------------------------------------------------------------------------------
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);
        Button UpdateDetailsButton;
        UpdateDetailsButton = (Button) findViewById(R.id.UpdateDetails);
        InsertValues();
// Check if we came to this page because of a change to an existing invoice //or because we entered a new invoice
        if(!HelperUtil.isChangeDetails) {
            InsertValuseFromTheDB();
        }
        // This function works as soon as the update button is pressed
            UpdateDetailsButton.setOnClickListener(new View.OnClickListener(){
                @SuppressLint("ShowToast")
                @Override
                public void onClick(View view) {
                    InsertValues();
                    if(!store.getText().toString().equals("") &&store!=null&&store.getText().toString()!="store name"&& !price.getText().toString().equals("") &&price!=null&& !date.getText().toString().equals("") &&date!=null&& !Category.getText().toString().equals("") &&Category!=null) {
                        ref2.child("sumInvoices").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task){
                                if (!task.isSuccessful()) {
                                    Intent intent = new Intent(InvoiceDetails.this, Camera.class);
                                    startActivity(intent);
                                } else {
                                    if(!HelperUtil.isChangeDetails) {
                                        index = Integer.parseInt(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
                                        NewInvoiceInsertion(index);
                                    }
                                    else
                                    {
                                        ChangeInvoice();
                                    }
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(InvoiceDetails.this,"All fields are required",Toast.LENGTH_LONG);
                    }
                }

            });
        }

//------------------------------------------------------------------------------------------------------------------
    //This function is activated when we reach this page after requesting to change the invoice details
    private void ChangeInvoice() {
        String index = getIntent().getStringExtra("index");
        DatabaseReference ref1 = database.getReference("users/" + FirebaseAuth.getInstance().getUid() + "/invoices/" + index);
        if(ref1.removeValue().isSuccessful())
            System.out.println("change invoice");
        DatabaseReference ref = database.getReference("users/" + FirebaseAuth.getInstance().getUid() + "/invoices/" + index);
        Invoic invoice = Repository.BuildNewInvoice(store.getText().toString(), Double.parseDouble(price.getText().toString()), new Date(date.getText().toString()), Category.getText().toString(), ImageUrl, ref.getKey());
        ref.setValue(invoice);
        Repository.setUrl(index);
    }

//------------------------------------------------------------------------------------------------------------------
    //This function is activated when we reach this page after requesting a new invoice
    private void NewInvoiceInsertion(int index) {
        DatabaseReference ref = database.getReference("users/" + FirebaseAuth.getInstance().getUid() + "/invoices/" + index + FirebaseAuth.getInstance().getUid());
        Invoic invoice = Repository.BuildNewInvoice(store.getText().toString(), Double.parseDouble(price.getText().toString()), new Date(date.getText().toString()), Category.getText().toString(), ImageUrl, ref.getKey());
        Intent intent = new Intent(InvoiceDetails.this, LengthToSave.class);
        intent.putExtra("length",invoice.getReference());
        index = index + 1;
        ref2.child("sumInvoices").setValue(index);
        ref.setValue(invoice);
        startActivity(intent);
    }

//---------------------------------------------------------------------------------------------------------------------
    //Change user information according to what he typed in the app
    private void InsertValues() {
        store = findViewById(R.id.StoresNameInput);
        price = findViewById(R.id.PurchaseAmountInput);
        date = findViewById(R.id.DateOfPurchaseInput);
        Category = findViewById(R.id.categoryInput);
    }

//----------------------------------------------------------------------------------------------------------------------
    //Obtaining user information according to what is in the database
    private void InsertValuseFromTheDB() {
        String StoreNameInvoice = getIntent().getStringExtra("StoreName");
        String DateNameInvoice = getIntent().getStringExtra("Date");
        String PriceNameInvoice = getIntent().getStringExtra("Price");
        store.setText(StoreNameInvoice);
        date.setText(DateNameInvoice);
        price.setText(PriceNameInvoice);
        store.setTextSize(15);
        date.setTextSize(15);
        price.setTextSize(15);
    }

}