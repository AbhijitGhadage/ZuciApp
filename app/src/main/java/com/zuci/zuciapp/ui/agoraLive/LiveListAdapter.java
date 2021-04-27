package com.zuci.zuciapp.ui.agoraLive;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.ui.agoraLive.liveVideo.LiveResponse;
import com.zuci.zuciapp.ui.agoraLive.liveVideo.LiveRoomActivity;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.utils.Methods;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.Constants;

public class LiveListAdapter extends RecyclerView.Adapter<LiveListAdapter.MyViewHolder> {
    private Context mContext;
    private List<LiveResponse> callListModelList;
    private long regID;
    private String totalCoins;

    public LiveListAdapter(Context mContext, List<LiveResponse> callListModelList, long regID, String totalCoins) {
        this.mContext = mContext;
        this.callListModelList = callListModelList;
        this.regID = regID;
        this.totalCoins = totalCoins;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_live_call_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        LiveResponse liveUserModel = callListModelList.get(position);
        holder.tv_live_uName.setText(liveUserModel.getUserName());
        if (!Methods.isEmptyOrNull(liveUserModel.getUserProfileImage()))
            Picasso.get()
                    .load(liveUserModel.getUserProfileImage())
                    .placeholder(R.drawable.profile_male)
                    .error(R.drawable.profile_male)
                    .into(holder.circle_live_user);
        /*}else{
            Picasso.get()
                    .load(liveUserModel.getUserProfileImage())
                    .placeholder(R.drawable.profile_male)
                    .error(R.drawable.profile_male)
                    .into(holder.circle_live_user);
        }*/
        long totalCoin = Long.parseLong(totalCoins);   // total coins user
        long liveCallCoins = liveUserModel.getUserLiveCallCoins();   // total coins user

        holder.llLiveUser.setOnClickListener(v -> {
            if (liveCallCoins < totalCoin) {
                Intent intent;
                intent = new Intent(mContext, LiveRoomActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(ConstantApp.ACTION_KEY_CROLE, Constants.CLIENT_ROLE_AUDIENCE);
                intent.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, liveUserModel.getChannelName());
                intent.putExtra("LIVE_USER", liveUserModel.getUserName());
                intent.putExtra("LIVE_USER_COINS", liveUserModel.getUserLiveCallCoins());
                intent.putExtra("LIVE_USER_COINS_JOIN", totalCoin);
                intent.putExtra("LIVE_USER_ID", liveUserModel.getRegId());
                intent.putExtra("LIVE_USER_ID_JOIN", regID);
                intent.putExtra("ROOT_USER", false);
                mContext.startActivity(intent);
            } else
                Toast.makeText(mContext, "Not sufficient coins !!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return callListModelList != null ? callListModelList.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_live_uName)
        TextView tv_live_uName;
        @BindView(R.id.ll_live_user)
        LinearLayout llLiveUser;
        @BindView(R.id.circle_live_user)
        CircleImageView circle_live_user;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setLiveCallModelList(List<LiveResponse> callListModelList) {
        this.callListModelList = callListModelList;
    }
}

