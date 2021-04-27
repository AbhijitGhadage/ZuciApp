package com.zuci.zuciapp.ui.home;

public class CoinsResponse {
    private long RegistrationId;
    private String TotalCoins;
    private boolean status;
    private int statusCode;
    private long AudioCallCoins;
    private long VideoCallCoins;
    private long ImageCoins;
    private long VideoCoins;

    public long getRegistrationId() {
        return RegistrationId;
    }

    public void setRegistrationId(long registrationId) {
        RegistrationId = registrationId;
    }

    public String getTotalCoins() {
        return TotalCoins;
    }

    public void setTotalCoins(String totalCoins) {
        TotalCoins = totalCoins;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public long getAudioCallCoins() {
        return AudioCallCoins;
    }

    public void setAudioCallCoins(long audioCallCoins) {
        AudioCallCoins = audioCallCoins;
    }

    public long getVideoCallCoins() {
        return VideoCallCoins;
    }

    public void setVideoCallCoins(long videoCallCoins) {
        VideoCallCoins = videoCallCoins;
    }

    public long getImageCoins() {
        return ImageCoins;
    }

    public void setImageCoins(long imageCoins) {
        ImageCoins = imageCoins;
    }

    public long getVideoCoins() {
        return VideoCoins;
    }

    public void setVideoCoins(long videoCoins) {
        VideoCoins = videoCoins;
    }
}
