package com.zuci.zuciapp.ui.agoraVoiceCall;

import java.io.Serializable;

public class SenderCallModel implements Serializable {
    private String SenderName;
    private String SenderImage;
    private String SenderDeviceToken;

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getSenderImage() {
        return SenderImage;
    }

    public void setSenderImage(String senderImage) {
        SenderImage = senderImage;
    }

    public String getSenderDeviceToken() {
        return SenderDeviceToken;
    }

    public void setSenderDeviceToken(String senderDeviceToken) {
        SenderDeviceToken = senderDeviceToken;
    }
}
