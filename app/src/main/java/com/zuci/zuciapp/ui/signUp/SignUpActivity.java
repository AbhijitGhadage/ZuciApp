package com.zuci.zuciapp.ui.signUp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class SignUpActivity extends BaseActivity<SignUpViewModel> {
    @Inject
    ViewModelFactory<SignUpViewModel> viewModelFactory;

    private SignUpViewModel signUpViewModel;

    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;

    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;
    @BindView(R.id.edit_profile_up_dob)
    TextView edit_profile_up_dob;
    @BindView(R.id.edit_input_name)
    EditText edit_input_name;
    @BindView(R.id.edit_input_nike_name)
    EditText edit_input_nike_name;
    @BindView(R.id.edit_input_userName)
    EditText edit_input_userName;
    @BindView(R.id.edit_input_mobile)
    EditText edit_input_mobile;
    @BindView(R.id.edit_profile_up_age)
    EditText edit_profile_up_age;
    @BindView(R.id.edit_input_password)
    EditText edit_input_password;
    @BindView(R.id.edit_input_confirm_pass) EditText edit_input_confirm_pass;
    @BindView(R.id.btn_next)
    AppCompatButton btn_next;

    private static final String TAG = "SignUpActivity";
    String selectedDob;
    int ageCal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        signUpViewModel = ViewModelProviders.of(this, viewModelFactory).get(SignUpViewModel.class);

    }

    @Override
    protected SignUpViewModel getViewModel() {
        return signUpViewModel;
    }

    @OnClick(R.id.btn_next)
    public void OnClickForgot() {
        signUpUser();
    }

    @OnClick(R.id.iv_back_btn)
    public void OnClickLogin() {
        onBackPressed();
    }

    private void signUpUser() {
        String name = edit_input_name.getText().toString().trim();
        String nikeName = edit_input_nike_name.getText().toString().trim();
        String mobile = edit_input_mobile.getText().toString();
        String email = edit_input_userName.getText().toString();
        String password = edit_input_password.getText().toString();
        String reEnterPassword = edit_input_confirm_pass.getText().toString();
        String dob = edit_profile_up_dob.getText().toString();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (name.equalsIgnoreCase("")) {
            edit_input_name.setError("Please enter name !!");
            edit_input_name.requestFocus();
        } else if (mobile.length() != 10) {
            edit_input_mobile.setError("Enter 10 digit number !!");
            edit_input_mobile.requestFocus();
        } else if (email.equalsIgnoreCase("")) {
            edit_input_userName.setError("Please enter email !!");
            edit_input_userName.requestFocus();
        } else if (!email.matches(emailPattern)) {
            edit_input_userName.setError("Invalid email address !!");
            edit_input_userName.requestFocus();
        } else if (dob.equalsIgnoreCase("")) {
            edit_profile_up_dob.setError("Please select DOB !!");
            edit_profile_up_dob.requestFocus();
        } else if (password.equalsIgnoreCase("")) {
            edit_input_password.setError("Please enter password !!");
            edit_input_password.requestFocus();
        } else if (reEnterPassword.equalsIgnoreCase("")) {
            edit_input_confirm_pass.setError("Please enter re-password !!");
            edit_input_confirm_pass.requestFocus();
        } else if (!password.equals(reEnterPassword)) {
            edit_input_confirm_pass.setError("Please confirm password !!");
            edit_input_confirm_pass.requestFocus();
            edit_input_confirm_pass.setText("");
        } else {
            if (isOnline()) {
                if (ageCal > 18) {
                    btn_next.setBackground(btn_next.getContext().getResources().getDrawable(R.drawable.shape_btn2));
                    navigator.navigateToGender(this, name, nikeName, mobile, email, dob, ageCal, password, reEnterPassword);
                    rightToLeftAnimated();
                    btn_next.setBackground(btn_next.getContext().getResources().getDrawable(R.drawable.shape_btn));
                } else {
                    Toast.makeText(this, "App is Prohibited For below 18 years User !", Toast.LENGTH_SHORT).show();
                }
            } else {
                navigator.navigateInternetConnectErrorScreen(this);
                rightToLeftAnimated();
            }
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
                        ageCal = getAge(year, monthOfYear, dayOfMonth); // calculate age
                        if (ageCal > 18) {
                            edit_profile_up_age.setText("" + ageCal);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        leftToRightAnimated();
    }

}