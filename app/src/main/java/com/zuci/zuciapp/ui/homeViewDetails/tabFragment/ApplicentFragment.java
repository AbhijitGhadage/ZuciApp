package com.zuci.zuciapp.ui.homeViewDetails.tabFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseFragment;
import com.zuci.zuciapp.utils.Methods;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class ApplicentFragment extends BaseFragment<TabViewModel> {

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

    public ApplicentFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.tv_affiliate)
    TextView tv_affiliate;

    static String affiliate;

    public static ApplicentFragment newInstance(String affiliate11) {
        affiliate = affiliate11;
        return new ApplicentFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_applicent, container, false);
        ButterKnife.bind(this, root);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TabViewModel.class);


        if (!Methods.isEmptyOrNull(affiliate))
            if (affiliate.equalsIgnoreCase("null"))
                tv_affiliate.setText("Web site: http://zucis.in/   \n\nLink About us: https://zucis.in/about/  \n\nLink Contact us: https://zucis.in/contact/");
            else
                tv_affiliate.setText(affiliate);
        else
            tv_affiliate.setText("Web site: http://zucis.in/   \n\nLink About us: https://zucis.in/about/  \n\nLink Contact us: https://zucis.in/contact/");

        return root;
    }


}