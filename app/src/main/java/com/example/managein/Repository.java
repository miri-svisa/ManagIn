package com.example.managein;

import java.util.ArrayList;
import java.util.Date;
//This class maintains and maintains fixed objects, lists, and variables that the application uses
public class Repository {
    private static User user;
    private static ArrayList<Invoic>Invoices=new ArrayList<>();
    private static int sumInvoices=0;
    private static Invoic invoice;
    private static String url;

    public Repository(User user){
        this.user = user;
    }

    //--------------------------------------------------------------------------------------
    //     A function that returnsan array with possible categories for invoice search
    public static ArrayList<String>getFindes(){
        ArrayList<String>finds=new ArrayList<>();
        finds.add("category");
        finds.add("date");
        finds.add("store");
        finds.add("price");
        return finds;
    }

//------------------------------------------------------------------------------------------------------
    public static void setListInvoices(ArrayList<Invoic> arrInvoices) {
        Invoices = arrInvoices;
    }
//------------------------------------------------------------------------------------------------------------------------
    public static ArrayList<Invoic>getInvoiceList(){
        return Repository.Invoices;
    }
//------------------------------------------------------------------------------------------------------------------------
    public static ArrayList<String>getActionsInInvoice(){
        ArrayList<String>Invoices=new ArrayList<>();
        Invoices.add("V");
        Invoices.add("Scanning a new invoice");
        Invoices.add("Send the invoice by email");
        return Invoices;
    }
//-------------------------------------------------------------------------------------------------------------------------
//A function that returns an array with options for invoice operations
    public static ArrayList<String>getLengthOfTimeToSaveInvoice(){
        ArrayList<String>Invoices=new ArrayList<>();
        Invoices.add("One Day");
        Invoices.add("Five days");
        Invoices.add("14 days");
        Invoices.add("A month");
        return Invoices;
    }
    //----------------------------------------------------------------------------------------------------------------------------------
    //This function returns the length of time the user has chosen to save the invoice
    public static int getLength(int position)
    {
        if (position==0)
            return 1;
        if (position==1)
            return 5;
        if (position==2)
            return 14;
        return 30;
    }
    //----------------------------------------------------------------------------------------------------------------------------
    //A function that builds a new user
    public static User BuildNewUser(String email, String user_name, String reference, int password, int id)
    {
        user=new User(email,user_name ,reference,password,id);
        return user;
    }
    //-----------------------------------------------------------------------------------------------------------------------------
    //A function that builds a new invoice
    public static Invoic BuildNewInvoice(String store, Double price, Date date, String category, String ImageUrl, String ref)
    {
       invoice=new Invoic(store,price ,date,category,ImageUrl,ref);
        return invoice;
    }
//--------------------------------------------------------------------------------------------------------------------------------
    public static Invoic getInvoice(int position) {
        return Invoices.get(position);
    }
    //----------------------------------------------------------------------------------------------------------------------
    public static void setInvoices(int position)
    {
        Invoices.remove(position);
    }
    //----------------------------------------------------------------------------------------------------------------------
    public static User getUser() {
        return user;
    }
    //----------------------------------------------------------------------------------------------------------------------
    public static String getUrl() {
        return url;
    }
    //----------------------------------------------------------------------------------------------------------------------
    public static void setUrl(String url) {
        Repository.url = url;
    }
}
