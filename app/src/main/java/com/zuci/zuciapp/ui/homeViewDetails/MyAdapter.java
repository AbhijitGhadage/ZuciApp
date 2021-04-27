package com.zuci.zuciapp.ui.homeViewDetails;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.ui.homeViewDetails.tabFragment.ApplicentFragment;
import com.zuci.zuciapp.ui.homeViewDetails.tabFragment.ImageFragment;
import com.zuci.zuciapp.ui.homeViewDetails.tabFragment.VideoFragment;
import com.zuci.zuciapp.ui.homeViewDetails.tabFragment.ProfileFragment;

public class MyAdapter extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[] {R.string.tab_affiliate,R.string.tab_image, R.string.tab_video , R.string.tab_profile };
    private final Context mContext;
    int userId;
    String affiliate;

    public MyAdapter(Context context, FragmentManager fm, int userId, String affiliate) {
        super(fm);
        mContext = context;
        this.userId = userId;
        this.affiliate = affiliate;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ApplicentFragment.newInstance(affiliate);
            case 1:
                return ImageFragment.newInstance(userId);
            case 2:
                return VideoFragment.newInstance(userId);
            case 3:
                return ProfileFragment.newInstance();
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