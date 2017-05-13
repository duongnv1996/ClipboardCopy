package com.duongkk.clipboardcopypro.models;

/**
 * Created by MyPC on 8/23/2016.
 */
public class User {
    String email;
    String userID;
    Message message;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
