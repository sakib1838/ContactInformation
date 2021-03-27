package com.example.contactinformation.FirebaseModel;


public class Contacts {
    private String Name;
    private String Phone;
    private String ImageUrl;
    private Boolean Favourite;

    public Contacts(String name, String phone, String imageUrl, Boolean favourite) {
        Name = name;
        Phone = phone;
        ImageUrl = imageUrl;
        Favourite = favourite;
    }

    public Contacts(){

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public Boolean getFavourite() {
        return Favourite;
    }

    public void setFavourite(Boolean favourite) {
        Favourite = favourite;
    }
}
