package com.zuci.zuciapp.di.modules;

import com.zuci.zuciapp.ui.transaction.AddCoinsRateFragment;
import com.zuci.zuciapp.ui.transaction.PointsFragment;
import com.zuci.zuciapp.ui.agoraVideoCall.VideoCalleeActivity;
import com.zuci.zuciapp.ui.agoraVideoCall.VideoCallerActivity;
import com.zuci.zuciapp.ui.agoraVoiceCall.VoiceCalleeActivity;
import com.zuci.zuciapp.ui.agoraVoiceCall.VoiceCallerActivity;
import com.zuci.zuciapp.ui.changePass.ChangePasswordActivity;
import com.zuci.zuciapp.ui.firestoreMessageChat.messageChatHistory.MessageListFragment;
import com.zuci.zuciapp.ui.chat.message.readMessage.MessageChatActivity;
import com.zuci.zuciapp.ui.feedback.FeedBackActivity;
import com.zuci.zuciapp.ui.forgotPass.ForgotPassOtpActivity;
import com.zuci.zuciapp.ui.forgotPass.NewPasswordActivity;
import com.zuci.zuciapp.ui.gallery.GalleryActivity;
import com.zuci.zuciapp.ui.gallery.UploadImgActivity;
import com.zuci.zuciapp.ui.galleryVideo.GalleryVideoActivity;
import com.zuci.zuciapp.ui.galleryVideo.UploadVideoActivity;
import com.zuci.zuciapp.ui.imgVideoView.ViewImageActivity;
import com.zuci.zuciapp.ui.imgVideoView.ViewVideoActivity;
import com.zuci.zuciapp.ui.help.HelpActivity;
import com.zuci.zuciapp.ui.homeViewDetails.HomeViewDetailActivity;
import com.zuci.zuciapp.ui.forgotPass.ForgotPasswordActivity;
import com.zuci.zuciapp.ui.home.HomeFragment;
import com.zuci.zuciapp.ui.homeViewDetails.tabFragment.ApplicentFragment;
import com.zuci.zuciapp.ui.homeViewDetails.tabFragment.ImageFragment;
import com.zuci.zuciapp.ui.homeViewDetails.tabFragment.VideoFragment;
import com.zuci.zuciapp.ui.mainPage.MainHomeActivity;
import com.zuci.zuciapp.ui.matchingPartnerQA.MatchingPartnerQueAnsActivity;
import com.zuci.zuciapp.ui.navigation.NavigationFragment;
import com.zuci.zuciapp.ui.network.NetWorkActivity;
import com.zuci.zuciapp.ui.homeViewDetails.tabFragment.ProfileFragment;
import com.zuci.zuciapp.ui.profileCreate.ProfileCreateActivity;
import com.zuci.zuciapp.ui.reels.ReelsVideoFragment;
import com.zuci.zuciapp.ui.settings.SettingsActivity;
import com.zuci.zuciapp.ui.signUp.OtpActivity;
import com.zuci.zuciapp.ui.signUp.GenderActivity;
import com.zuci.zuciapp.ui.login.LoginActivity;
import com.zuci.zuciapp.ui.signUp.SignUpActivity;
import com.zuci.zuciapp.ui.splash.SplashActivity;
import com.zuci.zuciapp.ui.transaction.CashWithdrawalFragment;
import com.zuci.zuciapp.ui.transaction.TransactionFragment;
import com.zuci.zuciapp.ui.transaction.TransactionLogFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract SplashActivity bindSplashActivity();

    @ContributesAndroidInjector
    abstract NetWorkActivity bindNetWorkActivity();

    @ContributesAndroidInjector
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector
    abstract SignUpActivity bindSignUpActivity();

    @ContributesAndroidInjector
    abstract GenderActivity bindGenderActivity();

    @ContributesAndroidInjector
    abstract OtpActivity bindOtpActivity();

    @ContributesAndroidInjector
    abstract ProfileCreateActivity bindProfileCreateActivity();

    @ContributesAndroidInjector
    abstract MainHomeActivity bindHomeActivity();

    @ContributesAndroidInjector
    abstract ForgotPasswordActivity bindForgotPasswordActivity();

    @ContributesAndroidInjector
    abstract HomeViewDetailActivity bindDetailActivity();

    @ContributesAndroidInjector
    abstract GalleryActivity bindGalleryActivity();

    @ContributesAndroidInjector
    abstract ChangePasswordActivity bindChangePasswordActivity();

    @ContributesAndroidInjector
    abstract FeedBackActivity bindFeedBackActivity();

    @ContributesAndroidInjector
    abstract HelpActivity bindHelpActivity();

    @ContributesAndroidInjector
    abstract UploadImgActivity bindUploadImgActivity();

    @ContributesAndroidInjector
    abstract ForgotPassOtpActivity bindForgotPassOtpActivity();

    @ContributesAndroidInjector
    abstract NewPasswordActivity bindNewPassOtpActivity();

    @ContributesAndroidInjector
    abstract GalleryVideoActivity bindGalleryVideoActivity();

    @ContributesAndroidInjector
    abstract MessageChatActivity bindMessageChatActivity();

    @ContributesAndroidInjector
    abstract VoiceCallerActivity bindVoiceCallerActivity();

    @ContributesAndroidInjector
    abstract VoiceCalleeActivity bindVoiceCalleeActivity();

    @ContributesAndroidInjector
    abstract VideoCallerActivity bindVideoCallerActivity();

    @ContributesAndroidInjector
    abstract VideoCalleeActivity bindVideoCalleeActivity();

    @ContributesAndroidInjector
    abstract UploadVideoActivity bindUploadVideoActivity();

    @ContributesAndroidInjector
    abstract ViewVideoActivity bindViewVideoActivity();

    @ContributesAndroidInjector
    abstract ViewImageActivity bindViewImageActivity();

    @ContributesAndroidInjector
    abstract SettingsActivity bindSettingsActivity();

    @ContributesAndroidInjector
    abstract MatchingPartnerQueAnsActivity bindMatchingPartnerQueAnsActivity();

//===============================================================================

    @ContributesAndroidInjector
    abstract HomeFragment bindHomeFragment();

    @ContributesAndroidInjector
    abstract NavigationFragment bindSettingFragment();

    @ContributesAndroidInjector
    abstract MessageListFragment bindMessageListFragment();

    @ContributesAndroidInjector
    abstract ReelsVideoFragment bindReelsVideoFragment();

    @ContributesAndroidInjector
    abstract PointsFragment bindPointsFragment();

    @ContributesAndroidInjector
    abstract ApplicentFragment bindApplicentFragment();

    @ContributesAndroidInjector
    abstract ImageFragment bindImageFragment();

    @ContributesAndroidInjector
    abstract VideoFragment bindVideoFragment();

    @ContributesAndroidInjector
    abstract ProfileFragment bindProfileFragment();

    @ContributesAndroidInjector
    abstract TransactionFragment bindTransactionFragment();

    @ContributesAndroidInjector
    abstract CashWithdrawalFragment bindCashWithdrawalFragment();

    @ContributesAndroidInjector
    abstract TransactionLogFragment bindTransactionLogFragment();

    @ContributesAndroidInjector
    abstract AddCoinsRateFragment bindAddCoinsRateFragment();

}

