package com.zuci.zuciapp.ui.homeViewDetails.tabFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class ProfileFragment extends BaseFragment<TabViewModel> {

    @Inject
    ViewModelFactory<TabViewModel> viewModelFactory;
    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;
    TabViewModel viewModel;

    @Override
    public TabViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, root);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TabViewModel.class);


        return root;
    }


}