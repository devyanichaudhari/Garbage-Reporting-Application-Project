package com.example.garbagereport;

public class Details
{
    String location;
    String complaint;
    public Details(String location, String complaint) {
        this.location = location;
        this.complaint = complaint;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getLocation() {
        return location;
    }

    public String getComplaint() {
        return complaint;
    }
}
