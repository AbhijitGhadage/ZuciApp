package com.zuci.zuciapp.ui.imgVideoView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseActivity;
import com.zuci.zuciapp.ui.gallery.MediaModel;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zuci.zuciapp.di.modules.NetworkModule.IMAGE_URL;

public class ViewImageActivity extends BaseActivity<ViewsViewModel> {
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

    @BindView(R.id.view_image_view)
    ImageView view_image_view;
    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;

    Uri video;
    int videoId;

    @BindView(R.id.rg_edit_image)
    RadioGroup rg_edit_image;
    @BindView(R.id.rb_edit_public)
    RadioButton rb_edit_public;
    @BindView(R.id.rb_edit_private)
    RadioButton rb_edit_private;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        ButterKnife.bind(this);

        viewsViewModel = ViewModelProviders.of(this, viewModelFactory).get(ViewsViewModel.class);

        try {
            MediaModel mediaModel = getIntent().getParcelableExtra("MEDIA_MODEL");
            MediaModel mediaModelProfile = getIntent().getParcelableExtra("MEDIA_MODEL_PROFILE");


            if (mediaModel != null) {
                rg_edit_image.setVisibility(View.VISIBLE);


                Picasso.get()
                        .load(IMAGE_URL + mediaModel.getName())
                        .into(view_image_view);

                videoId = (int) mediaModel.getId();


                if (mediaModel.getStatus().equalsIgnoreCase("Public")) {
                    rb_edit_public.setChecked(true);
                } else {
                    rb_edit_private.setChecked(true);
                }

            } else {
                rg_edit_image.setVisibility(View.GONE);

                Picasso.get()
                        .load(IMAGE_URL + mediaModelProfile.getName())
                        .into(view_image_view);
            }

        } catch (Exception ignored) {
        }

        bindViewModel();
    }


    public void onClickedPublic(View view) {
        int selectedId = rg_edit_image.getCheckedRadioButtonId();
        RadioButton radioSexButton = (RadioButton) findViewById(selectedId);
        String sltPublicPri = radioSexButton.getText().toString().trim();

        viewsViewModel.updateStatus(sharedPref.getRegisterId(), videoId, sltPublicPri);

    }

    public void onClickedPrivate(View view) {
        int selectedId = rg_edit_image.getCheckedRadioButtonId();
        RadioButton radioSexButton = (RadioButton) findViewById(selectedId);
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