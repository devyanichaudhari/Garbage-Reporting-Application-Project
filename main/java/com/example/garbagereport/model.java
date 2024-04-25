package com.example.garbagereport;

public class model {
    private String imageuri;
    public model(){

    }
    public model(String imageuri)
    {
        this.imageuri = imageuri;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }
}
