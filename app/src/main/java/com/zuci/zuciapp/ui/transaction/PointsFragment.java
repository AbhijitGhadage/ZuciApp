package com.zuci.zuciapp.ui.transaction;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.base.BaseFragment;
import com.zuci.zuciapp.ui.googlePay.PaymentsUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import dagger.android.support.AndroidSupportInjection;

@RequiresApi(api = Build.VERSION_CODES.N)
public class PointsFragment extends BaseFragment<PointsViewModel> implements PaymentResultListener {

    @Inject
    ViewModelFactory<PointsViewModel> viewModelFactory;

    private PointsViewModel viewModel;

    @Inject
    Navigator navigator;

    @Inject
    SharedPref sharedPref;

    @Override
    protected PointsViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onAttach(@NotNull Context mContext) {
        AndroidSupportInjection.inject(this);
        super.onAttach(mContext);
    }

    @BindView(R.id.edit_add_amount)
    EditText edit_add_amount;
    @BindView(R.id.tv_add_coins)
    TextView tv_add_coins;

    //============================= google pay ================================
    // Arbitrarily-picked constant integer you define to track a request for payment data activity.
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

    private static final long SHIPPING_COST_CENTS = 90 * PaymentsUtil.CENTS_IN_A_UNIT.longValue();

    // A client for interacting with the Google Pay API.
    private PaymentsClient paymentsClient;

    View root;
    String addAmount = "";
    int amtIntCoins = 50;

    PointsFragment pointsFragment;
    static int userId;

    public PointsFragment() {
        // Required empty public constructor
    }

    public static PointsFragment newInstance(int aaa) {
        userId = aaa;
        return new PointsFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_points, container, false);
        ButterKnife.bind(this, root);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PointsViewModel.class);

        pointsFragment = this;

        // ==================== google pay =========================
        // Initialize a Google Pay API client for an environment suitable for testing.
        // It's recommended to create the PaymentsClient object inside of the onCreate method.
        paymentsClient = PaymentsUtil.createPaymentsClient(getActivity());
        possiblyShowGooglePayButton();

//        bindViewModel();
        return root;
    }

