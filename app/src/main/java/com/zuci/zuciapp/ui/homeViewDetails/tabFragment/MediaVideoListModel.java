package com.zuci.zuciapp.ui.homeViewDetails.tabFragment;

import android.os.Parcel;
import android.os.Parcelable;

public class MediaVideoListModel implements Parcelable{
    private long RegistrationId;
    private long Id;
    private String Title;
    private String Name;
    private String MediaType;
    private String Status;
    private String LikesCount;
    private String ViewerStatus;
    private long ImageCoins;
    private long VideoCoins;
    private long TotalView;
    private long SetCoins;

    protected MediaVideoListModel(Parcel in) {
        RegistrationId = in.readLong();
        Id = in.readLong();
        Title = in.readString();
        Name = in.readString();
        MediaType = in.readString();
        Status = in.readString();
        LikesCount = in.readString();
        ViewerStatus = in.readString();
        ImageCoins = in.readLong();
        VideoCoins = in.readLong();
        TotalView = in.readLong();
        SetCoins = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(RegistrationId);
        dest.writeLong(Id);
        dest.writeString(Title);
        dest.writeString(Name);
        dest.writeString(MediaType);
        dest.writeString(Status);
        dest.writeString(LikesCount);
        dest.writeString(ViewerStatus);
        dest.writeLong(ImageCoins);
        dest.writeLong(VideoCoins);
        dest.writeLong(TotalView);
        dest.writeLong(SetCoins);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MediaVideoListModel> CREATOR = new Creator<MediaVideoListModel>() {
        @Override
        public MediaVideoListModel createFromParcel(Parcel in) {
            return new MediaVideoListModel(in);
        }

        @Override
        public MediaVideoListModel[] newArray(int size) {
            return new MediaVideoListModel[size];
        }
    };

    public long getRegistrationId() {
        return RegistrationId;
    }

    public void setRegistrationId(long registrationId) {
        RegistrationId = registrationId;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMediaType() {
        return MediaType;
    }

    public void setMediaType(String mediaType) {
        MediaType = mediaType;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getLikesCount() {
        return LikesCount;
    }

    public void setLikesCount(String likesCount) {
        LikesCount = likesCount;
    }

    public String getViewerStatus() {
        return ViewerStatus;
    }

    public void setViewerStatus(String viewerStatus) {
        ViewerStatus = viewerStatus;
    }

    public long getImageCoins() {
        return ImageCoins;
    }

    public void setImageCoins(long imageCoins) {
        ImageCoins = imageCoins;
    }

    public long getVideoCoins() {
        return VideoCoins;
    }

    public void setVideoCoins(long videoCoins) {
        VideoCoins = videoCoins;
    }

    public long getTotalView() {
        return TotalView;
    }

    public void setTotalView(long totalView) {
        TotalView = totalView;
    }

    public long getSetCoins() {
        return SetCoins;
    }

    public void setSetCoins(long setCoins) {
        SetCoins = setCoins;
    }
}
