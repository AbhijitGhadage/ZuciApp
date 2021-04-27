package com.zuci.zuciapp.ui.firestoreMessageChat.messageChatHistory;

import java.io.Serializable;

public class ChatListModel implements Serializable {
    private String ProfileName;
    private String ProfileImage;
    private long RegistrationId;

    public ChatListModel(String profileName, String profileImage, long registrationId) {
        ProfileName = profileName;
        ProfileImage = profileImage;
        RegistrationId = registrationId;
    }

    public ChatListModel() {
    }

    public String getProfileName() {
        return ProfileName;
    }

    public void setProfileName(String profileName) {
        ProfileName = profileName;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public long getRegistrationId() {
        return RegistrationId;
    }

    public void setRegistrationId(long registrationId) {
        RegistrationId = registrationId;
    }
}
