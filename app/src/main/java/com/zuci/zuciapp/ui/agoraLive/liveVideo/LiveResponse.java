package com.zuci.zuciapp.ui.agoraLive.liveVideo;

import com.zuci.zuciapp.ui.agoraLive.FollowingListModel;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class LiveResponse implements Serializable {
    private String channelName;
    private String accessToken;
    private String userName;
    private String userProfileImage;
    List<FollowingListModel> followingList;
    private boolean liveStatus;
    private String liveId;
    private long regId;
    private long userLiveCallCoins;
    @ServerTimestamp
    private Date liveStartDateTime;
    @ServerTimestamp
    private Date liveEndDateTime;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public List<FollowingListModel> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<FollowingListModel> followingList) {
        this.followingList = followingList;
    }

    public boolean isLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(boolean liveStatus) {
        this.liveStatus = liveStatus;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public long getRegId() {
        return regId;
    }

    public void setRegId(long regId) {
        this.regId = regId;
    }


    public Date getLiveStartDateTime() {
        return liveStartDateTime;
    }

    public void setLiveStartDateTime(Date liveStartDateTime) {
        this.liveStartDateTime = liveStartDateTime;
    }

    public Date getLiveEndDateTime() {
        return liveEndDateTime;
    }

    public void setLiveEndDateTime(Date liveEndDateTime) {
        this.liveEndDateTime = liveEndDateTime;
    }

    public long getUserLiveCallCoins() {
        return userLiveCallCoins;
    }

    public void setUserLiveCallCoins(long userLiveCallCoins) {
        this.userLiveCallCoins = userLiveCallCoins;
    }
}
