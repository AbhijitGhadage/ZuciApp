package com.zuci.zuciapp.ui.agoraLive.liveVideo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.modules.ServiceBuilder;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.firebase.FirebaseConstant;
import com.zuci.zuciapp.network.TipsGoApiService;
import com.zuci.zuciapp.ui.agoraLive.LiveCallRepository;
import com.zuci.zuciapp.ui.agoraLive.LiveCallRepositoryImpl;
import com.zuci.zuciapp.ui.agoraLive.LiveCallResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.home.CoinsResponse;
import com.zuci.zuciapp.utils.Methods;
import com.google.firebase.firestore.DocumentReference;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveRoomActivity extends AgoraBaseActivity implements AGEventHandler {
    private final static Logger log = LoggerFactory.getLogger(LiveRoomActivity.class);
    private GridVideoViewContainer mGridVideoViewContainer;
    private RelativeLayout mSmallVideoViewDock;
    private final HashMap<Integer, SurfaceView> mUidsList = new HashMap<>(); // uid = 0 || uid == EngineConfig.mUid
    private LiveCallRepository liveCallHistoryRepository;
    private boolean isBroadCaster;
    private LiveCallResponse liveCallResponse;

    public int mViewType = VIEW_TYPE_DEFAULT;
    public static final int VIEW_TYPE_DEFAULT = 0;
    public static final int VIEW_TYPE_SMALL = 1;

    boolean rootUser;
    // timer
    private long startTime = 0L;
    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private long perMinCallCharge;
    long liveUserCoins, liveUserId, totalCoins, liveUserIdJoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    private boolean isBroadcaster(int cRole) {
        return cRole == Constants.CLIENT_ROLE_BROADCASTER;
    }

    private boolean isBroadcaster() {
        return isBroadcaster(config().mClientRole);
    }

    @Override
    protected void initUIandEvent() {
        event().addEventHandler(this);
        Intent i = getIntent();
        liveCallHistoryRepository = new LiveCallRepositoryImpl();
        int cRole = i.getIntExtra(ConstantApp.ACTION_KEY_CROLE, 0);
        isBroadCaster = (cRole == Constants.CLIENT_ROLE_BROADCASTER);
        if (cRole == 0)
            throw new RuntimeException("Should not reach here");
        String roomName = i.getStringExtra(ConstantApp.ACTION_KEY_ROOM_NAME);
        String liveUserName = i.getStringExtra("LIVE_USER");
        rootUser = i.getBooleanExtra("ROOT_USER", true);
        if (!rootUser) {
            liveUserCoins = i.getLongExtra("LIVE_USER_COINS", 0);
            totalCoins = i.getLongExtra("LIVE_USER_COINS_JOIN", 0);
            liveUserId = i.getLongExtra("LIVE_USER_ID", 0);
            liveUserIdJoins = i.getLongExtra("LIVE_USER_ID_JOIN", 0);
            timerStart();
        }
        // save callresponse to firebase
        liveCallResponse = (LiveCallResponse) i.getSerializableExtra(ConstantApp.CALL_RESPONSE);
        if (liveCallResponse != null)
            saveOrUpdateLiveResponseToFirebase(true);
        doConfigEngine(cRole);
        mGridVideoViewContainer = findViewById(R.id.grid_video_view_container);
        mGridVideoViewContainer.setItemEventHandler((v, item) -> {
            log.debug("onItemDoubleClick " + v + " " + item);
            if (mUidsList.size() < 2) {
                return;
            }
            if (mViewType == VIEW_TYPE_DEFAULT)
                switchToSmallVideoView(((VideoStatusData) item).mUid);
            else
                switchToDefaultVideoView();
        });

        ImageView button1 = findViewById(R.id.btn_1);
        ImageView button2 = findViewById(R.id.btn_2);
        ImageView button3 = findViewById(R.id.btn_3);
        ImageView button4 = findViewById(R.id.btn_4);
        if (isBroadcaster(cRole)) {
            SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
            rtcEngine().setupLocalVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, 0));
            mUidsList.put(0, surfaceV); // get first surface view
            mGridVideoViewContainer.initViewContainer(getApplicationContext(), 0, mUidsList); // first is now full view
            worker().preview(true, surfaceV, 0);
            broadcasterUI(button1, button2, button3, button4);
        } else {
            audienceUI(button1, button2, button3);
        }
        worker().joinChannel(roomName, config().mUid);
        TextView textRoomName = findViewById(R.id.room_name);
        textRoomName.setText(liveUserName);
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
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
//            tv_call_time.setText("" + mins + ":" + String.format("%02d", secs));  //+ ":" + String.format("%03d", milliseconds)

            mins = mins + 1;
            perMinCallCharge = mins * liveUserCoins;

            if (totalCoins > perMinCallCharge) {
                // available coins call continue
            } else {
                cutCoins();   // cut call
                finish();
//                onChangeCallStatus(callResponse.toEndCallMap());
//                Toast.makeText(VoiceCallerActivity.this, "cut call AC", Toast.LENGTH_SHORT).show();
            }
            customHandler.postDelayed(this, 0);
        }
    };

    private void broadcasterUI(final ImageView button1, ImageView button2, ImageView button3, ImageView button4) {
        button1.setTag(true);
        button1.setOnClickListener(v -> {
            Object tag = v.getTag();
            button1.setEnabled(false);
            doSwitchToBroadcaster(tag == null || !((boolean) tag));
        });
        button1.setColorFilter(getResources().getColor(R.color.agora_blue), PorterDuff.Mode.MULTIPLY);
        button2.setOnClickListener(v -> worker().getRtcEngine().switchCamera());
        button3.setOnClickListener(v -> {
            Object tag = v.getTag();
            boolean flag = true;
            if (tag != null && (boolean) tag) {
                flag = false;
            }
            worker().getRtcEngine().muteLocalAudioStream(flag);
            ImageView button = (ImageView) v;
            button.setTag(flag);
            if (flag) {
                button.setColorFilter(getResources().getColor(R.color.agora_blue), PorterDuff.Mode.MULTIPLY);
            } else {
                button.clearColorFilter();
            }
        });

        button4.setOnClickListener(v -> {
            String msg = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            worker().eventHandler().setMetadata(msg.getBytes(StandardCharsets.UTF_8));
        });
    }

    private void audienceUI(final ImageView button1, ImageView button2, ImageView button3) {
        button1.setTag(null);
        button1.setOnClickListener(v -> {
            Object tag = v.getTag();
            button1.setEnabled(false);
            doSwitchToBroadcaster(tag == null || !((boolean) tag));
        });
        button1.clearColorFilter();
        button2.setVisibility(View.GONE);
        button3.setTag(null);
        button3.setVisibility(View.GONE);
        button3.clearColorFilter();
    }

    private void doConfigEngine(int cRole) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int prefIndex = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_PROFILE_IDX, ConstantApp.DEFAULT_PROFILE_IDX);
        if (prefIndex > ConstantApp.VIDEO_DIMENSIONS.length - 1) {
            prefIndex = ConstantApp.DEFAULT_PROFILE_IDX;
        }
        VideoEncoderConfiguration.VideoDimensions dimension = ConstantApp.VIDEO_DIMENSIONS[prefIndex];
        worker().configEngine(cRole, dimension);
    }

    @Override
    protected void deInitUIandEvent() {
        doLeaveChannel();
        event().removeEventHandler(this);
        mUidsList.clear();
    }

    private void doLeaveChannel() {
        worker().leaveChannel(config().mChannel);
        if (isBroadCaster && liveCallResponse != null)
            saveOrUpdateLiveResponseToFirebase(false);
            if (!rootUser)
                cutCoins();


        if (isBroadcaster()) {
            worker().preview(false, null, 0);
        }
    }

    private void cutCoins() {
        if (liveUserCoins <= totalCoins) {
            long viewerCoins = liveUserCoins / 2;
            long adminCoins = liveUserCoins - viewerCoins;
            liveCallCoinsCut(viewerCoins, adminCoins);  // api call
        } else
            Toast.makeText(this, "Not sufficient coins !!", Toast.LENGTH_SHORT).show();
    }

    public void liveCallCoinsCut(long viewerCoins, long adminCoins) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
        String TransactionDate = sdf.format(new Date());

        TipsGoApiService apiService = ServiceBuilder.getClient().create(TipsGoApiService.class);
        LiveCallCoinsDeductsModel liveCallDeduct = new LiveCallCoinsDeductsModel();
        liveCallDeduct.setDeductionType("LiveCall");
        liveCallDeduct.setViewerStatus("Public");
        liveCallDeduct.setDeductionDate(TransactionDate);
        liveCallDeduct.setAdmindeductCoins(adminCoins);
        liveCallDeduct.setDeductCoins(viewerCoins);
        liveCallDeduct.setLiveUserId(liveUserId);
        liveCallDeduct.setJoinLiveUserId(liveUserIdJoins);

        Call<LiveCallCoinsDeductsModelResponse> call = apiService.getLiveDeductsCoins(liveCallDeduct);
        call.enqueue(new Callback<LiveCallCoinsDeductsModelResponse>() {
            @Override
            public void onResponse(@NotNull Call<LiveCallCoinsDeductsModelResponse> call, @NotNull Response<LiveCallCoinsDeductsModelResponse> response) {
                assert response.body() != null;
                long status = response.body().getStatusCode();
                if (status == 200) {
//                    sharedPref.setTotalCoins(String.valueOf(response.body().getTotalCoins()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<LiveCallCoinsDeductsModelResponse> call, @NotNull Throwable t) {
            }
        });
    }

    public void onClickClose(View view) {
        finish();
    }

    public void onShowHideClicked(View view) {
        boolean toHide = true;
        if (view.getTag() != null && (boolean) view.getTag()) {
            toHide = false;
        }
        view.setTag(toHide);
        doShowButtons(toHide);
    }

    private void doShowButtons(boolean hide) {
        View topArea = findViewById(R.id.top_area);
        topArea.setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
        View button1 = findViewById(R.id.btn_1);
        button1.setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
        View button2 = findViewById(R.id.btn_2);
        View button3 = findViewById(R.id.btn_3);
        if (isBroadcaster()) {
            button2.setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
            button3.setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
        } else {
            button2.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
    }

    private void doSwitchToBroadcaster(boolean broadcaster) {
        final int currentHostCount = mUidsList.size();
        final int uid = config().mUid;
        log.debug("doSwitchToBroadcaster " + currentHostCount + " " + (uid & 0XFFFFFFFFL) + " " + broadcaster);
        final ImageView button1 = findViewById(R.id.btn_1);
        final ImageView button2 = findViewById(R.id.btn_2);
        final ImageView button3 = findViewById(R.id.btn_3);
        final ImageView button4 = findViewById(R.id.btn_4);
        if (broadcaster) {
            doConfigEngine(Constants.CLIENT_ROLE_BROADCASTER);

            new Handler().postDelayed(() -> {
                doRenderRemoteUi(uid);
                broadcasterUI(button1, button2, button3, button4);
                button1.setEnabled(true);
                doShowButtons(false);
            }, 1000); // wait for reconfig engine
        } else {
            button1.setEnabled(true);
            stopInteraction(currentHostCount, uid);
        }
    }

    private void stopInteraction(final int currentHostCount, final int uid) {
        doConfigEngine(Constants.CLIENT_ROLE_AUDIENCE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doRemoveRemoteUi(uid);
                ImageView button1 = findViewById(R.id.btn_1);
                ImageView button2 = findViewById(R.id.btn_2);
                ImageView button3 = findViewById(R.id.btn_3);
                audienceUI(button1, button2, button3);
                doShowButtons(false);
            }
        }, 1000); // wait for reconfig engine
    }

    private void doRenderRemoteUi(final int uid) {
        runOnUiThread(() -> {
            if (isFinishing()) {
                return;
            }
            SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
            mUidsList.put(uid, surfaceV);
            if (config().mUid == uid) {
                rtcEngine().setupLocalVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));
            } else {
                rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));
            }
            if (mViewType == VIEW_TYPE_DEFAULT) {
                log.debug("doRenderRemoteUi VIEW_TYPE_DEFAULT" + " " + (uid & 0xFFFFFFFFL));
                switchToDefaultVideoView();
            } else {
                int bigBgUid = mSmallVideoViewAdapter.getExceptedUid();
                log.debug("doRenderRemoteUi VIEW_TYPE_SMALL" + " " + (uid & 0xFFFFFFFFL) + " " + (bigBgUid & 0xFFFFFFFFL));
                switchToSmallVideoView(bigBgUid);
            }
        });
    }

    @Override
    public void onJoinChannelSuccess(final String channel, final int uid, final int elapsed) {
        runOnUiThread(() -> {
            if (isFinishing()) {
                return;
            }
            if (mUidsList.containsKey(uid)) {
                log.debug("already added to UI, ignore it " + (uid & 0xFFFFFFFFL) + " " + mUidsList.get(uid));
                return;
            }
            final boolean isBroadcaster = isBroadcaster();
            log.debug("onJoinChannelSuccess " + channel + " " + uid + " " + elapsed + " " + isBroadcaster);
            worker().getEngineConfig().mUid = uid;
            SurfaceView surfaceV = mUidsList.remove(0);
            if (surfaceV != null) {
                mUidsList.put(uid, surfaceV);
            }
        });
    }

    @Override
    public void onUserOffline(int uid, int reason) {
        log.debug("onUserOffline " + (uid & 0xFFFFFFFFL) + " " + reason);
        doRemoveRemoteUi(uid);
    }

    @Override
    public void onUserJoined(int uid, int elapsed) {
        doRenderRemoteUi(uid);
    }

    @Override
    public void onMetadataReceived(byte[] bytes, int i, long l) {
        showLongToast("Metadata received: \n" + new String(bytes, StandardCharsets.UTF_8));
    }

    private void requestRemoteStreamType(final int currentHostCount) {
        log.debug("requestRemoteStreamType " + currentHostCount);
        new Handler().postDelayed(() -> {
            HashMap.Entry<Integer, SurfaceView> highest = null;
            for (HashMap.Entry<Integer, SurfaceView> pair : mUidsList.entrySet()) {
                log.debug("requestRemoteStreamType " + currentHostCount + " local " + (config().mUid & 0xFFFFFFFFL) + " " + (pair.getKey() & 0xFFFFFFFFL) + " " + pair.getValue().getHeight() + " " + pair.getValue().getWidth());
                if (pair.getKey() != config().mUid && (highest == null || highest.getValue().getHeight() < pair.getValue().getHeight())) {
                    if (highest != null) {
                        rtcEngine().setRemoteVideoStreamType(highest.getKey(), Constants.VIDEO_STREAM_LOW);
                        log.debug("setRemoteVideoStreamType switch highest VIDEO_STREAM_LOW " + currentHostCount + " " + (highest.getKey() & 0xFFFFFFFFL) + " " + highest.getValue().getWidth() + " " + highest.getValue().getHeight());
                    }
                    highest = pair;
                } else if (pair.getKey() != config().mUid && (highest != null && highest.getValue().getHeight() >= pair.getValue().getHeight())) {
                    rtcEngine().setRemoteVideoStreamType(pair.getKey(), Constants.VIDEO_STREAM_LOW);
                    log.debug("setRemoteVideoStreamType VIDEO_STREAM_LOW " + currentHostCount + " " + (pair.getKey() & 0xFFFFFFFFL) + " " + pair.getValue().getWidth() + " " + pair.getValue().getHeight());
                }
            }
            if (highest != null && highest.getKey() != 0) {
                rtcEngine().setRemoteVideoStreamType(highest.getKey(), Constants.VIDEO_STREAM_HIGH);
                log.debug("setRemoteVideoStreamType VIDEO_STREAM_HIGH " + currentHostCount + " " + (highest.getKey() & 0xFFFFFFFFL) + " " + highest.getValue().getWidth() + " " + highest.getValue().getHeight());
            }
        }, 500);
    }

    private void doRemoveRemoteUi(final int uid) {
        runOnUiThread(() -> {
            if (isFinishing()) {
                return;
            }
            mUidsList.remove(uid);
            int bigBgUid = -1;
            if (mSmallVideoViewAdapter != null) {
                bigBgUid = mSmallVideoViewAdapter.getExceptedUid();
            }
            log.debug("doRemoveRemoteUi " + (uid & 0xFFFFFFFFL) + " " + (bigBgUid & 0xFFFFFFFFL));
            if (mViewType == VIEW_TYPE_DEFAULT || uid == bigBgUid) {
                switchToDefaultVideoView();
            } else {
                switchToSmallVideoView(bigBgUid);
            }
        });
    }

    private SmallVideoViewAdapter mSmallVideoViewAdapter;

    private void switchToDefaultVideoView() {
        if (mSmallVideoViewDock != null)
            mSmallVideoViewDock.setVisibility(View.GONE);
        mGridVideoViewContainer.initViewContainer(getApplicationContext(), config().mUid, mUidsList);
        mViewType = VIEW_TYPE_DEFAULT;
        int sizeLimit = mUidsList.size();
        if (sizeLimit > ConstantApp.MAX_PEER_COUNT + 1) {
            sizeLimit = ConstantApp.MAX_PEER_COUNT + 1;
        }
        for (int i = 0; i < sizeLimit; i++) {
            int uid = mGridVideoViewContainer.getItem(i).mUid;
            if (config().mUid != uid) {
                rtcEngine().setRemoteVideoStreamType(uid, Constants.VIDEO_STREAM_HIGH);
                log.debug("setRemoteVideoStreamType VIDEO_STREAM_HIGH " + mUidsList.size() + " " + (uid & 0xFFFFFFFFL));
            }
        }
    }

    private void switchToSmallVideoView(int uid) {
        HashMap<Integer, SurfaceView> slice = new HashMap<>(1);
        slice.put(uid, mUidsList.get(uid));
        mGridVideoViewContainer.initViewContainer(getApplicationContext(), uid, slice);
        bindToSmallVideoView(uid);
        mViewType = VIEW_TYPE_SMALL;
        requestRemoteStreamType(mUidsList.size());
    }


    private void bindToSmallVideoView(int exceptUid) {
        if (mSmallVideoViewDock == null) {
            ViewStub stub = findViewById(R.id.small_video_view_dock);
            mSmallVideoViewDock = (RelativeLayout) stub.inflate();
        }
        RecyclerView recycler = findViewById(R.id.small_video_view_container);
        boolean create = false;
        if (mSmallVideoViewAdapter == null) {
            create = true;
            mSmallVideoViewAdapter = new SmallVideoViewAdapter(this, exceptUid, mUidsList, new VideoViewEventListener() {
                @Override
                public void onItemDoubleClick(View v, Object item) {
                    switchToDefaultVideoView();
                }
            });
            mSmallVideoViewAdapter.setHasStableIds(true);
        }
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        recycler.setAdapter(mSmallVideoViewAdapter);
        recycler.setDrawingCacheEnabled(true);
        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        if (!create) {
            mSmallVideoViewAdapter.notifyUiChanged(mUidsList, exceptUid, null, null);
        }
        recycler.setVisibility(View.VISIBLE);
        mSmallVideoViewDock.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // save live response to firestore
    private void saveOrUpdateLiveResponseToFirebase(boolean isForCreate) {
        if (liveCallResponse != null && Methods.isEmptyOrNull(liveCallResponse.getLiveId()))
            liveCallResponse.setLiveId(FirebaseConstant.LIVE_CALL_HISTORY_COLLECTION_REF.document().getId());
        assert liveCallResponse != null;  // abhi
        DocumentReference documentReference = FirebaseConstant.LIVE_CALL_HISTORY_COLLECTION_REF.document(liveCallResponse.getLiveId());
        liveCallHistoryRepository.addOrUpdateLiveCallModel(documentReference, isForCreate ? liveCallResponse.toCreateLiveCallMap() : liveCallResponse.toEndLiveCallMap());
    }

}
