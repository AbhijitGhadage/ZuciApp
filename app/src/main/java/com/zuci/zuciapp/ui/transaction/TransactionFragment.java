package com.zuci.zuciapp.ui.transaction;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseFragment;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class TransactionFragment  extends BaseFragment<PointsViewModel> {
    @Inject
    ViewModelFactory<PointsViewModel> viewModelFactory;
    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;

    private PointsViewModel viewModel;
    @Override
    protected PointsViewModel getViewModel() {
        return viewModel;
    }
    @Override
    public void onAttach(@NotNull Context mContext) {
        AndroidSupportInjection.inject(this);
        super.onAttach(mContext);
    }

    public TransactionFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.tv_total_coins) TextView tv_total_coins;

    View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_transaction, container, false);
        ButterKnife.bind(this, root);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PointsViewModel.class);
        int userId = sharedPref.getRegisterId();
        tv_total_coins.setText(sharedPref.getTotalCoins());
        TransactionTabAdapter tabsPagerAdapter = new TransactionTabAdapter(getActivity(), getActivity().getSupportFragmentManager(), userId);
        viewPager.setAdapter(tabsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return root;
    }
}