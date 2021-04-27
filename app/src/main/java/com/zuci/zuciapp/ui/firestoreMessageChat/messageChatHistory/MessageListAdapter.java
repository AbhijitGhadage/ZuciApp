package com.zuci.zuciapp.ui.firestoreMessageChat.messageChatHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zuci.zuciapp.utils.Methods;
import com.zuci.zuciapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyViewHolder> {
    private Context mContext;
    private List<ChatModel> chatListModelList;
    private final View.OnClickListener onClickListener;

    public MessageListAdapter(Context mContext, List<ChatModel> chatListModelList, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.chatListModelList = chatListModelList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_message_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        ChatModel chatListModel = chatListModelList.get(position);
        if (!Methods.isEmptyOrNull(chatListModel.getReceiverProfile()))
            Picasso.get()
                    .load(chatListModel.getReceiverProfile())
                    .placeholder(R.drawable.profile_male)
                    .error(R.drawable.profile_male)
                    .into(holder.circle_profile_image);
        holder.tv_chat_uName.setText(chatListModel.getReceiverName());

        holder.rel_message_frd.setTag(chatListModel);
        holder.rel_message_frd.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return chatListModelList != null ? chatListModelList.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_chat_uName)
        TextView tv_chat_uName;
        @BindView(R.id.rel_message_frd)
        RelativeLayout rel_message_frd;
        @BindView(R.id.circle_profile_image)
        CircleImageView circle_profile_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
