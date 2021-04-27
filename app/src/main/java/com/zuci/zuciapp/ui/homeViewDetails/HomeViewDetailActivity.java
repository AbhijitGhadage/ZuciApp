package com.zuci.zuciapp.ui.homeViewDetails;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.*;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;
import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.modules.ServiceBuilder;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.network.TipsGoApiService;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.agoraVoiceCall.CallResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.base.BaseActivity;
import com.zuci.zuciapp.ui.firestoreMessageChat.messageChatHistory.ChatListModel;
import com.zuci.zuciapp.ui.gallery.MediaModel;
import com.zuci.zuciapp.ui.home.CoinsResponse;
import com.zuci.zuciapp.ui.home.HomeListModel;
import com.zuci.zuciapp.ui.homeViewDetails.tabFragment.MediaVideoListModel;
import com.zuci.zuciapp.utils.Methods;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewDetailActivity extends BaseActivity<HomeViewDetailViewModel> {
    @Inject
    ViewModelFactory<HomeViewDetailViewModel> viewModelFactory;

    private HomeViewDetailViewModel homeViewDetailViewModel;

    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;

    @Override
    protected HomeViewDetailViewModel getViewModel() {
        return homeViewDetailViewModel;
    }

    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;

    @BindView(R.id.iv_back_btn)
    AppCompatImageView iv_back_btn;
    @BindView(R.id.tv_user_profile_name)
    AppCompatTextView tv_user_profile_name;
    @BindView(R.id.tv_user_profile_name_tool)
    TextView tv_user_profile_name_tool;

    @BindView(R.id.iv_user_profile)
    ImageView iv_user_profile;
    @BindView(R.id.tv_home_follow)
    TextView tv_home_follow;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    HomeListModel homeListModel;
    int userId;
    String affiliate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view_details);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        homeViewDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewDetailViewModel.class);

        homeListModel = getIntent().getParcelableExtra("HOME_MODEL");
        tv_user_profile_name.setText(homeListModel.getProfileName() + " , " + homeListModel.getAge());
        tv_user_profile_name_tool.setText(homeListModel.getProfileName());
        userId = homeListModel.getRegistrationId();
        affiliate = homeListModel.getAffiliate();

        if (homeListModel.isFollowerStatus())
            tv_home_follow.setText("Following");
        else
            tv_home_follow.setText("Follow");

        try {
            if (!Methods.isEmptyOrNull(homeListModel.getProfileImage())) {
                Picasso.get()
                        .load(homeListModel.getProfileImage())
                        .placeholder(R.drawable.profile_male)
                        .error(R.drawable.profile_male)
                        .into(iv_user_profile);
            } else {
                if (homeListModel.getGender().equalsIgnoreCase("Male")) {
                    iv_user_profile.setImageResource(R.drawable.profile_male);
                } else {
                    iv_user_profile.setImageResource(R.drawable.profile_female);
                }
            }
        } catch (Exception e) {
            Log.e("bindViewHolder", e.getMessage());
        }

        MyAdapter tabsPagerAdapter = new MyAdapter(this, getSupportFragmentManager(), userId, affiliate);
        viewPager.setAdapter(tabsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        bindViewModel();
    }

    // nitin code
    public void getTotalAudioVideoCoins(int registerId, String audioCall) {
        TipsGoApiService apiService = ServiceBuilder.getClient().create(TipsGoApiService.class);
        Call<CoinsResponse> call = apiService.getTotalCoins(registerId);
        call.enqueue(new Callback<CoinsResponse>() {
            @Override
            public void onResponse(@NotNull Call<CoinsResponse> call, @NotNull Response<CoinsResponse> response) {
                if (response != null) {
                    getTotalCoins(response.body(), audioCall);
                }
            }

            @Override
            public void onFailure(@NotNull Call<CoinsResponse> call, @NotNull Throwable t) {
            }
        });
    }

    private void getTotalCoins(CoinsResponse coinsResponse, String audioCall) {
        if (coinsResponse != null) {
            int totalCoins = Integer.parseInt(sharedPref.getTotalCoins());
            long receiverCoins = audioCall.equalsIgnoreCase(ConstantApp.AUDIO_CALL) ? coinsResponse.getAudioCallCoins() : coinsResponse.getVideoCallCoins();
            if (totalCoins >= receiverCoins)
                if (audioCall.equalsIgnoreCase(ConstantApp.AUDIO_CALL))
                    getTokenForVoiceCall(homeListModel);
                else
                    getTokenForVideoCall(homeListModel);
            else
                Toast.makeText(this, "Coins Insufficient !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Coins Data Not Found !", Toast.LENGTH_SHORT).show();
        }
    }

    private void bindViewModel() {
        homeViewDetailViewModel.getFollowResponse()
                .observe(this, loginApi -> {
                    assert loginApi != null;
                    switch (loginApi.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(loginApi.responce);
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("Message").equalsIgnoreCase("Follow"))
                                    tv_home_follow.setText("Following");
                                else
                                    tv_home_follow.setText("Follow");

                                sharedPref.setFollowers(jsonObject.getLong("FollowerCount"));
                                sharedPref.setFollowing(jsonObject.getLong("FollowingCount"));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert loginApi.error != null;
                            showErrorMessage(content_parent, loginApi.error.getMessage());
                            break;
                    }
                });


    /*    homeViewDetailViewModel.getTotalCoinsVCResponse()
                .observe(this, loginApi -> {
                    assert loginApi != null;
                    switch (loginApi.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(loginApi.responce);
                                JSONObject jsonObject = new JSONObject(response);

                                if (jsonObject.getBoolean("status")) {

                                    sharedPref.setAudioCall(jsonObject.getInt("AudioCallCoins"));
                                    sharedPref.setVideoCall(jsonObject.getInt("VideoCallCoins"));

                                    int totalCoins = Integer.parseInt(sharedPref.getTotalCoins());
                                    if (totalCoins >= sharedPref.getVideoCall())
                                        getTokenForVideoCall(homeListModel);
                                    else
                                        Toast.makeText(this, "Coins Insufficient !", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert loginApi.error != null;
                            showErrorMessage(content_parent, loginApi.error.getMessage());
                            break;
                    }
                });*/
    }

    public void openVoiceCall(CallResponse callResponse, int callUserType) {
        navigator.navigateToVoiceCall(this, callResponse, callUserType);
        rightToLeftAnimated();
    }

    public void openVideoCall(CallResponse callResponse, int callUserType) {
        navigator.navigateToVideoCall(this, callResponse, callUserType);
        rightToLeftAnimated();
    }

    public void openImageView(MediaModel mediaModel) {
        navigator.navigateToImageViewProfile(this, mediaModel);
        rightToLeftAnimated();
    }

    public void openVideoView(MediaVideoListModel mediaModel) {
        navigator.navigateToVideoViewFromHome(this, mediaModel);
        rightToLeftAnimated();
    }

    @OnClick(R.id.tv_home_message)
    public void message() {
        callChatMessageActivity(homeListModel);
    }

    @OnClick(R.id.tv_home_audio)
    public void audio() {
        getTotalAudioVideoCoins(homeListModel.getRegistrationId(), ConstantApp.AUDIO_CALL);
//        homeViewDetailViewModel.getTotalCoinsAudio(homeListModel.getRegistrationId());
    }

    @OnClick(R.id.tv_home_video)
    public void video() {
        getTotalAudioVideoCoins(homeListModel.getRegistrationId(), ConstantApp.VIDEO_CALL);
//        homeViewDetailViewModel.getTotalCoinsVideo(homeListModel.getRegistrationId());
    }

    @OnClick(R.id.tv_home_follow)
    public void follow() {
        homeViewDetailViewModel.setFollow(sharedPref.getRegisterId(), homeListModel.getRegistrationId());
    }

    private void callChatMessageActivity(HomeListModel homeListModel) {
        ChatListModel chatListModel = new ChatListModel();
        if (homeListModel != null) {
            chatListModel.setRegistrationId(homeListModel.getRegistrationId());
            if (!Methods.isEmptyOrNull(homeListModel.getProfileImage()))
                chatListModel.setProfileImage(homeListModel.getProfileImage());
            if (!Methods.isEmptyOrNull(homeListModel.getProfileName()))
                chatListModel.setProfileName(homeListModel.getProfileName());
        }
        navigator.navigateToChatting(this, chatListModel);
        rightToLeftAnimated();
    }

    private void getTokenForVoiceCall(HomeListModel homeListModel) {
        int userId = sharedPref.getRegisterId();
        homeViewDetailViewModel.getVoiceCallResponse(userId, homeListModel.getRegistrationId(), HomeViewDetailActivity.this);
    }

    private void getTokenForVideoCall(HomeListModel homeListModel) {
        int userId = sharedPref.getRegisterId();
        homeViewDetailViewModel.getVideoCallResponse(userId, homeListModel.getRegistrationId(), HomeViewDetailActivity.this);
    }

    @OnClick(R.id.iv_back_btn)
    public void OnClickBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        leftToRightAnimated();
    }

    public void showDialog(Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialogbox_bio);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    /*
        Button dialogBtn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"Cancel" ,Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });*/

        dialog.show();
    }
}
