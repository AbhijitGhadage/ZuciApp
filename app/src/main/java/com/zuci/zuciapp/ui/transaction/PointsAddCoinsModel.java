package com.zuci.zuciapp.ui.transaction;

public class PointsAddCoinsModel {
      int RegistrationId;
      String Coins;
      double Amount;
      String TransactionId;
      String TotalCoins;
      String TransactionDate;

    public PointsAddCoinsModel() {
    }

    public int getRegistrationId() {
        return RegistrationId;
    }

    public void setRegistrationId(int registrationId) {
        RegistrationId = registrationId;
    }

    public String getCoins() {
        return Coins;
    }

    public void setCoins(String coins) {
        Coins = coins;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
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
}
