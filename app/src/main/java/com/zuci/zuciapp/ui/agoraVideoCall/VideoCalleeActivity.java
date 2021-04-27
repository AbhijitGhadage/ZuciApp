package com.zuci.zuciapp.ui.agoraVideoCall;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.firebase.CallBack;
import com.zuci.zuciapp.firebase.CallHistoryRepository;
import com.zuci.zuciapp.firebase.CallHistoryRepositoryImpl;
import com.zuci.zuciapp.firebase.FirebaseConstant;
import com.zuci.zuciapp.ui.agoraVoiceCall.CallResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.agoraVoiceCall.SenderCallModel;
import com.zuci.zuciapp.ui.base.BaseActivity;
import com.google.firebase.firestore.DocumentReference;
import com.squareup.picasso.Picasso;

import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class VideoCalleeActivity extends BaseActivity<VideoCallViewModel> {
    private final String TAG = VideoCalleeActivity.class.getSimpleName();
    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Inject
    ViewModelFactory<VideoCallViewModel> viewModelFactory;
    private VideoCallViewModel videoCallViewModel;
    private CallHistoryRepository callHistoryRepository;
    private CallResponse callResponse;
    private String accessToken, channelName;
    private long callId;
    private MediaPlayer mPlayer;
    private IRtcEngineEventHandler mRtcEventHandler;

    private RtcEngine mRtcEngine;
    private boolean mMuted,vMute;

    private FrameLayout mLocalContainer;
    private RelativeLayout mRemoteContainer, controlPanel, rlCallRinging, layoutRingingCall;
    private VideoCanvas mLocalVideo, mRemoteVideo;
    private TextView txtUserName;
    private CircleImageView circleProfileImage;

    private ImageView mMuteBtn,btn_video_mute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_callee);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);
        videoCallViewModel = ViewModelProviders.of(this, viewModelFactory).get(VideoCallViewModel.class);
        initialization();
        setInitializeRtcEngine();
        getIntentData();
        //initUI();
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
            //initEngineAndJoinChannel();
        }
    }

    private void setInitializeRtcEngine() {
        mRtcEventHandler = new IRtcEngineEventHandler() {
            @Override
            public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
                runOnUiThread(() -> {
//                    mLogView.logI("Join channel success, uid: " + (uid & 0xFFFFFFFFL));
                });
            }

            @Override
            public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
                runOnUiThread(() -> {
                    setupRemoteVideo(uid);
                });
            }

            @Override
            public void onUserOffline(final int uid, int reason) {
                runOnUiThread(() -> {
                    onRemoteUserLeft(uid);
                });
            }
        };
    }

    private void initialization() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        callHistoryRepository = new CallHistoryRepositoryImpl();
        initializeView();
    }

    private void initializeView() {
        mLocalContainer = findViewById(R.id.local_video_view_container);
        mRemoteContainer = findViewById(R.id.remote_video_view_container);
        mMuteBtn = findViewById(R.id.btn_mute);
        btn_video_mute = findViewById(R.id.btn_video_mute);
        controlPanel = findViewById(R.id.control_panel);
        rlCallRinging = findViewById(R.id.rl_call_ringing);
        layoutRingingCall = findViewById(R.id.layout_ringing_call);
        txtUserName = findViewById(R.id.txt_user_name);
        circleProfileImage = findViewById(R.id.circle_profile_image);
    }

    private void getIntentData() {
        callResponse = (CallResponse) getIntent().getSerializableExtra(ConstantApp.CALL_RESPONSE);
        accessToken = callResponse.getAccessToken();
        channelName = callResponse.getChannelName();
        callId = callResponse.getCallId();
        mPlayer = playCallerRing();
        if (callResponse != null) {
            SenderCallModel senderCallModel = callResponse.getSenderDetails();
            if (senderCallModel != null) {
                txtUserName.setText(String.valueOf(senderCallModel.getSenderName()));
                if (!senderCallModel.getSenderImage().isEmpty() && senderCallModel.getSenderImage() != null) {
                    Picasso.get()
                            .load(senderCallModel.getSenderImage())
                            .error(R.drawable.profile_male)
                            .placeholder(R.drawable.profile_male)
                            .into(circleProfileImage);
                }
            }
            getCallStatusIsReceivedOrNot();
        }
    }

    private void initUI() {
        initEngineAndJoinChannel();
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQ_ID) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                showLongToast("Need permissions " + Manifest.permission.RECORD_AUDIO +
                        "/" + Manifest.permission.CAMERA + "/" + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                finish();
                return;
            }
        }
    }

    private void showLongToast(final String msg) {
        this.runOnUiThread(() -> Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show());
    }

    private void initEngineAndJoinChannel() {
        initializeEngine();
        setupVideoConfig();
        setupLocalVideo();
        joinChannel();
    }

    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private void setupVideoConfig() {
        mRtcEngine.enableVideo();
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    private void setupLocalVideo() {
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(true);
        mLocalContainer.addView(view);
        mLocalVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, 0);
        mRtcEngine.setupLocalVideo(mLocalVideo);
    }

    private void joinChannel() {
        if (TextUtils.isEmpty(accessToken) || TextUtils.equals(accessToken, "#YOUR ACCESS TOKEN#"))
            accessToken = null; // default, no token
        mRtcEngine.joinChannel(accessToken, channelName, "Extra Optional Data", 0);
    }

    @Override
    protected VideoCallViewModel getViewModel() {
        return videoCallViewModel;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRtcEngine != null)
            mRtcEngine.leaveChannel();
        RtcEngine.destroy();
        mRtcEngine = null;
    }

    private void onRemoteUserLeft(int uid) {
        if (mRemoteVideo != null && mRemoteVideo.uid == uid) {
            removeFromParent(mRemoteVideo);
            mRemoteVideo = null;
            onChangeCallStatus(callResponse.toEndCallMap());
        }
    }

    // on Local audio mute
    public void onLocalAudioMuteClicked(View view) {
        mMuted = !mMuted;
        mRtcEngine.muteLocalAudioStream(mMuted);
        int res = mMuted ? R.drawable.btn_mute_normal : R.drawable.btn_unmute;
        mMuteBtn.setImageResource(res);
    }

    public void onClickVideoMuteClick(View view) {
        vMute = !vMute;
        mRtcEngine.muteLocalVideoStream(vMute);
        int res = vMute ? R.drawable.btn_video_mute_pressed : R.drawable.btn_video_mute_normal;
        btn_video_mute.setImageResource(res);
    }
    private void setupRemoteVideo(int uid) {
        ViewGroup parent = mRemoteContainer;
        if (parent.indexOfChild(mLocalVideo.view) > -1)
            parent = mLocalContainer;
        if (mRemoteVideo != null)
            return;
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(parent == mLocalContainer);
        parent.addView(view);
        mRemoteVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid);
        mRtcEngine.setupRemoteVideo(mRemoteVideo);
    }

    // on switch camera click
    public void onSwitchCameraClicked(View view) {
        if (mRtcEngine != null)
            mRtcEngine.switchCamera();
    }

    public void onCallClicked(View view) {
        onChangeCallStatus(callResponse.toEndCallMap());
    }

    // on click reject incomming video call
    public void onClickRejectCall(View view) {
        stopRinging();
        onChangeCallStatus(callResponse.toEndCallMap());
    }

    // on click received incomming video call
    public void onClickReceivedCall(View view) {
        stopRinging();
        updateFirebaseCallStatus(callResponse.toReceivedCallMap());
    }

    private ViewGroup removeFromParent(VideoCanvas canvas) {
        if (canvas != null) {
            ViewParent parent = canvas.view.getParent();
            if (parent != null) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(canvas.view);
                return group;
            }
        }
        return null;
    }

    private void switchView(VideoCanvas canvas) {
        ViewGroup parent = removeFromParent(canvas);
        if (parent == mLocalContainer) {
            if (canvas.view instanceof SurfaceView) {
                ((SurfaceView) canvas.view).setZOrderMediaOverlay(false);
            }
            mRemoteContainer.addView(canvas.view);
        } else if (parent == mRemoteContainer) {
            if (canvas.view instanceof SurfaceView) {
                ((SurfaceView) canvas.view).setZOrderMediaOverlay(true);
            }
            mLocalContainer.addView(canvas.view);
        }
    }

    public void onLocalContainerClick(View view) {
        switchView(mLocalVideo);
        switchView(mRemoteVideo);
    }

    private MediaPlayer playCallerRing() {
        MediaPlayer player = MediaPlayer.create(this, R.raw.simple);
        player.setLooping(true);
        player.start();
        return player;
    }

    private void stopRinging() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void onChangeCallStatus(Map<String, Object> callMap) {
        updateFirebaseCallStatus(callMap);
        videoCallViewModel.getChangeCallStaus(callId).observe(this, apiResponse -> {
                    if (apiResponse != null)
                        finish();
                }
        );
    }

    private void updateFirebaseCallStatus(Map<String, Object> callMap) {
        DocumentReference documentReference = FirebaseConstant.CALL_HISTORY_COLLECTION_REF.document(callResponse.getFirebaseCallId());
        callHistoryRepository.addOrUpdateCallModel(documentReference, callMap);
    }

    private void getCallStatusIsReceivedOrNot() {
        callHistoryRepository.notifyVideoCallStatusChanged(callResponse.getFirebaseCallId(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    callResponse = (CallResponse) object;
                    if (callResponse.getStatus().equals(ConstantApp.STATUS_RECEIVED)) {
                        controlPanel.setVisibility(View.VISIBLE);
                        rlCallRinging.setVisibility(View.GONE);
                        mRemoteContainer.setVisibility(View.VISIBLE);
                        layoutRingingCall.setVisibility(View.GONE);
                        mLocalContainer.setVisibility(View.VISIBLE);
                        stopRinging();
                        initUI();
                    } else if (callResponse.getStatus().equals(ConstantApp.STATUS_END)) {
                        stopRinging();
                        finish();
                    }
                }
            }

            @Override
            public void onError(Object object) {
                stopRinging();
                finish();
            }
        });
    }
}