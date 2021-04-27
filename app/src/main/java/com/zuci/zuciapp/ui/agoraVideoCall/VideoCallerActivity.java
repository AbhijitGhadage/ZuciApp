package com.zuci.zuciapp.ui.agoraVideoCall;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.firebase.CallBack;
import com.zuci.zuciapp.firebase.CallHistoryRepository;
import com.zuci.zuciapp.firebase.CallHistoryRepositoryImpl;
import com.zuci.zuciapp.firebase.FirebaseConstant;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.agoraVoiceCall.CallResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.agoraVoiceCall.ReceiverCallModel;
import com.zuci.zuciapp.ui.agoraVoiceCall.VideoAudioCutCoinsModel;
import com.zuci.zuciapp.ui.base.BaseActivity;
import com.google.firebase.firestore.DocumentReference;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class VideoCallerActivity extends BaseActivity<VideoCallViewModel> {
    private final String TAG = VideoCallerActivity.class.getSimpleName();
    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Inject
    ViewModelFactory<VideoCallViewModel> viewModelFactory;
    private VideoCallViewModel videoCallViewModel;

    @Inject
    Navigator navigator;

    @Inject
    SharedPref sharedPref;

    @Override
    protected VideoCallViewModel getViewModel() {
        return videoCallViewModel;
    }

    @BindView(R.id.activity_video_chat_view)
    RelativeLayout activity_video_chat_view;

    ReceiverCallModel receiverCallModel;
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
    private TextView tv_call_time;

    // timer
    private long startTime = 0L;
    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    int mins;
    // timer 30 sec
    private long startTime1 = 0L;
    private Handler customHandler1 = new Handler();

    long timeInMilliseconds1 = 0L;
    long timeSwapBuff1 = 0L;
    long updatedTime1 = 0L;

    private long perMinCallCharge, totalCoins, audioCoins, videoCoins;
    private Boolean callReceived = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_caller);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);
        videoCallViewModel = ViewModelProviders.of(this, viewModelFactory).get(VideoCallViewModel.class);
        initialization();
        setInitializeRtcEngine();
        getIntentData();

        bindViewModel();

        //initUI();
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
            //initEngineAndJoinChannel();
        }

        timerStart30Sec();
    }

    private void timerStart30Sec() {
        startTime1 = SystemClock.uptimeMillis();
        customHandler1.postDelayed(updateTimerThread1, 0);
    }

    private Runnable updateTimerThread1 = new Runnable() {
        public void run() {
            timeInMilliseconds1 = SystemClock.uptimeMillis() - startTime1;
            updatedTime1 = timeSwapBuff1 + timeInMilliseconds1;

            int secs = (int) (updatedTime1 / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime1 % 1000);

            String checkTime = mins + ":" + String.format("%02d", secs);
            if (!callReceived && checkTime.equalsIgnoreCase("0:30")) {
                    timeSwapBuff1 += timeInMilliseconds1;
                    customHandler1.removeCallbacks(updateTimerThread1);

                    onChangeCallStatus(callResponse.toEndCallMap());
                }
            customHandler1.postDelayed(this, 0);
        }
    };


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
        totalCoins = Long.parseLong(sharedPref.getTotalCoins());

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
        tv_call_time = findViewById(R.id.tv_call_time);
    }

    private void getIntentData() {
        callResponse = (CallResponse) getIntent().getSerializableExtra(ConstantApp.CALL_RESPONSE);
        accessToken = callResponse.getAccessToken();
        channelName = callResponse.getChannelName();
        callId = callResponse.getCallId();

//        audioCoins = callResponse.getAudioCallCoins();
//        videoCoins = callResponse.getVideoCallCoins();

        mPlayer = playCallerRing();
        if (callResponse != null) {
            receiverCallModel = callResponse.getReceiverDetails();
            if (receiverCallModel != null) {
                txtUserName.setText(String.valueOf(receiverCallModel.getReceiverName()));
                if (!receiverCallModel.getReceiverImage().isEmpty() && receiverCallModel.getReceiverImage() != null) {
                    Picasso.get()
                            .load(receiverCallModel.getReceiverImage())
                            .error(R.drawable.profile_male)
                            .placeholder(R.drawable.profile_male)
                            .into(circleProfileImage);
                }
            }
            saveDataToFirebaseCallHistory();
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

    // on call button click
    public void onCallClicked(View view) {
        onChangeCallStatus(callResponse.toEndCallMap());
    }

    // on click cancel call
    public void onClickCancelCall(View view) {
        onChangeCallStatus(callResponse.toEndCallMap());
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
        MediaPlayer player = MediaPlayer.create(this, R.raw.basic_ring);
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

    private void saveDataToFirebaseCallHistory() {
        String key = FirebaseConstant.CALL_HISTORY_COLLECTION_REF.document().getId();
        DocumentReference documentReference = FirebaseConstant.CALL_HISTORY_COLLECTION_REF.document(key);
        callResponse.setFirebaseCallId(key);
        callHistoryRepository.addOrUpdateCallModel(documentReference, callResponse.toCreateCallMap());
        getCallStatusIsReceivedOrNot();
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

                        callReceived = true;
                        timerStart();   // cal time

                    } else if (callResponse.getStatus().equals(ConstantApp.STATUS_END)) {
                        if (callReceived) {
                            long videoCallCoins = callResponse.getVideoCallCoins();
                            videoCallCoins = videoCallCoins * mins;
                            long viewerCoins = videoCallCoins / 2;
                            long adminCoins = videoCallCoins - viewerCoins;

                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
                            String TransactionDate = sdf.format(new Date());
                            VideoAudioCutCoinsModel videoAudioCutCoinsModel =new VideoAudioCutCoinsModel();
                            videoAudioCutCoinsModel.setCallerRegistrationId(callResponse.getSenderUserId());
                            videoAudioCutCoinsModel.setPickUpUserId(sharedPref.getRegisterId());
                            videoAudioCutCoinsModel.setDeductCoins(viewerCoins);
                            videoAudioCutCoinsModel.setAdmindeductCoins(adminCoins);
                            videoAudioCutCoinsModel.setDeductionType("Call");
                            videoAudioCutCoinsModel.setViewerStatus("Public");
                            videoAudioCutCoinsModel.setDeductionDate(TransactionDate);

                            videoCallViewModel.deductsCoinsCallsApi(videoAudioCutCoinsModel);
                        }
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

    private void timerStart() {
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            mins = 0;
            mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            tv_call_time.setText("" + mins + ":" + String.format("%02d", secs));  //+ ":" + String.format("%03d", milliseconds)

            videoCoins = sharedPref.getVideoCall();

            mins = mins + 1;
            perMinCallCharge = mins * videoCoins;

            if (totalCoins > perMinCallCharge) {
                // available coins call continue
            } else {
                onChangeCallStatus(callResponse.toEndCallMap());
//                Toast.makeText(VideoCallerActivity.this, "cut call VC", Toast.LENGTH_SHORT).show();
            }

            /*   String checkTime=mins+":"+String.format("%02d", secs);
            if(checkTime.equalsIgnoreCase("0:30")) {
                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);

                showAlertDialogButtonClicked();
            }*/
            customHandler.postDelayed(this, 0);
        }
    };

    public void bindViewModel() {
        videoCallViewModel.getCallsResponse()
                .observe(this, forgotPass -> {
                    assert forgotPass != null;
                    switch (forgotPass.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(forgotPass.responce);
                                JSONObject jsonObject = new JSONObject(response);

                           /*     if(jsonObject.getBoolean("status")) {
                                    try {
                                        sharedPref.setTotalCoins(jsonObject.getString("TotalCoins"));
                                    } catch (Exception ignored) {
                                        sharedPref.setTotalCoins("0");
                                    }
                                }*/

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert forgotPass.error != null;
                            onErrorMessage(activity_video_chat_view, forgotPass.error);
                            break;
                    }
                });
    }

    public void onClickVideoMuteClick(View view) {
        vMute = !vMute;
        mRtcEngine.muteLocalVideoStream(vMute);
        int res = vMute ? R.drawable.btn_video_mute_pressed : R.drawable.btn_video_mute_normal;
        btn_video_mute.setImageResource(res);
    }

    public void showAlertDialogButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Subscribe Plan !!");
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_cust_call, null);
        builder.setView(customLayout);
        builder.setCancelable(false);
//        TextView editText = customLayout.findViewById(R.id.tv_dialog_timer);

        builder.setPositiveButton("OK", (dialog, which) -> onChangeCallStatus(callResponse.toEndCallMap()));
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}