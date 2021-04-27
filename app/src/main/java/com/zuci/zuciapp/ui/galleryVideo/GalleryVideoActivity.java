package com.zuci.zuciapp.ui.galleryVideo;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.base.BaseActivity;
import com.zuci.zuciapp.ui.gallery.MediaModel;
import com.zuci.zuciapp.utils.Methods;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class GalleryVideoActivity extends BaseActivity<GalleryVideoViewModel> implements View.OnClickListener {

    @Inject
    ViewModelFactory<GalleryVideoViewModel> viewModelFactory;

    private GalleryVideoViewModel galleryVideoViewModel;

    @Inject
    Navigator navigator;

    @Inject
    SharedPref sharedPref;

    @Override
    protected GalleryVideoViewModel getViewModel() {
        return galleryVideoViewModel;
    }

    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;
    @BindView(R.id.recyclerView_video_list)
    RecyclerView recyclerView_video_list;
    private List<MediaModel> videoListModelList;
    private VideoListAdapter videoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_video);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        galleryVideoViewModel = ViewModelProviders.of(this, viewModelFactory).get(GalleryVideoViewModel.class);

        videoListModelList = new ArrayList<>();
        GridLayoutManager manager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView_video_list.setLayoutManager(manager);
        recyclerView_video_list.setHasFixedSize(true);

        setUpView();

        bindViewModel();
    }

    @OnClick(R.id.fab_upload_video)
    public void uploadVideo() {
        navigator.navigateToUploadVideo(this);
        rightToLeftAnimated();
    }

    private void setUpView() {
        galleryVideoViewModel.getMediaList(sharedPref.getRegisterId());
    }

    private void bindViewModel() {
        galleryVideoViewModel.getGalleryUploadVideoListResponse()
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
                                    MediaModel mediaModel = gson.fromJson(jsonObject.toString(), MediaModel.class);
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

        galleryVideoViewModel.getDeleteVideoResponse()
                .observe(this, videoApi -> {
                    assert videoApi != null;
                    switch (videoApi.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(videoApi.responce);
                                JSONObject jsonObject = new JSONObject(response);
                                boolean status = jsonObject.getBoolean("status");
                                String message = jsonObject.getString("message");
                                Methods.showMessage(GalleryVideoActivity.this, message);
                                Toast.makeText(this, "Delete Video !!", Toast.LENGTH_SHORT).show();
                                if (status)
                                    bindViewModel();
                            } catch (Exception e) {
                                Log.e("deleteImage", e.getMessage());
                            }

                            break;
                        case ERROR:
                            assert videoApi.error != null;
                            showErrorMessage(content_parent, videoApi.error.getMessage());
                            break;
                    }
                });
    }

    private void setImageAdapter() {
        if (videoListAdapter == null) {
            videoListAdapter = new VideoListAdapter(this, videoListModelList, this);
        } else {
            videoListAdapter.setGalleryListModelList(videoListModelList);
            videoListAdapter.notifyDataSetChanged();
        }
        recyclerView_video_list.setAdapter(videoListAdapter);
    }

    @OnClick(R.id.iv_back_btn)
    public void OnClickBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        leftToRightAnimated();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantApp.MEDIA_REQUEST_CODE && resultCode == ConstantApp.MEDIA_RESULT_CODE)
            setUpView();
    }

    @Override
    public void onClick(View view) {
        MediaModel mediaModel = (MediaModel) view.getTag();
        switch (view.getId()) {
            case R.id.video_view:
                navigator.navigateToVideoView(this, mediaModel);
                rightToLeftAnimated();
                break;
            case R.id.iv_gallery_image_delete:
                confirmToDeleteImage(mediaModel);
                break;
        }
    }

    private void confirmToDeleteImage(MediaModel mediaModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are You Sure To Delete This Video ?");
        builder.setPositiveButton("YES", (dialog, which) -> {
            galleryVideoViewModel.deleteMediaFile(mediaModel.getId());
            dialog.dismiss();
        });
        builder.setNegativeButton("NO", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}