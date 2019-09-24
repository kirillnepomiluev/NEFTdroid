package com.example.android.project1;

import java.util.HashMap;
import java.util.Map;

public class Present {
    private String title;
    private String sendorId;
    private String receiverId;
    private String giftItemId;
    private String sendorName;
    private String receiverName;
    private String wishes;
    private String time;
    private String validityDate;
    private boolean agree;
    private String presentId;
    private String picUrl;
    private String conditions;

    public Present(){

    }

    public Present(String title, String sendorId, String receiverId, String giftItemId,String sendorName, String receiverName, String wishes, String time, String validityDate, boolean agree, String presentId, String picUrl, String conditions) {
        this.title = title;
        this.sendorId = sendorId;
        this.receiverId = receiverId;
        this.giftItemId = giftItemId;
        this.sendorName = sendorName;
        this.receiverName = receiverName;
        this.wishes = wishes;
        this.time = time;
        this.validityDate = validityDate;
        this.agree = agree;
        this.presentId = presentId;
        this.picUrl = picUrl;
        this.conditions = conditions;

    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("sendorId", sendorId);
        result.put("receiverId", receiverId);
        result.put("giftItemId", giftItemId);
        result.put("sendorName", sendorName);
        result.put("receiverName", receiverName);
        result.put("wishes", wishes);
        result.put("time", time);
        result.put("validityDate", validityDate);
        result.put("agree", agree);
        result.put("presentId", presentId);
        result.put("picUrl", picUrl);
        result.put("conditions", conditions);


        return result;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSendorId() {
        return sendorId;
    }

    public void setSendorId(String sendorId) {
        this.sendorId = sendorId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getGiftItemId() {
        return giftItemId;
    }

    public void setGiftItemId(String giftItemId) {
        this.giftItemId = giftItemId;
    }

    public String getWishes() {
        return wishes;
    }

    public void setWishes(String wishes) {
        this.wishes = wishes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(String validityDate) {
        this.validityDate = validityDate;
    }

    public boolean isAgree() {
        return agree;
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }

    public String getPresentId() {
        return presentId;
    }

    public void setPresentId(String presentId) {
        this.presentId = presentId;
    }

    public String getSendorName() {
        return sendorName;
    }

    public void setSendorName(String sendorName) {
        this.sendorName = sendorName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
}
