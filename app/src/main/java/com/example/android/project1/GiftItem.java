package com.example.android.project1;

import java.util.HashMap;
import java.util.Map;

public class GiftItem {
    private String title;
    private String description;
    private String conditions;
    private String picUrl;
    private int price;
    private int validity;
    private String giftItemId;



    public GiftItem(){

    }

    public GiftItem(String title, String description,String conditions, String picUrl, int price, String giftItemId, int validity) {
        this.title = title;
        this.description = description;
        this.conditions = conditions;
        this.picUrl = picUrl;
        this.price = price;
        this.validity = validity;
        this.giftItemId = giftItemId;
    }

    public GiftItem(String title, String picUrl, int price, String giftItemId) {
        this.title = title;
        this.picUrl = picUrl;
        this.price = price;
        this.giftItemId = giftItemId;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("description", description);
        result.put("conditions", conditions);
        result.put("picUrl", picUrl);
        result.put("price", price);
        result.put("validity", validity);
        result.put("giftItemId", giftItemId);


        return result;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int  getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }


    public String getGiftItemId() {
        return giftItemId;
    }

    public void setGiftItemId(String giftItemId) {
        this.giftItemId = giftItemId;
    }
}
