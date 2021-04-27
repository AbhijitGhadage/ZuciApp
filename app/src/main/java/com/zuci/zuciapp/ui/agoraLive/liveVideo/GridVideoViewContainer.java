package com.zuci.zuciapp.ui.agoraLive.liveVideo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;


public class GridVideoViewContainer extends RecyclerView {
    public GridVideoViewContainer(Context context) {
        super(context);
    }

    public GridVideoViewContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GridVideoViewContainer(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private GridVideoViewContainerAdapter mGridVideoViewContainerAdapter;

    private VideoViewEventListener mEventListener;

    public void setItemEventHandler(VideoViewEventListener listener) {
        this.mEventListener = listener;
    }

    private boolean initAdapter(int localUid, HashMap<Integer, SurfaceView> uids) {
        if (mGridVideoViewContainerAdapter == null) {
            mGridVideoViewContainerAdapter = new GridVideoViewContainerAdapter(getContext(), localUid, uids, mEventListener);
            mGridVideoViewContainerAdapter.setHasStableIds(true);
            return true;
        }
        return false;
    }

    public void initViewContainer(Context context, int localUid, HashMap<Integer, SurfaceView> uids) {
        boolean newCreated = initAdapter(localUid, uids);

        if (!newCreated) {
            mGridVideoViewContainerAdapter.setLocalUid(localUid);
            mGridVideoViewContainerAdapter.init(uids, localUid, true);
        }

        this.setAdapter(mGridVideoViewContainerAdapter);

        int count = uids.size();
        if (count <= 2) { // only local full view or or with one peer
            this.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        } else if (count > 2 && count <= 4) {
            this.setLayoutManager(new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false));
        }

        mGridVideoViewContainerAdapter.notifyDataSetChanged();
    }

    public SurfaceView getSurfaceView(int index) {
        return mGridVideoViewContainerAdapter.getItem(index).mView;
    }

    public VideoStatusData getItem(int position) {
        return mGridVideoViewContainerAdapter.getItem(position);
    }
}
