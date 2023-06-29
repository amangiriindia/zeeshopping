package com.amzsoft.zeeshopping.models;

public class MyOrderModel {
    String Method,currentDate,orderId,productImgUrl,productName,productPrice;
    String productQuantity, documentId;


    public MyOrderModel() {
    }


    public MyOrderModel(String method, String currentDate, String orderId,  String productImgUrl, String productName, String productPrice,   String productQuantity,  String documentId) {
       this.Method = method;
        this.currentDate = currentDate;
        this.orderId = orderId;
        this.productImgUrl = productImgUrl;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.documentId = documentId;

    }



    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }




    public String getCurrentDate() {
        return currentDate;
    }



    public String getOrderId() {
        return orderId;
    }




    public String getProductImgUrl() {
        return productImgUrl;
    }


    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }


    public String getProductQuantity() {
        return productQuantity;
    }

}
