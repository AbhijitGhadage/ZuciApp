package com.zuci.zuciapp.ui;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;


import com.zuci.zuciapp.ui.agoraLive.LiveCallResponse;
import com.zuci.zuciapp.ui.agoraLive.liveVideo.LiveRoomActivity;
import com.zuci.zuciapp.ui.agoraVideoCall.VideoCalleeActivity;
import com.zuci.zuciapp.ui.agoraVideoCall.VideoCallerActivity;
import com.zuci.zuciapp.ui.agoraVoiceCall.CallResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.agoraVoiceCall.VoiceCalleeActivity;
import com.zuci.zuciapp.ui.agoraVoiceCall.VoiceCallerActivity;
import com.zuci.zuciapp.ui.changePass.ChangePasswordActivity;
import com.zuci.zuciapp.ui.chat.message.readMessage.MessageChatActivity;
import com.zuci.zuciapp.ui.feedback.FeedBackActivity;
import com.zuci.zuciapp.ui.firestoreMessageChat.messageChatHistory.ChatListModel;
import com.zuci.zuciapp.ui.forgotPass.ForgotPassOtpActivity;
import com.zuci.zuciapp.ui.forgotPass.NewPasswordActivity;
import com.zuci.zuciapp.ui.gallery.GalleryActivity;
import com.zuci.zuciapp.ui.gallery.MediaModel;
import com.zuci.zuciapp.ui.gallery.UploadImgActivity;
import com.zuci.zuciapp.ui.galleryVideo.GalleryVideoActivity;
import com.zuci.zuciapp.ui.galleryVideo.UploadVideoActivity;
import com.zuci.zuciapp.ui.imgVideoView.ViewImageActivity;
import com.zuci.zuciapp.ui.imgVideoView.ViewVideoActivity;
import com.zuci.zuciapp.ui.home.HomeListModel;
import com.zuci.zuciapp.ui.homeViewDetails.HomeViewDetailActivity;
import com.zuci.zuciapp.ui.forgotPass.ForgotPasswordActivity;
import com.zuci.zuciapp.ui.homeViewDetails.tabFragment.MediaVideoListModel;
import com.zuci.zuciapp.ui.mainPage.MainHomeActivity;
import com.zuci.zuciapp.ui.matchingPartnerQA.MatchingPartnerQueAnsActivity;
import com.zuci.zuciapp.ui.profileCreate.ProfileCreateActivity;
import com.zuci.zuciapp.ui.settings.SettingsActivity;
import com.zuci.zuciapp.ui.signUp.OtpActivity;
import com.zuci.zuciapp.ui.signUp.GenderActivity;
import com.zuci.zuciapp.ui.signUp.SignUpActivity;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.login.LoginActivity;
import com.zuci.zuciapp.ui.network.NetWorkActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.agora.rtc.Constants;

@Singleton
public final class Navigator {

    @Inject
    SharedPref sharedPref;

    @Inject
    public Navigator() {

    }

    public void navigateInternetConnectErrorScreen(@NonNull Activity context) {
        Intent intent = new Intent(context, NetWorkActivity.class);
        context.startActivity(intent);
    }

    public void navigateToLoginScreen(@NonNull Activity context) {
        Intent intent;
        intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        context.finishAffinity();
    }

    public void navigateToForgot(@NonNull Activity context) {
        Intent intent;
        intent = new Intent(context, ForgotPasswordActivity.class);
        context.startActivity(intent);
//        context.finishAffinity();
    }

    public void navigateToGender(@NonNull Activity context, String name, String nikeName, String mobile, String email, String dob, int ageCal, String pass, String rePass) {
        Intent intent;
        intent = new Intent(context, GenderActivity.class);
        intent.putExtra("FULL_NAME", name);
        intent.putExtra("NIKE_NAME", nikeName);
        intent.putExtra("USER_MOBILE", mobile);
        intent.putExtra("USER_EMAIL", email);
        intent.putExtra("USER_DOB", dob);
        intent.putExtra("USER_AGE", ageCal);
        intent.putExtra("USER_PASS", pass);
        intent.putExtra("USER_RE_PASS", rePass);
        context.startActivity(intent);
        context.finish();
    }

