package com.zuci.zuciapp.ui.agoraLive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegistrationModel  implements Serializable {

    @SerializedName("RegistrationId")
    @Expose
    private Integer registrationId;
    @SerializedName("ProfileName")
    @Expose
    private String profileName;
    @SerializedName("NickName")
    @Expose
    private String nickName;
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("EmailId")
    @Expose
    private String emailId;
    @SerializedName("DOB")
    @Expose
    private String dOB;
    @SerializedName("CountryId")
    @Expose
    private Integer countryId;
    @SerializedName("StateId")
    @Expose
    private Integer stateId;
    @SerializedName("CityName")
    @Expose
    private String cityName;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("JoinDate")
    @Expose
    private String joinDate;
    @SerializedName("ProfileImage")
    @Expose
    private String profileImage;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("UserTypeId")
    @Expose
    private Integer userTypeId;
    @SerializedName("StatusId")
    @Expose
    private Integer statusId;
    @SerializedName("TotalPoints")
    @Expose
    private Double totalPoints;
    @SerializedName("IsLive")
    @Expose
    private Boolean isLive;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Lat")
    @Expose
    private String lat;
    @SerializedName("Long")
    @Expose
    private String _long;
    @SerializedName("Bio")
    @Expose
    private String bio;
    @SerializedName("Age")
    @Expose
    private Integer age;
    @SerializedName("RegType")
    @Expose
    private String regType;
    @SerializedName("UniqueRegId")
    @Expose
    private String uniqueRegId;
    @SerializedName("Otp")
    @Expose
    private String otp;
    @SerializedName("DeviceToken")
    @Expose
    private String deviceToken;
    @SerializedName("TotalCoins")
    @Expose
    private String totalCoins;
    @SerializedName("AudioCallCoins")
    @Expose
    private Integer audioCallCoins;
    @SerializedName("VideoCallCoins")
    @Expose
    private Integer videoCallCoins;
    @SerializedName("ImageCoins")
    @Expose
    private Integer imageCoins;
    @SerializedName("VideoCoins")
    @Expose
    private Integer videoCoins;
    @SerializedName("FollowerStatus")
    @Expose
    private Boolean followerStatus;
    @SerializedName("Affiliate")
    @Expose
    private String affiliate;
    @SerializedName("LiveCallCoins")
    @Expose
    private Integer LiveCallCoins;

    public Integer getLiveCallCoins() {
        return LiveCallCoins;
    }

    public void setLiveCallCoins(Integer liveCallCoins) {
        LiveCallCoins = liveCallCoins;
    }

    public Integer getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Integer registrationId) {
        this.registrationId = registrationId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getdOB() {
        return dOB;
    }

    public void setdOB(String dOB) {
        this.dOB = dOB;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Double totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Boolean getLive() {
        return isLive;
    }

    public void setLive(Boolean live) {
        isLive = live;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String get_long() {
        return _long;
    }

    public void set_long(String _long) {
        this._long = _long;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getRegType() {
        return regType;
    }

    public void setRegType(String regType) {
        this.regType = regType;
    }

    public String getUniqueRegId() {
        return uniqueRegId;
    }

    public void setUniqueRegId(String uniqueRegId) {
        this.uniqueRegId = uniqueRegId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(String totalCoins) {
        this.totalCoins = totalCoins;
    }

    public Integer getAudioCallCoins() {
        return audioCallCoins;
    }

    public void setAudioCallCoins(Integer audioCallCoins) {
        this.audioCallCoins = audioCallCoins;
    }

    public Integer getVideoCallCoins() {
        return videoCallCoins;
    }

    public void setVideoCallCoins(Integer videoCallCoins) {
        this.videoCallCoins = videoCallCoins;
    }

    public Integer getImageCoins() {
        return imageCoins;
    }

    public void setImageCoins(Integer imageCoins) {
        this.imageCoins = imageCoins;
    }

    public Integer getVideoCoins() {
        return videoCoins;
    }

    public void setVideoCoins(Integer videoCoins) {
        this.videoCoins = videoCoins;
    }

    public Boolean getFollowerStatus() {
        return followerStatus;
    }

    public void setFollowerStatus(Boolean followerStatus) {
        this.followerStatus = followerStatus;
    }

    public String getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(String affiliate) {
        this.affiliate = affiliate;
    }
}
