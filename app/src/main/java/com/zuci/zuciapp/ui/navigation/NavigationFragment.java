package com.zuci.zuciapp.ui.navigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.content.CursorLoader;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseFragment;
import com.zuci.zuciapp.ui.mainPage.MainHomeActivity;
import com.zuci.zuciapp.ui.transaction.TransactionFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.AndroidSupportInjection;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class NavigationFragment extends BaseFragment<NavigationViewModel> {

    @Inject
    ViewModelFactory<NavigationViewModel> viewModelFactory;

    @Inject
    Navigator navigator;

    @Inject
    SharedPref sharedPref;


    private NavigationViewModel viewModel;

    @Override
    public NavigationViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    public NavigationFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.circle_profile_image)
    CircleImageView circle_profile_image;
    @BindView(R.id.tv_user_profile_name)
    TextView tv_user_profile_name;
    @BindView(R.id.tv_total_coins)
    TextView tv_total_coins;
    @BindView(R.id.ll_btn_change_pass)
    LinearLayout ll_btn_change_pass;

    // google
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;

    View root;

    // image upload
    private Uri selectedImage;
    Bitmap bitmap;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_navigation, container, false);
        ButterKnife.bind(this, root);

        String registerType = sharedPref.getRegisterType();
        if (registerType.equalsIgnoreCase("Normal")) {
            ll_btn_change_pass.setVisibility(View.VISIBLE);
        } else {
            ll_btn_change_pass.setVisibility(View.GONE);
        }

        tv_total_coins.setText(sharedPref.getTotalCoins() + " Points");

        tv_user_profile_name.setText(sharedPref.getUserName());
        if (!sharedPref.getUserProfile().isEmpty() && sharedPref.getUserProfile() != null) {
            Picasso.get()
                    .load(sharedPref.getUserProfile())
                    .error(R.drawable.profile_male)
                    .placeholder(R.drawable.profile_male)
                    .into(circle_profile_image);
        }

        // google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(NavigationViewModel.class);


        bindViewModel();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        tv_total_coins.setText(sharedPref.getTotalCoins() + " Coins");
    }


    @OnClick(R.id.ll_btn_basic_profile)
    public void basicProfile() {
        if (getActivity() instanceof MainHomeActivity) {
            ((MainHomeActivity) getActivity()).openBasicProfile();
        }
    }

    @SuppressLint("NewApi")
    @OnClick(R.id.ll_btn_point_add)
    public void addPointsList() {
        if (getActivity() instanceof MainHomeActivity) {
            ((MainHomeActivity) getActivity()).attachFragment(new TransactionFragment());
        }
    }

    @OnClick(R.id.ll_btn_gallery_image)
    public void basicGallery() {
        if (getActivity() instanceof MainHomeActivity) {
            ((MainHomeActivity) getActivity()).openGallery();
        }
    }

    @OnClick(R.id.ll_btn_gallery_video)
    public void basicGalleryVideo() {
        if (getActivity() instanceof MainHomeActivity) {
            ((MainHomeActivity) getActivity()).openGalleryVideo();
        }
    }

    @OnClick(R.id.ll_btn_setting)
    public void settings() {
        if (getActivity() instanceof MainHomeActivity) {
            ((MainHomeActivity) getActivity()).openSettings();
        }
    }

    @OnClick(R.id.ll_btn_change_pass)
    public void changePass() {
        if (getActivity() instanceof MainHomeActivity) {
            ((MainHomeActivity) getActivity()).openChangePass();
        }
    }

    @OnClick(R.id.ll_btn_feedback)
    public void feedback() {
        if (getActivity() instanceof MainHomeActivity) {
            ((MainHomeActivity) getActivity()).openFeedback();
        }
    }

    @OnClick(R.id.ll_btn_logout)
    public void logOutScreen() {
        if (getActivity() instanceof MainHomeActivity) {

            logOutGoogle();
            logOutFacebook();
            sharedPref.setLogin("False");
            ((MainHomeActivity) getActivity()).openLogOutScreen();
        }
    }

    public void logOutGoogle() {
        mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

        LoginManager.getInstance().logOut();
    }

    public void logOutFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE,
                new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();
                    }
                })
                .executeAsync();
    }

    @OnClick(R.id.circle_edit_profile)
    public void updateProfile() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            //the image URI
            selectedImage = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                // Log.d(TAG, String.valueOf(bitmap));


                updateImage();


            } catch (IOException e) {
                e.printStackTrace();

            }

        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getActivity(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    public void updateImage() {
        int registerId = sharedPref.getRegisterId();

        File file = null;
        if (selectedImage != null) {
            file = new File(getRealPathFromURI(selectedImage));
        }

        if (file != null) {
            viewModel.uploadImage(registerId, file);

        } else {
            Toast.makeText(getActivity(), "Please select image !!", Toast.LENGTH_SHORT).show();

        }

    }

    private void bindViewModel() {
        viewModel.getUpdateImgResponse()
                .observe(this, uploadImage -> {
                    assert uploadImage != null;
                    switch (uploadImage.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(uploadImage.responce);
                                JSONObject jsonObject = new JSONObject(response);

                                try {
                                    String imageUrl = jsonObject.getString("imageUrl");
                                    Picasso.get()
                                            .load(imageUrl)
                                            .error(R.drawable.profile_male)
                                            .placeholder(R.drawable.profile_male)
                                            .into(circle_profile_image);

                                    sharedPref.setUserProfile(imageUrl);
                                    Toast.makeText(getActivity(), "Update Profile Successful !!", Toast.LENGTH_SHORT).show();

//                                    circle_profile_image.setImageBitmap(bitmap);
                                } catch (Exception e) {

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert uploadImage.error != null;
                            showErrorMessage(root, uploadImage.error.getMessage());
                            break;
                    }
                });
    }


}