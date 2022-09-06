package com.example.managein;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
//This class shows some options used for the selected invoice.
//--------------------------------------------------------------------------------------------------------
public class SelectInvoiceUses extends AppCompatActivity {
    private static final String TAG ="SelectInvoiceUses" ;
    private int refrence;
    String ImageUri;
    Invoic in;
    Button ShowButton, DeleteButton, EditButton;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    //------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_invoice_uses);
        ImageView imageView=(ImageView)findViewById(R.id.LitteiInvoice);
        refrence=getIntent().getIntExtra("position",0);
        imageView.setImageURI(Uri.parse(ImageUri));
        ImageUri= getIntent().getStringExtra("ImageUri");
        ShowButton = (Button) findViewById(R.id.Show);
        DeleteButton = (Button) findViewById(R.id.Delete);
        EditButton = (Button) findViewById(R.id.Edit);
        in= Repository.getInvoice(refrence);
        ShowButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ShowButtonPressed();
            }
        });
        DeleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                DeleteButtonPressed();
            }
        });
        EditButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditButtonPressed();
            }
        });
    }
//------------------------------------------------------------------------------------------------------------------------
//This function is called when the user chooses to change the invoice information
    private void EditButtonPressed() {
        HelperUtil.isChangeDetails=true;
        DatabaseReference ref2=database.getReference("users/" + FirebaseAuth.getInstance().getUid() + "/invoices");
        Intent intent = new Intent(SelectInvoiceUses.this, InvoiceDetails.class);
        String mCurrentPhotoPath=in.getImageUrl();
        intent.putExtra("ImageUri",mCurrentPhotoPath);
        intent.putExtra("index",in.getReference());
        startActivity(intent);
    }
//----------------------------------------------------------------------------------------------------------------------
//This function is called when the user chooses to delete the invoice
    private void DeleteButtonPressed() {
        DatabaseReference ref = database.getReference("users/" + FirebaseAuth.getInstance().getUid() + "/invoices/" + in.getReference());
        ref.removeValue();
        DatabaseReference ref2 = database.getReference("users/"+FirebaseAuth.getInstance().getUid());
        ref2.child("sumInvoices").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                int sumInv=Integer.parseInt(String.valueOf(Objects.requireNonNull (task.getResult()).getValue()));
                ref2.child("sumInvoices").setValue(sumInv-1);
            }
        });
        Repository.setInvoices(refrence);
        popupMessage();
        Intent intent = new Intent(SelectInvoiceUses.this, DisplaysInvoiceList.class);
        startActivity(intent);
    }
//------------------------------------------------------------------------------------------------------------------------
//This function pops up a notification to the user that the invoice has been deleted
    public void popupMessage(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DeleteButton.getContext());
        alertDialogBuilder.setMessage("The invoice has been deleted.");
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
//-----------------------------------------------------------------------------------------------------------------------
//This function is called when the user chooses to view the invoice
    private void ShowButtonPressed() {
        Intent intent = new Intent(SelectInvoiceUses.this, ViewInvoice.class);
        intent.putExtra("ImageUri",ImageUri);
        startActivity(intent);
    }

}