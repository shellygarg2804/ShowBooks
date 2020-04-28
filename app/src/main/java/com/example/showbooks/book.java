package com.example.showbooks;

import android.icu.text.StringPrepParseException;

public class book {
    private String name=null;
    private String author=null;
    private  String pages=null;
    private  String price="0";
    private  double rating= 0.0;
    private String imageid;
    String selfurl=null;

    public book(String name, String author, String pages,String price,double rating, String imageid){
        this.name=name;
        this.author=author;
        this.pages=pages;
        this.price=price;
        this.rating=rating;
        this.imageid=imageid;
    }

    public book(String name, String author, String pages,String price,double rating, String imageid,String selfurl){
        this.name=name;
        this.author=author;
        this.pages=pages;
        this.price=price;
        this.rating=rating;
        this.imageid=imageid;
        this.selfurl=selfurl;
    }

    public double getRating() {
        return rating;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public String getPages() {
        return pages;
    }

    public String getPrice() {
        return price;
    }

    public String getImageid() {
        return imageid;
    }

    public String getSelfurl() {
        return selfurl;
    }
}
