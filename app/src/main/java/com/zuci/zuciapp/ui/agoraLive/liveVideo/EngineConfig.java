package com.zuci.zuciapp.ui.agoraLive.liveVideo;

import io.agora.rtc.video.VideoEncoderConfiguration;

public class EngineConfig {
    public int mClientRole;

    public VideoEncoderConfiguration.VideoDimensions mVideoDimension;

    public int mUid;

    public String mChannel;

    public void reset() {
        mChannel = null;
    }

    EngineConfig() {
    }
}
