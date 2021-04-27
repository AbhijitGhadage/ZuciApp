package com.zuci.zuciapp.ui.gallery;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
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

import com.zuci.zuciapp.utils.Methods;
import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.base.BaseActivity;
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

public class GalleryActivity extends BaseActivity<GalleryViewModel> implements View.OnClickListener {
    @Inject
    ViewModelFactory<GalleryViewModel> viewModelFactory;
    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;
    private GalleryViewModel galleryViewModel;
    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;
    @BindView(R.id.iv_back_btn)
    AppCompatImageView iv_back_btn;
    @BindView(R.id.recyclerView_gallery)
    RecyclerView recyclerView_gallery;

    private List<MediaModel> galleryListModelList;
    private GalleryAdapter galleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);
        galleryViewModel = ViewModelProviders.of(this, viewModelFactory).get(GalleryViewModel.class);

        galleryListModelList = new ArrayList<>();
        GridLayoutManager manager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView_gallery.setLayoutManager(manager);
        recyclerView_gallery.setHasFixedSize(true);

        setUpView();
        bindViewModel();
    }

    private void bindViewModel() {
        galleryViewModel.getMediaList(sharedPref.getRegisterId()).observe(this, uploadImage -> {
            assert uploadImage != null;
            switch (uploadImage.status) {
                case SUCCESS:
                    try {
                        galleryListModelList.clear();
                        String response = String.valueOf(uploadImage.responce);
                        JSONArray jsonArray = new JSONArray(response);
                        for (int index = 0; index < jsonArray.length(); index++) {
                            Gson gson = new Gson();
                            JSONObject jsonObject = jsonArray.getJSONObject(index);
                            MediaModel mediaModel = gson.fromJson(jsonObject.toString(), MediaModel.class);
                            galleryListModelList.add(mediaModel);
                        }
                        setImageAdapter();
                    } catch (Exception e) {
                        Log.e("getImageList", e.getMessage());
                    }
                    break;
                case ERROR:
                    assert uploadImage.error != null;
                    onErrorMessage(content_parent, uploadImage.error);
                    break;
            }
        });
    }

    private void setImageAdapter() {
        if (galleryAdapter == null) {
            galleryAdapter = new GalleryAdapter(this, galleryListModelList, this);
        } else {
            galleryAdapter.setGalleryListModelList(galleryListModelList);
            galleryAdapter.notifyDataSetChanged();
        }
        recyclerView_gallery.setAdapter(galleryAdapter);
    }

    @Override
    protected GalleryViewModel getViewModel() {
        return galleryViewModel;
    }

    @OnClick(R.id.fab_upload_img)
    public void uploadImage() {
        navigator.navigateToUploadImg(this);
        rightToLeftAnimated();
    }

    private void setUpView() {
        galleryViewModel.getMediaList(sharedPref.getRegisterId());
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
    public void onClick(View view) {
        MediaModel mediaModel = (MediaModel) view.getTag();
        switch (view.getId()) {
            case R.id.iv_gallery_image:
                navigator.navigateToImageView(this, mediaModel);
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
        builder.setMessage("Are You Sure To Delete This Image ?");
        builder.setPositiveButton("YES", (dialog, which) -> {
            deleteImage(mediaModel.getId());
            dialog.dismiss();
        });
        builder.setNegativeButton("NO", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteImage(long mediaId) {
        galleryViewModel.deleteMediaFile(mediaId).observe(this, apiResponse -> {
            assert apiResponse != null;
            switch (apiResponse.status) {
                case SUCCESS:
                    try {
                        String response = String.valueOf(apiResponse.responce);
                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.getBoolean("status");
                        String message = jsonObject.getString("message");
                        Methods.showMessage(GalleryActivity.this, message);
                        Toast.makeText(this, "Delete Image !!", Toast.LENGTH_SHORT).show();
                        if (status)
                            bindViewModel();
                    } catch (Exception e) {
                        Log.e("deleteImage", e.getMessage());
                    }
                    break;
                case ERROR:
                    assert apiResponse.error != null;
                    onErrorMessage(content_parent, apiResponse.error);
                    break;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantApp.MEDIA_REQUEST_CODE && resultCode == ConstantApp.MEDIA_RESULT_CODE)
            bindViewModel();
    }
}