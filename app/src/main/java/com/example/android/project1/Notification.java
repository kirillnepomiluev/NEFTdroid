package com.example.android.project1;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Notification {
    private String name;
    private String descr;
    private String id;
    private long time;
    private boolean read = false;
    private Calendar calendar;

    public Notification() {
    }

    public Notification(String name, String userid) {
        this.name = name;
        calendar = Calendar.getInstance();
        time =  calendar.getTimeInMillis();
        id = time + userid;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Notification(String name, String descr, String id) {
        this.name = name;
        this.descr = descr;
        calendar = Calendar.getInstance();
        time =  calendar.getTimeInMillis();
        id = time + id;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("time", time);
        result.put("read", read);
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean getRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
