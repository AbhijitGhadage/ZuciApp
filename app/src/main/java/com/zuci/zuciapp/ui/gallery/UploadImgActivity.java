package com.zuci.zuciapp.ui.gallery;

import androidx.lifecycle.ViewModelProviders;
import androidx.loader.content.CursorLoader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zuci.zuciapp.utils.Methods;
import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.base.BaseActivity;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class UploadImgActivity extends BaseActivity<GalleryViewModel> {
    @Inject
    ViewModelFactory<GalleryViewModel> viewModelFactory;
    private GalleryViewModel galleryViewModel;
    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;
    @BindView(R.id.content_parent)
    RelativeLayout content_parent;
    @BindView(R.id.iv_gallery_upload)
    ImageView iv_gallery_upload;
    @BindView(R.id.edit_gallery_title) EditText edit_gallery_title;
    @BindView(R.id.edit_gallery_desc) EditText edit_gallery_desc;
    @BindView(R.id.rg_image_p) RadioGroup rg_image_p;
    @BindView(R.id.rb_public) RadioButton rb_public;

    @BindView(R.id.ll_rate)
    LinearLayout ll_rate;
    @BindView(R.id.edit_set_rate) EditText edit_set_rate;

    private Uri selectedImage;
//    ArrayList<Uri> selectedImage=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_img);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);
        galleryViewModel = ViewModelProviders.of(this, viewModelFactory).get(GalleryViewModel.class);

        rb_public.setChecked(true);


        bindViewModel();

        rg_image_p.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    String name= rb.getText().toString();
                    if(name.equalsIgnoreCase("Public")){
                        ll_rate.setVisibility(View.GONE);
                    }else {
                        ll_rate.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
    }

    @Override
    protected GalleryViewModel getViewModel() {
        return galleryViewModel;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        leftToRightAnimated();
    }

    @OnClick(R.id.iv_back_btn)
    public void OnClickBack() {
        onBackPressed();
    }

    @OnClick(R.id.iv_gallery_upload)
    public void OnClickImg() {
        selectImage();
    }

    @OnClick(R.id.btn_upload_img)
    public void OnClickUploadImg() {
        uploadImage();
    }

    private void selectImage() {
        final CharSequence[] options = {ConstantApp.TAKE_PHOTO, ConstantApp.CHOOSE_FROM_GALLERY, ConstantApp.CANCEL};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Select Image");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals(ConstantApp.TAKE_PHOTO)) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            } else if (options[item].equals(ConstantApp.CHOOSE_FROM_GALLERY)) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);

         /*
                  Intent intent=new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select images"), 1);
         */

            } else if (options[item].equals(ConstantApp.CANCEL)) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK) {
                        Bitmap image = (Bitmap) data.getExtras().get("data");
                        iv_gallery_upload.setImageBitmap(image);
                        selectedImage = Methods.getImageUri(this, image);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK) {
                        selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                                iv_gallery_upload.setImageBitmap(bitmap);
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void uploadImage() {
        File file = null;
        if (selectedImage != null)
            file = new File(getRealPathFromURI(selectedImage));
        if (file != null) {
            if (isOnline()) {
                galleryViewModel.uploadImage(getImageUploadData(), file);
                //galleryViewModel.uploadImage(registerId, imageTitle, file);
            } else {
                navigator.navigateInternetConnectErrorScreen(this);
                rightToLeftAnimated();
            }
        } else {
            Methods.showMessage(this, "Please select image");
        }
    }

    private Map<String, RequestBody> getImageUploadData() {
        int selectedId=rg_image_p.getCheckedRadioButtonId();
        RadioButton radioSexButton=(RadioButton)findViewById(selectedId);
        String sltPublicPri = radioSexButton.getText().toString().trim();
        String imageRates;
        if(sltPublicPri.equalsIgnoreCase("Public"))
            imageRates = "0";
        else
            imageRates = edit_set_rate.getText().toString().trim();

        int registerId = sharedPref.getRegisterId();
        String imageTitle = edit_gallery_title.getText().toString().trim();
        String desc = edit_gallery_desc.getText().toString().trim();
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("RegistrationId", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(registerId)));
        requestBodyMap.put("Title", RequestBody.create(MediaType.parse("text/plain"), imageTitle));
        requestBodyMap.put("Status", RequestBody.create(MediaType.parse("text/plain"), sltPublicPri));
        requestBodyMap.put("MediaDescription", RequestBody.create(MediaType.parse("text/plain"), desc));
        requestBodyMap.put("MediaType", RequestBody.create(MediaType.parse("text/plain"), ConstantApp.IMAGE));
        requestBodyMap.put("SetCoins", RequestBody.create(MediaType.parse("text/plain"), imageRates));
        return requestBodyMap;
    }

    private void bindViewModel() {
        galleryViewModel.getGalleryUploadResponse()
                .observe(this, uploadImage -> {
                    assert uploadImage != null;
                    switch (uploadImage.status) {
                        case SUCCESS:
                            setResult(ConstantApp.MEDIA_RESULT_CODE);
                            onBackPressed();
                            Toast.makeText(this, "Upload Image Successful !!", Toast.LENGTH_SHORT).show();

                            break;
                        case ERROR:
                            assert uploadImage.error != null;
                            onErrorMessage(content_parent, uploadImage.error);
                            break;
                    }
                });
    }
}