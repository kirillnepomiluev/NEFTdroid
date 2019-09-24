package com.example.android.project1;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message {
    private String sendorId;
    private String message;
    private long time;
    private String type;
    private String id;

    public Message() {}

    public Message(String sendorId, String message, String type) {
        this.sendorId = sendorId;
        this.message = message;
        this.time = new Date().getTime();
        this.id = this.time+sendorId;
        this.type  = type;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("sendorId", sendorId);
        result.put("message", message);
        result.put("time", time);
        result.put("id", id);
        result.put("type", type);

        return result;
    }



    public String getSendorId() {
        return sendorId;
    }

    public void setSendorId(String sendorId) {
        this.sendorId = sendorId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
