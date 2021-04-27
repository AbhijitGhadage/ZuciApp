package com.zuci.zuciapp.ui.agoraLive;

import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.FieldValue;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveCallResponse implements Serializable {
    @SerializedName("Registration")
    @Expose
    private RegistrationModel registration;
    @SerializedName("FollowingList")
    @Expose
    private List<FollowingListModel> followingList;
    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("ChannelName")
    @Expose
    private String ChannelName;
    @SerializedName("AccessToken")
    @Expose
    private String AccessToken;
    private String liveId;

    public RegistrationModel getRegistration() {
        return registration;
    }

    public void setRegistration(RegistrationModel registration) {
        this.registration = registration;
    }

    public List<FollowingListModel> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<FollowingListModel> followingList) {
        this.followingList = followingList;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String channelName) {
        ChannelName = channelName;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    @Exclude
    public Map<String, Object> toCreateLiveCallMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("channelName", ChannelName);
        result.put("accessToken", AccessToken);
        result.put("regId", registration.getRegistrationId());
        result.put("userName", registration.getProfileName());
        result.put("userLiveCallCoins", registration.getLiveCallCoins());
        result.put("userProfileImage", registration.getProfileImage());
        result.put("liveStatus", true);
        result.put("liveStartDateTime", FieldValue.serverTimestamp());
        result.put("followingList", followingList);
        result.put("liveId", liveId);
        return result;
    }

    private Map<String, FollowingListModel> getListOfFollowingMap() {
        Map<String, FollowingListModel> listModelMap = new HashMap<>();
        if (followingList != null)
            for (FollowingListModel followingListModel : followingList)
                listModelMap.put(String.valueOf(followingListModel.getFollowingRegistrationID()), followingListModel);
        return listModelMap;
    }

    @Exclude
    public Map<String, Object> toEndLiveCallMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("liveStatus", false);
        result.put("liveEndDateTime", FieldValue.serverTimestamp());
        return result;
    }
}
