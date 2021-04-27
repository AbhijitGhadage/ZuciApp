package com.zuci.zuciapp.ui.chat.call;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zuci.zuciapp.utils.Methods;
import com.zuci.zuciapp.R;
import com.zuci.zuciapp.ui.agoraVoiceCall.CallResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.agoraVoiceCall.ReceiverCallModel;
import com.zuci.zuciapp.ui.agoraVoiceCall.SenderCallModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.MyViewHolder> {
    private Context mContext;
    private List<CallResponse> callListModelList;
    private long regID;

    public CallListAdapter(Context mContext, List<CallResponse> callListModelList, long regID) {
        this.mContext = mContext;
        this.callListModelList = callListModelList;
        this.regID = regID;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_call_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        CallResponse callResponse = callListModelList.get(position);
        SenderCallModel senderCallModel = callResponse.getSenderDetails();
        ReceiverCallModel receiverCallModel = callResponse.getReceiverDetails();
        if (callResponse != null) {
            holder.tvCallDateTime.setText(Methods.dateToFormatedString(callResponse.getCallDateTime()));
            if (!Methods.isEmptyOrNull(callResponse.getCallType())) {
                if (callResponse.getCallType().equals(ConstantApp.AUDIO_CALL))
                    holder.ivAudVidCall.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_vector_phone_call));
                else
                    holder.ivAudVidCall.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_vector_video_call));
            }
            if (callResponse.getSenderUserId() == regID) {
                holder.tvInOutCall.setText("Outgoing Call");
                if (receiverCallModel != null) {
                    if (!Methods.isEmptyOrNull(receiverCallModel.getReceiverName()))
                        holder.tvChatUName.setText(receiverCallModel.getReceiverName());
                    if (!Methods.isEmptyOrNull(receiverCallModel.getReceiverImage()))
                        Picasso.get()
                                .load(receiverCallModel.getReceiverImage())
                                .placeholder(R.drawable.profile_male)
                                .error(R.drawable.profile_male)
                                .into(holder.circleProfileImage);

                }
            } else {
                holder.tvInOutCall.setText("Incoming Call");
                if (senderCallModel != null) {
                    if (!Methods.isEmptyOrNull(senderCallModel.getSenderName()))
                        holder.tvChatUName.setText(senderCallModel.getSenderName());
                    if (!Methods.isEmptyOrNull(senderCallModel.getSenderImage()))
                        Picasso.get()
                                .load(senderCallModel.getSenderImage())
                                .placeholder(R.drawable.profile_male)
                                .error(R.drawable.profile_male)
                                .into(holder.circleProfileImage);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return callListModelList != null ? callListModelList.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.circle_profile_image)
        CircleImageView circleProfileImage;
        @BindView(R.id.tv_chat_uName)
        TextView tvChatUName;
        @BindView(R.id.iv_aud_vid_call)
        ImageView ivAudVidCall;
        @BindView(R.id.tv_in_out_call)
        TextView tvInOutCall;
        @BindView(R.id.tv_call_date_time)
        TextView tvCallDateTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setCallListModelList(List<CallResponse> callListModelList) {
        this.callListModelList = callListModelList;
    }
}
