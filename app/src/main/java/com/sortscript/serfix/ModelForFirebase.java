package com.sortscript.serfix;


import com.google.gson.annotations.SerializedName;

public class ModelForFirebase {
    String Address, City,UserType,Email,Password,PhoneNumber,Private,ServiceType,UID,UserName,Active_user;
    double Latitude,Longitude;
    public ModelForFirebase() {
    }

    public ModelForFirebase(String address, String city, String usertype, String email,
                            String password, String phoneNumber, String aPrivate,
                            String serviceType, String UID, String userName,
                            String active_user, double latitude, double longitude) {
        Address = address;
        City = city;
        UserType = usertype;
        Email = email;
        Password = password;
        PhoneNumber = phoneNumber;
        Private = aPrivate;
        ServiceType = serviceType;
        this.UID = UID;
        UserName = userName;
        Active_user = active_user;
        Latitude = latitude;
        Longitude = longitude;
    }

    public ModelForFirebase(String address, String phoneNumber, String userName) {
        Address = address;
        PhoneNumber = phoneNumber;
        UserName = userName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String usertype) {
            UserType = usertype;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getPrivate() {
        return Private;
    }

    public void setPrivate(String aPrivate) {
        Private = aPrivate;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getActive_user() {
        return Active_user;
    }

    public void setActive_user(String active_user) {
        Active_user = active_user;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
