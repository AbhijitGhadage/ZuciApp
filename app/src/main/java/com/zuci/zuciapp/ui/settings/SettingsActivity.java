package com.zuci.zuciapp.ui.settings;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseActivity;
import com.zuci.zuciapp.ui.splash.SplashViewModel;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class SettingsActivity  extends BaseActivity<SplashViewModel> {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel.class);

    }
    @OnClick(R.id.ll_invite)
    public void shareLink() {
        shareIt();
    }

    private void shareIt() {
        Intent intent =new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Zucci App");
        intent.putExtra(Intent.EXTRA_TEXT,"Zuci Android App Development http://zucis.in/");
        intent.setType("text/plain");
        startActivity(intent);
    }

    @OnClick(R.id.ll_que_ans)
    public void que_ans(){
        navigator.navigateToMatchingPat(this);
        rightToLeftAnimated();
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
}