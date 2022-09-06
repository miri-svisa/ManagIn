package com.example.managein;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DisplaysInvoiceList extends AppCompatActivity {

    private static final String TAG = "DisplaysInvoiceList";
    private ListView listViewInvoices;
    private ArrayList<Invoic> invoiceList;//יצירת רשימה של חשבוניות
    private ArrayAdapter<String> arrayAdapter;//=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,invoiceList);
    private String Item;
//-------------------------------------------------------------------------------------------------------------------
    @Override
    //This is the function that activates the specific page of the function
    protected void onCreate(Bundle savedInstanceState){
        invoiceList= Repository.getInvoiceList();
        Item = getIntent().getStringExtra("Item");
        String valueToSearch = getIntent().getStringExtra("theValueToSearch");
        MyAdapter arrayAdapter=new MyAdapter();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displays_invoice_list);
         if (invoiceList.size() == 0){
                if(HelperUtil.isChangeDetails)
                {
                    Intent intent = new Intent(DisplaysInvoiceList.this, LengthToSave.class);
                    intent.putExtra("length", Repository.getUrl());
                    finish();
                    this.startActivity(intent);
                }
                else{
                    if(HelperUtil.isChangeDetails)
                        Toast.makeText(DisplaysInvoiceList.this, "you delete all the invoices with the " + Item + " " + valueToSearch, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(DisplaysInvoiceList.this, homepage.class);
                    finish();
                    this.startActivity(intent);
                }
            }


        listViewInvoices=(ListView)findViewById(R.id.options);
        listViewInvoices.setDividerHeight(30);
        listViewInvoices.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
//----------------------------------------------------------------------------------------------------------------------------
        //This function works when an invoice is selected from the defined list
    listViewInvoices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(DisplaysInvoiceList.this, SelectInvoiceUses.class);
                System.out.println("position = "+position);
                System.out.println("ImageUri = "+invoiceList.get(position).getImageUrl());
                System.out.println("ImageUri = "+invoiceList.get(position).getStore());
                intent.putExtra("ImageUri",invoiceList.get(position).getImageUrl());
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }
//-------------------------------------------------------------------------------------------------------
//This department builds us a dynamic list
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return invoiceList.size();
        }
    //-------------------------------------
        @Override
        public Object getItem(int position) {
            return invoiceList.get(position);
        }
    //-------------------------------------
        @Override
        public long getItemId(int position) {
            return 0;
        }
    //-------------------------------------
        @Override
        //This function returns the specific object in the selected list
        public View getView(int position, View convertView, ViewGroup parent) {
            String name = getIntent().getStringExtra("Item");
                LayoutInflater inflater=getLayoutInflater();
                convertView= inflater.inflate(R.layout.line_in_invoices_list,null);
            TextView tv=convertView.findViewById(R.id.text_invoice_list);
            if(name.equals("category") || name.equals("date") || name.equals("Price")||name.equals("Favorites"))
                tv.setText(invoiceList.get(position).getStore());
            else
                tv.setText(invoiceList.get(position).getDate().toString());
            return convertView;
        }
    }
}