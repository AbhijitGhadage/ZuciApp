package com.zuci.zuciapp.ui.signUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseActivity;
import com.zuci.zuciapp.utils.Methods;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;


public class GenderActivity extends BaseActivity<SignUpGenderViewModel> {
    private static final String TAG = "GenderActivity";
    @Inject
    ViewModelFactory<SignUpGenderViewModel> viewModelFactory;
    private SignUpGenderViewModel signUpGenderViewModel;
    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;

    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;
    @BindView(R.id.card_signUp_male)
    CardView card_signUp_male;
    @BindView(R.id.card_signUp_Female)
    CardView card_signUp_Female;
    @BindView(R.id.btn_gender_continue)
    AppCompatButton btn_gender_continue;

    String fullName, email, userMobile, password, rePassword, dob, nikeName;
    String gender = "";
    String deviceToken = "";
    int ageCal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            fullName = intent.getStringExtra("FULL_NAME");
            nikeName = intent.getStringExtra("NIKE_NAME");
            userMobile = intent.getStringExtra("USER_MOBILE");
            email = intent.getStringExtra("USER_EMAIL");
            dob = intent.getStringExtra("USER_DOB");
            ageCal = intent.getIntExtra("USER_AGE", 0);
            password = intent.getStringExtra("USER_PASS");
            rePassword = intent.getStringExtra("USER_RE_PASS");
        }

        Log.d(TAG, "Refreshed token: " + deviceToken);
        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
                deviceToken = "";
                deviceToken = instanceIdResult.getToken();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        signUpGenderViewModel = ViewModelProviders.of(this, viewModelFactory).get(SignUpGenderViewModel.class);

        bindViewModel();

    }

    @Override
    protected SignUpGenderViewModel getViewModel() {
        return signUpGenderViewModel;
    }

    @OnClick(R.id.card_signUp_male)
    public void OnClickSignUpMale() {
        gender = "Male";
        card_signUp_male.setCardBackgroundColor(getResources().getColor(R.color.app_cardView_white2));
        card_signUp_Female.setCardBackgroundColor(getResources().getColor(R.color.app_cardView_white));
    }

    @OnClick(R.id.card_signUp_Female)
    public void OnClickSignUpFemale() {
        gender = "Female";
        card_signUp_male.setCardBackgroundColor(getResources().getColor(R.color.app_cardView_white));
        card_signUp_Female.setCardBackgroundColor(getResources().getColor(R.color.app_cardView_white2));
    }

    @OnClick(R.id.btn_gender_continue)
    public void OnClickSignUp() {
        signUpClick();
    }

    private void signUpClick() {
        if (gender.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please select gender !!", Toast.LENGTH_SHORT).show();
        } else {
            if (isOnline()) {
                SignUpModel signUpModel = new SignUpModel();
                signUpModel.setProfileName(fullName);
                signUpModel.setNickName(nikeName);
                signUpModel.setMobileNo(userMobile);
                signUpModel.setEmailId(email);
                signUpModel.setDOB(dob);
                signUpModel.setAge(ageCal);
                signUpModel.setRegType("Normal");
                signUpModel.setPassword(password);
                signUpModel.setGender(gender);
                signUpModel.setDeviceToken(deviceToken);

                btn_gender_continue.setBackground(btn_gender_continue.getContext().getResources().getDrawable(R.drawable.shape_btn2));
                signUpGenderViewModel.signUpUser(signUpModel);

            } else {
                navigator.navigateInternetConnectErrorScreen(this);
                rightToLeftAnimated();
            }
        }
    }


    private void bindViewModel() {
        signUpGenderViewModel.getSignUpResponse()
                .observe(this, forgotPass -> {
                    assert forgotPass != null;
                    switch (forgotPass.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(forgotPass.responce);
                                JSONObject jsonObject = new JSONObject(response);
                                btn_gender_continue.setBackground(btn_gender_continue.getContext().getResources().getDrawable(R.drawable.shape_btn));

                                if (jsonObject.getInt("RegistrationId") == 0) {
                                    Toast.makeText(this, "Email ID already exists !", Toast.LENGTH_SHORT).show();
                                } else {
                                    sharedPref.setRegisterId(jsonObject.getInt("RegistrationId"));
                                    sharedPref.setUserName(jsonObject.getString("ProfileName"));
                                    sharedPref.setUserEmail(jsonObject.getString("EmailId"));
                                    sharedPref.setUserProfile(jsonObject.getString("ProfileImage"));
                                    sharedPref.setGender(jsonObject.getString("Gender"));
                                    sharedPref.setDob(jsonObject.getString("DOB"));
                                    sharedPref.setRegisterType(jsonObject.getString("RegType"));
                                    sharedPref.setCountry(jsonObject.getInt("CountryId"));
//                                sharedPref.setLogin("True");

                                    if (!Methods.isEmptyOrNull(jsonObject.getString("Bio")))
                                        sharedPref.setBio(jsonObject.getString("Bio"));
                                    else
                                        sharedPref.setBio("");

                                    if (!Methods.isEmptyOrNull(jsonObject.getString("Address")))
                                        sharedPref.setAddress(jsonObject.getString("Address"));
                                    else
                                        sharedPref.setAddress("");

                                    if (!Methods.isEmptyOrNull(jsonObject.getString("MobileNo")))
                                        sharedPref.setUserPhone(jsonObject.getString("MobileNo"));
                                    else
                                        sharedPref.setUserPhone("");

                                    if (!Methods.isEmptyOrNull(jsonObject.getString("TotalCoins"))) {
                                        sharedPref.setTotalCoins(jsonObject.getString("TotalCoins"));
                                    } else
                                        sharedPref.setTotalCoins("0");

                                    sharedPref.setAudioCall(jsonObject.getInt("AudioCallCoins"));
                                    sharedPref.setVideoCall(jsonObject.getInt("VideoCallCoins"));
                                    sharedPref.setImageRate(jsonObject.getInt("ImageCoins"));
                                    sharedPref.setVideoRate(jsonObject.getInt("VideoCoins"));

                                    try {
                                        sharedPref.setAge(String.valueOf(jsonObject.getInt("Age")));
                                    } catch (Exception ignored) {
                                        sharedPref.setAge("0");
                                    }

                                    int registerIdInt = jsonObject.getInt("RegistrationId");
                                    String emailId = jsonObject.getString("EmailId");
                                    String otp = jsonObject.getString("Otp");

                                    if (isOnline()) {
                                        navigator.navigateToOtpScreen(this, registerIdInt, emailId, otp);
                                        rightToLeftAnimated();
                                    } else {
                                        navigator.navigateInternetConnectErrorScreen(this);
                                        rightToLeftAnimated();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            btn_gender_continue.setBackground(btn_gender_continue.getContext().getResources().getDrawable(R.drawable.shape_btn));
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