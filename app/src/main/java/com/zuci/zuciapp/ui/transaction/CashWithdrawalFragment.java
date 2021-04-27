package com.zuci.zuciapp.ui.transaction;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.AndroidSupportInjection;

public class CashWithdrawalFragment  extends BaseFragment<CashWithdrawalViewModel> {

    @Inject
    ViewModelFactory<CashWithdrawalViewModel> viewModelFactory;

    private CashWithdrawalViewModel cashWithdrawalViewModel;

    @Inject
    Navigator navigator;

    @Inject
    SharedPref sharedPref;

    @Override
    protected CashWithdrawalViewModel getViewModel() {
        return cashWithdrawalViewModel;
    }

    @Override
    public void onAttach(@NotNull Context mContext) {
        AndroidSupportInjection.inject(this);
        super.onAttach(mContext);
    }

    public CashWithdrawalFragment() {
        // Required empty public constructor
    }

    static int userId;

    public static CashWithdrawalFragment newInstance(int aaa) {
        userId = aaa;
        return new CashWithdrawalFragment();
    }

    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;
    @BindView(R.id.tv_withdrawal_name)
    TextView tv_withdrawal_name;
    @BindView(R.id.tv_withdrawal_email)
    TextView tv_withdrawal_email;
    @BindView(R.id.edit_withdrawal_no)
    EditText edit_withdrawal_no;
    @BindView(R.id.edit_withdrawal_amt)
    EditText edit_withdrawal_amt;
    @BindView(R.id.edit_withdrawal_message)
    EditText edit_withdrawal_message;

    View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_cash_withdrawal, container, false);
        ButterKnife.bind(this, root);

        cashWithdrawalViewModel = ViewModelProviders.of(this, viewModelFactory).get(CashWithdrawalViewModel.class);



        bindViewModel();

        return root;
    }

    @OnClick(R.id.btn_withdrawal)
    public void withdrawal() {
        sendRequest();
    }

    private void sendRequest() {

        String number = edit_withdrawal_no.getText().toString().trim();
        String amt = edit_withdrawal_amt.getText().toString().trim();
        String remark = edit_withdrawal_message.getText().toString().trim();

        if (number.equalsIgnoreCase("")) {
            edit_withdrawal_no.setError("Please enter Number !!");
            edit_withdrawal_no.requestFocus();
        } else if (number.length() != 10) {
            edit_withdrawal_no.setError("Enter 10 digit number !!");
            edit_withdrawal_no.requestFocus();
        } else if (amt.equalsIgnoreCase("")) {
            edit_withdrawal_amt.setError("Please enter Amount !!");
            edit_withdrawal_amt.requestFocus();
        } else {
            double amtDouble = Double.parseDouble(amt);
                CashWithdrawalModel cashWithdrawalModel = new CashWithdrawalModel();
                cashWithdrawalModel.setRegistrationId(sharedPref.getRegisterId());
                cashWithdrawalModel.setName(sharedPref.getUserName());
                cashWithdrawalModel.setEmail(sharedPref.getUserEmail());
                cashWithdrawalModel.setNumber(number);
                cashWithdrawalModel.setAmount(amtDouble);
                cashWithdrawalModel.setRemark(remark);
                cashWithdrawalViewModel.cashWithdrawalApi(cashWithdrawalModel);

        }
    }

    public void bindViewModel() {
        tv_withdrawal_name.setText(sharedPref.getUserName());
        tv_withdrawal_email.setText(sharedPref.getUserEmail());
        edit_withdrawal_no.setText(sharedPref.getUserPhone());

        cashWithdrawalViewModel.getCashWithdrawalResponse()
                .observe(this, cashWithdrawal -> {
                    assert cashWithdrawal != null;
                    switch (cashWithdrawal.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(cashWithdrawal.responce);
                                JSONObject jsonObject = new JSONObject(response);


                                Toast.makeText(getActivity(), "Send Request Successfully !!", Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert cashWithdrawal.error != null;
//                            onErrorMessage(content_parent, cashWithdrawal.error);
                            showErrorMessage(root, cashWithdrawal.error.getMessage());
                            break;
                    }
                });
    }

}