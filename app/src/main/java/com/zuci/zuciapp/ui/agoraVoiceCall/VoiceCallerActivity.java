package com.zuci.zuciapp.ui.agoraVoiceCall;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.firebase.CallBack;
import com.zuci.zuciapp.firebase.CallHistoryRepository;
import com.zuci.zuciapp.firebase.CallHistoryRepositoryImpl;
import com.zuci.zuciapp.firebase.FirebaseConstant;
import com.zuci.zuciapp.firebase.notification.BodyNotificationModel;
import com.zuci.zuciapp.firebase.notification.RequestNotificaton;
import com.zuci.zuciapp.firebase.notification.SendNotificationModel;
import com.zuci.zuciapp.network.TipsGoApiService;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VoiceCallerActivity extends BaseActivity<VoiceCallViewModel> {
    private final String LOG_TAG = VoiceCallerActivity.class.getSimpleName();
    @Inject
    ViewModelFactory<VoiceCallViewModel> viewModelFactory;
    private VoiceCallViewModel voiceCallViewModel;
    @Inject
    Navigator navigator;

    @Inject
    SharedPref sharedPref;

    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private RtcEngine mRtcEngine;
    private String accessToken, channelName;
    private long callId;
    private IRtcEngineEventHandler mRtcEventHandler;
    private CallResponse callResponse;

    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;
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
    @BindView(R.id.tv_call_time)
    TextView tv_call_time;

    private CallHistoryRepository callHistoryRepository;
    private MediaPlayer mPlayer;

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
    protected VoiceCallViewModel getViewModel() {
        return voiceCallViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_voice_caller);
            AndroidInjection.inject(this);
            ButterKnife.bind(this);
            voiceCallViewModel = ViewModelProviders.of(this, viewModelFactory).get(VoiceCallViewModel.class);
            initialization();
            getIntentData();
            setRtcEventHandler();
            checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO);
            bindViewModel();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
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


    private void initialization() {
        totalCoins = Long.parseLong(sharedPref.getTotalCoins());
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
        call_status.setText("Outgoing ...");
//        audioCoins = callResponse.getAudioCallCoins();
//        videoCoins = callResponse.getVideoCallCoins();
        mPlayer = playCallerRing();
        if (callResponse != null) {
            ReceiverCallModel receiverCallModel = callResponse.getReceiverDetails();
            if (receiverCallModel != null) {
                txt_user_name.setText(String.valueOf(receiverCallModel.getReceiverName()));
                if (!receiverCallModel.getReceiverImage().isEmpty() && receiverCallModel.getReceiverImage() != null) {
                    Picasso.get()
                            .load(receiverCallModel.getReceiverImage())
                            .error(R.drawable.profile_male)
                            .placeholder(R.drawable.profile_male)
                            .into(circle_profile_image);
                }
            }
            saveDataToFirebaseCallHistory();
        }
    }

    private void getCallStatusIsReceivedOrNot() {
        callHistoryRepository.notifyVoiceCallStatusChanged(callResponse.getFirebaseCallId(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    //stopRinging();
                    callResponse = (CallResponse) object;
                    if (callResponse.getStatus().equals(ConstantApp.STATUS_RECEIVED)) {
                        rl_call_ringing.setVisibility(View.GONE);
                        rl_call_received.setVisibility(View.VISIBLE);
                        call_status.setText("Call Ongoing");
                        stopRinging();
                        initAgoraEngineAndJoinChannel();
                        callReceived = true;
                        timerStart();   // cal time
                    } else if (callResponse.getStatus().equals(ConstantApp.STATUS_END)) {
                        stopRinging();
                        finish();
                        if (callReceived) {
                            long audioCallCoins = callResponse.getAudioCallCoins();
                            audioCallCoins = audioCallCoins * mins;
                            long viewerCoins = audioCallCoins / 2;
                            long adminCoins = audioCallCoins - viewerCoins;

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
                            voiceCallViewModel.deductsCoinsCallsApi(videoAudioCutCoinsModel);
                        }
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

            audioCoins = sharedPref.getAudioCall();

            mins = mins + 1;
            perMinCallCharge = mins * audioCoins;

            if (totalCoins > perMinCallCharge) {
                // available coins call continue
            } else {
                onChangeCallStatus(callResponse.toEndCallMap());
//                Toast.makeText(VoiceCallerActivity.this, "cut call AC", Toast.LENGTH_SHORT).show();
            }
            customHandler.postDelayed(this, 0);
        }
    };

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

    public void bindViewModel() {
        voiceCallViewModel.getCallsResponse()
                .observe(this, forgotPass -> {
                    assert forgotPass != null;
                    switch (forgotPass.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(forgotPass.responce);
                                JSONObject jsonObject = new JSONObject(response);

/*                                if (jsonObject.getBoolean("status")) {
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
                            onErrorMessage(content_parent, forgotPass.error);
                            break;
                    }
                });
    }

    @OnClick(R.id.img_cancel_call)
    public void onCancelCall(View view) {
        stopRinging();
        onChangeCallStatus(callResponse.toEndCallMap());
        //finish();
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
        this.runOnUiThread(() -> Toast.makeText(VoiceCallerActivity.this, "" + msg, Toast.LENGTH_SHORT).show());
    }

    // --------------------firebase call
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
//        notificationVoiceCall();
    }

    private void notificationVoiceCall() {
        SendNotificationModel sendNotificationModel = new SendNotificationModel("", "");
        BodyNotificationModel bodyNotificationModel = new BodyNotificationModel("AC");
        RequestNotificaton requestNotificaton = new RequestNotificaton();
        requestNotificaton.setSendNotificationModel(sendNotificationModel);
        requestNotificaton.setBodyNotificationModel(bodyNotificationModel);
        //token is id , whom you want to send notification ,
        requestNotificaton.setToken(callResponse.getReceiverDetails().getReceiverDeviceToken());

        TipsGoApiService apiService = getClient().create(TipsGoApiService.class);
        Call<JsonElement> jsonElementSingle = apiService.sendChatNotification(requestNotificaton);
        jsonElementSingle.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {
                Toast.makeText(VoiceCallerActivity.this, "Success !!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                Toast.makeText(VoiceCallerActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://fcm.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
