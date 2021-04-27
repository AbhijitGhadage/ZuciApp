package com.zuci.zuciapp.ui.home;

import android.os.Parcel;
import android.os.Parcelable;

public class HomeListModel implements Parcelable {
    String Address;
    String CityName;
    String CountryId;
    String DOB;
    String EmailId;
    String Gender;
    boolean IsLive;
    String JoinDate;
    String Lat;
    String Long;
    String MobileNo;
    String NickName;
    String Password;
    String ProfileImage;
    String ProfileName;
    Integer RegistrationId;
    Integer StateId;
    boolean Status;
    Integer StatusId;
    double TotalPoints;
    String UserName;
    Integer UserTypeId;
    boolean FollowerStatus;

    String Bio;
    Integer Age;
    String DeviceToken;
    String Affiliate;

    public HomeListModel() {
    }

    protected HomeListModel(Parcel in) {
        Address = in.readString();
        CityName = in.readString();
        CountryId = in.readString();
        DOB = in.readString();
        EmailId = in.readString();
        Gender = in.readString();
        IsLive = in.readByte() != 0;
        JoinDate = in.readString();
        Lat = in.readString();
        Long = in.readString();
        MobileNo = in.readString();
        NickName = in.readString();
        Password = in.readString();
        ProfileImage = in.readString();
        ProfileName = in.readString();
        if (in.readByte() == 0) {
            RegistrationId = null;
        } else {
            RegistrationId = in.readInt();
        }
        if (in.readByte() == 0) {
            StateId = null;
        } else {
            StateId = in.readInt();
        }
        Status = in.readByte() != 0;
        if (in.readByte() == 0) {
            StatusId = null;
        } else {
            StatusId = in.readInt();
        }
        TotalPoints = in.readDouble();
        UserName = in.readString();
        if (in.readByte() == 0) {
            UserTypeId = null;
        } else {
            UserTypeId = in.readInt();
        }
        FollowerStatus = in.readByte() != 0;
        Bio = in.readString();
        if (in.readByte() == 0) {
            Age = null;
        } else {
            Age = in.readInt();
        }
        DeviceToken = in.readString();
        Affiliate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Address);
        dest.writeString(CityName);
        dest.writeString(CountryId);
        dest.writeString(DOB);
        dest.writeString(EmailId);
        dest.writeString(Gender);
        dest.writeByte((byte) (IsLive ? 1 : 0));
        dest.writeString(JoinDate);
        dest.writeString(Lat);
        dest.writeString(Long);
        dest.writeString(MobileNo);
        dest.writeString(NickName);
        dest.writeString(Password);
        dest.writeString(ProfileImage);
        dest.writeString(ProfileName);
        if (RegistrationId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(RegistrationId);
        }
        if (StateId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(StateId);
        }
        dest.writeByte((byte) (Status ? 1 : 0));
        if (StatusId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(StatusId);
        }
        dest.writeDouble(TotalPoints);
        dest.writeString(UserName);
        if (UserTypeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(UserTypeId);
        }
        dest.writeByte((byte) (FollowerStatus ? 1 : 0));
        dest.writeString(Bio);
        if (Age == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Age);
        }
        dest.writeString(DeviceToken);
        dest.writeString(Affiliate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HomeListModel> CREATOR = new Creator<HomeListModel>() {
        @Override
        public HomeListModel createFromParcel(Parcel in) {
            return new HomeListModel(in);
        }

        @Override
        public HomeListModel[] newArray(int size) {
            return new HomeListModel[size];
        }
    };

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getCountryId() {
        return CountryId;
    }

    public void setCountryId(String countryId) {
        CountryId = countryId;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public boolean isLive() {
        return IsLive;
    }

    public void setLive(boolean live) {
        IsLive = live;
    }

    public String getJoinDate() {
        return JoinDate;
    }

    public void setJoinDate(String joinDate) {
        JoinDate = joinDate;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getProfileName() {
        return ProfileName;
    }

    public void setProfileName(String profileName) {
        ProfileName = profileName;
    }

    public Integer getRegistrationId() {
        return RegistrationId;
    }

    public void setRegistrationId(Integer registrationId) {
        RegistrationId = registrationId;
    }

    public Integer getStateId() {
        return StateId;
    }

    public void setStateId(Integer stateId) {
        StateId = stateId;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public Integer getStatusId() {
        return StatusId;
    }

    public void setStatusId(Integer statusId) {
        StatusId = statusId;
    }

    public double getTotalPoints() {
        return TotalPoints;
    }

    public void setTotalPoints(double totalPoints) {
        TotalPoints = totalPoints;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public Integer getUserTypeId() {
        return UserTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        UserTypeId = userTypeId;
    }

    public boolean isFollowerStatus() {
        return FollowerStatus;
    }

    public void setFollowerStatus(boolean followerStatus) {
        FollowerStatus = followerStatus;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public Integer getAge() {
        return Age;
    }

    public void setAge(Integer age) {
        Age = age;
    }

    public String getDeviceToken() {
        return DeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        DeviceToken = deviceToken;
    }

    public String getAffiliate() {
        return Affiliate;
    }

    public void setAffiliate(String affiliate) {
        Affiliate = affiliate;
    }
}
