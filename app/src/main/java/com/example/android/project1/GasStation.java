package com.example.android.project1;

import java.util.HashMap;
import java.util.Map;

public class GasStation {
    private String id;
    private double latitude;
    private double longitude;
    private String info;
    private String photoURL;
    private String address;
    private String phone;
    private String title;


    public GasStation () {

    }
    public GasStation (String id, double latitude, double longitude, String info, String photoURL, String address , String phone, String title) {
        this.id = id;
        this.longitude = longitude;
        this.info = info;
        this.latitude = latitude;
        this.photoURL = photoURL;
        this.address = address;
        this.phone = phone;
        this.title = title;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("info", info);
        result.put("photoURL", photoURL);
        result.put("address", address);
        result.put("phone", phone);
        result.put("title", title);

        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

