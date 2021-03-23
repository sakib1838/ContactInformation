package com.example.contactinformation.FirebaseModel;

public class Users {

    private String PhoneNumber;
    private String UserName;
    private String Password;
    private String Role;


    public Users(String phoneNumber, String userName, String password, String role) {
        PhoneNumber = phoneNumber;
        UserName = userName;
        Password = password;
        Role = role;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
