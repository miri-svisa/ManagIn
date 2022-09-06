package com.example.managein;
//This class holds and manages an object of the database that stores the user's information
public class User {
    private String email;
    private String user_name;
    private String reference;
    private int password;
    private int id;
    private int sumInvoices;

    public User(String email,String user_name ,String reference,int password,int id)
    {
        this.email=email;
        this.id=id;
        this.password=password;
        this.user_name=user_name;
        this.reference=reference;
        this.sumInvoices=0;
    }
    public User(){}

    public int getSumInvoices() { return sumInvoices; }

    public void setSumInvoices(){ this.sumInvoices ++;}

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getId() {
        return id;
    }

    public int getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
