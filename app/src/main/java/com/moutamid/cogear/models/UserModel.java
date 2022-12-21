package com.moutamid.cogear.models;

public class UserModel {
    String name, email, password, interests, region, date, city;
    boolean isSubscribe;
    int numberOfEvents;

    public UserModel() {
    }

    public UserModel(String name, String email, String password, String date, int numberOfEvents) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.date = date;
        this.numberOfEvents = numberOfEvents;
    }

    public UserModel(String name, String email, String password, String interests, String region, String date, String city, boolean isSubscribe, int numberOfEvents) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.interests = interests;
        this.region = region;
        this.date = date;
        this.city = city;
        this.isSubscribe = isSubscribe;
        this.numberOfEvents = numberOfEvents;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getNumberOfEvents() {
        return numberOfEvents;
    }

    public void setNumberOfEvents(int numberOfEvents) {
        this.numberOfEvents = numberOfEvents;
    }

    public boolean isSubscribe() {
        return isSubscribe;
    }

    public void setSubscribe(boolean subscribe) {
        isSubscribe = subscribe;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
