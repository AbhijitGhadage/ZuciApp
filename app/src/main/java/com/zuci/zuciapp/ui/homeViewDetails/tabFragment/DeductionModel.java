package com.zuci.zuciapp.ui.homeViewDetails.tabFragment;

public class DeductionModel {
    long ViewerRegistrationId;
    long MediaOwnerRegistrationId;
    long MediaId;
    long deductCoins;
    long AdmindeductCoins;
    String DeductionType;
    String DeductionDate;
    String ViewerStatus;

    public DeductionModel() {
    }

    public long getViewerRegistrationId() {
        return ViewerRegistrationId;
    }

    public void setViewerRegistrationId(long viewerRegistrationId) {
        ViewerRegistrationId = viewerRegistrationId;
    }

    public long getMediaOwnerRegistrationId() {
        return MediaOwnerRegistrationId;
    }

    public void setMediaOwnerRegistrationId(long mediaOwnerRegistrationId) {
        MediaOwnerRegistrationId = mediaOwnerRegistrationId;
    }

    public long getMediaId() {
        return MediaId;
    }

    public void setMediaId(long mediaId) {
        MediaId = mediaId;
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
