package com.example.android.project1;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Operation {
    private String name, description, senderPhone;
    private int amount;
    private boolean plus;
    private String id;
    private long time;

    public Operation(){}

    public Operation(String name, boolean plus, int x, String userid ){
        this.name=name;
        this.plus=plus;
        amount=x;
        time=new Date().getTime();
        id=time+userid;
    }

    public Operation(String name, String description, String senderPhone, boolean plus, int x, String userid ){
        this.name=name;
        this.plus=plus;
        this.description = description;
        amount=x;
        time=new Date().getTime();
        id=time+userid;
        this.senderPhone = senderPhone;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("time", time);
        result.put("plus", plus);
        result.put("amount", amount);
        if ((description != null) && (!description.equals(""))) result.put("description", description);
        if ((senderPhone != null) && (!senderPhone.equals(""))) result.put("senderPhone", senderPhone);

        return result;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean getPlus() {
        return plus;
    }

    public void setPlus(boolean plus) {
        this.plus = plus;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }
}

