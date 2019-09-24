package com.example.android.project1;

import java.util.HashMap;
import java.util.Map;

public class VerifyEmail {
    private String email;
    private int code;

    public VerifyEmail(){

    }

    public VerifyEmail(String email, int code) {
        this.email = email;
        this.code = code;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("code", code);
        return result;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
