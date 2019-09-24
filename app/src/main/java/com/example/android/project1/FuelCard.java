package com.example.android.project1;

import java.util.HashMap;
import java.util.Map;

public class FuelCard {
    private String userId;
    private String cardId;
    private String userAddress;
    private String dateofOrder;

    public FuelCard(){

    }

    public FuelCard(String userId, String cardId, String userAddress, String dateofOrder) {
        this.userId = userId;
        this.cardId = cardId;
        this.userAddress = userAddress;
        this.dateofOrder = dateofOrder;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("cardId", cardId);
        result.put("userAddress", userAddress);
        result.put("dateofOrder", dateofOrder);

        return result;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getDateofOrder() {
        return dateofOrder;
    }

    public void setDateofOrder(String dateofOrder) {
        this.dateofOrder = dateofOrder;
    }

}