    public void navigateToOtpScreen(@NonNull Activity context, int registerIdInt, String emailId, String otp) {
        Intent intent;
        intent = new Intent(context, OtpActivity.class);
        intent.putExtra("FULL_REG_ID", registerIdInt);
        intent.putExtra("USER_EMAIL", emailId);
        intent.putExtra("USER_OTP", otp);
        context.startActivity(intent);
        context.finish();
    }

    public void navigateToSignUp(@NonNull Activity context) {
        Intent intent;
        intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
//        context.finishAffinity();
    }

    public void navigateToProfileCreate(@NonNull Activity context) {
        Intent intent;
        intent = new Intent(context, ProfileCreateActivity.class);
        intent.putExtra(ConstantApp.FROM_MAIN_ACTIVITY, false);
        context.startActivity(intent);
//        context.finishAffinity();
    }


    public void navigateToDetails(@NonNull Activity context, HomeListModel homeListModel) {
        Intent intent;
        intent = new Intent(context, HomeViewDetailActivity.class);
        intent.putExtra("HOME_MODEL", homeListModel);
        context.startActivity(intent);
//        context.finish();
    }

    public void navigateLoginToHome(@NonNull Activity context,boolean firstTimeLogin) {
        Intent intent;
        intent = new Intent(context, MainHomeActivity.class);
        intent.putExtra("FirstTimeLogin", firstTimeLogin);
        context.startActivity(intent);
        context.finishAffinity();
    }

    public void navigateToHome(@NonNull Activity context) {
        Intent intent;
        intent = new Intent(context, MainHomeActivity.class);
        context.startActivity(intent);
        context.finishAffinity();
    }

    public void navigateToHomeBackProfile(@NonNull Activity context) {
        Intent intent;
        intent = new Intent(context, MainHomeActivity.class);
        context.startActivity(intent);
        context.finishAffinity();
    }

    public void navigateToLogOutScreen(@NonNull Activity context) {
        Intent intent;
        intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        context.finishAffinity();
    }

    public void navigateToBasicProfile(@NonNull Activity context) {
        Intent intent;
        intent = new Intent(context, ProfileCreateActivity.class);
        intent.putExtra(ConstantApp.FROM_MAIN_ACTIVITY, true);
        context.startActivity(intent);
//        context.finishAffinity();
    }

    public void navigateToGallery(@NonNull Activity context) {
        Intent intent;
        intent = new Intent(context, GalleryActivity.class);
        context.startActivity(intent);
//        context.finishAffinity();
    }

    public void navigateToGalleryVideo(@NonNull Activity context) {
        Intent intent;
        intent = new Intent(context, GalleryVideoActivity.class);
        context.startActivity(intent);
//        context.finishAffinity();
    }

    public void navigateToVideoView(@NonNull Activity context, MediaModel mediaModel) {
        Intent intent;
        intent = new Intent(context, ViewVideoActivity.class);
        intent.putExtra("MEDIA_MODEL", mediaModel);
        context.startActivity(intent);
//        context.finishAffinity();
    }

    public void navigateToVideoViewFromHome(@NonNull Activity context, MediaVideoListModel mediaModel) {
        Intent intent;
        intent = new Intent(context, ViewVideoActivity.class);
        intent.putExtra("MEDIA_MODEL_HOME", mediaModel);
        context.startActivity(intent);
//        context.finishAffinity();
    }

    public void navigateToImageView(@NonNull Activity context, MediaModel mediaModel) {
        Intent intent;
        intent = new Intent(context, ViewImageActivity.class);
        intent.putExtra("MEDIA_MODEL", mediaModel);
        context.startActivity(intent);
//        context.finishAffinity();
    }

    public void navigateToImageViewProfile(@NonNull Activity context, MediaModel mediaModel) {
        Intent intent;
        intent = new Intent(context, ViewImageActivity.class);
        intent.putExtra("MEDIA_MODEL_PROFILE", mediaModel);
        context.startActivity(intent);
//        context.finishAffinity();
    }


    public void navigateToChangePass(@NonNull Activity context) {
        Intent intent;
        intent = new Intent(context, ChangePasswordActivity.class);
        context.startActivity(intent);
//        context.finishAffinity();
    }

