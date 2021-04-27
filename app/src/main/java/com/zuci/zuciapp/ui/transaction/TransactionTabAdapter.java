package com.zuci.zuciapp.ui.transaction;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.zuci.zuciapp.R;

public class TransactionTabAdapter extends FragmentStatePagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[] {R.string.tab_addPoint, R.string.tab_addRates, R.string.tab_withdrawal, R.string.tab_tran };
    private final Context mContext;
    int userId;

    public TransactionTabAdapter(Context context, FragmentManager fm, int userId) {
        super(fm);
        mContext = context;
        this.userId = userId;
    }
    @SuppressLint("NewApi")
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PointsFragment.newInstance(userId);
            case 1:
                return AddCoinsRateFragment.newInstance(userId);
            case 2:
                return CashWithdrawalFragment.newInstance(userId);
            case 3:
                return TransactionLogFragment.newInstance(userId);
            default:
                return null;
        }
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
    @Override
    public int getCount() {
        // Show 3 total pages.
        return 4;
    }
}