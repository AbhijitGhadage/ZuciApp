package com.zuci.zuciapp.ui.firestoreMessageChat.messageChatHistory;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatModel implements Serializable {
    private long senderId;
    private long receiverId;
    private String receiverName;
    private String receiverProfile;
    private String message;
    private String chatId;
    @ServerTimestamp
    private Date createdDateTime;

    public String getReceiverProfile() {
        return receiverProfile;
    }

    public void setReceiverProfile(String receiverProfile) {
        this.receiverProfile = receiverProfile;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Map<String, Object> toChatMap() {
        HashMap<String, Object> chatMap = new HashMap<>();
        chatMap.put("senderId", senderId);
        chatMap.put("receiverId", receiverId);
        chatMap.put("receiverName", receiverName);
        chatMap.put("receiverProfile", receiverProfile);
        chatMap.put("receiverStatus", false);
        chatMap.put("senderStatus", false);
        chatMap.put("message", message);
        chatMap.put("chatId", chatId);
        chatMap.put("createdDateTime", FieldValue.serverTimestamp());
        return chatMap;
    }
}
