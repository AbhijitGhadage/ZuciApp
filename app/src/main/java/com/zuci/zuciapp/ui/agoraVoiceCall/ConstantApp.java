package com.zuci.zuciapp.ui.agoraVoiceCall;

import io.agora.rtc.video.VideoEncoderConfiguration;

public class ConstantApp {
    public static final String CALL_RESPONSE = "CALL_RESPONSE";
    public static final int CALLEE = 0;
    public static final int CALLER = 1;
    public static final String USER_CALLER_CALLEE = "USER_CALLER_CALLEE";
    public static final String CALL_USER_TYPE = "CALL_USER_TYPE";
    public static final String AUDIO_CALL = "AC";
    public static final String VIDEO_CALL = "VC";
    public static final String SUCCESS = "Success";
    public static final String FAILURE = "Failure";
    public static final String EXPIRED = "Expired";
    public static final String STATUS_RECEIVED = "Received";
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_END = "End";
    public static final String CHATTING_MODEL = "CHATTING_MODEL";
    public static final String FROM_MAIN_ACTIVITY = "FROM_MAIN_ACTIVITY";

    public static final String TAKE_PHOTO = "Take a Photo";
    public static final String CHOOSE_FROM_GALLERY = "Choose From Gallery";
    public static final String CHOOSE_FROM_VIDEO = "Choose From Video";
    public static final String CANCEL = "Cancel";

    public static final String IMAGE = "Image";
    public static final String VIDEO = "Video";

    public static final int MEDIA_REQUEST_CODE = 101;
    public static final int MEDIA_RESULT_CODE = 102;

    // notification
    final private static String SERVER_KEY = "key=" + "Your Firebase server key";
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String CONTENT_TYPE = "application/json";

    public static final String LIVE_CALL = "LC";

    public static final int MAX_PEER_COUNT = 3;

    public static class PrefManager {
        public static final String PREF_PROPERTY_PROFILE_IDX = "pref_profile_index";
        public static final String PREF_PROPERTY_UID = "pOCXx_uid";
    }

    public static final int BASE_VALUE_PERMISSION = 0X0001;

    public static final int PERMISSION_REQ_ID_RECORD_AUDIO = BASE_VALUE_PERMISSION + 1;
    public static final int PERMISSION_REQ_ID_CAMERA = BASE_VALUE_PERMISSION + 2;
    public static final int PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE = BASE_VALUE_PERMISSION + 3;

    public static final String APP_BUILD_DATE = "today";

    public static final String ACTION_KEY_CROLE = "C_Role";
    public static final String ACTION_KEY_ROOM_NAME = "ecHANEL";

    public static VideoEncoderConfiguration.VideoDimensions[] VIDEO_DIMENSIONS = new VideoEncoderConfiguration.VideoDimensions[] {
            VideoEncoderConfiguration.VD_160x120,
            VideoEncoderConfiguration.VD_320x180,
            VideoEncoderConfiguration.VD_320x240,
            VideoEncoderConfiguration.VD_640x360,
            VideoEncoderConfiguration.VD_640x480,
            VideoEncoderConfiguration.VD_1280x720
    };

    public static final int DEFAULT_PROFILE_IDX = 4; // default use 480P
}
