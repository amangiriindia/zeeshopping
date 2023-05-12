package com.example.amzoodmart.models;

import java.io.Serializable;

public class MyOrderModel {
    String Method,currentDate,currentTime,orderId,productDesc,productImgUrl,productName,productPrice,orderStatus;
    String userCity,userCode,userDistict,userName,userNumber,productQuantity,userAddress_detailed;


    public MyOrderModel() {
    }

    public MyOrderModel(String method, String currentDate, String currentTime, String orderId, String productDesc, String productImgUrl,
                        String productName, String productPrice, String userCity, String userCode, String userDistict,
                        String userName, String userNumber,String productQuantity,String userAddress_detailed,String orderStatus) {
        this.Method = method;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.orderId = orderId;
        this.productDesc = productDesc;
        this.productImgUrl = productImgUrl;
        this.productName = productName;
        this.productPrice = productPrice;
        this.userCity = userCity;
        this.userCode = userCode;
        this.userDistict = userDistict;
        this.userName = userName;
        this.userNumber = userNumber;
        this.productQuantity =productQuantity;
        this.userAddress_detailed =userAddress_detailed;
        this.orderStatus =orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getUserAddress_detailed() {
        return userAddress_detailed;
    }

    public void setUserAddress_detailed(String userAddress_detailed) {
        this.userAddress_detailed = userAddress_detailed;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductImgUrl() {
        return productImgUrl;
    }

    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
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

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserDistict() {
        return userDistict;
    }

    public void setUserDistict(String userDistict) {
        this.userDistict = userDistict;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }
    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }
}
