package com.example.android.project1;

import java.util.HashMap;
import java.util.Map;

public class Verification {
    private String selfiePhotoUrl;
    private String userId;
    private String verificationId;
    private String comment;
    private boolean verifyCheck;

    public Verification(){

    }

    public Verification(String selfiePhotoUrl, String userId, String verificationId, String comment, boolean verifyCheck) {
        this.selfiePhotoUrl=selfiePhotoUrl ;
        this.userId = userId;
        this.verificationId = verificationId;
        this.comment = comment;
        this.verifyCheck = verifyCheck;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("selfiePhotoUrl", selfiePhotoUrl);
        result.put("userId", userId);
        result.put("verificationId", verificationId);
        result.put("comment", comment);
        result.put("verifyCheck", verifyCheck);
        return result;
    }


    public String getSelfiePhotoUrl() {
        return selfiePhotoUrl;
    }

    public void setSelfiePhotoUrl(String selfiePhotoUrl) {
        this.selfiePhotoUrl = selfiePhotoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isVerifyCheck() {
        return verifyCheck;
    }

    public void setVerifyCheck(boolean verifyCheck) {
        this.verifyCheck = verifyCheck;
    }
}
