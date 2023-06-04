package com.amzsoft.zeeshopping.models;

public class MyCartModel {
    String currentTime;
    String productImgurl;
    String currentDate;
    String productName;
    String productPrice;
    int totalQuantity;
    int totalPrice;
    String documentId;

    public MyCartModel() {
    }

    public MyCartModel(String currentTime, String productImgurl, String currentDate, String productName, String productPrice, int totalQuantity, int totalPrice) {
        this.currentTime = currentTime;
        this.productImgurl = productImgurl;
        this.currentDate = currentDate;
        this.productName = productName;
        this.productPrice = productPrice;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getProductImgurl() {
        return productImgurl;
    }

    public void setProductImgurl(String productImgurl) {
        this.productImgurl = productImgurl;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}

