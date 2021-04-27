package com.zuci.zuciapp.ui.firestoreMessageChat.messageChatHistory;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.firebase.FirestoreChildCallBack;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.base.BaseFragment;
import com.zuci.zuciapp.ui.chat.message.readMessage.MessageChatAdapter;
import com.zuci.zuciapp.ui.firestoreMessageChat.MessageChatRepository;
import com.zuci.zuciapp.ui.firestoreMessageChat.MessageChatRepositoryImpl;
import com.zuci.zuciapp.ui.mainPage.MainHomeActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class MessageListFragment extends BaseFragment<MessageListViewModel> implements View.OnClickListener {
    @Inject
    ViewModelFactory<MessageListViewModel> viewModelFactory;
    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;
    MessageListViewModel viewModel;

    @Override
    public MessageListViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    // required
    public MessageListFragment() {
    }

    public static MessageListFragment newInstance() {
        return new MessageListFragment();
    }

    @BindView(R.id.recyclerView_message_list)
    RecyclerView recyclerView_message_list;
    private MessageListAdapter messageListAdapter;

    private MessageChatRepository messageChatRepository;
    private List<ChatModel> chatModelList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_message_list, container, false);
        ButterKnife.bind(this, root);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MessageListViewModel.class);
        setInitialization();

        getChatHistory();


        return root;
    }


    private void setInitialization() {
        chatModelList = new ArrayList<>();
        messageChatRepository = new MessageChatRepositoryImpl();
    }


    private void getChatHistory() {
        long regId = sharedPref.getRegisterId();
        messageChatRepository.getChatHistoryByChatRoomIdFragmentList(regId, new FirestoreChildCallBack() {
            @Override
            public void onChildAdded(Object object) {
                if (object != null) {
                    ChatModel chatModel = (ChatModel) object;
                    chatModelList.add(0, chatModel);
                    setChatAdapter(regId);
                }
            }

            @Override
            public void onChildModified(Object object) {
            }

            @Override
            public void onChildRemoved(Object object) {
            }

            @Override
            public void onError(Object object) {
            }
        });
    }

    private void setChatAdapter(long regId) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        recyclerView_message_list.setLayoutManager(linearLayoutManager);
        recyclerView_message_list.setHasFixedSize(true);
//        recyclerView_message_list.setItemAnimator(new DefaultItemAnimator());
        messageListAdapter = new MessageListAdapter(getActivity(), chatModelList, this);
//        messageListAdapter.setChatRoomId(chatRoomId);
        recyclerView_message_list.setAdapter(messageListAdapter);

//        messageListAdapter.setScrollToPosition(linearLayoutManager);
//        messageListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {
    /*    if (view.getId() == R.id.rel_message_frd) {
            ChatListModel chatListModel = (ChatListModel) view.getTag();
            if (getActivity() instanceof MainHomeActivity)
                ((MainHomeActivity) getActivity()).openChatting(chatListModel);
        }*/
    }

    /*
        private void setInitialization() {

            viewModel.getChatUserList(sharedPref.getRegisterId()).observe(this, chatListModelList -> {
                if (chatListModelList != null)
                    setChatListAdapters(chatListModelList);
            });
        }

    private void setChatListAdapters(List<ChatListModel> chatListModelList) {
        recyclerView_message_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView_message_list.setHasFixedSize(true);
        messageListAdapter = new MessageListAdapter(getActivity(), chatListModelList, this);
        recyclerView_message_list.setAdapter(messageListAdapter);
    }   */
}