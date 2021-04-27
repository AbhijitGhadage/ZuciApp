package com.zuci.zuciapp.ui.transaction;

public class TransactionLogModel {
    long RegistrationId;
    String Coins;
    long Amount;
    String TransactionId;
    String TotalCoins;
    String TransactionDate;

    long ViewerRegistrationId;
    String DeductionType;
    String DeductionDate;
    String ViewerStatus;
    long MediaId;
    long MediaOwnerRegistrationId;
    long DeductCoins;
    long AdminDeductCoins;
    String ViewerUserName;
    String OwnerUserName;


    public TransactionLogModel() {
    }

    public long getRegistrationId() {
        return RegistrationId;
    }

    public void setRegistrationId(long registrationId) {
        RegistrationId = registrationId;
    }

    public String getCoins() {
        return Coins;
    }

    public void setCoins(String coins) {
        Coins = coins;
    }

    public long getAmount() {
        return Amount;
    }

    public void setAmount(long amount) {
        Amount = amount;
    }

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }

    public String getTotalCoins() {
        return TotalCoins;
    }

    public void setTotalCoins(String totalCoins) {
        TotalCoins = totalCoins;
    }

    public String getTransactionDate() {
        return TransactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        TransactionDate = transactionDate;
    }

    public long getViewerRegistrationId() {
        return ViewerRegistrationId;
    }

    public void setViewerRegistrationId(long viewerRegistrationId) {
        ViewerRegistrationId = viewerRegistrationId;
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

    public long getMediaId() {
        return MediaId;
    }

    public void setMediaId(long mediaId) {
        MediaId = mediaId;
    }

    public long getMediaOwnerRegistrationId() {
        return MediaOwnerRegistrationId;
    }

    public void setMediaOwnerRegistrationId(long mediaOwnerRegistrationId) {
        MediaOwnerRegistrationId = mediaOwnerRegistrationId;
    }

    public long getDeductCoins() {
        return DeductCoins;
    }

    public void setDeductCoins(long deductCoins) {
        DeductCoins = deductCoins;
    }

    public long getAdminDeductCoins() {
        return AdminDeductCoins;
    }

    public void setAdminDeductCoins(long adminDeductCoins) {
        AdminDeductCoins = adminDeductCoins;
    }

    public String getViewerUserName() {
        return ViewerUserName;
    }

    public void setViewerUserName(String viewerUserName) {
        ViewerUserName = viewerUserName;
    }

    public String getOwnerUserName() {
        return OwnerUserName;
    }

    public void setOwnerUserName(String ownerUserName) {
        OwnerUserName = ownerUserName;
    }
}
