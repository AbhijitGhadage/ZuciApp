package com.zuci.zuciapp.ui.agoraLive.liveVideo;

public class LiveCallCoinsDeductsModel {
    long deductCoins;
    long AdmindeductCoins;
    long LiveUserId;
    long JoinLiveUserId;
    String DeductionType;
    String DeductionDate;
    String ViewerStatus;

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

    public long getLiveUserId() {
        return LiveUserId;
    }

    public void setLiveUserId(long liveUserId) {
        LiveUserId = liveUserId;
    }

    public long getJoinLiveUserId() {
        return JoinLiveUserId;
    }

    public void setJoinLiveUserId(long joinLiveUserId) {
        JoinLiveUserId = joinLiveUserId;
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
