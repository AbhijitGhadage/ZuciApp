package com.zuci.zuciapp.ui.profileCreate;

import androidx.appcompat.widget.*;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.content.CursorLoader;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.zuci.zuciapp.utils.Methods;
import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.base.BaseActivity;
import com.zuci.zuciapp.ui.signUp.SignUpModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileCreateActivity extends BaseActivity<ProfileCreateViewModel> {
    @Inject
    ViewModelFactory<ProfileCreateViewModel> viewModelFactory;

    private ProfileCreateViewModel profileCreateViewModel;

    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;

    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;
    @BindView(R.id.tv_btn_Skip)
    TextView tv_btn_Skip;
    @BindView(R.id.tv_user_profile_name)
    TextView tv_user_profile_name;
    @BindView(R.id.tv_user_profile_gmail)
    TextView tv_user_profile_gmail;
    @BindView(R.id.tv_user_profile_bio)
    TextView tv_user_profile_bio;
    @BindView(R.id.circle_profile_image)
    CircleImageView circle_profile_image;
    @BindView(R.id.btn_profile_create)
    AppCompatButton btn_profile_create;
    @BindView(R.id.edit_profile_up_dob)
    TextView edit_profile_up_dob;
    @BindView(R.id.edit_profile_up_age)
    EditText edit_profile_up_age;
    @BindView(R.id.edit_user_profile_name)
    EditText edit_user_profile_name;
    @BindView(R.id.edit_profile_up_bio)
    EditText edit_profile_up_bio;
    @BindView(R.id.edit_profile_up_affiliate)
    EditText edit_profile_up_affiliate;
    @BindView(R.id.edit_profile_up_address)
    EditText edit_profile_up_address;
    @BindView(R.id.card_profile_male) CardView card_profile_male;
    @BindView(R.id.card_profile_Female) CardView card_profile_Female;
    @BindView(R.id.spinner_country)
    AppCompatSpinner spinner_country;
    @BindView(R.id.tv_profile_post)
    TextView tv_profile_post;
    @BindView(R.id.tv_profile_followers)
    TextView tv_profile_followers;
    @BindView(R.id.tv_profile_following)
    TextView tv_profile_following;

    int registerId = 0, countryId = 1;
    private String gender, selectedDob, name, address, bioInformation, affiliateInformation, age, selectedProfImage;
    boolean fromMainActivity;

    ArrayList<CountryListModel> countryListModelArrayList = new ArrayList<>();
    ArrayList<String> selectClassList = new ArrayList<>();

    // image upload
    private Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_create);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);
        profileCreateViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileCreateViewModel.class);

        getIntentData();
        countryListApi(); // country api call
        bindViewModel();
    }

    @Override
    protected ProfileCreateViewModel getViewModel() {
        return profileCreateViewModel;
    }

    private void getIntentData() {
        fromMainActivity = getIntent().getBooleanExtra(ConstantApp.FROM_MAIN_ACTIVITY, false);
        tv_btn_Skip.setVisibility(fromMainActivity ? View.GONE : View.VISIBLE);
    }

    private void countryListApi() {
        if (isOnline()) {
            profileCreateViewModel.countryList();
        } else {
            navigator.navigateInternetConnectErrorScreen(this);
            rightToLeftAnimated();
        }
    }

    //================================= update profile ======================================
    @OnClick(R.id.circle_edit_profile)
    public void updateProfile() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            //the image URI
            selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                // Log.d(TAG, String.valueOf(bitmap));
                updateImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public void updateImage() {
        int registerId = sharedPref.getRegisterId();
        File file = null;
        if (selectedImage != null) {
            file = new File(getRealPathFromURI(selectedImage));
        }
        if (file != null) {
            profileCreateViewModel.uploadImage(registerId, file);
        } else {
            Toast.makeText(this, "Please select image !!", Toast.LENGTH_SHORT).show();
        }
    }

    //================================ end ================================================
    @OnClick(R.id.iv_back_btn)
    public void OnClickBlack() {
        onBackPressed();
    }

    @OnClick(R.id.tv_btn_Skip)
    public void OnClickSkip() {
        onBackPressed();
    }

    private void setCountrySpinnerAdapter() {
        try {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_row_layout, countryListModelArrayList);
            spinner_country.setAdapter(arrayAdapter);
            spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    ((TextView)parent.getChildAt(0)).setTextColor(R.color.app_txt_color_black1);
//                    ((TextView) view).setTextColor(getResources().getColor(R.color.app_txt_color_black1));
                    CountryListModel countryListModel = (CountryListModel) parent.getSelectedItem();
                    if (countryListModel != null)
                        countryId = countryListModel.getCountryId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (Exception e) {
            Log.e("setCountrySpinnerAdapter", e.getMessage());
        }

        selectSpinnerItemByValue();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!fromMainActivity) {
            navigator.navigateToHome(this);
            rightToLeftAnimated();
        } else {
            finish();
            leftToRightAnimated();
        }
    }

    private void setDefaultViewValues() {
        registerId = sharedPref.getRegisterId();
        if (!Methods.isEmptyOrNull(sharedPref.getUserName())) {
            tv_user_profile_name.setText(sharedPref.getUserName());
            edit_user_profile_name.setText(sharedPref.getUserName());
            name = sharedPref.getUserName();
        }
        if (!Methods.isEmptyOrNull(sharedPref.getUserEmail()))
            tv_user_profile_gmail.setText(sharedPref.getUserEmail());
        if (!Methods.isEmptyOrNull(sharedPref.getBio())) {
            bioInformation = sharedPref.getBio();
            tv_user_profile_bio.setText(sharedPref.getBio());
            edit_profile_up_bio.setText(sharedPref.getBio());
        }
        if (!Methods.isEmptyOrNull(sharedPref.getAffiliate())) {
            affiliateInformation = sharedPref.getAffiliate();
            edit_profile_up_affiliate.setText(sharedPref.getAffiliate());
        }
        if (!Methods.isEmptyOrNull(sharedPref.getDob())) {
            selectedDob = sharedPref.getDob();
            edit_profile_up_dob.setText(sharedPref.getDob());
        }
        if (!Methods.isEmptyOrNull(sharedPref.getAge()) && Integer.parseInt(sharedPref.getAge()) != 0) {
            age = sharedPref.getAge();
            edit_profile_up_age.setText(sharedPref.getAge());
        }
        if (!Methods.isEmptyOrNull(sharedPref.getAddress())) {
            address = sharedPref.getAddress();
            edit_profile_up_address.setText(sharedPref.getAddress());
        }

        if (!Methods.isEmptyOrNull(sharedPref.getUserProfile())) {
            selectedProfImage = sharedPref.getUserProfile();
            Picasso.get()
                    .load(sharedPref.getUserProfile())
                    .error(R.drawable.profile_male)
                    .placeholder(R.drawable.profile_male)
                    .into(circle_profile_image);
        }
        if (!Methods.isEmptyOrNull(sharedPref.getGender()))
            gender = sharedPref.getGender();
        else
            gender = "Male";
        if (gender.equalsIgnoreCase("Male")) {
            card_profile_male.setCardBackgroundColor(getResources().getColor(R.color.app_cardView_white2));
            card_profile_Female.setCardBackgroundColor(getResources().getColor(R.color.app_cardView_white));
        } else {
            card_profile_Female.setCardBackgroundColor(getResources().getColor(R.color.app_cardView_white2));
            card_profile_male.setCardBackgroundColor(getResources().getColor(R.color.app_cardView_white));
        }

        tv_profile_post.setText("" + sharedPref.getPostCount());
        tv_profile_followers.setText("" + sharedPref.getFollowers());
        tv_profile_following.setText("" + sharedPref.getFollowing());
//        selectSpinnerItemByValue();
    }

    @OnClick(R.id.card_profile_male)
    public void OnClickSignUpMale() {
        gender = "Male";
        card_profile_male.setCardBackgroundColor(getResources().getColor(R.color.app_cardView_white2));
        card_profile_Female.setCardBackgroundColor(getResources().getColor(R.color.app_cardView_white));
    }

    @OnClick(R.id.card_profile_Female)
    public void OnClickSignUpFemale() {
        gender = "Female";
        card_profile_Female.setCardBackgroundColor(getResources().getColor(R.color.app_cardView_white2));
        card_profile_male.setCardBackgroundColor(getResources().getColor(R.color.app_cardView_white));
    }

    public void selectSpinnerItemByValue() {
        int position = 0;
        for (CountryListModel countryListModel : countryListModelArrayList)
            if (sharedPref.getCountry() == countryListModel.getCountryId()) {
                position++;
                spinner_country.setSelection(position - 1);
            } else {
                position++;
            }
    }

    @OnClick(R.id.edit_profile_up_dob)
    public void OnClickCalendar() {
        setDateTimeField();
    }

    private void setDateTimeField() {
        try {
            final Calendar c = Calendar.getInstance();
            int mYear, mMonth, mDay;
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
                        selectedDob = sdff.format(calendar.getTime());
                        edit_profile_up_dob.setText(selectedDob);
                        int ageCal = getAge(year, monthOfYear, dayOfMonth);
                        if (ageCal > 18) {
                            edit_profile_up_age.setText("" + ageCal);   // calculate age
                        } else {
                            edit_profile_up_dob.setText("");
                            edit_profile_up_age.setText("");
                            Toast.makeText(this, "App is Prohibited For below 18 years User !", Toast.LENGTH_SHORT).show();
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }
    }

    private int getAge(int year, int month, int day) {
        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(year, month, day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH)) || ((m == cal.get(Calendar.MONTH)) && (d < cal.get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if (a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;
    }

    @OnClick(R.id.btn_profile_create)
    public void OnClickProfileCreate() {
        profileSubmitData();
    }

    private void profileSubmitData() {
        if (isValidData()) {
            if (isOnline()) {
                SignUpModel signUpModel = new SignUpModel();
                signUpModel.setRegistrationId(registerId);
                signUpModel.setProfileName(name);
                signUpModel.setDOB(selectedDob);
                signUpModel.setBio(bioInformation);
                signUpModel.setAffiliate(affiliateInformation);
                signUpModel.setGender(gender);
                signUpModel.setAge(Integer.parseInt(age));
                signUpModel.setAddress(address);
                signUpModel.setCountryId(countryId);
                profileCreateViewModel.profileCreateUser(signUpModel);
            } else {
                navigator.navigateInternetConnectErrorScreen(this);
                rightToLeftAnimated();
            }
        }
    }

    private boolean isValidData() {
        boolean isValidData = true;
        selectedDob = edit_profile_up_dob.getText().toString().trim();
        age = edit_profile_up_age.getText().toString().trim();
        bioInformation = edit_profile_up_bio.getText().toString().trim();
        affiliateInformation = edit_profile_up_affiliate.getText().toString().trim();
        address = edit_profile_up_address.getText().toString().trim();
        name = edit_user_profile_name.getText().toString().trim();
        if (Methods.isEmptyOrNull(name)) {
            isValidData = false;
            edit_user_profile_name.setError("Profile Name Should Not Be Empty");
        }
        if (Methods.isEmptyOrNull(selectedDob)) {
            isValidData = false;
            Methods.showMessage(this, "Please Select DOB");
        }
        if (Methods.isEmptyOrNull(age)) {
            isValidData = false;
            edit_profile_up_age.setError("Age Should Not Be Empty");
        }
        if (Methods.isEmptyOrNull(bioInformation)) {
            isValidData = false;
            edit_profile_up_bio.setError("Bio Information Should Not Be Empty");
        }
        if (Methods.isEmptyOrNull(affiliateInformation)) {
            isValidData = false;
            edit_profile_up_affiliate.setError("Affiliate Information Should Not Be Empty");
        }
        if (Methods.isEmptyOrNull(address)) {
            isValidData = false;
            edit_profile_up_address.setError("Address Should Not Be Empty");
        }
        return isValidData;
    }

    private void bindViewModel() {
        profileCreateViewModel.getProfileCreatedResponse()
                .observe(this, forgotPass -> {
                    assert forgotPass != null;
                    switch (forgotPass.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(forgotPass.responce);
                                JSONObject jsonObject = new JSONObject(response);
//                                int ageInt = jsonObject.getInt("Age");
//                                sharedPref.setAge(String.valueOf(ageInt));
//                                sharedPref.setUserName(jsonObject.getString("ProfileName"));
//                                sharedPref.setDob(jsonObject.getString("DOB"));
//                                sharedPref.setBio(jsonObject.getString("Bio"));
//                                sharedPref.setAddress(jsonObject.getString("Address"));
//                                sharedPref.setGender(jsonObject.getString("Gender"));
//                                sharedPref.setCountry(jsonObject.getInt("CountryId"));

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

                                sharedPref.setAudioCall(jsonObject.getInt("AudioCallCoins"));
                                sharedPref.setVideoCall(jsonObject.getInt("VideoCallCoins"));
                                sharedPref.setImageRate(jsonObject.getInt("ImageCoins"));
                                sharedPref.setVideoRate(jsonObject.getInt("VideoCoins"));

                                try {
                                    sharedPref.setAge(String.valueOf(jsonObject.getInt("Age")));
                                } catch (Exception ignored) {
                                    sharedPref.setAge("0");
                                }

                                navigator.navigateToHome(this);
                                rightToLeftAnimated();
                            } catch (Exception e) {
                                Log.e("bindViewModel", e.getMessage());
                            }
                            break;
                        case ERROR:
                            assert forgotPass.error != null;
                            onErrorMessage(content_parent, forgotPass.error);
                            break;
                    }
                });

        profileCreateViewModel.getCountryListResponse()
                .observe(this, countryListRes -> {
                    assert countryListRes != null;
                    switch (countryListRes.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(countryListRes.responce);
//                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    CountryListModel countryListModel = new CountryListModel();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    countryListModel.setCountryId(jsonObject.getInt("CountryId"));
                                    countryListModel.setCountryName(jsonObject.getString("CountryName"));
                                    countryListModel.setCountryIcon(jsonObject.getString("CountryIcon"));
                                    selectClassList.add(jsonObject.getString("CountryName"));
                                    countryListModelArrayList.add(countryListModel);
                                }
                                setCountrySpinnerAdapter();
                                /*ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, selectClassList);
                                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_country.setAdapter(aa);*/
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert countryListRes.error != null;
                            onErrorMessage(content_parent, countryListRes.error);
                            break;
                    }
                });

        profileCreateViewModel.getUpdateImgResponse()
                .observe(this, uploadImage -> {
                    assert uploadImage != null;
                    switch (uploadImage.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(uploadImage.responce);
                                JSONObject jsonObject = new JSONObject(response);
                                try {
                                    String imageUrl = jsonObject.getString("imageUrl");
                                    Picasso.get()
                                            .load(imageUrl)
                                            .error(R.drawable.profile_male)
                                            .placeholder(R.drawable.profile_male)
                                            .into(circle_profile_image);
                                    sharedPref.setUserProfile(imageUrl);
                                    Toast.makeText(this, "Update Profile Successful !!", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert uploadImage.error != null;
                            onErrorMessage(content_parent, uploadImage.error);
                            break;
                    }
                });

        setDefaultViewValues();  // share pref set data
    }

    /*@OnItemSelected(R.id.spinner_country)
    void onItemSelected(int position) {
        try {
            countryId = countryListModelArrayList.get(position).getCountryId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}