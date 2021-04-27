package com.zuci.zuciapp.ui.chat.message.readMessage;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zuci.zuciapp.utils.Methods;
import com.zuci.zuciapp.R;
import com.zuci.zuciapp.di.utils.ViewModelFactory;
import com.zuci.zuciapp.domain.pref.SharedPref;
import com.zuci.zuciapp.firebase.FirebaseConstant;
import com.zuci.zuciapp.firebase.FirestoreChildCallBack;
import com.zuci.zuciapp.ui.Navigator;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.base.BaseActivity;
import com.zuci.zuciapp.ui.firestoreMessageChat.MessageChatRepository;
import com.zuci.zuciapp.ui.firestoreMessageChat.MessageChatRepositoryImpl;
import com.zuci.zuciapp.ui.firestoreMessageChat.messageChatHistory.ChatListModel;
import com.zuci.zuciapp.ui.firestoreMessageChat.messageChatHistory.ChatModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageChatActivity extends BaseActivity<MessageChatViewModel> {
    @Inject
    ViewModelFactory<MessageChatViewModel> viewModelFactory;
    private MessageChatViewModel messageChatViewModel;
    @Inject
    Navigator navigator;
    @Inject
    SharedPref sharedPref;

    @Override
    protected MessageChatViewModel getViewModel() {
        return messageChatViewModel;
    }

    @BindView(R.id.content_parent)
    RelativeLayout content_parent;
    @BindView(R.id.tv_chatting_name)
    TextView tv_chatting_name;
    @BindView(R.id.recyclerView_chatting)
    RecyclerView recyclerView_chatting;
    @BindView(R.id.img_profile)
    CircleImageView img_profile;

    @BindView(R.id.edt_message)
    EditText edt_message;

    //private List<MessageChatModel> messageChatModelList;
    private MessageChatAdapter messageChatAdapter;
    private MessageChatRepository messageChatRepository;
    private List<ChatModel> chatModelList;
    private long receiverRegId, senderRegId, chatRoomId;
    String receiverName,receiverProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chat);
        AndroidInjection.inject(this);
        ButterKnife.bind(this);
        setInitialization();
        getIntentData();
    }

    private void getIntentData() {
        ChatListModel chatListModel = (ChatListModel) getIntent().getSerializableExtra(ConstantApp.CHATTING_MODEL);
        receiverRegId = chatListModel.getRegistrationId();
        receiverName = chatListModel.getProfileName();
        receiverProfile = chatListModel.getProfileImage();
        senderRegId = sharedPref.getRegisterId();
        chatRoomId = messageChatRepository.getChatRoomId(senderRegId, receiverRegId);
        if (!Methods.isEmptyOrNull(chatListModel.getProfileName()))
            tv_chatting_name.setText(chatListModel.getProfileName());
        if (!Methods.isEmptyOrNull(chatListModel.getProfileImage()))
            Picasso.get()
                    .load(chatListModel.getProfileImage())
                    .placeholder(R.drawable.profile_male)
                    .error(R.drawable.profile_male)
                    .into(img_profile);
        getChatHistory(chatListModel.getRegistrationId());
    }

    private void setInitialization() {
        messageChatViewModel = ViewModelProviders.of(this, viewModelFactory).get(MessageChatViewModel.class);
        messageChatRepository = new MessageChatRepositoryImpl();
        chatModelList = new ArrayList<>();
    }

    private void getChatHistory(long receiverRegId) {
        long regId = sharedPref.getRegisterId();
        messageChatRepository.getChatHistoryByChatRoomId(regId, receiverRegId, new FirestoreChildCallBack() {
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        if (messageChatAdapter == null) {
            recyclerView_chatting.setLayoutManager(linearLayoutManager);
            recyclerView_chatting.setHasFixedSize(true);
            recyclerView_chatting.setItemAnimator(new DefaultItemAnimator());
            messageChatAdapter = new MessageChatAdapter(this, chatModelList, regId);
            messageChatAdapter.setChatRoomId(chatRoomId);
            recyclerView_chatting.setAdapter(messageChatAdapter);
        } else {
            messageChatAdapter.setChatModelList(chatModelList);
        }
        messageChatAdapter.setScrollToPosition(linearLayoutManager);
        messageChatAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.iv_back_btn)
    public void OnClickBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_attach_file)
    public void attachFile() {
        uploadFiles();
    }

    private void uploadFiles() {
        final CharSequence[] options = {ConstantApp.TAKE_PHOTO, ConstantApp.CHOOSE_FROM_GALLERY,
                ConstantApp.CHOOSE_FROM_VIDEO, ConstantApp.CANCEL};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Select Image");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals(ConstantApp.TAKE_PHOTO)) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            } else if (options[item].equals(ConstantApp.CHOOSE_FROM_GALLERY)) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);

            } else if (options[item].equals(ConstantApp.CHOOSE_FROM_VIDEO)) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a Video "), 3);

            } else if (options[item].equals(ConstantApp.CANCEL)) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        leftToRightAnimated();
    }

    public void onClickButtonSend(View view) {
        String typeMessage = edt_message.getText().toString().trim();
        edt_message.setText("");
        if (!Methods.isEmptyOrNull(typeMessage)) {
            ChatModel chatModel = new ChatModel();
            String chatId = FirebaseConstant.CHAT_HISTORY_COLLECTION_REF.document(String.valueOf(chatRoomId)).collection("message").document().getId();
            chatModel.setChatId(chatId);
            chatModel.setSenderId(senderRegId);
            chatModel.setReceiverId(receiverRegId);
            chatModel.setReceiverName(receiverName);
            chatModel.setReceiverProfile(receiverProfile);
            chatModel.setMessage(Methods.encryptChatData(typeMessage, chatRoomId, senderRegId, receiverRegId));
            messageChatRepository.addNewChatMessage(chatId, chatRoomId, chatModel.toChatMap());
        }
    }
}