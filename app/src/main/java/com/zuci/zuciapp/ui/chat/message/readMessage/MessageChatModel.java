package com.zuci.zuciapp.ui.chat.message.readMessage;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageChatModel implements Parcelable {
    String name;

    public MessageChatModel(String name) {
        this.name = name;
    }

    protected MessageChatModel(Parcel in) {
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

    public static final Creator<MessageChatModel> CREATOR = new Creator<MessageChatModel>() {
        @Override
        public MessageChatModel createFromParcel(Parcel in) {
            return new MessageChatModel(in);
        }

        @Override
        public MessageChatModel[] newArray(int size) {
            return new MessageChatModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
