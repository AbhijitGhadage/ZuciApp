package com.zuci.zuciapp.ui.forgotPass;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

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

public class NewPasswordActivity extends BaseActivity<NewPasswordViewModel> {
    @Inject
    ViewModelFactory<NewPasswordViewModel> viewModelFactory;

    private NewPasswordViewModel newPasswordViewModel;

    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;

    @Override
    protected NewPasswordViewModel getViewModel() {
        return newPasswordViewModel;
    }

    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;
    @BindView(R.id.edit_new_pass)
    EditText edit_new_pass;
    @BindView(R.id.edit_new_pass_con)
    EditText edit_new_pass_con;

    String emailId = "";
    int registerIdInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);


        Intent intent = getIntent();
        if (intent != null) {
            registerIdInt = intent.getIntExtra("USER_REGISTER_OTP", 0);
            emailId = intent.getStringExtra("USER_EMAIL_OTP");

        }

        newPasswordViewModel = ViewModelProviders.of(this, viewModelFactory).get(NewPasswordViewModel.class);

        bindViewModel();


    }


    @OnClick(R.id.btn_new_pass)
    public void newPass() {
        doValidation();
    }

    private void doValidation() {

        String newPass = edit_new_pass.getText().toString().trim();
        String newPassCon = edit_new_pass_con.getText().toString().trim();

        if (newPass.equalsIgnoreCase("")) {
            edit_new_pass.setError("Please enter password !!");
            edit_new_pass.requestFocus();
        } else if (newPassCon.equalsIgnoreCase("")) {
            edit_new_pass_con.setError("Please enter re-password !!");
            edit_new_pass_con.requestFocus();
        } else if (!newPass.equals(newPassCon)) {
            edit_new_pass_con.setError("Password NOT match !!");
            edit_new_pass_con.requestFocus();
            edit_new_pass_con.setText("");
        } else {


            if (isOnline()) {
                newPasswordViewModel.newPassword(registerIdInt, emailId, newPass);

            } else {

                navigator.navigateInternetConnectErrorScreen(this);
                rightToLeftAnimated();
            }
        }
    }

    private void bindViewModel() {
        newPasswordViewModel.getNewPasswordResponse()
                .observe(this, forgotPass -> {
                    assert forgotPass != null;
                    switch (forgotPass.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(forgotPass.responce);
//                                JSONObject jsonObject = new JSONObject(response);

//                                int otpVerify=jsonObject.getInt(response);
//                                if(otpVerify==1){
                                if (response.equalsIgnoreCase("1")) {
                                    navigator.navigateToLoginScreen(this);
                                    rightToLeftAnimated();
                                } else {
                                    Toast.makeText(this, "New Password Problem !!", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert forgotPass.error != null;
                            onErrorMessage(content_parent, forgotPass.error);
                            break;
                    }
                });
    }

    @OnClick(R.id.iv_back_btn)
    public void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        leftToRightAnimated();
    }

}