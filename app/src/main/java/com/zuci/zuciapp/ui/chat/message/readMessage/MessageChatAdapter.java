package com.zuci.zuciapp.ui.chat.message.readMessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zuci.zuciapp.utils.Methods;
import com.zuci.zuciapp.R;
import com.zuci.zuciapp.ui.firestoreMessageChat.messageChatHistory.ChatModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageChatAdapter extends RecyclerView.Adapter<MessageChatAdapter.MyViewHolder> {
    private Context mContext;
    private List<ChatModel> chatModelList;
    private final long regId;
    private long chatRoomId;

    public MessageChatAdapter(Context mContext, List<ChatModel> chatModelList, long regId) {
        this.mContext = mContext;
        this.chatModelList = chatModelList;
        this.regId = regId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_message_chatting, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        ChatModel chatModel = chatModelList.get(position);

        holder.setIsRecyclable(false);

        if (chatModel != null) {
            String msg = Methods.decryptChatData(chatModel.getMessage(), chatRoomId, chatModel.getSenderId(), chatModel.getReceiverId());
            if (chatModel.getSenderId() == regId) {
                holder.rlSenderMessage.setVisibility(View.VISIBLE);
                holder.rlReceiverMessage.setVisibility(View.GONE);
                holder.tvSenderMessage.setText(msg);
                try {
                    holder.tv_sender_message_date.setText(Methods.dateToFormatedString(chatModel.getCreatedDateTime()));
                } catch (Exception ignored) {
                }

            } else {
                holder.rlSenderMessage.setVisibility(View.GONE);
                holder.rlReceiverMessage.setVisibility(View.VISIBLE);
                holder.tvReceiverMessage.setText(msg);
                try {
                    holder.tv_receiver_message_date.setText(Methods.dateToFormatedString(chatModel.getCreatedDateTime()));
                } catch (Exception ignored) {
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return chatModelList != null ? chatModelList.size() : 0;
    }

    public void setChatModelList(List<ChatModel> chatModelList) {
        this.chatModelList = chatModelList;
    }

    public void setChatRoomId(long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public void setScrollToPosition(LinearLayoutManager linearLayoutManager) {
        linearLayoutManager.scrollToPosition(chatModelList.size() - 1);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_sender_message)
        TextView tvSenderMessage;
        @BindView(R.id.tv_sender_message_date)
        TextView tv_sender_message_date;
        @BindView(R.id.tv_receiver_message)
        TextView tvReceiverMessage;
        @BindView(R.id.tv_receiver_message_date)
        TextView tv_receiver_message_date;
        @BindView(R.id.rl_sender_message)
        RelativeLayout rlSenderMessage;
        @BindView(R.id.rl_receiver_message)
        RelativeLayout rlReceiverMessage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
