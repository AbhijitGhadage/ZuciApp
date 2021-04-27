package com.zuci.zuciapp.ui.agoraVoiceCall;

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
import android.view.View;
import android.view.WindowManager;
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
import com.zuci.zuciapp.ui.base.BaseActivity;
import com.google.firebase.firestore.DocumentReference;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

public class VoiceCalleeActivity extends BaseActivity<VoiceCallViewModel> {
    private final String LOG_TAG = VoiceCalleeActivity.class.getSimpleName();
    @Inject
    ViewModelFactory<VoiceCallViewModel> viewModelFactory;
    private VoiceCallViewModel voiceCallViewModel;
    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private RtcEngine mRtcEngine;
    private String accessToken, channelName;
    private long callId;
    private IRtcEngineEventHandler mRtcEventHandler;
    private CallResponse callResponse;
    @BindView(R.id.txt_user_name)
    TextView txt_user_name;
    @BindView(R.id.call_status)
    TextView call_status;
    @BindView(R.id.circle_profile_image)
    CircleImageView circle_profile_image;
    @BindView(R.id.rl_call_ringing)
    RelativeLayout rl_call_ringing;
    @BindView(R.id.rl_call_received)
    RelativeLayout rl_call_received;
    private CallHistoryRepository callHistoryRepository;
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_voice_callee);
            AndroidInjection.inject(this);
            ButterKnife.bind(this);
            voiceCallViewModel = ViewModelProviders.of(this, viewModelFactory).get(VoiceCallViewModel.class);
            initialization();
            getIntentData();
            setRtcEventHandler();
            checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    private void initialization() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        callHistoryRepository = new CallHistoryRepositoryImpl();
    }

    private void setRtcEventHandler() {
        mRtcEventHandler = new IRtcEngineEventHandler() {
            @Override
            public void onUserOffline(final int uid, final int reason) {
                runOnUiThread(() -> onRemoteUserLeft(uid, reason));
            }

            @Override
            public void onUserMuteAudio(final int uid, final boolean muted) {
                runOnUiThread(() -> onRemoteUserVoiceMuted(uid, muted));
            }
        };
    }

    private void getIntentData() {
        callResponse = (CallResponse) getIntent().getSerializableExtra(ConstantApp.CALL_RESPONSE);
        accessToken = callResponse.getAccessToken();
        channelName = callResponse.getChannelName();
        callId = callResponse.getCallId();
        call_status.setText("Incoming ...");
        mPlayer = playCallerRing();
        if (callResponse != null) {
            SenderCallModel senderCallModel = callResponse.getSenderDetails();
            if (senderCallModel != null) {
                txt_user_name.setText(String.valueOf(senderCallModel.getSenderName()));
                if (!senderCallModel.getSenderImage().isEmpty() && senderCallModel.getSenderImage() != null) {
                    Picasso.get()
                            .load(senderCallModel.getSenderImage())
                            .error(R.drawable.profile_male)
                            .placeholder(R.drawable.profile_male)
                            .into(circle_profile_image);
                }
            }
            getCallStatusIsReceivedOrNot();
        }
    }

    private void getCallStatusIsReceivedOrNot() {
        callHistoryRepository.notifyVoiceCallStatusChanged(callResponse.getFirebaseCallId(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    callResponse = (CallResponse) object;
                    if (callResponse.getStatus().equals(ConstantApp.STATUS_RECEIVED)) {
                        rl_call_ringing.setVisibility(View.GONE);
                        rl_call_received.setVisibility(View.VISIBLE);
                        call_status.setText("Call Incoming ...");
                        stopRinging();
                        initAgoraEngineAndJoinChannel();
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

    private void initAgoraEngineAndJoinChannel() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
            if (TextUtils.equals(accessToken, "") || TextUtils.equals(accessToken, "#YOUR ACCESS TOKEN#"))
                accessToken = null;
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
            mRtcEngine.joinChannel(accessToken, channelName, "Extra Optional Data", 0); // if you do not specify the uid, we will generate the uid for you
        } catch (Exception e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRtcEngine != null)
            mRtcEngine.leaveChannel();
        RtcEngine.destroy();
        mRtcEngine = null;
    }

    @Override
    protected VoiceCallViewModel getViewModel() {
        return voiceCallViewModel;
    }

    @OnClick(R.id.img_audio)
    public void onLocalAudioMuteClicked(View view) {
        ImageView iv = (ImageView) view;
        if (iv.isSelected()) {
            iv.setSelected(false);
        } else {
            iv.setSelected(true);
        }
        iv.setImageResource(iv.isSelected() ? R.drawable.btn_mute_normal : R.drawable.btn_unmute);
        mRtcEngine.muteLocalAudioStream(iv.isSelected());
    }

    @OnClick(R.id.img_speaker)
    public void onSwitchSpeakerphoneClicked(View view) {
        ImageView iv = (ImageView) view;
        if (iv.isSelected()) {
            iv.setSelected(false);
        } else {
            iv.setSelected(true);
        }

        iv.setImageResource(iv.isSelected() ? R.drawable.btn_speacker2 : R.drawable.btn_speaker);
        mRtcEngine.setEnableSpeakerphone(view.isSelected());
    }

    @OnClick(R.id.img_end_call)
    public void onEncCallClicked(View view) {
        onChangeCallStatus(callResponse.toEndCallMap());
        //finish();
    }

    @OnClick(R.id.img_reject_call)
    public void onRejectCallClick(View view) {
        stopRinging();
        onChangeCallStatus(callResponse.toEndCallMap());
        //finish();
    }

    @OnClick(R.id.img_received_call)
    public void onReceivedCallClick(View view) {
        stopRinging();
        updateFirebaseCallStatus(callResponse.toReceivedCallMap());
    }

    private void onRemoteUserLeft(int uid, int reason) {
        showLongToast(String.format(Locale.US, "user %d left %d", (uid & 0xFFFFFFFFL), reason));
        View tipMsg = findViewById(R.id.txt_user_name); // optional UI
        tipMsg.setVisibility(View.VISIBLE);
        onChangeCallStatus(callResponse.toEndCallMap());
    }

    private void onRemoteUserVoiceMuted(int uid, boolean muted) {
        showLongToast(String.format(Locale.US, "user %d muted or unmuted %b", (uid & 0xFFFFFFFFL), muted));
    }

    public void onChangeCallStatus(Map<String, Object> callMap) {
        updateFirebaseCallStatus(callMap);
        voiceCallViewModel.getChangeCallStaus(callId).observe(this, apiResponse -> {
                    if (apiResponse != null)
                        finish();
                }
        );
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        Log.i(LOG_TAG, "checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        Log.i(LOG_TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode);
        if (requestCode == PERMISSION_REQ_ID_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                //initAgoraEngineAndJoinChannel();
            } else {
                showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO);
                finish();
            }
        }
    }

    public final void showLongToast(final String msg) {
        this.runOnUiThread(() -> Toast.makeText(VoiceCalleeActivity.this, "" + msg, Toast.LENGTH_SHORT).show());
    }

    // --------------------firebase call
    private void updateFirebaseCallStatus(Map<String, Object> callMap) {
        DocumentReference documentReference = FirebaseConstant.CALL_HISTORY_COLLECTION_REF.document(callResponse.getFirebaseCallId());
        callHistoryRepository.addOrUpdateCallModel(documentReference, callMap);
    }
}
