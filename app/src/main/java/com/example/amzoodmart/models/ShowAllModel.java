package com.example.amzoodmart.models;

import java.io.Serializable;

public class ShowAllModel implements Serializable {

    String description;
    String name;
    double rating;
    int price;
    int offer;
    String img_url;
    String type;
    int delivery;
    String delivery_time;
    String replace;
    String return1;



    public ShowAllModel() {
    }

    public ShowAllModel(String description, String name, double rating, int price, int offer, String img_url, String type, int delivery, String delivery_time, String replace, String return1) {
        this.description = description;
        this.name = name;
        this.rating = rating;
        this.price = price;
        this.offer = offer;
        this.img_url = img_url;
        this.type = type;
        this.delivery = delivery;
        this.delivery_time = delivery_time;
        this.replace = replace;
        this.return1 = return1;
    }

    public int getOffer() {
        return offer;
    }

    public void setOffer(int offer) {
        this.offer = offer;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getReplace() {
        return replace;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }

    public String getReturn1() {
        return return1;
    }

    public void setReturn1(String return1) {
        this.return1 = return1;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
