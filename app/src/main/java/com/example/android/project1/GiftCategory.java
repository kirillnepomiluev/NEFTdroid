package com.example.android.project1;

import java.util.HashMap;
import java.util.Map;

public class GiftCategory {
    private String title;
    private String picture;
    private String categoryId;

    public GiftCategory(){

    }

    public GiftCategory(String title, String picture, String categoryId) {
        this.title = title;
        this.picture = picture;
        this.categoryId = categoryId;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("picture", picture);
        result.put("categoryId", categoryId);

        return result;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
