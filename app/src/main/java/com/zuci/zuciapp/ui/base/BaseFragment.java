package com.zuci.zuciapp.ui.base;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.ui.Navigator;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public abstract class BaseFragment<VM extends BaseViewModel> extends Fragment {
    @Inject
    Navigator navigator;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViewModel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

    protected void showProgressDialog() {
        if (progressDialog == null && getActivity() != null) {
            progressDialog = ProgressDialog.show(getActivity(), null, "Please wait...", false, false);

        }
    }

    protected void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    protected void showError(Throwable e) {
        if (getActivity() == null) return;

        DialogInterface.OnClickListener onErrorClickedListener = null;

        new AlertDialog.Builder(getActivity())
                .setMessage(e.getLocalizedMessage())
                .setTitle("Error")
                .setPositiveButton(getResources().getString(android.R.string.ok), onErrorClickedListener)
                .setCancelable(false)
                .create()
                .show();
    }

    protected void showErrorMessage(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}
