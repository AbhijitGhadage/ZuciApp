package com.zuci.zuciapp.ui.agoraLive.liveVideo;

public interface AGEventHandler {
    void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed);

    void onJoinChannelSuccess(String channel, int uid, int elapsed);

    void onUserOffline(int uid, int reason);

    void onUserJoined(int uid, int elapsed);

    void onMetadataReceived(byte[] bytes, int i, long l);
}
