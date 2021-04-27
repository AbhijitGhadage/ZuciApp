package com.zuci.zuciapp.ui.homeViewDetails.tabFragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseFragment;
import com.zuci.zuciapp.ui.gallery.MediaModel;
import com.zuci.zuciapp.ui.homeViewDetails.HomeViewDetailActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class ImageFragment extends BaseFragment<TabViewModel> implements View.OnClickListener {

    @Inject
    ViewModelFactory<TabViewModel> viewModelFactory;
    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;
    TabViewModel viewModel;

    @Override
    public TabViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    public ImageFragment() {
        // Required empty public constructor
    }

    static int userId;

    public static ImageFragment newInstance(int aaa) {
        userId = aaa;
        return new ImageFragment();
    }

    @BindView(R.id.ll_root)
    LinearLayout ll_root;

    @BindView(R.id.recyclerView_home_img_list)
    RecyclerView recyclerView_home_img_list;
    private List<MediaModel> mediaModelList;
    private HomeImageListAdapter homeImageListAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_images, container, false);
        ButterKnife.bind(this, root);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TabViewModel.class);

        if (getArguments() != null) {
            int parameter = getArguments().getInt("USER_ID");
        }

        mediaModelList = new ArrayList<>();
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        recyclerView_home_img_list.setLayoutManager(manager);
//        recyclerView_home_img_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        recyclerView_home_img_list.setHasFixedSize(true);

        setUpView();

        bindViewModel();


        return root;
    }

    private void setUpView() {
        viewModel.getMediaIMGList(userId, sharedPref.getRegisterId());
    }

    private void bindViewModel() {
        viewModel.ResponseHomeImageList()
                .observe(this, uploadImage -> {
                    assert uploadImage != null;
                    switch (uploadImage.status) {
                        case SUCCESS:
                            try {
                                mediaModelList.clear();
                                String response = String.valueOf(uploadImage.responce);
                                JSONArray jsonArray = new JSONArray(response);
                                for (int index = 0; index < jsonArray.length(); index++) {
                                    Gson gson = new Gson();
                                    JSONObject jsonObject = jsonArray.getJSONObject(index);
                                    MediaModel mediaModel = gson.fromJson(jsonObject.toString(), MediaModel.class);
                                    mediaModelList.add(mediaModel);
                                }
                                setImageAdapter();
                            } catch (Exception e) {
                                Log.e("getImageList", e.getMessage());
                            }
                            break;
                        case ERROR:
                            assert uploadImage.error != null;
                            showErrorMessage(ll_root, uploadImage.error.getMessage());
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
                            showErrorMessage(ll_root, uploadImage.error.getMessage());
                            break;
                    }
                });
    }

    private void setImageAdapter() {
        if (homeImageListAdapter == null) {
            homeImageListAdapter = new HomeImageListAdapter(getActivity(), mediaModelList, this);
        } else {
            homeImageListAdapter.setGalleryListModelList(mediaModelList);
        }
        recyclerView_home_img_list.setAdapter(homeImageListAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_home_ad_image:
                MediaModel mediaModel11 = (MediaModel) view.getTag();
                if (mediaModel11.getStatus().equalsIgnoreCase("Public")) {
                    if (getActivity() instanceof HomeViewDetailActivity)
                        ((HomeViewDetailActivity) getActivity()).openImageView(mediaModel11);

                }else if (mediaModel11.getStatus().equalsIgnoreCase("Private")
                        && mediaModel11.getViewerStatus() == null) {
                    openDialog(mediaModel11);

                } else if (mediaModel11.getStatus().equalsIgnoreCase("Private")
                        && mediaModel11.getViewerStatus().equalsIgnoreCase("Public")) {
                    if (getActivity() instanceof HomeViewDetailActivity)
                        ((HomeViewDetailActivity) getActivity()).openImageView(mediaModel11);

                }  else {
                    Toast.makeText(getActivity(), "View", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_image_private:
                MediaModel mediaModel = (MediaModel) view.getTag();
                if (mediaModel.getStatus().equalsIgnoreCase("Public")) {
                    if (getActivity() instanceof HomeViewDetailActivity)
                        ((HomeViewDetailActivity) getActivity()).openImageView(mediaModel);

                } else if (mediaModel.getStatus().equalsIgnoreCase("Private")
                        && mediaModel.getViewerStatus() == null) {
                    openDialog(mediaModel);

                } else if (mediaModel.getStatus().equalsIgnoreCase("Private")
                        && mediaModel.getViewerStatus().equalsIgnoreCase("Public")) {
                    if (getActivity() instanceof HomeViewDetailActivity)
                        ((HomeViewDetailActivity) getActivity()).openImageView(mediaModel);

                } else {
                    Toast.makeText(getActivity(), "View 2", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void openDialog(MediaModel mediaModel) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_deducted_coins);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView iv_close_dialog = dialog.findViewById(R.id.iv_close_dialog);
        TextView tv_dialog_sms = dialog.findViewById(R.id.tv_dialog_sms);
        AppCompatButton btn_dialog_cancel = dialog.findViewById(R.id.btn_dialog_cancel);
        AppCompatButton btn_dialog_ok = dialog.findViewById(R.id.btn_dialog_ok);

        tv_dialog_sms.setText("Spend " + mediaModel.getSetCoins() + " Coins for view this Image !");

        iv_close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCallDeduction(mediaModel);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void apiCallDeduction(MediaModel mediaModel) {
        long myCoins = Long.parseLong(sharedPref.getTotalCoins());
        long coins = mediaModel.getSetCoins();

        if (coins <= myCoins ) {
            long viewerCoins = coins / 2;
            long adminCoins = coins - viewerCoins;

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
            String date = sdf.format(System.currentTimeMillis());

            DeductionModel deductionModel = new DeductionModel();
            deductionModel.setViewerRegistrationId(sharedPref.getRegisterId());
            deductionModel.setMediaOwnerRegistrationId(mediaModel.getRegistrationId());
            deductionModel.setMediaId(mediaModel.getId());
            deductionModel.setDeductCoins(viewerCoins);
            deductionModel.setAdmindeductCoins(adminCoins);
            deductionModel.setDeductionType("Image");
            deductionModel.setDeductionDate(date);
            deductionModel.setViewerStatus("");

            viewModel.deductionCoinsApi(deductionModel);

        } else {
            Toast.makeText(getActivity(), "Coins Insufficient !", Toast.LENGTH_SHORT).show();
        }
    }
}