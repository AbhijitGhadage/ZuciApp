package com.zuci.zuciapp.ui.forgotPass;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

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

public class ForgotPasswordActivity extends BaseActivity<ForgotViewModel> {
    @Inject
    ViewModelFactory<ForgotViewModel> viewModelFactory;

    private ForgotViewModel forgotViewModel;

    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;

    @Override
    protected ForgotViewModel getViewModel() {
        return forgotViewModel;
    }

    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;
    @BindView(R.id.edit_forgot_no) EditText edit_forgot_no;
    @BindView(R.id.btn_forgot_pass)
    AppCompatButton btn_forgot_pass;

    String emailId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        forgotViewModel = ViewModelProviders.of(this, viewModelFactory).get(ForgotViewModel.class);

        bindViewModel();

    }

    @OnClick(R.id.btn_forgot_pass)
    public void OnClickForgotPassword() {
        doValidation();
    }

    private void doValidation() {
        emailId = edit_forgot_no.getText().toString().trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (emailId.equalsIgnoreCase("")) {
            edit_forgot_no.setError("Please enter emailId !!");
            edit_forgot_no.requestFocus();
        } else if (!emailId.matches(emailPattern)) {
            edit_forgot_no.setError("Invalid email address !!");
            edit_forgot_no.requestFocus();
        } else {

            if (isOnline()) {
                btn_forgot_pass.setBackground(btn_forgot_pass.getContext().getResources().getDrawable(R.drawable.shape_btn2));
                forgotViewModel.forgotPasswordMailId(emailId);
            } else {
                navigator.navigateInternetConnectErrorScreen(this);
                rightToLeftAnimated();
            }
        }
    }


    private void bindViewModel() {
        forgotViewModel.getForgotPasswordResponse()
                .observe(this, loginApi -> {
                    assert loginApi != null;
                    switch (loginApi.status) {
                        case SUCCESS:
                            btn_forgot_pass.setBackground(btn_forgot_pass.getContext().getResources().getDrawable(R.drawable.shape_btn));
                            try {
                                String response = String.valueOf(loginApi.responce);
//                                JSONObject jsonObject = new JSONObject(response);

                                String removeChar = response.substring(1, response.length() - 1);

                                if (removeChar.equalsIgnoreCase("Email Not Registered")) {
                                    Toast.makeText(this, "Email Not Registered !!", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONObject jsonObject = new JSONObject(response);

                                    int registerId = jsonObject.getInt("RegistrationId");
                                    navigator.navigateToForgotOTPScreen(this, emailId,registerId);
                                    rightToLeftAnimated();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            btn_forgot_pass.setBackground(btn_forgot_pass.getContext().getResources().getDrawable(R.drawable.shape_btn));
                            assert loginApi.error != null;
                            onErrorMessage(content_parent, loginApi.error);
                            break;
                    }
                });
    }


    @OnClick(R.id.iv_back_btn)
    public void OnClickLogin() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        leftToRightAnimated();
    }

}