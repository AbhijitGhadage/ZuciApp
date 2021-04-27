package com.zuci.zuciapp.ui.signUp;

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

public class OtpActivity extends BaseActivity<OtpViewModel> {

    @Inject
    ViewModelFactory<OtpViewModel> viewModelFactory;

    private OtpViewModel otpViewModel;

    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;

    @Override
    protected OtpViewModel getViewModel() {
        return otpViewModel;
    }


    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;
    @BindView(R.id.otp_pin_view)
    PinView otp_pin_view;
    @BindView(R.id.tv_resend_otp_timer)
    TextView tv_resend_otp_timer;
    @BindView(R.id.tv_resend_otp) TextView tv_resend_otp;
    @BindView(R.id.btn_otp_submit)
    AppCompatButton btn_otp_submit;

    int registerIdInt;
    String emailId, otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        otpViewModel = ViewModelProviders.of(this, viewModelFactory).get(OtpViewModel.class);


        Intent intent = getIntent();
        if (intent != null) {
            registerIdInt = intent.getIntExtra("FULL_REG_ID", 0);
            emailId = intent.getStringExtra("USER_EMAIL");
            otp = intent.getStringExtra("USER_OTP");
            if(otp.equalsIgnoreCase("NotActive")){
                otpViewModel.resendOtpMailId(emailId);
            }
        }

        bindViewModel();

        timer();
    }

    @OnClick(R.id.tv_resend_otp)
    public void timerSendOtp() {
        if (isOnline()) {
            timer();
            btn_otp_submit.setBackground(btn_otp_submit.getContext().getResources().getDrawable(R.drawable.shape_btn));
            tv_resend_otp.setVisibility(View.GONE);
            tv_resend_otp_timer.setVisibility(View.VISIBLE);
            otpViewModel.resendOtpMailId(emailId);
        } else {
            navigator.navigateInternetConnectErrorScreen(this);
            rightToLeftAnimated();
        }
    }

    public void timer() {
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


    @OnClick(R.id.btn_otp_submit)
    public void otpClick() {
        verifyOtp();
    }

    private void verifyOtp() {
        String otpVerify = otp_pin_view.getText().toString().trim();

        if (otpVerify.length() == 4) {
            if (isOnline()) {
                btn_otp_submit.setBackground(btn_otp_submit.getContext().getResources().getDrawable(R.drawable.shape_btn2));
                otpViewModel.otpVerify(registerIdInt, emailId, otpVerify);

            } else {
                navigator.navigateInternetConnectErrorScreen(this);
                rightToLeftAnimated();
            }
        } else {
            Toast.makeText(this, "Please enter otp !!", Toast.LENGTH_SHORT).show();
        }
    }


    private void bindViewModel() {
        otpViewModel.getOtpResponse()
                .observe(this, forgotPass -> {
                    assert forgotPass != null;
                    switch (forgotPass.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(forgotPass.responce);
//                                JSONObject jsonObject = new JSONObject(response);
                                btn_otp_submit.setBackground(btn_otp_submit.getContext().getResources().getDrawable(R.drawable.shape_btn));

//                                int otpVerify=jsonObject.getInt(response);
//                                if(otpVerify==1){
                                if (response.equalsIgnoreCase("1")) {
                                    sharedPref.setLogin("True");
                                    navigator.navigateToProfileCreate(this);
                                    rightToLeftAnimated();
                                } else {
                                    Toast.makeText(this, "OTP not match !!", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            btn_otp_submit.setBackground(btn_otp_submit.getContext().getResources().getDrawable(R.drawable.shape_btn));
                            assert forgotPass.error != null;
                            onErrorMessage(content_parent, forgotPass.error);
                            break;
                    }
                });


        otpViewModel.getResendOtpResponse()
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