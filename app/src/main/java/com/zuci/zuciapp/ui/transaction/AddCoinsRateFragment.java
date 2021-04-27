package com.zuci.zuciapp.ui.transaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.AndroidSupportInjection;

public class AddCoinsRateFragment extends BaseFragment<AddCoinsRateViewModel> {

    @Inject
    ViewModelFactory<AddCoinsRateViewModel> coinsRateViewModelViewModelFactory;

    private AddCoinsRateViewModel addCoinsRateViewModel;

    @Inject
    Navigator navigator;

    @Inject
    SharedPref sharedPref;

    @Override
    protected AddCoinsRateViewModel getViewModel() {
        return addCoinsRateViewModel;
    }

    @BindView(R.id.content_parent)
    ConstraintLayout content_parent;
    @BindView(R.id.edit_rate_live_Call)
    EditText edit_rate_live_Call;
    @BindView(R.id.edit_rate_audio_Call)
    EditText edit_rate_audio_Call;
    @BindView(R.id.edit_rate_video_Call)
    EditText edit_rate_video_Call;
    @BindView(R.id.edit_rate_image)
    EditText edit_rate_image;
    @BindView(R.id.edit_rate_video)
    EditText edit_rate_video;
    @BindView(R.id.ll_rate_audio_Call)
    LinearLayout ll_rate_audio_Call;
    @BindView(R.id.ll_rate_video_Call)
    LinearLayout ll_rate_video_Call;

    @Override
    public void onAttach(@NotNull Context mContext) {
        AndroidSupportInjection.inject(this);
        super.onAttach(mContext);
    }

    static int userId;

    public AddCoinsRateFragment() {
        // Required empty public constructor
    }

    public static AddCoinsRateFragment newInstance(int aaa) {
        userId = aaa;
        return new AddCoinsRateFragment();
    }

    View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_add_coins_rate, container, false);
        ButterKnife.bind(this, root);

        addCoinsRateViewModel = ViewModelProviders.of(this, coinsRateViewModelViewModelFactory).get(AddCoinsRateViewModel.class);

        edit_rate_live_Call.setText("" + sharedPref.getLiveCall());
        edit_rate_audio_Call.setText("" + sharedPref.getAudioCall());
        edit_rate_video_Call.setText("" + sharedPref.getVideoCall());
        edit_rate_image.setText("" + sharedPref.getImageRate());
        edit_rate_video.setText("" + sharedPref.getVideoRate());

        bindViewModel();

        return root;
    }

    @OnClick(R.id.btn_save_rate)
    public void OnClickSave() {
        saveData();
    }

    private void saveData() {
        String liveCall = edit_rate_live_Call.getText().toString().trim();
        String audioCall = edit_rate_audio_Call.getText().toString().trim();
        String videoCall = edit_rate_video_Call.getText().toString().trim();
        String image = edit_rate_image.getText().toString().trim();
        String video = edit_rate_video.getText().toString().trim();

        if (liveCall.equalsIgnoreCase("")) {
            edit_rate_live_Call.setError("Please enter call rates !!");
            edit_rate_live_Call.requestFocus();
        } else if (audioCall.equalsIgnoreCase("")) {
            edit_rate_audio_Call.setError("Please enter call rates !!");
            edit_rate_audio_Call.requestFocus();
        } else if (videoCall.equalsIgnoreCase("")) {
            edit_rate_video_Call.setError("Please enter call rates !!");
            edit_rate_video_Call.requestFocus();
        } else if (image.equalsIgnoreCase("")) {
            edit_rate_image.setError("Please enter image rates !!");
            edit_rate_image.requestFocus();
        } else if (video.equalsIgnoreCase("")) {
            edit_rate_video.setError("Please enter video rates !!");
            edit_rate_video.requestFocus();
        } else {
            AddCoinsRateModel addCoinsRateModel = new AddCoinsRateModel();
            addCoinsRateModel.setRegistrationId(sharedPref.getRegisterId());
            addCoinsRateModel.setLiveCallCoins(Integer.parseInt(liveCall));
            addCoinsRateModel.setAudioCallCoins(Integer.parseInt(audioCall));
            addCoinsRateModel.setVideoCallCoins(Integer.parseInt(videoCall));
            addCoinsRateModel.setImageCoins(Integer.parseInt(image));
            addCoinsRateModel.setVideoCoins(Integer.parseInt(video));
            addCoinsRateViewModel.coinsRatesApi(addCoinsRateModel);
        }
    }

    @SuppressLint("SetTextI18n")
    public void bindViewModel() {
        addCoinsRateViewModel.getCoinsResponse()
                .observe(this, forgotPass -> {
                    assert forgotPass != null;
                    switch (forgotPass.status) {
                        case SUCCESS:
                            try {
                                String response = String.valueOf(forgotPass.responce);
                                JSONObject jsonObject = new JSONObject(response);

                                try {
                                    sharedPref.setLiveCall(jsonObject.getInt("LiveCallCoins"));
                                } catch (Exception e) {
                                    sharedPref.setLiveCall(0);
                                }

                                try {
                                    sharedPref.setAudioCall(jsonObject.getInt("AudioCallCoins"));
                                } catch (Exception e) {
                                    sharedPref.setAudioCall(0);
                                }
                                try {
                                    sharedPref.setVideoCall(jsonObject.getInt("VideoCallCoins"));
                                } catch (Exception e) {
                                    sharedPref.setVideoCall(0);
                                }
                                try {
                                    sharedPref.setImageRate(jsonObject.getInt("ImageCoins"));
                                } catch (Exception e) {
                                    sharedPref.setImageRate(0);
                                }
                                try {
                                    sharedPref.setVideoRate(jsonObject.getInt("VideoCoins"));
                                } catch (Exception e) {
                                    sharedPref.setVideoRate(0);
                                }

                                edit_rate_live_Call.setText("" + sharedPref.getLiveCall());
                                edit_rate_audio_Call.setText("" + sharedPref.getAudioCall());
                                edit_rate_video_Call.setText("" + sharedPref.getVideoCall());
                                edit_rate_image.setText("" + sharedPref.getImageRate());
                                edit_rate_video.setText("" + sharedPref.getVideoRate());

                                Toast.makeText(getActivity(), "Save coins successful !", Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert forgotPass.error != null;
                            showErrorMessage(root, forgotPass.error.getMessage());
                            break;
                    }
                });
    }
}