/*
    public void bindViewModel() {

        viewModel.getCoinsResponse()
                .observe(this, pointsAdd -> {
                    assert pointsAdd != null;
                    switch (pointsAdd.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(pointsAdd.responce);
                                JSONObject jsonObject = new JSONObject(response);

                                sharedPref.setTotalCoins(jsonObject.getString("TotalCoins"));
                                tv_total_coins.setText(sharedPref.getTotalCoins());
                                Toast.makeText(getActivity(), "Coins Added Successful !!", Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert pointsAdd.error != null;
                            showErrorMessage(root, pointsAdd.error.getMessage());
                            break;
                    }
                });
    }*/

    @OnTextChanged(value = R.id.edit_add_amount, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void nameChanged(CharSequence text) {
//        10 Rs. = 100 coins
        try {
            String amtStr = edit_add_amount.getText().toString().trim();
            int amtInt = Integer.parseInt(amtStr);
            amtIntCoins = amtInt * 5;
            tv_add_coins.setText("" + amtStr + "  Rs.  =  " + amtIntCoins + "  coins");
        } catch (Exception ignored) {
            amtIntCoins = 0;
            tv_add_coins.setText("" + 0 + "  Rs.  =  " + 0 + "  coins");
        }
    }

    @OnClick(R.id.ll_btn_add_money)
    public void addMoney() {
        addAmount = edit_add_amount.getText().toString().trim();
        if (addAmount.equalsIgnoreCase("")) {
            edit_add_amount.setError("Please enter Amount !!");
            edit_add_amount.requestFocus();
        } else {
            openDialog();

            sharedPref.setAmount(addAmount);
            sharedPref.setAmountCoins(String.valueOf(amtIntCoins));
        }
    }

    private void openDialog() {
        final CharSequence[] options = {"Razorpay Payment", "Google Payment", ConstantApp.CANCEL};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Payment Options");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Razorpay Payment")) {
                razorpayInt(addAmount);

            } else if (options[item].equals("Google Payment")) {
                googleInt(addAmount);

            } else if (options[item].equals(ConstantApp.CANCEL)) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /*   private void addCoins(String razorpayTranId) {
        AddCoinsModel addCoinsModel = new AddCoinsModel();
        addCoinsModel.setRegistrationId(sharedPref.getRegisterId());
        addCoinsModel.setAmount(Double.parseDouble(addAmount));
        addCoinsModel.setCoins(String.valueOf(amtIntCoins));
        addCoinsModel.setTransactionId(razorpayTranId);

        viewModel.addCoinsApi(addCoinsModel);
    }*/

    //------------------------------------------ Razorpay ------------------------------------------

    private void razorpayInt(String addAmount) {

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Proton Enterprises");
            options.put("description", "Zuci App Payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", R.drawable.logo);
            options.put("currency", "INR");
            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(addAmount);
            total = total * 100;
            options.put("amount", total);
            JSONObject preFill = new JSONObject();
            preFill.put("email", sharedPref.getUserEmail());
            preFill.put("contact", "1234567890");
            options.put("prefill", preFill);
            co.open(pointsFragment.getActivity(), options);

//            String coins = tv_total_coins.getText().toString().trim();
//            int aaa = Integer.parseInt(coins + amtIntCoins);
//            tv_total_coins.setText(""+aaa);

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayTranId) {
        // payment successfull pay_DGU19rDsInjcF2
        Log.e("TAG", " payment successfull " + razorpayTranId.toString());

        Toast.makeText(getActivity(), "Payment successfully done! " + razorpayTranId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("TAG", "error code " + String.valueOf(i) + " -- Payment failed " + s.toString());
        try {
            Toast.makeText(getActivity(), "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }

    //------------------------------------------ Google wallet ------------------------------------------
    private void googleInt(String addAmount) {
//        https://developers.google.com/pay/api/android/guides/tutorial#java
//        https://github.com/google-pay/android-quickstart/tree/475b218072b4568bcc07db478f3084fe1e84ad49/java/app/src/main/java/com/google/android/gms/samples/wallet
        requestPayment(addAmount);
    }

    public void requestPayment(String addAmount) {
        Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(Long.parseLong(addAmount));
        if (!paymentDataRequestJson.isPresent()) {
            return;
        }

        PaymentDataRequest request = PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            AutoResolveHelper.resolveTask(paymentsClient.loadPaymentData(request),
                    getActivity(), LOAD_PAYMENT_DATA_REQUEST_CODE);
        }
    }

    private void possiblyShowGooglePayButton() {
        final Optional<JSONObject> isReadyToPayJson = PaymentsUtil.getIsReadyToPayRequest();
        if (!isReadyToPayJson.isPresent()) {
            return;
        }

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(getActivity(),
                new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            setGooglePayAvailable(task.getResult());
                        } else {
                            Log.w("isReadyToPay failed", task.getException());
                        }
                    }
                });
    }

    private void setGooglePayAvailable(boolean available) {
        if (available) {
            Toast.makeText(getActivity(), "Allowed Payment", Toast.LENGTH_LONG).show();
//            googlePayButton.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getActivity(), R.string.googlepay_status_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    private void handlePaymentSuccess(PaymentData paymentData) {
        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        final String paymentInfo = paymentData.toJson();
        if (paymentInfo == null) {
            return;
        }

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".

            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            final String token = tokenizationData.getString("token");
            final JSONObject info = paymentMethodData.getJSONObject("info");
            final String billingName = info.getJSONObject("billingAddress").getString("name");
            Toast.makeText(getActivity(), getString(R.string.payments_show_name,
                    billingName), Toast.LENGTH_LONG).show();

            // Logging token string.
            Log.d("Google Pay token: ", token);

        } catch (JSONException e) {
            throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
        }
    }

    private void handleError(int statusCode) {
        Log.e("loadPaymentData failed", String.format("Error code: %d", statusCode));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    PaymentData paymentData = PaymentData.getFromIntent(data);
                    handlePaymentSuccess(paymentData);
                    break;

                case Activity.RESULT_CANCELED:
                    // The user cancelled the payment attempt
                    break;

                case AutoResolveHelper.RESULT_ERROR:
                    Status status = AutoResolveHelper.getStatusFromIntent(data);
                    handleError(status.getStatusCode());
                    break;
            }
        }
    }

    private void showDetails(String paymentDetails) {
        try {
            JSONObject jsonDetails = new JSONObject(paymentDetails);

            //Showing the paypal payment details and status from json object
            Log.e("Response id", jsonDetails.getString("id"));
            Log.e("Response state", jsonDetails.getString("state"));
            Toast.makeText(getActivity(), "    " + jsonDetails.getString("id")
                    + "     " + jsonDetails.getString("state"), Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}