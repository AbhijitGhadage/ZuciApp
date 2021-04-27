package com.zuci.zuciapp.ui.reels;

public class VideoAddLikeModel {
    int RegistrationId;
    String UserName;
    int MediaId;
    String MediaName;

    public VideoAddLikeModel() {
    }

    public int getRegistrationId() {
        return RegistrationId;
    }

    public void setRegistrationId(int registrationId) {
        RegistrationId = registrationId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getMediaId() {
        return MediaId;
    }

    public void setMediaId(int mediaId) {
        MediaId = mediaId;
    }

    public String getMediaName() {
        return MediaName;
    }

    public void setMediaName(String mediaName) {
        MediaName = mediaName;
    }
}
