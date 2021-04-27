package com.zuci.zuciapp.ui.chat.message;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageListModel implements Parcelable {
    String name;

    public MessageListModel(String name) {
        this.name = name;
    }

    protected MessageListModel(Parcel in) {
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageListModel> CREATOR = new Creator<MessageListModel>() {
        @Override
        public MessageListModel createFromParcel(Parcel in) {
            return new MessageListModel(in);
        }

        @Override
        public MessageListModel[] newArray(int size) {
            return new MessageListModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
