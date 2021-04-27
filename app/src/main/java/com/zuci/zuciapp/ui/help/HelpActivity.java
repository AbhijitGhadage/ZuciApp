package com.zuci.zuciapp.ui.help;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class HelpActivity extends BaseActivity<HelpViewModel> {
    @Inject
    ViewModelFactory<HelpViewModel> viewModelFactory;

    private HelpViewModel helpViewModel;

    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;

    @Override
    protected HelpViewModel getViewModel() {
        return helpViewModel;
    }


    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);


        helpViewModel = ViewModelProviders.of(this, viewModelFactory).get(HelpViewModel.class);



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