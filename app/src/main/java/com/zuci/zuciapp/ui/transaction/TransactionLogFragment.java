package com.zuci.zuciapp.ui.transaction;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class TransactionLogFragment extends BaseFragment<TransactionViewModel> implements View.OnClickListener {

    @Inject
    ViewModelFactory<TransactionViewModel> viewModelFactory;

    private TransactionViewModel transactionViewModel;

    @Inject
    Navigator navigator;

    @Inject
    SharedPref sharedPref;

    @Override
    protected TransactionViewModel getViewModel() {
        return transactionViewModel;
    }

    @Override
    public void onAttach(@NotNull Context mContext) {
        AndroidSupportInjection.inject(this);
        super.onAttach(mContext);
    }

    static int userId;

    public TransactionLogFragment() {
        // Required empty public constructor
    }

    public static TransactionLogFragment newInstance(int aaa) {
        userId = aaa;
        return new TransactionLogFragment();
    }

    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;
    @BindView(R.id.rv_transaction_log)
    RecyclerView rv_transaction_log;

    private List<TransactionLogModel> transactionLogModels;
    private TransactionAdapter transactionAdapter;

    View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_transaction_log, container, false);
        ButterKnife.bind(this, root);

        transactionViewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionViewModel.class);
        transactionLogModels = new ArrayList<>();
//        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
//        rv_transaction_log.setLayoutManager(manager);
//        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
//        rv_transaction_log.setLayoutManager(llm);
        rv_transaction_log.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_transaction_log.setHasFixedSize(true);

        setUpView();

        bindViewModel();

        return root;
    }

    private void setUpView() {
        transactionViewModel.transactionList(sharedPref.getRegisterId());
    }

    private void bindViewModel() {
        transactionViewModel.getTransactionListResponse()
                .observe(this, tranApi -> {
                    assert tranApi != null;
                    switch (tranApi.status) {
                        case SUCCESS:
                            try {
                                transactionLogModels.clear();
                                String response = String.valueOf(tranApi.responce);
//                                JSONArray jsonArray = new JSONArray(response);

                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray coinsJsonArray = jsonObject.getJSONArray("Coinmaster");
                                for (int i = 0; i < coinsJsonArray.length(); i++) {
                                    TransactionLogModel tranLogModel = new TransactionLogModel();
                                    JSONObject coinsMaster = coinsJsonArray.getJSONObject(i);
                                    tranLogModel.setRegistrationId(coinsMaster.getLong("RegistrationId"));
                                    tranLogModel.setCoins(coinsMaster.getString("Coins"));
                                    tranLogModel.setAmount(coinsMaster.getLong("Amount"));
                                    tranLogModel.setTransactionId(coinsMaster.getString("TransactionId"));
                                    tranLogModel.setTotalCoins(coinsMaster.getString("TotalCoins"));
                                    tranLogModel.setTransactionDate(coinsMaster.getString("TransactionDate"));
                                    transactionLogModels.add(tranLogModel);
                                }

                                JSONArray deductionJsonArray = jsonObject.getJSONArray("Deduction");
                                for (int i = 0; i < deductionJsonArray.length(); i++) {
                                    TransactionLogModel tranLogModel = new TransactionLogModel();
                                    JSONObject coinsMaster = deductionJsonArray.getJSONObject(i);
                                    tranLogModel.setViewerRegistrationId(coinsMaster.getLong("ViewerRegistrationId"));
                                    tranLogModel.setDeductionType(coinsMaster.getString("DeductionType"));
                                    tranLogModel.setDeductionDate(coinsMaster.getString("DeductionDate"));
                                    tranLogModel.setViewerStatus(coinsMaster.getString("ViewerStatus"));
                                    tranLogModel.setMediaId(coinsMaster.getLong("MediaId"));
                                    tranLogModel.setMediaOwnerRegistrationId(coinsMaster.getLong("MediaOwnerRegistrationId"));
                                    tranLogModel.setDeductCoins(coinsMaster.getLong("DeductCoins"));
                                    tranLogModel.setAdminDeductCoins(coinsMaster.getLong("AdminDeductCoins"));
                                    tranLogModel.setViewerUserName(coinsMaster.getString("ViewerUserName"));
                                    tranLogModel.setOwnerUserName(coinsMaster.getString("OwnerUserName"));
                                    transactionLogModels.add(tranLogModel);
                                }
//                                Collections.sort(transactionLogModels, Comparator.comparing(TransactionLogModel::getDeductionDate));
                                transactionAdapter = new TransactionAdapter(getActivity(), transactionLogModels, this, sharedPref.getRegisterId());
                                rv_transaction_log.setAdapter(transactionAdapter);
                                transactionAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert tranApi.error != null;
                            showErrorMessage(root, tranApi.error.getMessage());
                            break;
                    }
                });
    }

    @Override
    public void onClick(View v) {

    }
}