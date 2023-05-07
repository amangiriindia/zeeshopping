package com.example.amzoodmart.models;

public class AddressModel {
    String userAddress;
    String userAddress_detailed;
    String userCity;
    String userCode;
    String userDistict;
    String userName;
    String  userNumber;

    public AddressModel(String userAddress, String userAddress_detailed, String userCity, String userCode, String userDistict, String userName, String userNumber, boolean isSelected) {
        this.userAddress = userAddress;
        this.userAddress_detailed = userAddress_detailed;
        this.userCity = userCity;
        this.userCode = userCode;
        this.userDistict = userDistict;
        this.userName = userName;
        this.userNumber = userNumber;
        this.isSelected = isSelected;
    }

    boolean isSelected;

    public AddressModel() {
    }

    public String getUserAddress_detailed() {
        return userAddress_detailed;
    }

    public void setUserAddress_detailed(String userAddress_detailed) {
        this.userAddress_detailed = userAddress_detailed;
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

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
