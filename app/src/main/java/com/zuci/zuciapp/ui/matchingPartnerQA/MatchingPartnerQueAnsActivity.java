package com.zuci.zuciapp.ui.matchingPartnerQA;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.base.BaseActivity;
import com.zuci.zuciapp.utils.Methods;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class MatchingPartnerQueAnsActivity extends BaseActivity<MatchingPartnerQueAnsViewModel> {
    private final String TAG = MatchingPartnerQueAnsActivity.class.getSimpleName();
    private MatchingPartnerQueAnsViewModel partnerQueAnsViewModel;
    private MatchingPartnerQAPagerAdapter viewPagerAdapter;
    private List<QueAnsModel> queAnsModelList;
    private List<MatchingPartnerQueAnsModel> matchingQueAnsModelList;
    private int page = 0;

    @BindView(R.id.container)
    ViewPager container;
    @BindView(R.id.iv_prev)
    ImageView ivPrev;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.txtQueNumber)
    TextView txtQueNumber;
    @BindView(R.id.tv_total_que)
    TextView tvTotalQue;

    @Inject
    SharedPref sharedPref;

    @Inject
    ViewModelFactory<MatchingPartnerQueAnsViewModel> viewModelFactory;

    @Override
    protected MatchingPartnerQueAnsViewModel getViewModel() {
        return partnerQueAnsViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_partner_que_ans);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);
        partnerQueAnsViewModel = ViewModelProviders.of(this, viewModelFactory).get(MatchingPartnerQueAnsViewModel.class);
        setInitialization();
        onPageChangeListener();
        getMatchingPartnerQAList();
        onClickPrevious();
        onClickNext();
    }

    private void setInitialization() {
        matchingQueAnsModelList = new ArrayList<>();
        /*viewPagerAdapter = new MatchingPartnerQAPagerAdapter(this, queAnsModelList);
        container.setAdapter(viewPagerAdapter);*/
    }

    private void getMatchingPartnerQAList() {
        try {
            partnerQueAnsViewModel.getQueAnsModelList().observe(this, queAnsModels -> {
                if (queAnsModels != null && queAnsModels.size() > 0) {
                    tvTotalQue.setText("/ " + queAnsModels.size());
                    queAnsModelList = queAnsModels;
                    setDefaultMatchingQueAnsList(queAnsModels);
                    viewPagerAdapter = new MatchingPartnerQAPagerAdapter(this, queAnsModelList);
                    container.setAdapter(viewPagerAdapter);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void onPageChangeListener() {
        container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                ivPrev.setVisibility((page == 0) ? View.GONE : View.VISIBLE);
                if (!Methods.isEmptyOrNull(matchingQueAnsModelList.get(position).getAnswer()))
                    ivNext.setVisibility(View.VISIBLE);
                else
                    ivNext.setVisibility(View.GONE);
                updateIndicators(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void updateIndicators(int position) {
        txtQueNumber.setText(String.valueOf(position + 1));
    }

    private void onClickNext() {
        ivNext.setOnClickListener(v -> setNextView());
    }

    public void setNextView() {
        page += 1;
        if (page >= queAnsModelList.size())
            saveMatchingQueAnsData();
        else
            container.setCurrentItem(page, true);
    }

    private void onClickPrevious() {
        ivPrev.setOnClickListener(v -> setPreviousView());
    }

    public void setPreviousView() {
        page -= 1;
        if (page < 0)
            finish();
        else
            container.setCurrentItem(page, true);
    }

    private void setDefaultMatchingQueAnsList(List<QueAnsModel> queAnsModels) {
        for (QueAnsModel queAnsModel : queAnsModels) {
            MatchingPartnerQueAnsModel partnerQueAnsModel = new MatchingPartnerQueAnsModel();
            if (queAnsModel != null) {
                partnerQueAnsModel.setqId(queAnsModel.getqId());
                partnerQueAnsModel.setRegId(sharedPref.getRegisterId());
            }
            matchingQueAnsModelList.add(partnerQueAnsModel);
        }
    }

    private void saveMatchingQueAnsData() {
        try {
            partnerQueAnsViewModel.addPartnerQueAnsList(matchingQueAnsModelList).observe(this, appResponse -> {
                if (appResponse != null) {
                    if (appResponse.status)
                        finish();
                    Methods.showMessage(MatchingPartnerQueAnsActivity.this, appResponse.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public MatchingPartnerQueAnsModel getMatchingPartnerModelByQueId(long qId) {
        if (matchingQueAnsModelList != null && matchingQueAnsModelList.size() > 0) {
            for (MatchingPartnerQueAnsModel partnerQueAnsModel : matchingQueAnsModelList) {
                if (qId == partnerQueAnsModel.getqId())
                    return partnerQueAnsModel;
            }
        }
        return null;
    }

    public void updateMatchingPartnerModelByQueId(MatchingPartnerQueAnsModel partnerQueAnsModel) {
        if (partnerQueAnsModel != null) {
            for (int index = 0; index < matchingQueAnsModelList.size(); index++) {
                if (matchingQueAnsModelList.get(index).getqId() == partnerQueAnsModel.getqId()) {
                    matchingQueAnsModelList.set(index, partnerQueAnsModel);
                }
            }
        }
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