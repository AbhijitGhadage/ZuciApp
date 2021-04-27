package com.zuci.zuciapp.ui.chat.call;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.firebase.CallBack;
import com.zuci.zuciapp.firebase.CallHistoryRepository;
import com.zuci.zuciapp.firebase.CallHistoryRepositoryImpl;
import com.zuci.zuciapp.ui.agoraVoiceCall.CallResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CallListFragment extends Fragment {
    @BindView(R.id.recyclerView_call_list)
    RecyclerView recyclerView_call_list;
    private CallListAdapter callListAdapter;
    private CallHistoryRepository callHistoryRepository;
    private SharedPref sharedPref;
    private long regID;

    public static CallListFragment newInstance() {
        return new CallListFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_call_list, container, false);
        ButterKnife.bind(this, root);
        setInitialization();
        getAllCallList();
        return root;
    }

    private void setInitialization() {
        sharedPref = new SharedPref(getActivity());
        regID = sharedPref.getRegisterId();
        callHistoryRepository = new CallHistoryRepositoryImpl();
        recyclerView_call_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView_call_list.setHasFixedSize(true);
//        recyclerView_call_list.setItemAnimator(new DefaultItemAnimator());
    }

    private void getAllCallList() {
        try {
            callHistoryRepository.getCallHistory(regID, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    List<CallResponse> callResponseList = (List<CallResponse>) object;
                    setCallHistoryAdapter(callResponseList);
                }

                @Override
                public void onError(Object object) {
                }
            });
        } catch (Exception e) {
            Log.e("getAllCallList", e.getMessage());
        }
    }

    private void setCallHistoryAdapter(List<CallResponse> callResponseList) {
        if (callListAdapter == null) {
            callListAdapter = new CallListAdapter(getActivity(), callResponseList, regID);
        } else {
            callListAdapter.setCallListModelList(callResponseList);
            callListAdapter.notifyDataSetChanged();
        }
        recyclerView_call_list.setAdapter(callListAdapter);
    }
}