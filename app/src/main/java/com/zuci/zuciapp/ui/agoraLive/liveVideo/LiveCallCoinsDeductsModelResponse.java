package com.zuci.zuciapp.ui.agoraLive.liveVideo;

public class LiveCallCoinsDeductsModelResponse {
    boolean status;
    String message;
    long statusCode;
    long TotalCoins;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(long statusCode) {
        this.statusCode = statusCode;
    }

    public long getTotalCoins() {
        return TotalCoins;
    }

    public void setTotalCoins(long totalCoins) {
        TotalCoins = totalCoins;
    }
}
