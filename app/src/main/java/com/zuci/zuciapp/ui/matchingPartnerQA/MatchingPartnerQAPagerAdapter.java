package com.zuci.zuciapp.ui.matchingPartnerQA;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.PagerAdapter;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.utils.Methods;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchingPartnerQAPagerAdapter extends PagerAdapter {
    private MatchingPartnerQueAnsActivity activity;
    private LayoutInflater layoutInflater;
    private List<QueAnsModel> queAnsModelList;

    public MatchingPartnerQAPagerAdapter(MatchingPartnerQueAnsActivity activity, List<QueAnsModel> queAnsModelList) {
        this.activity = activity;
        this.queAnsModelList = queAnsModelList;
    }

    @Override
    public int getCount() {
        return queAnsModelList != null ? queAnsModelList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fragment_matching_partner_pager, container, false);
        TextView tv_question = view.findViewById(R.id.tv_question);
        RadioGroup radioGroupSelf = view.findViewById(R.id.rg_self_info);
        RadioGroup radioGroupSelfPartner = view.findViewById(R.id.rg_partner_info);
        CircleImageView profileImageView = view.findViewById(R.id.circle_profile_image);
        AppCompatButton btnSubmit = view.findViewById(R.id.btn_profile_create);

        // set data to view
        try {
            QueAnsModel queAnsModel = queAnsModelList.get(position);
            if (queAnsModel != null) {
                if (!Methods.isEmptyOrNull(queAnsModel.getQuestion())) {
                    tv_question.setText(queAnsModel.getQuestion());
                }
                List<RadioButton> selfRadioButtonArray = new ArrayList<>();
                List<RadioButton> partnerRadioButtonArray = new ArrayList<>();
                if (!Methods.isEmptyOrNull(queAnsModel.getOptOne())) {
                    selfRadioButtonArray.add(getRadioButtonView(queAnsModel.getOptOne(), 0));
                    partnerRadioButtonArray.add(getRadioButtonView(queAnsModel.getOptOne(), 0));
                }
                if (!Methods.isEmptyOrNull(queAnsModel.getOptTwo())) {
                    selfRadioButtonArray.add(getRadioButtonView(queAnsModel.getOptTwo(), 1));
                    partnerRadioButtonArray.add(getRadioButtonView(queAnsModel.getOptTwo(), 1));
                }
                if (!Methods.isEmptyOrNull(queAnsModel.getOptThree())) {
                    selfRadioButtonArray.add(getRadioButtonView(queAnsModel.getOptThree(), 2));
                    partnerRadioButtonArray.add(getRadioButtonView(queAnsModel.getOptThree(), 2));
                }
                if (!Methods.isEmptyOrNull(queAnsModel.getOptFour())) {
                    selfRadioButtonArray.add(getRadioButtonView(queAnsModel.getOptFour(), 3));
                    partnerRadioButtonArray.add(getRadioButtonView(queAnsModel.getOptFour(), 3));
                }
                for (RadioButton radioButton : selfRadioButtonArray) {
                    radioGroupSelf.addView(radioButton);
                    radioButton.setTextColor(R.color.app_txt_color_black1);
                }
                for (RadioButton radioButton : partnerRadioButtonArray) {
                    radioGroupSelfPartner.addView(radioButton);
                    radioButton.setTextColor(R.color.app_txt_color_black1);
                }
                MatchingPartnerQueAnsModel partnerQueAnsModel = activity.getMatchingPartnerModelByQueId(queAnsModel.getqId());

                radioGroupSelf.setOnCheckedChangeListener((group, checkedId) -> {
                    final RadioButton rb = view.findViewById(checkedId);
//                    rb.setTextColor(R.color.app_txt_color_black1);
                    partnerQueAnsModel.setAnswer(rb.getText().toString());
                    partnerQueAnsModel.setAnswerId(checkedId);
                    if (!Methods.isEmptyOrNull(partnerQueAnsModel.getAnswer()) &&
                            !Methods.isEmptyOrNull(partnerQueAnsModel.getPartnerAnswer())) {
                        btnSubmit.setVisibility(View.VISIBLE);
                    }
                });

                radioGroupSelfPartner.setOnCheckedChangeListener((group, checkedId) -> {
                    final RadioButton rb = view.findViewById(checkedId);
                    partnerQueAnsModel.setPartnerAnswer(rb.getText().toString());
                    partnerQueAnsModel.setPartnerAnswerId(checkedId);
                    if (!Methods.isEmptyOrNull(partnerQueAnsModel.getAnswer()) &&
                            !Methods.isEmptyOrNull(partnerQueAnsModel.getPartnerAnswer())) {
                        btnSubmit.setVisibility(View.VISIBLE);
                    }
                });
                // check if question is submitted
                if (partnerQueAnsModel != null) {
                    if (!Methods.isEmptyOrNull(partnerQueAnsModel.getAnswer())) {
                        btnSubmit.setVisibility(View.VISIBLE);
                        if (partnerQueAnsModel.getAnswerId() != -1)
                            radioGroupSelf.check(partnerQueAnsModel.getAnswerId());
                        if (partnerQueAnsModel.getPartnerAnswerId() != -1)
                            radioGroupSelfPartner.check(partnerQueAnsModel.getPartnerAnswerId());
                    } else {
                        btnSubmit.setVisibility(View.GONE);
                    }
                }
                onClickSubmitButton(btnSubmit, partnerQueAnsModel);
            }
        } catch (Exception e) {
            Log.e("instantiateItem", e.getMessage());
        }
        container.addView(view);
        return view;
    }

    private void onClickSubmitButton(AppCompatButton btnSubmit, MatchingPartnerQueAnsModel partnerQueAnsModel) {
        btnSubmit.setOnClickListener(v -> {
            activity.updateMatchingPartnerModelByQueId(partnerQueAnsModel);
            activity.setNextView();
        });
    }

    private RadioButton getRadioButtonView(String option, int id) {
        RadioButton rb = new RadioButton(activity);
        rb.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rb.setText(option);
        rb.setPadding(0, 5, 0, 0);
        rb.setId(id);
        return rb;
    }
}