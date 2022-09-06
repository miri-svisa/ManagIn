package com.example.managein;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
//This department manages the activity page of selecting an invoice retention period
//It stores the user's selection in a database and sends it to the home page with a message that the invoice has been saved successfully

public class LengthToSave extends AppCompatActivity {

    private static final String TAG = "LengthToSave";
    private final ArrayList<String> lengthToSave = Repository.getLengthOfTimeToSaveInvoice();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    //-------------------------------------------------------------------
//    This function displays the page and builds it
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_length_to_save);
        buildingArrayAndListeners();
    }


    //    -----------------------------------------------------------------
//    This function building the list and the listeners
    private void buildingArrayAndListeners(){
        ListView listViewLength = (ListView) findViewById(R.id.options);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, lengthToSave);//Inserting the contents of the list array in XML
        BuildListenerToItam(listViewLength,arrayAdapter);
    }


//    -----------------------------------------------------------------
//    This function building Listeners to Itams

    private void BuildListenerToItam(ListView listViewLength,ArrayAdapter<String> arrayAdapter){
        listViewLength.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String index=getIntent().getStringExtra("length");
                DatabaseReference ref2 = database.getReference("users/" + FirebaseAuth.getInstance().getUid() + "/invoices/" + index);
                ref2.child("lengthToSave").setValue(Repository.getLength(position));
                Toast.makeText(LengthToSave.this, "Invoice saved successfully!!!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LengthToSave.this, homepage.class);
                startActivity(intent);
            }
        });
        listViewLength.setAdapter(arrayAdapter);
    }
}
