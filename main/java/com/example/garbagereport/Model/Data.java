package com.example.garbagereport.Model;

public class Data{
        private String location,email;
        public Data()
        {

        }
    public Data(String location,String email)
    {
        this.location = location;
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}