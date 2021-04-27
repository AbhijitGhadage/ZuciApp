package com.zuci.zuciapp.ui.imgVideoView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.VideoView;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseActivity;
import com.zuci.zuciapp.ui.gallery.MediaModel;
import com.zuci.zuciapp.ui.homeViewDetails.tabFragment.MediaVideoListModel;

import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

import static com.zuci.zuciapp.di.modules.NetworkModule.IMAGE_URL;

public class ViewVideoActivity extends BaseActivity<ViewsViewModel> {
    @Inject
    ViewModelFactory<ViewsViewModel> viewModelFactory;

    private ViewsViewModel viewsViewModel;

    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;

    @Override
    protected ViewsViewModel getViewModel() {
        return viewsViewModel;
    }

    @BindView(R.id.view_video_view)
    VideoView view_video_view;
    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;

    Uri video;
    int videoId;

    @BindView(R.id.rg_edit_video_p)
    RadioGroup rg_edit_video_p;
    @BindView(R.id.rb_edit_public)
    RadioButton rb_edit_public;
    @BindView(R.id.rb_edit_private) RadioButton rb_edit_private;
    @BindView(R.id.iv_pause) ImageView iv_pause;

    private  boolean viewsApiCall = false,videoPause = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        viewsViewModel = ViewModelProviders.of(this, viewModelFactory).get(ViewsViewModel.class);

        try {
            MediaModel mediaModel = getIntent().getParcelableExtra("MEDIA_MODEL");

            MediaVideoListModel mediaVideoListModel = getIntent().getParcelableExtra("MEDIA_MODEL_HOME");

            if (mediaModel != null) {
                rg_edit_video_p.setVisibility(View.VISIBLE);
                video = Uri.parse(IMAGE_URL + mediaModel.getName());

                videoId = (int) mediaModel.getId();

                if (mediaModel.getStatus().equalsIgnoreCase("Public")) {
                    rb_edit_public.setChecked(true);
                } else {
                    rb_edit_private.setChecked(true);
                }

            } else {
                viewsApiCall = true;
                rg_edit_video_p.setVisibility(View.GONE);
                video = Uri.parse(IMAGE_URL + mediaVideoListModel.getName());
            }

            view_video_view.setVideoURI(video);
            view_video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    view_video_view.start();
                    if (viewsApiCall)
                        viewsViewModel.setViewsCount(sharedPref.getRegisterId(), mediaVideoListModel.getRegistrationId(),mediaVideoListModel.getId());
                }
            });
        } catch (Exception ignored) {
        }

        bindViewModel();

        view_video_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoPause) {
                    view_video_view.pause();
                    videoPause = false;
                    iv_pause.setVisibility(View.VISIBLE);
                    iv_pause.setImageDrawable(getResources().getDrawable(R.drawable.ic_vector_play_button));

                } else {
//                    holder.mVideoView.canPause();
                    view_video_view.start();
                    videoPause = true;
                    iv_pause.setVisibility(View.GONE);
//                    holder.iv_pause.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_vector_like_dis));
                }
            }
        });

    }

    public void onClickedPublic(View view) {
        int selectedId = rg_edit_video_p.getCheckedRadioButtonId();
        RadioButton radioSexButton = findViewById(selectedId);
        String sltPublicPri = radioSexButton.getText().toString().trim();

        viewsViewModel.updateStatus(sharedPref.getRegisterId(), videoId, sltPublicPri);

    }

    public void onClickedPrivate(View view) {
        int selectedId = rg_edit_video_p.getCheckedRadioButtonId();
        RadioButton radioSexButton = findViewById(selectedId);
        String sltPublicPri = radioSexButton.getText().toString().trim();

        viewsViewModel.updateStatus(sharedPref.getRegisterId(), videoId, sltPublicPri);
    }

    private void bindViewModel() {
        viewsViewModel.getViewsResponse()
                .observe(this, forgotPass -> {
                    assert forgotPass != null;
                    switch (forgotPass.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(forgotPass.responce);
                                JSONObject jsonObject = new JSONObject(response);

                                Toast.makeText(this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert forgotPass.error != null;
                            onErrorMessage(content_parent, forgotPass.error);
                            break;
                    }
                });

        viewsViewModel.getViewsCountResponse()
                .observe(this, forgotPass -> {
                    assert forgotPass != null;
                    switch (forgotPass.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(forgotPass.responce);
                                JSONObject jsonObject = new JSONObject(response);

                                Toast.makeText(this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert forgotPass.error != null;
                            onErrorMessage(content_parent, forgotPass.error);
                            break;
                    }
                });
    }

    @OnClick(R.id.iv_back_btn)
    public void backPress() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        leftToRightAnimated();
    }
}