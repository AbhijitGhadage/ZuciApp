package com.zuci.zuciapp.firebase.notification;

public class BodyNotificationModel {
    String callType;

    public BodyNotificationModel(String callType) {
        this.callType = callType;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }
}
