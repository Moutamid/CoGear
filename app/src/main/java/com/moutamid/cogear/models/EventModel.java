package com.moutamid.cogear.models;

public class EventModel {
    String ID, userID, title, description, category, city, image;
    int members;

    public EventModel(){}

    public EventModel(String ID, String userID, String title, String description, String category, String city, String image, int members) {
        this.ID = ID;
        this.userID = userID;
        this.title = title;
        this.description = description;
        this.category = category;
        this.city = city;
        this.image = image;
        this.members = members;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }
}
