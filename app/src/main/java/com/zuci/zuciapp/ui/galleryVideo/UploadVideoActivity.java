package com.zuci.zuciapp.ui.galleryVideo;

import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.base.BaseActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class UploadVideoActivity extends BaseActivity<GalleryVideoViewModel> {

    @Inject
    ViewModelFactory<GalleryVideoViewModel> viewModelFactory;
    private GalleryVideoViewModel galleryViewModel;
    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;

    @BindView(R.id.content_parent)
    RelativeLayout content_parent;

    @BindView(R.id.edit_gallery_title)
    EditText edit_gallery_title;
    @BindView(R.id.edit_gallery_desc)
    EditText edit_gallery_desc;
    @BindView(R.id.video_gallery_upload)
    VideoView video_gallery_upload;
    @BindView(R.id.btn_upload_video)
    AppCompatButton btn_upload_video;

    @BindView(R.id.rg_video_p)
    RadioGroup rg_video_p;
    @BindView(R.id.rb_public)
    RadioButton rb_public;

    @BindView(R.id.ll_rate)
    LinearLayout ll_rate;
    @BindView(R.id.edit_set_rate) EditText edit_set_rate;

    String path;
    private static final int BUFFER_SIZE = 1024 * 2;
    private static int videoSizeCount = 0;


//    private static final String IMAGE_DIRECTORY = "/demonuts_upload_gallery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);
        galleryViewModel = ViewModelProviders.of(this, viewModelFactory).get(GalleryVideoViewModel.class);

        rb_public.setChecked(true);


        bindViewModel();

        rg_video_p.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
    protected GalleryVideoViewModel getViewModel() {
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

    @OnClick(R.id.video_gallery_upload)
    public void OnClickVideo() {
        selectVideo();
    }


    @OnClick(R.id.btn_upload_video)
    public void OnClickUploadVideo() {
        if (videoSizeCount < 70000999) {
            video_gallery_upload.pause();
            uploadImage();
        } else {
            Toast.makeText(this, "Please upload video size less than 70 MB !!", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {
        File file = new File(path);
        if (isOnline()) {
            btn_upload_video.setBackground(btn_upload_video.getContext().getResources().getDrawable(R.drawable.shape_btn2));
            galleryViewModel.uploadVideo(getImageUploadData(), file);
            //galleryViewModel.uploadImage(registerId, imageTitle, file);
        } else {
            navigator.navigateInternetConnectErrorScreen(this);
            rightToLeftAnimated();
        }

    }

    private Map<String, okhttp3.RequestBody> getImageUploadData() {
        int selectedId = rg_video_p.getCheckedRadioButtonId();
        RadioButton radioSexButton = (RadioButton) findViewById(selectedId);
        String sltPublicPri = radioSexButton.getText().toString().trim();
        String videoRates;
        if(sltPublicPri.equalsIgnoreCase("Public"))
            videoRates = "0";
        else
            videoRates = edit_set_rate.getText().toString().trim();

        int registerId = sharedPref.getRegisterId();
        String imageTitle = edit_gallery_title.getText().toString().trim();
        String desc = edit_gallery_desc.getText().toString().trim();
        Map<String, okhttp3.RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("RegistrationId", okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), String.valueOf(registerId)));
        requestBodyMap.put("Title", okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), imageTitle));
        requestBodyMap.put("Status", okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), sltPublicPri));
        requestBodyMap.put("MediaDescription", okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), desc));
        requestBodyMap.put("MediaType", okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), ConstantApp.VIDEO));
        requestBodyMap.put("SetCoins", RequestBody.create(MediaType.parse("text/plain"), videoRates));
        return requestBodyMap;
    }

    private void selectVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();

            path = getFilePathFromURI(this, uri);
            Log.d("ioooossss", path);

            // duration cal
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path);
            long duration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            retriever.release();

            Uri video = Uri.parse(path);
            video_gallery_upload.setVideoURI(video);
            video_gallery_upload.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    video_gallery_upload.start();
                }
            });
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void bindViewModel() {
        galleryViewModel.getGalleryUploadVideoResponse()
                .observe(this, uploadImage -> {
                    assert uploadImage != null;
                    switch (uploadImage.status) {
                        case SUCCESS:
                            setResult(ConstantApp.MEDIA_RESULT_CODE);
                            btn_upload_video.setBackground(btn_upload_video.getContext().getResources().getDrawable(R.drawable.shape_btn));
                            onBackPressed();
                            Toast.makeText(this, "Upload video Successful !!", Toast.LENGTH_SHORT).show();

                            break;
                        case ERROR:
                            btn_upload_video.setBackground(btn_upload_video.getContext().getResources().getDrawable(R.drawable.shape_btn));
                            assert uploadImage.error != null;
                            onErrorMessage(content_parent, uploadImage.error);
                            break;
                    }
                });
    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path

//        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory().toString());

        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        File copyFile = new File(wallpaperDirectory + File.separator + Calendar.getInstance()
                .getTimeInMillis() + ".mp4");
        // create folder if not exists

        copy(context, contentUri, copyFile);
        Log.d("vPath--->", copyFile.getAbsolutePath());

        return copyFile.getAbsolutePath();

    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            copystream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int copystream(InputStream input, OutputStream output) throws Exception {
        byte[] buffer = new byte[BUFFER_SIZE];

        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        videoSizeCount = 0;
        int n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                videoSizeCount += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
            try {
                in.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
        }
        return videoSizeCount;
    }

}