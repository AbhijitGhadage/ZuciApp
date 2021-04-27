package com.zuci.zuciapp.ui.agoraVoiceCall;

public class VideoAudioCutCoinsModel {
    long CallerRegistrationId;
    long PickUpUserId;
    long deductCoins;
    long AdmindeductCoins;
    String DeductionType;
    String DeductionDate;
    String ViewerStatus;

    public long getCallerRegistrationId() {
        return CallerRegistrationId;
    }

    public void setCallerRegistrationId(long callerRegistrationId) {
        CallerRegistrationId = callerRegistrationId;
    }

    public long getPickUpUserId() {
        return PickUpUserId;
    }

    public void setPickUpUserId(long pickUpUserId) {
        PickUpUserId = pickUpUserId;
    }

    public long getDeductCoins() {
        return deductCoins;
    }

    public void setDeductCoins(long deductCoins) {
        this.deductCoins = deductCoins;
    }

    public long getAdmindeductCoins() {
        return AdmindeductCoins;
    }

    public void setAdmindeductCoins(long admindeductCoins) {
        AdmindeductCoins = admindeductCoins;
    }

    public String getDeductionType() {
        return DeductionType;
    }

    public void setDeductionType(String deductionType) {
        DeductionType = deductionType;
    }

    public String getDeductionDate() {
        return DeductionDate;
    }

    public void setDeductionDate(String deductionDate) {
        DeductionDate = deductionDate;
    }

    public String getViewerStatus() {
        return ViewerStatus;
    }

    public void setViewerStatus(String viewerStatus) {
        ViewerStatus = viewerStatus;
    }
}
