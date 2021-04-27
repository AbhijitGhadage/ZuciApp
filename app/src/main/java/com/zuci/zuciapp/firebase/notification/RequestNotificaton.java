package com.zuci.zuciapp.firebase.notification;

import com.google.gson.annotations.SerializedName;

public class RequestNotificaton {
    @SerializedName("to") //  "to" changed to token
    private String token;

    @SerializedName("notification")
    private SendNotificationModel sendNotificationModel;

    @SerializedName("data")
    private BodyNotificationModel bodyNotificationModel;

    public SendNotificationModel getSendNotificationModel() {
        return sendNotificationModel;
    }

    public void setSendNotificationModel(SendNotificationModel sendNotificationModel) {
        this.sendNotificationModel = sendNotificationModel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BodyNotificationModel getBodyNotificationModel() {
        return bodyNotificationModel;
    }

    public void setBodyNotificationModel(BodyNotificationModel bodyNotificationModel) {
        this.bodyNotificationModel = bodyNotificationModel;
    }
}
