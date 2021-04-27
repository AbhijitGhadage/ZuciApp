package com.zuci.zuciapp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zuci.zuciapp.di.modules.ServiceBuilder;
import com.zuci.zuciapp.firebase.CallBack;
import com.zuci.zuciapp.network.TipsGoApiService;
import com.zuci.zuciapp.ui.agoraLive.LiveCallRepository;
import com.zuci.zuciapp.ui.agoraLive.LiveCallRepositoryImpl;
import com.zuci.zuciapp.ui.agoraLive.LiveListAdapter;
import com.zuci.zuciapp.ui.agoraLive.liveVideo.LiveResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.navigation.NavigationFragment;
import com.zuci.zuciapp.utils.Methods;
import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseFragment;
import com.zuci.zuciapp.ui.firestoreMessageChat.messageChatHistory.ChatListModel;
import com.zuci.zuciapp.ui.mainPage.MainHomeActivity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import dagger.android.support.AndroidSupportInjection;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment<HomeViewModel> implements View.OnClickListener {
    @Inject
    ViewModelFactory<HomeViewModel> viewModelFactory;
    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;
    HomeViewModel viewModel;

    @Override
    public HomeViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.tv_btn_home_live)
    AppCompatTextView tv_btn_home_live;
    @BindView(R.id.tv_btn_home_online)
    AppCompatTextView tv_btn_home_online;
    @BindView(R.id.tv_btn_home_new_user)
    AppCompatTextView tv_btn_home_new_user;

    @BindView(R.id.view_live)
    View view_live;
    @BindView(R.id.view_online)
    View view_online;
    @BindView(R.id.view_newUser)
    View view_newUser;

    @BindView(R.id.circle_profile_image)
    CircleImageView circle_profile_image;
    @BindView(R.id.recyclerView_home)
    RecyclerView recyclerView_home;
    private List<HomeListModel> homeListModels;
    private HomeAdapter homeAdapter;
    HomeListModel homeListModelOnCLick;

    @BindView(R.id.recyclerView_live_list)
    RecyclerView recyclerView_live_list;
    private LiveListAdapter liveListAdapter;

    private LiveCallRepository liveCallHistoryRepository;

    View root;
    Integer registerId = 0, pageNo = 1, typeId = 1;
    @BindView(R.id.edit_home_search)
    EditText edit_home_search;
    boolean typeList = false;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);
        registerId = sharedPref.getRegisterId();

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel.class);
        setUpView();

        listApiCall();
        bindViewModel();

        liveCallHistoryRepository = new LiveCallRepositoryImpl();
        getAllCallList();


        return root;
    }// ------ end onCreate()-----------

    @Override
    public void onResume() {
        super.onResume();
        if (!sharedPref.getUserProfile().isEmpty() && sharedPref.getUserProfile() != null) {
            Picasso.get()
                    .load(sharedPref.getUserProfile())
                    .error(R.drawable.profile_male)
                    .placeholder(R.drawable.profile_male)
                    .into(circle_profile_image);
        } else {
//            Toast.makeText(getActivity(), "Image url null ", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.circle_profile_image)
    public void profileEdit() {
        if (getActivity() instanceof MainHomeActivity) {
            ((MainHomeActivity) getActivity()).attachFragment(new NavigationFragment());
        }
    }

    @OnClick(R.id.cir_btn_user_live)
    public void userLive() {
        viewModel.getLiveCallResponse(sharedPref.getRegisterId(), getActivity());
    }

    @OnClick(R.id.tv_btn_home_live)
    public void OnClickLive() {
        recyclerView_live_list.setVisibility(View.VISIBLE);
        recyclerView_home.setVisibility(View.GONE);

        view_live.setVisibility(View.VISIBLE);
        view_online.setVisibility(View.GONE);
        view_newUser.setVisibility(View.GONE);
    }

    @OnClick(R.id.tv_btn_home_online)
    public void OnClickRandom() {
        view_live.setVisibility(View.GONE);
        view_online.setVisibility(View.VISIBLE);
        view_newUser.setVisibility(View.GONE);

        typeId = 1;
        listApiCall();  // api call

        recyclerView_live_list.setVisibility(View.GONE);
        recyclerView_home.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_btn_home_new_user)
    public void OnClickNewUser() {
        view_live.setVisibility(View.GONE);
        view_online.setVisibility(View.GONE);
        view_newUser.setVisibility(View.VISIBLE);

        typeId = 3;
        listApiCall();    // api call

        recyclerView_live_list.setVisibility(View.GONE);
        recyclerView_home.setVisibility(View.VISIBLE);
    }

    private void listApiCall() {
        viewModel.getHomePageList(registerId, pageNo, typeId);
    }

    private void getAllCallList() {
        try {
            liveCallHistoryRepository.getLiveCallList(sharedPref.getRegisterId(), new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    List<LiveResponse> liveUserModelList = (List<LiveResponse>) object;
                    setLiveCallListAdapter(liveUserModelList);
                }

                @Override
                public void onError(Object object) {
                }
            });
        } catch (Exception e) {
            Log.e("getAllCallList", e.getMessage());
        }
    }

    private void setLiveCallListAdapter(List<LiveResponse> callResponseList) {
        if (liveListAdapter == null) {
            GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
//            recyclerView_live_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView_live_list.setHasFixedSize(true);
            recyclerView_live_list.setLayoutManager(manager);
            liveListAdapter = new LiveListAdapter(getActivity(), callResponseList, sharedPref.getRegisterId(),sharedPref.getTotalCoins());
        } else {
            liveListAdapter.setLiveCallModelList(callResponseList);
            liveListAdapter.notifyDataSetChanged();
        }
        recyclerView_live_list.setAdapter(liveListAdapter);
    }

    @OnTextChanged(R.id.edit_home_search)
    public void search() {
        if (!homeListModels.isEmpty()) {
            List<HomeListModel> temp = new ArrayList<HomeListModel>();
            for (HomeListModel product : homeListModels) {
                String name = product.getProfileName().toLowerCase();
                if (name.contains(edit_home_search.getText().toString().trim())) {
                    temp.add(product);
                }
            }
            homeAdapter.updateList(temp);
        }
    }

    private void setUpView() {
        homeListModels = new ArrayList<>();
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView_home.setHasFixedSize(true);
        recyclerView_home.setLayoutManager(manager);
    }

    private void bindViewModel() {
        viewModel.setHomePageList()
                .observe(this, homePageApi -> {
                    assert homePageApi != null;
                    switch (homePageApi.status) {
                        case SUCCESS:
                            try {
                                homeListModels.clear();
                                edit_home_search.setText("");
                                String response = String.valueOf(homePageApi.responce);
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    if (sharedPref.getRegisterId() == jsonObject.getInt("RegistrationId"))
                                        continue;
                                    HomeListModel homeListModel = new HomeListModel();
                                    homeListModel.setAddress(jsonObject.getString("Address"));
                                    homeListModel.setProfileName(jsonObject.getString("ProfileName"));
                                    homeListModel.setGender(jsonObject.getString("Gender"));
                                    homeListModel.setMobileNo(jsonObject.getString("MobileNo"));
                                    homeListModel.setCityName(jsonObject.getString("CityName"));
                                    homeListModel.setEmailId(jsonObject.getString("EmailId"));
                                    homeListModel.setDOB(jsonObject.getString("DOB"));
                                    homeListModel.setStatus(jsonObject.getBoolean("Status"));
                                    homeListModel.setAge(jsonObject.getInt("Age"));
                                    homeListModel.setRegistrationId(jsonObject.getInt("RegistrationId"));
                                    homeListModel.setProfileImage(jsonObject.getString("ProfileImage"));
                                    homeListModel.setAffiliate(jsonObject.getString("Affiliate"));
                                    homeListModel.setFollowerStatus(jsonObject.getBoolean("FollowerStatus"));
                                    homeListModels.add(homeListModel);
                                }
                                homeAdapter = new HomeAdapter(getActivity(), homeListModels, this);
                                recyclerView_home.setAdapter(homeAdapter);
                                homeAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ERROR:
                            assert homePageApi.error != null;
                            showErrorMessage(root, homePageApi.error.getMessage());
                            break;
                    }
                });
    }

    @Override
    public void onClick(View view) {
        homeListModelOnCLick = (HomeListModel) view.getTag();
        switch (view.getId()) {
            case R.id.iv_home_image:
                if (getActivity() instanceof MainHomeActivity)
                    ((MainHomeActivity) getActivity()).openDetails(homeListModelOnCLick);
                break;

            case R.id.iv_voice_call:
//                getTokenForVoiceCall(homeListModelOnCLick);
                //getTotalCoinsAudio(homeListModelOnCLick.getRegistrationId());
                getTotalAudioVideoCoins(homeListModelOnCLick.getRegistrationId(), ConstantApp.AUDIO_CALL);
                break;

            case R.id.iv_video_call:
//                 getTokenForVideoCall(homeListModelOnCLick);
                //getTotalCoinsVideo(homeListModelOnCLick.getRegistrationId());
                getTotalAudioVideoCoins(homeListModelOnCLick.getRegistrationId(), ConstantApp.VIDEO_CALL);
                break;

            case R.id.iv_chat_message:
                callChatMessageActivity(homeListModelOnCLick);
                break;
        }
    }

    // nitin code
    public void getTotalAudioVideoCoins(int registerId, String audioCall) {
        TipsGoApiService apiService = ServiceBuilder.getClient().create(TipsGoApiService.class);
        Call<CoinsResponse> call = apiService.getTotalCoins(registerId);
        call.enqueue(new Callback<CoinsResponse>() {
            @Override
            public void onResponse(@NotNull Call<CoinsResponse> call, @NotNull Response<CoinsResponse> response) {
                if (response != null) {
                    getTotalCoins(response.body(), audioCall);
                }
            }

            @Override
            public void onFailure(@NotNull Call<CoinsResponse> call, @NotNull Throwable t) {
            }
        });
    }

    private void getTotalCoins(CoinsResponse coinsResponse, String audioCall) {
        if (coinsResponse != null) {
            int totalCoins = Integer.parseInt(sharedPref.getTotalCoins());
            long receiverCoins = audioCall.equalsIgnoreCase(ConstantApp.AUDIO_CALL) ? coinsResponse.getAudioCallCoins() : coinsResponse.getVideoCallCoins();
            if (totalCoins >= receiverCoins)
                if (audioCall.equalsIgnoreCase(ConstantApp.AUDIO_CALL))
                    getTokenForVoiceCall(homeListModelOnCLick);
                else
                    getTokenForVideoCall(homeListModelOnCLick);
            else
                Toast.makeText(getActivity(), "Coins Insufficient !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Coins Data Not Found !", Toast.LENGTH_SHORT).show();
        }

    }

    private void getTokenForVoiceCall(HomeListModel homeListModel) {
        int userId = sharedPref.getRegisterId();
        viewModel.getVoiceCallResponse(userId, homeListModel.getRegistrationId(), getActivity());
    }

    private void getTokenForVideoCall(HomeListModel homeListModel) {
        int userId = sharedPref.getRegisterId();
        viewModel.getVideoCallResponse(userId, homeListModel.getRegistrationId(), getActivity());
    }

    private void callChatMessageActivity(HomeListModel homeListModel) {
        ChatListModel chatListModel = new ChatListModel();
        if (homeListModel != null) {
            chatListModel.setRegistrationId(homeListModel.getRegistrationId());
            if (!Methods.isEmptyOrNull(homeListModel.getProfileImage()))
                chatListModel.setProfileImage(homeListModel.getProfileImage());
            if (!Methods.isEmptyOrNull(homeListModel.getProfileName()))
                chatListModel.setProfileName(homeListModel.getProfileName());
        }
        if (getActivity() instanceof MainHomeActivity)
            ((MainHomeActivity) getActivity()).openChatting(chatListModel);
    }

}