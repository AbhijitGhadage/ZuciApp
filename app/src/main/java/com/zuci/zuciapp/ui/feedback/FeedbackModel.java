package com.zuci.zuciapp.ui.feedback;

public class FeedbackModel {
    int RegistrationId;
    int StarNo;
    String Message;

    public int getRegistrationId() {
        return RegistrationId;
    }

    public void setRegistrationId(int registrationId) {
        RegistrationId = registrationId;
    }

    public int getStarNo() {
        return StarNo;
    }

    public void setStarNo(int starNo) {
        StarNo = starNo;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
