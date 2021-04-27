package com.zuci.zuciapp.ui.agoraVoiceCall;

import java.io.Serializable;

public class ReceiverCallModel implements Serializable {
    private String ReceiverName;
    private String ReceiverImage;
    private String ReceiverDeviceToken;

    public String getReceiverName() {
        return ReceiverName;
    }

    public void setReceiverName(String receiverName) {
        ReceiverName = receiverName;
    }

    public String getReceiverImage() {
        return ReceiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        ReceiverImage = receiverImage;
    }

    public String getReceiverDeviceToken() {
        return ReceiverDeviceToken;
    }

    public void setReceiverDeviceToken(String receiverDeviceToken) {
        ReceiverDeviceToken = receiverDeviceToken;
    }
}
