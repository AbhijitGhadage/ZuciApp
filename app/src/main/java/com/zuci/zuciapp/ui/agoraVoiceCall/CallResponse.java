package com.zuci.zuciapp.ui.agoraVoiceCall;

import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallResponse implements Serializable {
    private String ChannelName;
    private String AccessToken;
    private String CallStatus;
    private String CallType;
    private long SenderUserId;
    private long ReceiverUserId;
    private long CallId;
    private String firebaseCallId;
    private ReceiverCallModel ReceiverDetails;
    private SenderCallModel SenderDetails;
    private String status;
    @ServerTimestamp
    private Date callDateTime;
    private String callSummary;
    private List<Long> callRoomList;

    private long AudioCallCoins;
    private long VideoCallCoins;
//    private long TotalCoins;

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

    public String getCallStatus() {
        return CallStatus;
    }

    public void setCallStatus(String callStatus) {
        CallStatus = callStatus;
    }

    public String getCallType() {
        return CallType;
    }

    public void setCallType(String callType) {
        CallType = callType;
    }

    public long getSenderUserId() {
        return SenderUserId;
    }

    public void setSenderUserId(long senderUserId) {
        SenderUserId = senderUserId;
    }

    public long getReceiverUserId() {
        return ReceiverUserId;
    }

    public void setReceiverUserId(long receiverUserId) {
        ReceiverUserId = receiverUserId;
    }

    public long getCallId() {
        return CallId;
    }

    public void setCallId(long callId) {
        CallId = callId;
    }

    public String getFirebaseCallId() {
        return firebaseCallId;
    }

    public void setFirebaseCallId(String firebaseCallId) {
        this.firebaseCallId = firebaseCallId;
    }

    public ReceiverCallModel getReceiverDetails() {
        return ReceiverDetails;
    }

    public void setReceiverDetails(ReceiverCallModel receiverDetails) {
        ReceiverDetails = receiverDetails;
    }

    public SenderCallModel getSenderDetails() {
        return SenderDetails;
    }

    public void setSenderDetails(SenderCallModel senderDetails) {
        SenderDetails = senderDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCallDateTime() {
        return callDateTime;
    }

    public void setCallDateTime(Date callDateTime) {
        this.callDateTime = callDateTime;
    }

    public String getCallSummary() {
        return callSummary;
    }

    public void setCallSummary(String callSummary) {
        this.callSummary = callSummary;
    }

    public List<Long> getCallRoomList() {
        return callRoomList;
    }

    public void setCallRoomList(List<Long> callRoomList) {
        this.callRoomList = callRoomList;
    }

    public long getAudioCallCoins() {
        return AudioCallCoins;
    }

    public void setAudioCallCoins(long audioCallCoins) {
        AudioCallCoins = audioCallCoins;
    }

    public long getVideoCallCoins() {
        return VideoCallCoins;
    }

    public void setVideoCallCoins(long videoCallCoins) {
        VideoCallCoins = videoCallCoins;
    }

    @Exclude
    public Map<String, Object> toCreateCallMap() {
        // creating a call room list
        callRoomList = new ArrayList<>();
        callRoomList.add(SenderUserId);
        callRoomList.add(ReceiverUserId);
        //----------------------------------------------
        HashMap<String, Object> result = new HashMap<>();
        result.put("ChannelName", ChannelName);
        result.put("AccessToken", AccessToken);
        result.put("CallStatus", CallStatus);
        result.put("CallType", CallType);
        result.put("SenderUserId", SenderUserId);
        result.put("ReceiverUserId", ReceiverUserId);
        result.put("CallId", CallId);
        result.put("firebaseCallId", firebaseCallId);
        result.put("ReceiverDetails", ReceiverDetails);
        result.put("SenderDetails", SenderDetails);
        result.put("status", ConstantApp.STATUS_PENDING);
        result.put("callDateTime", FieldValue.serverTimestamp());
        result.put("callSummary", callSummary);
        result.put("callRoomList", callRoomList);
        result.put("audioCallCoins", AudioCallCoins);
        result.put("videoCallCoins", VideoCallCoins);
        return result;
    }

    @Exclude
    public Map<String, Object> toEndCallMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("CallStatus", ConstantApp.EXPIRED);
        result.put("status", ConstantApp.STATUS_END);
        return result;
    }

    @Exclude
    public Map<String, Object> toReceivedCallMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("status", ConstantApp.STATUS_RECEIVED);
        return result;
    }
}
