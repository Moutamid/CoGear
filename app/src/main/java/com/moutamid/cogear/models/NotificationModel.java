package com.moutamid.cogear.models;

public class NotificationModel {
    String userID, ID, name, eventName;
    long timestamps;

    public NotificationModel() {
    }

    public NotificationModel(String userID, String ID, String name, String eventName, long timestamps) {
        this.userID = userID;
        this.ID = ID;
        this.name = name;
        this.eventName = eventName;
        this.timestamps = timestamps;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public long getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(long timestamps) {
        this.timestamps = timestamps;
    }
}
