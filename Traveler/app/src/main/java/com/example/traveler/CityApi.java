package com.example.traveler;

import android.app.Application;

public class CityApi extends Application {

    private String username;
    private String userId;
    private static CityApi instance;
    private String documentReference;

    public String getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(String documentReference) {
        this.documentReference = documentReference;
    }

    public CityApi(){}

    public static CityApi getInstance(){
        if(instance == null)
            instance = new CityApi();
        return instance;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
