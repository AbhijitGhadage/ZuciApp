package com.zuci.zuciapp.ui.changePass;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

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

public class ChangePasswordActivity extends BaseActivity<ChangePasswordViewModel> {
    @Inject
    ViewModelFactory<ChangePasswordViewModel> viewModelFactory;

    private ChangePasswordViewModel changePasswordViewModel;

    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;

    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;
    @BindView(R.id.edit_change_pass_old)
    EditText edit_change_pass_old;
    @BindView(R.id.edit_change_pass_new)
    EditText edit_change_pass_new;
    @BindView(R.id.edit_change_pass_con)
    EditText edit_change_pass_con;
    @BindView(R.id.btn_changePass)
    AppCompatButton btn_changePass;

    @Override
    protected ChangePasswordViewModel getViewModel() {
        return changePasswordViewModel;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        changePasswordViewModel = ViewModelProviders.of(this, viewModelFactory).get(ChangePasswordViewModel.class);

        bindViewModel();

    }

    @OnClick(R.id.btn_changePass)
    public void OnClickChangePass() {
        doValidation();

    }

    private void doValidation() {
        String oldPass = edit_change_pass_old.getText().toString().trim();
        String oldNewPass = edit_change_pass_new.getText().toString().trim();
        String oldConPass = edit_change_pass_con.getText().toString().trim();

        int registerId = sharedPref.getRegisterId();
        String emailId = sharedPref.getUserEmail();

        if (oldPass.equalsIgnoreCase("")) {
            edit_change_pass_old.setError("Please enter old password !!");
            edit_change_pass_old.requestFocus();
        } else if (oldNewPass.equalsIgnoreCase("")) {
            edit_change_pass_new.setError("Please enter new password !!");
            edit_change_pass_new.requestFocus();
        } else if (oldConPass.equalsIgnoreCase("")) {
            edit_change_pass_con.setError("Please enter confirm password !!");
            edit_change_pass_con.requestFocus();
        } else if (!oldNewPass.equals(oldConPass)) {
            edit_change_pass_con.setError("Password NOT match !!");
            edit_change_pass_con.requestFocus();
            edit_change_pass_con.setText("");
        } else {

            if (isOnline()) {
                btn_changePass.setBackground(btn_changePass.getContext().getResources().getDrawable(R.drawable.shape_btn2));
                changePasswordViewModel.changePassword(emailId, oldPass, oldNewPass);
            } else {
                navigator.navigateInternetConnectErrorScreen(this);
                rightToLeftAnimated();
            }
        }
    }


    private void bindViewModel() {
        changePasswordViewModel.getChangePassResponse()
                .observe(this, forgotPass -> {
                    assert forgotPass != null;
                    switch (forgotPass.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(forgotPass.responce);
//                                JSONObject jsonObject = new JSONObject(response);

                                btn_changePass.setBackground(btn_changePass.getContext().getResources().getDrawable(R.drawable.shape_btn));
                                String removeChar = response.substring(1, response.length() - 1);

                                if (removeChar.equalsIgnoreCase("Password Change Successfully !")) {
                                    onBackPressed();

                                } else {
                                    Toast.makeText(this, "EmailId Or Password Not Matched !!", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            btn_changePass.setBackground(btn_changePass.getContext().getResources().getDrawable(R.drawable.shape_btn));
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