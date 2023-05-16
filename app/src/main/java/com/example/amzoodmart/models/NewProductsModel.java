package com.example.amzoodmart.models;

import java.io.Serializable;

public class NewProductsModel implements Serializable {
    String description;
    String name;
    double rating;
    int price;
    String img_url;


    public NewProductsModel() {
    }

    public NewProductsModel(String description, String name, double rating, int price,String img_url) {
        this.description = description;
        this.name = name;
        this.rating = rating;
       this.price = price;
        this.img_url = img_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
