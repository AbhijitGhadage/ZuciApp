package com.zuci.zuciapp.ui.reels;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseFragment;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class ReelsVideoFragment extends BaseFragment<ReelsVideoViewModel> implements View.OnClickListener {
    @Inject
    ViewModelFactory<ReelsVideoViewModel> viewModelFactory;
    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;

    ReelsVideoViewModel viewModel;

    @Override
    public ReelsVideoViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onAttach(@NotNull Context mContext) {
        AndroidSupportInjection.inject(this);
        super.onAttach(mContext);
    }

    public ReelsVideoFragment() {
        // Required empty public constructor
    }

    View root;

    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;
    @BindView(R.id.viewPagerVideos)
    ViewPager2 videosViewPager;
    private List<ReelsVideoModel> videoListModelList = new ArrayList<>();
    private ReelsVideoAdapter videoListAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_reels_video, container, false);
        ButterKnife.bind(this, root);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ReelsVideoViewModel.class);

        setUpView();

        bindViewModel();




        return root;
    }

    private void setUpView() {
        viewModel.getMediaList(sharedPref.getRegisterId());
    }


    private void bindViewModel() {
        viewModel.getGalleryUploadVideoListResponse()
                .observe(this, homePageApi -> {
                    assert homePageApi != null;
                    switch (homePageApi.status) {
                        case SUCCESS:
                            try {
                                videoListModelList.clear();
                                String response = String.valueOf(homePageApi.responce);
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Gson gson = new Gson();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    ReelsVideoModel mediaModel = gson.fromJson(jsonObject.toString(), ReelsVideoModel.class);
                                    videoListModelList.add(mediaModel);
                                }
                                setImageAdapter();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert homePageApi.error != null;
                            showErrorMessage(content_parent, homePageApi.error.getMessage());
                            break;
                    }
                });


        viewModel.getLikesResponse()
                .observe(this, homePageApi -> {
                    assert homePageApi != null;
                    switch (homePageApi.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(homePageApi.responce);
                                JSONObject jsonArray = new JSONObject(response);

//                                Toast.makeText(getActivity(), ""+jsonArray.getString("message"), Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert homePageApi.error != null;
                            showErrorMessage(content_parent, homePageApi.error.getMessage());
                            break;
                    }
                });

        viewModel.getDeductedImgResponse()
                .observe(this, uploadImage -> {
                    assert uploadImage != null;
                    switch (uploadImage.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(uploadImage.responce);
                                JSONObject jsonObject = new JSONObject(response);

                                try {
                                    sharedPref.setTotalCoins(jsonObject.getString("TotalCoins"));
                                } catch (Exception ignored) {
                                    sharedPref.setTotalCoins("0");
                                }

                                setUpView();

                            } catch (Exception e) {
                                Log.e("Error", e.getMessage());
                            }
                            break;
                        case ERROR:
                            assert uploadImage.error != null;
                            showErrorMessage(content_parent, uploadImage.error.getMessage());
                            break;
                    }
                });
    }

    private void setImageAdapter() {
        if (videoListAdapter == null) {
            videoListAdapter = new ReelsVideoAdapter(getActivity(), videoListModelList, this,
                    viewModel,sharedPref.getRegisterId(),sharedPref.getUserName(),sharedPref.getTotalCoins());
        } else {
            videoListAdapter.setModelList(videoListModelList);
            videoListAdapter.notifyDataSetChanged();
        }
        videosViewPager.setAdapter(videoListAdapter);
    }

    @Override
    public void onClick(View view) {
   /*     ReelsVideoModel reelsVideoModel = (ReelsVideoModel) view.getTag();
        switch (view.getId()) {
            case R.id.iv_video_like:
                VideoAddLikeModel  videoAddLikeModel = new VideoAddLikeModel();
                videoAddLikeModel.setRegistrationId(sharedPref.getRegisterId());
                videoAddLikeModel.setUserName(sharedPref.getUserName());
                videoAddLikeModel.setMediaId(reelsVideoModel.getMediaId());
                videoAddLikeModel.setMediaName(reelsVideoModel.getVideoName());

                viewModel.addLinksApi(videoAddLikeModel);

                break;

        }*/
    }
}