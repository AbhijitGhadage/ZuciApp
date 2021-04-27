package com.zuci.zuciapp.ui.profileCreate;

import androidx.annotation.NonNull;

public class CountryListModel {
    int CountryId;
    String CountryName;
    String CountryIcon;

    public int getCountryId() {
        return CountryId;
    }

    public void setCountryId(int countryId) {
        CountryId = countryId;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public String getCountryIcon() {
        return CountryIcon;
    }

    public void setCountryIcon(String countryIcon) {
        CountryIcon = countryIcon;
    }

    @NonNull
    @Override
    public String toString() {
        return this.CountryName;
    }
}
