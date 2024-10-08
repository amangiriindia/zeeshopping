package com.amzventures.zeeshopping.models;

import java.io.Serializable;

public class NewProductsModel implements Serializable {
    String description;
    String name;
    double rating;
    int price;
    String img_url;
    int delivery;
    String delivery_time;
    String replace;
    String return1;
    int offer;
    String type,productId;
    boolean outOfStock,colorFlag,sizeFlag;

    public NewProductsModel() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public boolean isColorFlag() {
        return colorFlag;
    }

    public void setColorFlag(boolean colorFlag) {
        this.colorFlag = colorFlag;
    }

    public boolean isSizeFlag() {
        return sizeFlag;
    }

    public void setSizeFlag(boolean sizeFlag) {
        this.sizeFlag = sizeFlag;
    }

    public NewProductsModel(String description, String name, double rating, int price, String img_url, int delivery, String delivery_time, String replace, String return1, int offer, String type, boolean outOfStock , boolean colorFlag, boolean sizeFlag, String productId) {
        this.description = description;
        this.name = name;
        this.rating = rating;
        this.price = price;
        this.img_url = img_url;
        this.delivery = delivery;
        this.delivery_time = delivery_time;
        this.replace = replace;
        this.return1 = return1;
        this.offer = offer;
        this.type = type;
        this.outOfStock =outOfStock;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public boolean isOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(boolean outOfStock) {
        this.outOfStock = outOfStock;
    }
}
