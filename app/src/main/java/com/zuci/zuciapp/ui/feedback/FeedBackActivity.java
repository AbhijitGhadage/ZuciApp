package com.zuci.zuciapp.ui.feedback;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseActivity;

import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class FeedBackActivity extends BaseActivity<FeedBackViewModel> {
    @Inject
    ViewModelFactory<FeedBackViewModel> viewModelFactory;

    private FeedBackViewModel feedBackViewModel;

    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;

    @Override
    protected FeedBackViewModel getViewModel() {
        return feedBackViewModel;
    }


    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;
    @BindView(R.id.rb_feedback)
    RatingBar rb_feedback;
    @BindView(R.id.edit_feedback) EditText edit_feedback;
    @BindView(R.id.btn_feedback)
    AppCompatButton btn_feedback;

    int ratingBar = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        AndroidInjection.inject(this);
        ButterKnife.bind(this);


        feedBackViewModel = ViewModelProviders.of(this, viewModelFactory).get(FeedBackViewModel.class);

        bindViewModel();
    }

/*
    @OnClick(R.id.rb_feedback)
    public void OnClickRatingBar(View v) {
        RatingBar bar = (RatingBar) v;
        ratingBar = (int) bar.getRating();

    }*/

    @OnClick(R.id.btn_feedback)
    public void OnClickFeedback() {

        String message = edit_feedback.getText().toString().trim();
        ratingBar = (int) rb_feedback.getRating();

        if (isOnline()) {

            btn_feedback.setBackground(btn_feedback.getContext().getResources().getDrawable(R.drawable.shape_btn2));
            FeedbackModel feedbackModel = new FeedbackModel();
            feedbackModel.setRegistrationId(sharedPref.getRegisterId());
            feedbackModel.setStarNo(ratingBar);
            feedbackModel.setMessage(message);
            feedBackViewModel.feedback(feedbackModel);
        } else {
            navigator.navigateInternetConnectErrorScreen(this);
            rightToLeftAnimated();
        }
    }


    public void bindViewModel() {
        feedBackViewModel.getFeedbackResponse()
                .observe(this, forgotPass -> {
                    assert forgotPass != null;
                    switch (forgotPass.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(forgotPass.responce);
                                JSONObject jsonObject = new JSONObject(response);

                                btn_feedback.setBackground(btn_feedback.getContext().getResources().getDrawable(R.drawable.shape_btn));
                                onBackPressed();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            btn_feedback.setBackground(btn_feedback.getContext().getResources().getDrawable(R.drawable.shape_btn));
                            assert forgotPass.error != null;
                            onErrorMessage(content_parent, forgotPass.error);
                            break;
                    }
                });
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