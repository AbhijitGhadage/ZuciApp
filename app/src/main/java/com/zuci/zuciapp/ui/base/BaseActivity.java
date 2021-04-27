package com.zuci.zuciapp.ui.base;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zuci.zuciapp.App;
import com.zuci.zuciapp.R;
import com.zuci.zuciapp.ui.Navigator;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public abstract class BaseActivity<VM extends BaseViewModel> extends AppCompatActivity {

    protected CompositeDisposable disposables = new CompositeDisposable();

    private App app;

    private static final long PAUSE_CALLBACK_DELAY = 500;
    private static final int REQUEST_AUDIO_PERMISSIONS_ID = 33;

 /*   private final Handler handler = new Handler();
    private Runnable pauseCallback = new Runnable() {
        @Override
        public void run() {
            app.onActivityPaused();
        }
    };*/


    @Override
    protected void onResume() {
        super.onResume();
//        app.onActivityResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        handler.postDelayed(pauseCallback, PAUSE_CALLBACK_DELAY);
    }

    /*
    protected void checkAudioRecordPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_AUDIO_PERMISSIONS_ID);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSIONS_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
*/

    @Inject
    Navigator navigator;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        app = (App) getApplication();
        setStatusBarColor(R.color.black);

        //lock to portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        bindViewModel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

    protected void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, null, "Please wait...", false, false);
        }
    }

    protected void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    protected void showError(Throwable e) {
        DialogInterface.OnClickListener onErrorClickedListener = null;

        new AlertDialog.Builder(this)
                .setMessage(e.getLocalizedMessage())
                .setTitle("Error")
                .setPositiveButton(getResources().getString(android.R.string.ok), onErrorClickedListener)
                .setCancelable(false)
                .create()
                .show();
    }

    protected void setStatusBarColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(color));
        }
    }

    @SuppressWarnings("ConstantConditions")
    protected void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    protected abstract VM getViewModel();

    private void bindViewModel() {
        getViewModel().getLoadingIndicator()
                .observe(this, this::showLoadingIndicator);
    }

    private void showLoadingIndicator(Boolean shouldShowLoadingIndicator) {
        if (shouldShowLoadingIndicator != null && shouldShowLoadingIndicator)
            showProgressDialog();
        else
            hideProgressDialog();
    }

    @SuppressLint("NewApi")
    protected void showErrorDialog(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            BaseActivity.this.finish();
        });
        // Create the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        // Get the alert dialog buttons reference
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.app_txt_color_black3));
    }

    protected void showErrorMessage(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public static void onErrorMessage(View view, Throwable e) {
        String error = "";
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException)e).response().errorBody();
            error=getErrorMessage(responseBody);
//            String err=getErrorMessage(responseBody);
//            error = "Technical Problem !!!";               // response null & procedure err
        } else if (e instanceof SocketTimeoutException) {
            error = "Oops something went wrong Please Retry again !!";    // solve internet
        } else if (e instanceof IOException) {
            error = "No Internet Available Please Connect Internet !!";      // internet not
        }else if (e instanceof IllegalStateException) {
            error = "Conversion Error !!";                              // conversion err
        }else if (e instanceof JsonSyntaxException) {
            error = "Something Went Wrong API is not responding properly !!";
//            error = "Something Went Wrong Please contact to Technical Team !!";
        } else {
            error = "Please contact to Technical Support Team !!";
        }

        Snackbar snackbar = Snackbar.make(view, error, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();

    }

    private static String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());

            Log.d("abc", "cbd" + responseBody);
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    protected void showToastMessage() {
        // Get the custom layout view.
        View toastView = getLayoutInflater().inflate(R.layout.dialog_add_menu, null);
        // Initiate the Toast instance.
        Toast toast = new Toast(getApplicationContext());
        // Set custom view in toast.
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        return netInfo != null && netInfo.isConnectedOrConnecting();
        return netInfo != null && netInfo.isConnected()
                && netInfo.isConnectedOrConnecting()
                && netInfo.isAvailable();
    }

    protected void leftToRightAnimated() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    protected void rightToLeftAnimated() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
}

