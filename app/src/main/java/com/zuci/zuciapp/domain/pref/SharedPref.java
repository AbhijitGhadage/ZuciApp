package com.zuci.zuciapp.domain.pref;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import static android.content.Context.MODE_PRIVATE;

@Singleton
public class SharedPref {

    private static String PREF_NAME = "ZuciApp";
    private String ISLOGIN = "ISLOGIN";
    private String UID = "UID";
    private String REGISTER_ID = "REGISTER_ID";
    private String REGISTER_TYPE = "REGISTER_TYPE";
    private String USER_NAME = "USER_FULL_NAME";
    private String USER_EMAIL = "USER_EMAIL";
    private String USER_PHONE = "PHONE";
    private String PROFILE_IMAGE = "PROFILE_IMAGE";
    private String USER_GENDER = "USER_GENDER";
    private String TOKEN = "TOKEN";

    private String AGE = "AGE";
    private String DOB = "DOB";
    private String BIO = "BIO";
    private String AFFILIATE = "AFFILIATE";
    private String ADDRESS = "ADDRESS";
    private String COUNTRY = "COUNTRY";
    private String TOTAL_COINS = "TOTAL_COINS";

    private String LIVE_CALL = "LIVE_CALL";
    private String AUDIO_CALL = "AUDIO_CALL";
    private String VIDEO_CALL = "VIDEO_CALL";
    private String IMAGE_RATE = "IMAGE_RATE";
    private String VIDEO_RATE = "VIDEO_RATE";

    private String AMOUNT = "AMOUNT";
    private String AMOUNT_COINS = "AMOUNT_COINS";

    private String FOLLOWERS = "FOLLOWERS";
    private String FOLLOWING = "FOLLOWING";
    private String POST_COUNT = "POST_COUNT";

    private Context mContext;

    @Inject
    public SharedPref(Context mContext) {
        this.mContext = mContext;
    }

    public void setRegisterId(Integer registerId) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putInt(REGISTER_ID, registerId);
        editor.apply();
    }

    public Integer getRegisterId() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getInt(REGISTER_ID, 0);
    }

    public void setLogin(String isLogin) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(ISLOGIN, isLogin);
        editor.apply();
    }

    public String getLogin() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(ISLOGIN, "false");
    }

    public void setUserName(String userName) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(USER_NAME, userName);
        editor.apply();
    }

    public String getUserName() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, "");
    }

    public void setUserEmail(String email) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(USER_EMAIL, email);
        editor.apply();
    }

    public String getUserEmail() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(USER_EMAIL, "");
    }

    public void setUserPhone(String phone) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(USER_PHONE, phone);
        editor.apply();
    }

    public String getUserPhone() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(USER_PHONE, "");
    }

    public void setUserProfile(String profile) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(PROFILE_IMAGE, profile);
        editor.apply();
    }

    public String getUserProfile() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(PROFILE_IMAGE, "");
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN, "");
    }

    public void setGender(String gender) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(USER_GENDER, gender);
        editor.apply();
    }

    public String getGender() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(USER_GENDER, "");
    }

    public void setAge(String age) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(AGE, age);
        editor.apply();
    }

    public String getAge() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(AGE, "");
    }

    public void setDob(String Dob) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(DOB, Dob);
        editor.apply();
    }

    public String getDob() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(DOB, "");
    }

    public void setBio(String bio) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(BIO, bio);
        editor.apply();
    }

    public String getBio() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(BIO, "");
    }

    public void setAffiliate(String Affiliate) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(AFFILIATE, Affiliate);
        editor.apply();
    }

    public String getAffiliate() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(AFFILIATE, "");
    }

    public void setAddress(String Address) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(ADDRESS, Address);
        editor.apply();
    }

    public String getAddress() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(ADDRESS, "");
    }

    public void setRegisterType(String registerType) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(REGISTER_TYPE, registerType);
        editor.apply();
    }

    public String getRegisterType() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(REGISTER_TYPE, "");
    }

    public void setCountry(int Country) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putInt(COUNTRY, Country);
        editor.apply();
    }

    public int getCountry() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getInt(COUNTRY, 0);
    }

    public void setTotalCoins(String totalCoins) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(TOTAL_COINS, totalCoins);
        editor.apply();
    }

    public String getTotalCoins() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(TOTAL_COINS, "");
    }

    public void setLiveCall(Integer liveCall) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putInt(LIVE_CALL, liveCall);
        editor.apply();
    }

    public Integer getLiveCall() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getInt(LIVE_CALL, 0);
    }

    public void setAudioCall(Integer audioCall) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putInt(AUDIO_CALL, audioCall);
        editor.apply();
    }

    public Integer getAudioCall() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getInt(AUDIO_CALL, 0);
    }

    public void setVideoCall(Integer videoCall) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putInt(VIDEO_CALL, videoCall);
        editor.apply();
    }

    public Integer getVideoCall() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getInt(VIDEO_CALL, 0);
    }

    public void setImageRate(Integer imageRate) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putInt(IMAGE_RATE, imageRate);
        editor.apply();
    }

    public Integer getImageRate() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getInt(IMAGE_RATE, 0);
    }

    public void setVideoRate(Integer videoRate) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putInt(VIDEO_RATE, videoRate);
        editor.apply();
    }

    public Integer getVideoRate() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getInt(VIDEO_RATE, 0);
    }

    public void setAmount(String videoRate) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(AMOUNT, videoRate);
        editor.apply();
    }

    public String getAmount() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(AMOUNT, "");
    }

    public void setAmountCoins(String amt) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(AMOUNT_COINS, amt);
        editor.apply();
    }

    public String getAmountCoins() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(AMOUNT_COINS, "");
    }

    public void setFollowers(long followers) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putLong(FOLLOWERS, followers);
        editor.apply();
    }

    public long getFollowers() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getLong(FOLLOWERS, 0);
    }

    public void setFollowing(long Following) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putLong(FOLLOWING, Following);
        editor.apply();
    }

    public long getFollowing() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getLong(FOLLOWING, 0);
    }

    public void setPostCount(long PostCount) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putLong(POST_COUNT, PostCount);
        editor.apply();
    }

    public long getPostCount() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getLong(POST_COUNT, 0);
    }
}
