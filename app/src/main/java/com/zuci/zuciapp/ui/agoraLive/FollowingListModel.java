package com.zuci.zuciapp.ui.agoraLive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FollowingListModel implements Serializable {
    @SerializedName("FollowingRegistrationID")
    @Expose
    private Integer followingRegistrationID;
    @SerializedName("FollowingName")
    @Expose
    private String followingName;
    @SerializedName("FollowingImage")
    @Expose
    private String followingImage;
 /*   @SerializedName("LiveCallCoins")
    @Expose
    private long LiveCallCoins;*/

    public Integer getFollowingRegistrationID() {
        return followingRegistrationID;
    }

    public void setFollowingRegistrationID(Integer followingRegistrationID) {
        this.followingRegistrationID = followingRegistrationID;
    }

    public String getFollowingName() {
        return followingName;
    }

    public void setFollowingName(String followingName) {
        this.followingName = followingName;
    }

    public String getFollowingImage() {
        return followingImage;
    }

    public void setFollowingImage(String followingImage) {
        this.followingImage = followingImage;
    }

}
