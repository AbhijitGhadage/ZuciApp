package com.zuci.zuciapp.ui.reels;

public class ReelsVideoModel {
    int RegistrationId;
    String UserName;
    String VideoName;
    String Status;
    int MediaId;
    boolean Likes;
    long LikesCount;
    Long TotalView;
    Long SetCoins;
    private String ViewerStatus;

    public ReelsVideoModel() {
    }

    public int getRegistrationId() {
        return RegistrationId;
    }

    public void setRegistrationId(int registrationId) {
        RegistrationId = registrationId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getVideoName() {
        return VideoName;
    }

    public void setVideoName(String videoName) {
        VideoName = videoName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getMediaId() {
        return MediaId;
    }

    public void setMediaId(int mediaId) {
        MediaId = mediaId;
    }

    public boolean isLikes() {
        return Likes;
    }

    public void setLikes(boolean likes) {
        Likes = likes;
    }

    public long getLikesCount() {
        return LikesCount;
    }

    public void setLikesCount(long likesCount) {
        LikesCount = likesCount;
    }

    public Long getTotalView() {
        return TotalView;
    }

    public void setTotalView(Long totalView) {
        TotalView = totalView;
    }

    public Long getSetCoins() {
        return SetCoins;
    }

    public void setSetCoins(Long setCoins) {
        SetCoins = setCoins;
    }

    public String getViewerStatus() {
        return ViewerStatus;
    }

    public void setViewerStatus(String viewerStatus) {
        ViewerStatus = viewerStatus;
    }
}
