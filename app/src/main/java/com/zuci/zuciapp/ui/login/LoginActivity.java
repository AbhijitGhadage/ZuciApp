package com.zuci.zuciapp.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseActivity;
import com.zuci.zuciapp.ui.signUp.SignUpModel;
import com.zuci.zuciapp.utils.Methods;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.Arrays;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class LoginActivity extends BaseActivity<LoginViewModel> {

    private static final String TAG = "LoginActivity";

    @Inject
    ViewModelFactory<LoginViewModel> viewModelFactory;

    private LoginViewModel loginViewModel;

    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;

    @Override
    protected LoginViewModel getViewModel() {
        return loginViewModel;
    }

    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;
    @BindView(R.id.edit_login_email)
    EditText edit_login_email;
    @BindView(R.id.edit_login_password)
    EditText edit_login_password;
    @BindView(R.id.btn_facebook_login) ImageView btn_facebook_login;
    @BindView(R.id.btn_login)
    AppCompatButton btn_login;

    // google
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 7;

    // facebook
    @BindView(R.id.login_button)
    LoginButton loginButton;

    private CallbackManager callbackManager;
    private LoginManager loginManager;

    String deviceToken = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);
        Log.d(TAG, "Refreshed token: " + deviceToken);
        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
                deviceToken = "";
                deviceToken = instanceIdResult.getToken();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);
        bindViewModel();
        // google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // facebook
        FacebookSdk.sdkInitialize(LoginActivity.this);
        callbackManager = CallbackManager.Factory.create();
        facebookLogin();


        logOutGoogle();
        logOutFacebook();
        sharedPref.setLogin("False");
    }//======================== onCreate end ==========================

    @OnClick(R.id.forgotpass)
    public void OnClickForgot() {
        navigator.navigateToForgot(this);
        rightToLeftAnimated();
    }

    @OnClick(R.id.ll_link_signup)
    public void OnClickSignUp() {
        navigator.navigateToSignUp(this);
        rightToLeftAnimated();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_login)
    public void OnClickLogin() {
        loginUser();
    }

    private void loginUser() {
        String emailId = edit_login_email.getText().toString().trim();
        String password = edit_login_password.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (emailId.equalsIgnoreCase("")) {
            edit_login_email.setError("Please enter emailId !!");
            edit_login_email.requestFocus();
        } else if (!emailId.matches(emailPattern)) {
            edit_login_email.setError("Invalid email address !!");
            edit_login_email.requestFocus();
        } else if (password.equalsIgnoreCase("")) {
            edit_login_password.setError("Please enter password !!");
            edit_login_password.requestFocus();
        } else {
            if (isOnline()) {
                btn_login.setBackground(btn_login.getContext().getResources().getDrawable(R.drawable.shape_btn2));
                loginViewModel.loginUser(emailId, password, deviceToken);
            } else {
                navigator.navigateInternetConnectErrorScreen(this);
                rightToLeftAnimated();
            }
        }
    }
    // ============================================ google login =============================================
    // http://www.tutorialsface.com/2020/05/integrating-google-sign-in-into-android-app-sample-example-tutorial/
    // https://stackoverflow.com/questions/31327897/custom-facebook-login-button-android#:~:text=8%20Answers&text=Step%201%3A%20First%20add%20FrameLayout,auto%22%20in%20your%20main%20layout.&text=Step%202%3A%20Initialize%20FacebookSdk%20in%20onCreate%20before%20inflecting%20layout.

    @Override
    public void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    }

    @OnClick(R.id.btn_gmail_login)
    public void OnClickGoogleLogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String personName = null, personPhotoUrl = null, email = null, uniqueId = null;
            if (account != null) {
                personName = account.getDisplayName();
                try {
                    personPhotoUrl = Objects.requireNonNull(account.getPhotoUrl()).toString();
                } catch (Exception e) {
                    personPhotoUrl = "";
                }
                email = account.getEmail();
                uniqueId = account.getId();
            }
            if (isOnline()) {
                SignUpModel signUpModel = new SignUpModel();
                signUpModel.setProfileName(personName);
                signUpModel.setEmailId(email);
                signUpModel.setProfileImage(personPhotoUrl);
                signUpModel.setRegType("Google");
                signUpModel.setGender("Male");
                signUpModel.setUniqueRegId(uniqueId);
                signUpModel.setDeviceToken(deviceToken);
                loginViewModel.socialLogin(signUpModel);
            } else {
                navigator.navigateInternetConnectErrorScreen(this);
                rightToLeftAnimated();
            }
            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode() + "\n" + e.getLocalizedMessage() + "\nMEssg: " + e.getMessage());
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode() + "\n" + e.getLocalizedMessage() + "\nMEssg: " + e.getMessage());
        }
    }

    //====================================================== end google ====================================
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        //  Toast.makeText(this,"OnActivityResultCalled"+requestCode,Toast.LENGTH_SHORT).show();
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //========================================= Facebook login ===================================
//    https://www.geeksforgeeks.org/how-to-create-a-facebook-login-using-an-android-app/
    // https://www.simplifiedcoding.net/login-with-facebook-android-studio-using-facebook-sdk-4/
    // https://developers.facebook.com/docs/facebook-login/android

    @OnClick(R.id.btn_facebook_login)
    public void OnClickFacebookLogin() {
//        loginButton.performClick();
        loginManager.logInWithReadPermissions(LoginActivity.this,
                Arrays.asList("email", "public_profile", "user_friends"));
    }

    public void facebookLogin() {
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        (object, response) -> {
                            if (object != null) {
                                try {
                                    String uniqueId = object.getString("id");
                                    String email = "";
                                    String name = object.getString("name");
                                    try {
                                        email = object.getString("email");
                                    } catch (Exception e) {
                                        email = uniqueId + "@gmail.com";
                                    }
//                                        String first_name = object.getString("first_name");
//                                        String last_name = object.getString("last_name");
                                    String personPhotoUrl = "", gender = "Male", birthday = "";
                                    try {
                                        personPhotoUrl = "https://graph.facebook.com/" + uniqueId + "/picture?type=normal";
                                    } catch (Exception e) {
                                        personPhotoUrl = "";
                                    }
                                    try {
                                        if (object.has("gender")) {
                                            gender = object.getString("gender");
                                        }
                                    } catch (Exception e) {
                                    }
//                                        disconnectFromFacebook();
                                    if (isOnline()) {
                                        SignUpModel signUpModel = new SignUpModel();
                                        signUpModel.setProfileName(name);
                                        signUpModel.setEmailId(email);
                                        signUpModel.setProfileImage(personPhotoUrl);
                                        signUpModel.setRegType("Facebook");
                                        signUpModel.setGender(gender);
//                                            signUpModel.setDOB(birthday);
                                        signUpModel.setUniqueRegId(uniqueId);
                                        signUpModel.setDeviceToken(deviceToken);
                                        loginViewModel.socialLogin(signUpModel);
                                    } else {
                                        navigator.navigateInternetConnectErrorScreen(LoginActivity.this);
                                        rightToLeftAnimated();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id, email, first_name, last_name");
                parameters.putString("fields", "id, name, email, gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.v("LoginScreen", "---onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                // here write code when get error
                Log.v("LoginScreen", "----onError: " + error.getMessage());
            }
        });
    }

    public void logOutFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE,
                new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();
                    }
                })
                .executeAsync();
    }

    public void logOutGoogle() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

        LoginManager.getInstance().logOut();
    }

    private void bindViewModel() {
        loginViewModel.getLoginResponse()
                .observe(this, loginApi -> {
                    assert loginApi != null;
                    switch (loginApi.status) {
                        case SUCCESS:
                            btn_login.setBackground(btn_login.getContext().getResources().getDrawable(R.drawable.shape_btn));
                            try {
                                String response = String.valueOf(loginApi.responce);
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getInt("statusCode") == 200) {
                                    sharedPref.setRegisterId(jsonObject.getInt("RegistrationId"));
                                    sharedPref.setUserName(jsonObject.getString("ProfileName"));
                                    sharedPref.setUserEmail(jsonObject.getString("EmailId"));
                                    sharedPref.setUserProfile(jsonObject.getString("ProfileImage"));
                                    sharedPref.setGender(jsonObject.getString("Gender"));
                                    sharedPref.setDob(jsonObject.getString("DOB"));
                                    sharedPref.setRegisterType(jsonObject.getString("RegType"));
                                    sharedPref.setCountry(jsonObject.getInt("CountryId"));
                                    sharedPref.setLogin("True");

                                    if (!Methods.isEmptyOrNull(jsonObject.getString("Bio")))
                                        sharedPref.setBio(jsonObject.getString("Bio"));
                                    else
                                        sharedPref.setBio("");

                                    if (!Methods.isEmptyOrNull(jsonObject.getString("Affiliate")))
                                        sharedPref.setAffiliate(jsonObject.getString("Affiliate"));
                                    else
                                        sharedPref.setAffiliate("");

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

                                    sharedPref.setLiveCall(jsonObject.getInt("LiveCallCoins"));
                                    sharedPref.setAudioCall(jsonObject.getInt("AudioCallCoins"));
                                    sharedPref.setVideoCall(jsonObject.getInt("VideoCallCoins"));
                                    sharedPref.setImageRate(jsonObject.getInt("ImageCoins"));
                                    sharedPref.setVideoRate(jsonObject.getInt("VideoCoins"));

                                    sharedPref.setFollowers(jsonObject.getLong("FollowerCount"));
                                    sharedPref.setFollowing(jsonObject.getLong("FollowingCount"));
                                    sharedPref.setPostCount(jsonObject.getLong("PostCount"));

                                    try {
                                        sharedPref.setAge(String.valueOf(jsonObject.getInt("Age")));
                                    } catch (Exception ignored) {
                                        sharedPref.setAge("0");
                                    }
                                    
                                    boolean firstTimeLogin = false;
                                    try {
                                        firstTimeLogin = jsonObject.getBoolean("FirstTimeLogin");
                                    } catch (Exception ignored) {
                                    }

                                    navigator.navigateLoginToHome(this,firstTimeLogin);
                                    rightToLeftAnimated();
                                    Toast.makeText(this, "Login Successfully  !!!", Toast.LENGTH_SHORT).show();

                                } else if (jsonObject.getInt("statusCode") == 400) {
                                    Toast.makeText(this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                } else if (jsonObject.getInt("statusCode") == 404) {
                                    Toast.makeText(this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    if (isOnline()) {
                                        int registerId = jsonObject.getInt("RegistrationId");
                                        String emailId = jsonObject.getString("EmailId");
                                        navigator.navigateToOtpScreen(this, registerId, emailId, "NotActive");
                                        rightToLeftAnimated();
                                    } else {
                                        navigator.navigateInternetConnectErrorScreen(this);
                                        rightToLeftAnimated();
                                    }

                                } else {
                                    Toast.makeText(this, "Error Login", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception ignored1) {
                                Log.e("Login",ignored1.getMessage());
                                Log.e("Login",ignored1.getMessage());
                            }
                            break;
                        case ERROR:
                            btn_login.setBackground(btn_login.getContext().getResources().getDrawable(R.drawable.shape_btn));
                            assert loginApi.error != null;
                            onErrorMessage(content_parent, loginApi.error);
                            break;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
