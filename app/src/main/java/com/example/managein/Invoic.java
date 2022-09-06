package com.example.managein;

import java.util.Date;
//This class holds and manages an invoice-type object including constructors and get set functions
public class Invoic  {
   private String store;
   private Double price;
   private Date date;
   private String category;
   private String ImageUrl;
   private String reference;
   private int lengthToSave;

   public Invoic(String store,Double price,Date date,String category,String ImageUrl,String reference)
   {
       this.category=category;
       this.date=date;
       this.price=price;
       this.store=store;
       this.ImageUrl=ImageUrl;
       this.reference=reference;
   }
    public Invoic()
    {
    }
    public Invoic(Invoic invoic)
    {
        this.lengthToSave=invoic.lengthToSave;
        this.date=invoic.date;
        this.store=invoic.store;
        this.ImageUrl=invoic.ImageUrl;
        this.reference=invoic.reference;
        this.category=invoic.category;
    }

    public int getLengthToSave() {
        return lengthToSave;
    }

    public void setLengthToSave(int lengthToSave) {
        this.lengthToSave = Repository.getLength(lengthToSave);
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public Date getDate() {
        return date;
    }

    public Double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getStore() {
        return store;
    }

    public void setCategory(String category) {
        category = category;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setStore(String store) {
        this.store = store;
    }
}
