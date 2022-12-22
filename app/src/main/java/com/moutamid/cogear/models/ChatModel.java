package com.moutamid.cogear.models;

public class ChatModel {
    String message;
    String time;
    String userID;
    String image;
    long timestamps;

    public ChatModel() {}

    public ChatModel(String message, String time, String userID, String image, long timestamps) {
        this.message = message;
        this.time = time;
        this.userID = userID;
        this.image = image;
        this.timestamps = timestamps;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(long timestamps) {
        this.timestamps = timestamps;
    }
}