    public void navigateToFeedback(@NonNull Activity context) {
        Intent intent;
        intent = new Intent(context, FeedBackActivity.class);
        context.startActivity(intent);
//        context.finishAffinity();
    }

    public void navigateToUploadImg(@NonNull Activity context) {
        Intent intent;
        intent = new Intent(context, UploadImgActivity.class);
        context.startActivityForResult(intent, ConstantApp.MEDIA_REQUEST_CODE);
//        context.finishAffinity();
    }

    public void navigateToUploadVideo(@NonNull Activity context) {
        Intent intent;
        intent = new Intent(context, UploadVideoActivity.class);
        context.startActivityForResult(intent, ConstantApp.MEDIA_REQUEST_CODE);
//        context.finishAffinity();
    }

    public void navigateToForgotOTPScreen(@NonNull Activity context, String emailId, int registerId) {
        Intent intent;
        intent = new Intent(context, ForgotPassOtpActivity.class);
        intent.putExtra("USER_EMAIL_OTP", emailId);
        intent.putExtra("USER_REGISTER_OTP", registerId);
        context.startActivity(intent);
        context.finish();
    }

    public void navigateToNewPasswordScreen(@NonNull Activity context, int registerId, String emailId) {
        Intent intent;
        intent = new Intent(context, NewPasswordActivity.class);
        intent.putExtra("USER_REGISTER_OTP", registerId);
        intent.putExtra("USER_EMAIL_OTP", emailId);
        context.startActivity(intent);
        context.finish();
    }

    public void navigateToVoiceCall(@NonNull Activity context, CallResponse callResponse, int callUserType) {
        Intent intent;
        if (callUserType == 1)
            intent = new Intent(context, VoiceCallerActivity.class);
        else
            intent = new Intent(context, VoiceCalleeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ConstantApp.CALL_RESPONSE, callResponse);
        intent.putExtra(ConstantApp.USER_CALLER_CALLEE, callUserType);
        context.startActivity(intent);
//        context.finish();
    }

    public void navigateToVideoCall(@NonNull Activity context, CallResponse callResponse, int callUserType) {
        Intent intent;
        if (callUserType == 1)
            intent = new Intent(context, VideoCallerActivity.class);
        else
            intent = new Intent(context, VideoCalleeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ConstantApp.CALL_RESPONSE, callResponse);
        intent.putExtra(ConstantApp.USER_CALLER_CALLEE, callUserType);
        context.startActivity(intent);
//        context.finish();
    }

    public void navigateToLiveCall(@NonNull Activity context, LiveCallResponse liveCallResponse) {
        Intent intent;
        intent = new Intent(context, LiveRoomActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ConstantApp.ACTION_KEY_CROLE, Constants.CLIENT_ROLE_BROADCASTER);
        intent.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, liveCallResponse.getChannelName());
        intent.putExtra("LIVE_USER", liveCallResponse.getRegistration().getProfileName());
        intent.putExtra("ROOT_USER", true);
        intent.putExtra(ConstantApp.CALL_RESPONSE, liveCallResponse);
        context.startActivity(intent);
//        context.finish();
    }


    /*public void navigateToChatting(@NonNull Activity context, MessageListModel messageListModel) {
        Intent intent;
        intent = new Intent(context, MessageChatActivity.class);
        intent.putExtra("CHATTING_MODEL", messageListModel);
        context.startActivity(intent);
//        context.finish();
    }*/

    public void navigateToChatting(@NonNull Activity context, ChatListModel chatListModel) {
        Intent intent;
        intent = new Intent(context, MessageChatActivity.class);
        intent.putExtra(ConstantApp.CHATTING_MODEL, chatListModel);
        context.startActivity(intent);
//        context.finish();
    }


    public void navigateToSettings(@NonNull Activity context) {
        Intent intent;
        intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
//        context.finish();
    }

    public void navigateToMatchingPat(@NonNull Activity context) {
        Intent intent;
        intent = new Intent(context, MatchingPartnerQueAnsActivity.class);
        context.startActivity(intent);
//        context.finishAffinity();
    }
}

