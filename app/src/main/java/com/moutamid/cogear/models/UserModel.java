package com.moutamid.cogear.models;

public class UserModel {
    String name, email, password, interests, region, date;

    public UserModel() {
    }

    public UserModel(String name, String email, String password, String date) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.date = date;
    }

    public UserModel(String name, String email, String password, String interests, String region) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.interests = interests;
        this.region = region;
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
