package com.zuci.zuciapp.ui.firestoreMessageChat;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.ui.chat.call.CallListFragment;
import com.zuci.zuciapp.ui.firestoreMessageChat.messageChatHistory.MessageListFragment;

public class ChatTabAdapter  extends FragmentStatePagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[] { R.string.tab_message, R.string.tab_history };
    private final Context mContext;

    public ChatTabAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }
    @SuppressLint("NewApi")
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MessageListFragment.newInstance();
            case 1:
                return CallListFragment.newInstance();
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
        return 2;
    }
}
