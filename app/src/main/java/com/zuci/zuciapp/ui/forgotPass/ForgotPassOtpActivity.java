package com.zuci.zuciapp.ui.forgotPass;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseActivity;
import com.gne.www.lib.PinView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class ForgotPassOtpActivity extends BaseActivity<ForgotPassOTPViewModel> {
    @Inject
    ViewModelFactory<ForgotPassOTPViewModel> viewModelFactory;

    private ForgotPassOTPViewModel forgotPassOTPViewModel;

    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;

    @Override
    protected ForgotPassOTPViewModel getViewModel() {
        return forgotPassOTPViewModel;
    }

    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;
    @BindView(R.id.otp_pin_view)
    PinView otp_pin_view;
    @BindView(R.id.tv_resend_otp_timer)
    TextView tv_resend_otp_timer;
    @BindView(R.id.tv_resend_otp) TextView tv_resend_otp;
    @BindView(R.id.btn_otp_submit_forgot)
    AppCompatButton btn_otp_submit_forgot;

    String emailId = "";
    int registerIdInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_otp);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            emailId = intent.getStringExtra("USER_EMAIL_OTP");
            registerIdInt = intent.getIntExtra("USER_REGISTER_OTP", 0);

        }


        forgotPassOTPViewModel = ViewModelProviders.of(this, viewModelFactory).get(ForgotPassOTPViewModel.class);

        bindViewModel();

        timer();
    }


    @OnClick(R.id.tv_resend_otp)
    public void timerSendOtp() {
        if (isOnline()) {
            timer();
            btn_otp_submit_forgot.setBackground(btn_otp_submit_forgot.getContext().getResources().getDrawable(R.drawable.shape_btn));
            tv_resend_otp.setVisibility(View.GONE);
            tv_resend_otp_timer.setVisibility(View.VISIBLE);
            forgotPassOTPViewModel.resendOtpMailId(emailId);
        } else {
            navigator.navigateInternetConnectErrorScreen(this);
            rightToLeftAnimated();
        }
    }


    public void timer(){
        new CountDownTimer(63000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                tv_resend_otp_timer.setText(f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
//                textView.setText("00:00:00");
                tv_resend_otp.setVisibility(View.VISIBLE);
                tv_resend_otp_timer.setVisibility(View.GONE);
            }
        }.start();
    }


    @OnClick(R.id.btn_otp_submit_forgot)
    public void otpVerify() {
        verifyOtp();
    }

    private void verifyOtp() {

        String otpVerify = otp_pin_view.getText().toString().trim();

        if (otpVerify.length() == 4) {
            if (isOnline()) {
                btn_otp_submit_forgot.setBackground(btn_otp_submit_forgot.getContext().getResources().getDrawable(R.drawable.shape_btn2));
                forgotPassOTPViewModel.forgotPasswordMailId(registerIdInt, emailId, otpVerify);
            } else {
                navigator.navigateInternetConnectErrorScreen(this);
                rightToLeftAnimated();
            }
        } else {
            Toast.makeText(this, "Please enter otp !!", Toast.LENGTH_SHORT).show();
        }
    }

    private void bindViewModel() {
        forgotPassOTPViewModel.getForgotOTPResponse()
                .observe(this, forgotPass -> {
                    assert forgotPass != null;
                    switch (forgotPass.status) {
                        case SUCCESS:
                            try {
                                btn_otp_submit_forgot.setBackground(btn_otp_submit_forgot.getContext().getResources().getDrawable(R.drawable.shape_btn));
                                String response = String.valueOf(forgotPass.responce);
//                                JSONObject jsonObject = new JSONObject(response);

//                                int otpVerify=jsonObject.getInt(response);
//                                if(otpVerify==1){
                                if (response.equalsIgnoreCase("1")) {
                                    navigator.navigateToNewPasswordScreen(this,registerIdInt,emailId);
                                    rightToLeftAnimated();
                                } else {
                                    Toast.makeText(this, "OTP not match !!", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            btn_otp_submit_forgot.setBackground(btn_otp_submit_forgot.getContext().getResources().getDrawable(R.drawable.shape_btn));
                            assert forgotPass.error != null;
                            onErrorMessage(content_parent, forgotPass.error);
                            break;
                    }
                });

        forgotPassOTPViewModel.getResendOtpResponse()
                .observe(this, loginApi -> {
                    assert loginApi != null;
                    switch (loginApi.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(loginApi.responce);
//                                JSONObject jsonObject = new JSONObject(response);

                                  /*   String removeChar = response.substring(1, response.length() - 1);

                                if (removeChar.equalsIgnoreCase("Email Not Registered")) {
                                    Toast.makeText(this, "Email Not Registered !!", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONObject jsonObject = new JSONObject(response);

                                    int registerId = jsonObject.getInt("RegistrationId");
                                    navigator.navigateToForgotOTPScreen(this, emailId,registerId);
                                    rightToLeftAnimated();
                                }*/

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert loginApi.error != null;
                            onErrorMessage(content_parent, loginApi.error);
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