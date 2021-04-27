package com.zuci.zuciapp.ui.mainPage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.firebase.CallHistoryRepository;
import com.zuci.zuciapp.firebase.CallHistoryRepositoryImpl;
import com.zuci.zuciapp.firebase.FirestoreChildCallBack;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.agoraLive.LiveCallResponse;
import com.zuci.zuciapp.ui.transaction.PointsAddCoinsModel;
import com.zuci.zuciapp.ui.agoraVoiceCall.CallResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.base.BaseActivity;
import com.zuci.zuciapp.ui.firestoreMessageChat.messageChatHistory.ChatListModel;
import com.zuci.zuciapp.ui.home.HomeFragment;
import com.zuci.zuciapp.ui.home.HomeListModel;
import com.zuci.zuciapp.ui.reels.ReelsVideoFragment;
import com.zuci.zuciapp.ui.firestoreMessageChat.ChatFragment;
import com.zuci.zuciapp.ui.transaction.TransactionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.razorpay.PaymentResultListener;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class MainHomeActivity extends BaseActivity<MainHomeViewModel> implements PaymentResultListener {
    @Inject
    ViewModelFactory<MainHomeViewModel> viewModelFactory;
    private MainHomeViewModel homeViewModel;
    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;

    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;

    public FragmentManager mFragmentManager = getSupportFragmentManager();
    private CallHistoryRepository callHistoryRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);
        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainHomeViewModel.class);

        Intent intent = getIntent();
        if (intent != null) {
            boolean firstTimeLogin = intent.getBooleanExtra("FirstTimeLogin", false);
            if (firstTimeLogin)
                attachFragment(new TransactionFragment());
            else
//          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                attachFragment(new HomeFragment());
        }

        rightToLeftAnimated();
        callHistoryRepository = new CallHistoryRepositoryImpl();
        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
        checkIfCallReceived();

        bindViewModel();
    }

    @Override
    protected MainHomeViewModel getViewModel() {
        return homeViewModel;
    }

    private void checkIfCallReceived() {
        callHistoryRepository.notifyCallReceived(sharedPref.getRegisterId(), new FirestoreChildCallBack() {
            @Override
            public void onChildAdded(Object object) {
                if (object != null) {
                    CallResponse callResponse = (CallResponse) object;
                    if (callResponse.getCallType().equals(ConstantApp.AUDIO_CALL))
                        openVoiceCall(callResponse, ConstantApp.CALLEE);
                    else
                        openVideoCall(callResponse, ConstantApp.CALLEE);
                }
            }

            @Override
            public void onChildModified(Object object) {
            }

            @Override
            public void onChildRemoved(Object object) {
            }

            @Override
            public void onError(Object object) {
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        attachFragment(new HomeFragment());
                        rightToLeftAnimated();
                        return true;
                    case R.id.navigation_payment:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            attachFragment(new TransactionFragment());
                            rightToLeftAnimated();
                        }
                        return true;
                    case R.id.navigation_profile:
                        attachFragment(new ReelsVideoFragment());
                        rightToLeftAnimated();
                        return true;
                    case R.id.navigation_chat:
                        attachFragment(new ChatFragment());
                        rightToLeftAnimated();
                        return true;
                }
                return false;
            };

    public void attachFragment(Fragment mCurrentFragment) {
        String tag = mCurrentFragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction
                .replace(R.id.nav_bottom_fragment, mCurrentFragment, tag)
                .addToBackStack(tag)
                .commit();
    }


    public boolean isFragmentPresent(String tag) {
        Fragment frag = getSupportFragmentManager().findFragmentByTag(tag);
        return frag instanceof HomeFragment;
    }

    public void clearStack() {
        //Here we are clearing back stack fragment entries
        int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }
    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
        if (backStackEntryCount == 0 || backStackEntryCount == 1)
            closeApp();
        else {
            super.onBackPressed();
        }
    }

    @SuppressLint("NewApi")
    public void closeApp() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure want to close this application");
        alertDialogBuilder.setPositiveButton("yes", (arg0, arg1) -> {
            finishAffinity();
        });
        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> {
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void openDetails(HomeListModel homeListModel) {
        navigator.navigateToDetails(this, homeListModel);
        rightToLeftAnimated();
    }

    public void openLogOutScreen() {
        navigator.navigateToLogOutScreen(this);
        leftToRightAnimated();
    }

    public void openBasicProfile() {
        navigator.navigateToBasicProfile(this);
        rightToLeftAnimated();
    }

    public void openGallery() {
        navigator.navigateToGallery(this);
        rightToLeftAnimated();
    }

    public void openGalleryVideo() {
        navigator.navigateToGalleryVideo(this);
        rightToLeftAnimated();
    }

    public void openChangePass() {
        navigator.navigateToChangePass(this);
        rightToLeftAnimated();
    }

    public void openFeedback() {
        navigator.navigateToFeedback(this);
        rightToLeftAnimated();
    }

    public void openVoiceCall(CallResponse callResponse, int callUserType) {
        navigator.navigateToVoiceCall(this, callResponse, callUserType);
        rightToLeftAnimated();
    }

    public void openVideoCall(CallResponse callResponse, int callUserType) {
        navigator.navigateToVideoCall(this, callResponse, callUserType);
        rightToLeftAnimated();
    }

    public void openLiveCall(LiveCallResponse liveCallResponse) {
        navigator.navigateToLiveCall(this, liveCallResponse);
        rightToLeftAnimated();
    }

    public void openChatting(ChatListModel chatListModel) {
        navigator.navigateToChatting(this, chatListModel);
        rightToLeftAnimated();
    }

    public void openSettings() {
        navigator.navigateToSettings(this);
        rightToLeftAnimated();
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            addCoins(razorpayPaymentID.toString());  // api call

            Toast.makeText(this, "Transaction Id: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("com.merchant", e.getMessage(), e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    public void onPaymentError(int code, String response) {
        try {
            Log.e("com.merchant", "Payment failed: " + Integer.toString(code) + " " + response);
            Toast.makeText(this, "Payment failed: retry...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("com.merchant", e.getMessage(), e);
        }
    }

    private void addCoins(String razorpayTranId) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        String dateTime = sdf.format(new Date());

        double addAmount = Double.parseDouble(sharedPref.getAmount());
        String amtIntCoins = sharedPref.getAmountCoins();

        PointsAddCoinsModel pointsAddCoinsModel = new PointsAddCoinsModel();
        pointsAddCoinsModel.setRegistrationId(sharedPref.getRegisterId());
        pointsAddCoinsModel.setAmount(addAmount);
        pointsAddCoinsModel.setCoins(String.valueOf(amtIntCoins));
        pointsAddCoinsModel.setTransactionId(razorpayTranId);
        pointsAddCoinsModel.setTransactionDate(dateTime);

        homeViewModel.addCoinsApi(pointsAddCoinsModel);
    }

    public void bindViewModel() {

        homeViewModel.getCoinsResponse()
                .observe(this, pointsAdd -> {
                    assert pointsAdd != null;
                    switch (pointsAdd.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(pointsAdd.responce);
                                JSONObject jsonObject = new JSONObject(response);

                                sharedPref.setTotalCoins(jsonObject.getString("TotalCoins"));
//                                tv_total_coins.setText(sharedPref.getTotalCoins());
                                Toast.makeText(this, "Coins Added Successful !!", Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert pointsAdd.error != null;
                            showErrorMessage(content_parent, pointsAdd.error.getMessage());
                            break;
                    }
                });
    }


}