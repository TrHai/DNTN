package com.example.bruce.myapp.Data;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class AddMissingLocation {
    public String name;
    public ArrayList<String> urlImage;
    public String address;
    public String describe="";
    public String phone="";

    public AddMissingLocation() {
    }

    public AddMissingLocation(String name, ArrayList<String> urlImage, String address, String describe, String phone) {
        this.name = name;
        this.urlImage = urlImage;
        this.address = address;
        this.describe = describe;
        this.phone = phone;
    }
}
