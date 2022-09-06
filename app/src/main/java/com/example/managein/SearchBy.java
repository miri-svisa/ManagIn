package com.example.managein;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SearchBy extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private int index;
    private ListView listViewFinds;
    private ArrayList<String> finds= Repository.getFindes();
    private  int invoices=0;
    private String showItem;
    private ArrayList<String>arrShowItem;
    private  ArrayList<Invoic>arrInvoices=new ArrayList<>();
    private ArrayList<Invoic> invoiceList;
    EditText theValueToSearch = findViewById(R.id.valueToSearch);
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref2 = database.getReference("users/"+FirebaseAuth.getInstance().getUid());
    private static final String TAG = "QueryActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_search_by);
        listViewFinds=(ListView)findViewById(R.id.options);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,finds);
        //-----------------------------------------------------------------------------------
        //This function works when a search option is selected from a fixed list of options
        //The search is performed by going through all the invoices stored by the user and checking each invoice to see
        // if it has the appropriate data for the search if and the invoice is added to the list of the invoices suitable
        // for the search if we do not move to the next invoice.
        listViewFinds.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
              Button  search = (Button) findViewById(R.id.searchInvoice);
                search.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        String Item=(String)parent.getItemAtPosition(position);
                        ChackItem(Item);
                        ref2.child("sumInvoices").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                }
                                else {
                                    index=Integer.parseInt(String.valueOf(Objects.requireNonNull (task.getResult()).getValue()));
                                    FoundThePlaceInDB(Item,index);
                                }
                            }
                        });
                    }
                });
            }
        });
        listViewFinds.setAdapter(arrayAdapter);
    }
//------------------------------------------------------------------------------------------------------------
    //This function is called when a node is found with the information entered by the user in a database
    private void FoundThePlaceInDB(String Item,int index) {
        ChackingIndex();
        invoices=0;
        Query refinvoic = database.getReference("users/" + FirebaseAuth.getInstance().getUid() + "/invoices");

        refinvoic.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                SearchInvoiceInDB(dataSnapshot,Item);
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
                Intent intent = new Intent(SearchBy.this, SearchBy.class);
                finish();
                startActivity(intent);
            }
        });
    }
//-------------------------------------------------------------------------------------------------------
    //This function checks if the list we received from the database is empty, ie no data is found with the information entered by the user
    private void ChackingIndex() {
        if(index==0)
        {
            Toast.makeText(SearchBy.this, "there is no invoices", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(SearchBy.this, SearchBy.class);
            startActivity(intent);
        }
    }

//----------------------------------------------------------------------------------------------------------
    //Check what item the user entered to know what to display on the invoice line
    private void ChackItem(String Item) {
        if(Item=="store")
        {
            showItem="date";
        }
        else if(Item=="category"||Item=="date")
        {
            showItem="store";
        }
    }

//------------------------------------------------------------------------------------------------------------------
    //Search for data in a database that matches what the user entered
    private void SearchInvoiceInDB(DataSnapshot dataSnapshot,String Item) {
        arrInvoices.clear();
        for (DataSnapshot invoiceSnapshot: dataSnapshot.getChildren()){
            ChackIfTheValusEquals(invoiceSnapshot,Item);
        }
        Intent intent = new Intent(SearchBy.this, DisplaysInvoiceList.class);
        intent.putExtra("Item",Item);
        intent.putExtra("theValueToSearch",theValueToSearch.getText().toString());
        Repository.setListInvoices(arrInvoices);
        finish();
        startActivity(intent);
    }

//----------------------------------------------------------------------------------------
    //Data comparison
    private void ChackIfTheValusEquals(DataSnapshot invoiceSnapshot,String Item) {
        if(invoiceSnapshot.child(Item).getValue().toString().equals(theValueToSearch.getText().toString()))
        {
            Invoic c = invoiceSnapshot.getValue(Invoic.class);
            arrInvoices.add(c);
            invoices++;
        }
        else {
            System.out.println("falseeee");
        }
        // TODO: handle the post
    }
}