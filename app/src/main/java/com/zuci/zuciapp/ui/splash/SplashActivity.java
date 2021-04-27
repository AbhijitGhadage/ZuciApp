package com.zuci.zuciapp.ui.splash;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseActivity;
import com.zuci.zuciapp.utils.Methods;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;


public class SplashActivity extends BaseActivity<SplashViewModel> {

    @Inject
    ViewModelFactory<SplashViewModel> viewModelFactory;

    private SplashViewModel viewModel;

    @Inject
    Navigator navigator;

    @Inject
    SharedPref sharedPref;

    @Override
    protected SplashViewModel getViewModel() {
        return viewModel;
    }

    @BindView(R.id.rel_splash)
    RelativeLayout rel_splash;

    private String[] permissions = {
//            Manifest.permission.READ_CONTACTS,
//            Manifest.permission.WRITE_CONTACTS,
//            Manifest.permission.CALL_PHONE,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);

//        setStatusBarColor(R.color.colorBlack);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel.class);

        viewModel.getTotalCoins(sharedPref.getRegisterId());

        bindViewModel();
    }

    private void bindViewModel() {
        viewModel.getTotalCoinsResponse()
                .observe(this, loginApi -> {
                    assert loginApi != null;
                    switch (loginApi.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(loginApi.responce);
                                JSONObject jsonObject = new JSONObject(response);

                                if (jsonObject.getBoolean("status")) {

                                    String totalCoins = jsonObject.getString("TotalCoins");
                                    if (!Methods.isEmptyOrNull(totalCoins)) {
                                        sharedPref.setTotalCoins(totalCoins);
                                    } else
                                        sharedPref.setTotalCoins("0");

                                    sharedPref.setLiveCall(jsonObject.getInt("LiveCallCoins"));
                                    sharedPref.setAudioCall(jsonObject.getInt("AudioCallCoins"));
                                    sharedPref.setVideoCall(jsonObject.getInt("VideoCallCoins"));
                                    sharedPref.setImageRate(jsonObject.getInt("ImageCoins"));
                                    sharedPref.setVideoRate(jsonObject.getInt("VideoCoins"));

                                    sharedPref.setFollowers(jsonObject.getLong("FollowerCount"));
                                    sharedPref.setFollowing(jsonObject.getLong("FollowingCount"));
                                    sharedPref.setPostCount(jsonObject.getLong("PostCount"));

                                } else {
//                                    Toast.makeText(this, "NOt found", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert loginApi.error != null;
                            onErrorMessage(rel_splash, loginApi.error);
                            break;
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean arePermissionsEnabled() {
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestMultiplePermissions() {
        List<String> remainingPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                remainingPermissions.add(permission);
            }
        }
        requestPermissions(remainingPermissions.toArray(new String[remainingPermissions.size()]), 101);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (arePermissionsEnabled()) {
                initialize();
            } else {
                requestMultiplePermissions();
            }
        }
    }

    private void initialize() {
        int SPLASH_DELAY_MILLIS = 3000;
        new Handler().postDelayed(this::goToNextScreen, SPLASH_DELAY_MILLIS);
    }

    private void goToNextScreen() {
        if (sharedPref.getLogin().equals("True")) {
            navigator.navigateToHome(this);
        } else {
            navigator.navigateToLoginScreen(this);
        }
        rightToLeftAnimated();
    }

}
