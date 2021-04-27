package com.zuci.zuciapp.ui.reels;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.ui.homeViewDetails.HomeViewDetailActivity;
import com.zuci.zuciapp.ui.homeViewDetails.tabFragment.DeductionModel;
import com.zuci.zuciapp.ui.homeViewDetails.tabFragment.MediaVideoListModel;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zuci.zuciapp.di.modules.NetworkModule.IMAGE_URL;

public class ReelsVideoAdapter extends RecyclerView.Adapter<ReelsVideoAdapter.MyViewHolder> {
    private Context mContext;
    private List<ReelsVideoModel> reelsVideoModelList;
    private View.OnClickListener onClickListener;
    boolean videoPause = true;
    ReelsVideoViewModel viewModel;
    int getRegisterId;
    String getUserName;
    String totalCoins;

    public ReelsVideoAdapter(Context mContext,
                             List<ReelsVideoModel> reelsVideoModelList,
                             View.OnClickListener onClickListener,
                             ReelsVideoViewModel viewModel,
                             int getRegisterId,
                             String getUserName,
                             String totalCoins) {
        this.mContext = mContext;
        this.reelsVideoModelList = reelsVideoModelList;
        this.viewModel = viewModel;
        this.onClickListener = onClickListener;
        this.getRegisterId = getRegisterId;
        this.getUserName = getUserName;
        this.totalCoins = totalCoins;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_reels_video, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        ReelsVideoModel reelsVideoModel = reelsVideoModelList.get(position);

        holder.tv_reels_name.setText(reelsVideoModel.getUserName());
        holder.tv_video_like.setText("" + reelsVideoModel.getLikesCount());
        holder.tv_video_views.setText("" + reelsVideoModel.getTotalView() + " Views");

        if (reelsVideoModel.isLikes()) {
            holder.iv_video_like.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_vector_like));
        } else {
            holder.iv_video_like.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_vector_like_dis));
        }

        // play videos
        if (reelsVideoModel.getStatus().equalsIgnoreCase("Private")
                && reelsVideoModel.getViewerStatus() == null) {
            openDialog(reelsVideoModel);

        } else if (reelsVideoModel.getStatus().equalsIgnoreCase("Public")) {
            holder.mVideoView.setVideoPath(IMAGE_URL + reelsVideoModel.getVideoName());
            holder.mVideoView.setOnPreparedListener(mp -> {
                holder.mProgressBar.setVisibility(View.GONE);
                mp.start();
                viewModel.setViewsCount(getRegisterId, reelsVideoModel.getRegistrationId(), reelsVideoModel.getMediaId());

                float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                float screenRatio = holder.mVideoView.getWidth() / (float) holder.mVideoView.getHeight();
                float scale = videoRatio / screenRatio;
                if (scale >= 1f) {
                    holder.mVideoView.setScaleX(scale);
                } else {
                    holder.mVideoView.setScaleY(1f / scale);
                }
            });

            holder.mVideoView.setOnCompletionListener(mp -> {
                mp.start();
                viewModel.setViewsCount(getRegisterId, reelsVideoModel.getRegistrationId(), reelsVideoModel.getMediaId());
            });
        } else if (reelsVideoModel.getStatus().equalsIgnoreCase("Private")
                && reelsVideoModel.getViewerStatus().equalsIgnoreCase("Public")) {

            holder.mVideoView.setVideoPath(IMAGE_URL + reelsVideoModel.getVideoName());
            holder.mVideoView.setOnPreparedListener(mp -> {
                holder.mProgressBar.setVisibility(View.GONE);
                mp.start();
                viewModel.setViewsCount(getRegisterId, reelsVideoModel.getRegistrationId(), reelsVideoModel.getMediaId());

                float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                float screenRatio = holder.mVideoView.getWidth() / (float) holder.mVideoView.getHeight();
                float scale = videoRatio / screenRatio;
                if (scale >= 1f) {
                    holder.mVideoView.setScaleX(scale);
                } else {
                    holder.mVideoView.setScaleY(1f / scale);
                }
            });

            holder.mVideoView.setOnCompletionListener(mp -> {
                mp.start();
                viewModel.setViewsCount(getRegisterId, reelsVideoModel.getRegistrationId(), reelsVideoModel.getMediaId());
            });

        }

        // like conditon
        holder.iv_video_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!reelsVideoModel.isLikes()) {
                    reelsVideoModel.setLikes(true);
                    String tvCount = holder.tv_video_like.getText().toString().trim();
                    int tvCountAdd = Integer.parseInt(tvCount);
                    tvCountAdd++;
                    holder.tv_video_like.setText("" + tvCountAdd);
                    holder.iv_video_like.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_vector_like));

                } else {
                    reelsVideoModel.setLikes(false);
                    String tvCount = holder.tv_video_like.getText().toString().trim();
                    int tvCountAdd = Integer.parseInt(tvCount);
                    int count = tvCountAdd - 1;
                    holder.tv_video_like.setText("" + count);
                    holder.iv_video_like.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_vector_like_dis));
                }

                VideoAddLikeModel videoAddLikeModel = new VideoAddLikeModel();
                videoAddLikeModel.setRegistrationId(getRegisterId);
                videoAddLikeModel.setUserName(getUserName);
                videoAddLikeModel.setMediaId(reelsVideoModel.getMediaId());
                videoAddLikeModel.setMediaName(reelsVideoModel.getVideoName());
                viewModel.addLinksApi(videoAddLikeModel);
            }
        });

        // pause and play condition
        holder.mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reelsVideoModel.getStatus().equalsIgnoreCase("Private")
                        && reelsVideoModel.getViewerStatus() == null) {
                    openDialog(reelsVideoModel);

                } else if (reelsVideoModel.getStatus().equalsIgnoreCase("Public")) {
                    if (videoPause) {
                        holder.mVideoView.pause();
                        videoPause = false;
                        holder.iv_pause.setVisibility(View.VISIBLE);
                        holder.iv_pause.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_vector_play_button));

                    } else {
                        holder.mVideoView.start();
                        videoPause = true;
                        holder.iv_pause.setVisibility(View.GONE);
                    }
                } else if (reelsVideoModel.getStatus().equalsIgnoreCase("Private")
                        && reelsVideoModel.getViewerStatus().equalsIgnoreCase("Public")) {

                    if (videoPause) {
                        holder.mVideoView.pause();
                        videoPause = false;
                        holder.iv_pause.setVisibility(View.VISIBLE);
                        holder.iv_pause.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_vector_play_button));

                    } else {
                        holder.mVideoView.start();
                        videoPause = true;
                        holder.iv_pause.setVisibility(View.GONE);
                    }
                }
            }
        });

    }


    private void openDialog(ReelsVideoModel reelsVideoModel) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_deducted_coins);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView iv_close_dialog = dialog.findViewById(R.id.iv_close_dialog);
        TextView tv_dialog_sms = dialog.findViewById(R.id.tv_dialog_sms);
        AppCompatButton btn_dialog_cancel = dialog.findViewById(R.id.btn_dialog_cancel);
        AppCompatButton btn_dialog_ok = dialog.findViewById(R.id.btn_dialog_ok);

        tv_dialog_sms.setText("Spend " + reelsVideoModel.getSetCoins() + " Coins for view this Video !");

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
                apiCallDeduction(reelsVideoModel);

                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void apiCallDeduction(ReelsVideoModel reelsVideoModel) {
        try {
            long myCoins = Long.parseLong(totalCoins);
            long coins = reelsVideoModel.getSetCoins();

            if (coins <= myCoins) {

                long viewerCoins = coins / 2;
                long adminCoins = coins - viewerCoins;

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
                String date = sdf.format(System.currentTimeMillis());

                DeductionModel deductionModel = new DeductionModel();
                deductionModel.setViewerRegistrationId(getRegisterId);
                deductionModel.setMediaOwnerRegistrationId(reelsVideoModel.getRegistrationId());
                deductionModel.setMediaId(reelsVideoModel.getMediaId());
                deductionModel.setDeductCoins(viewerCoins);
                deductionModel.setAdmindeductCoins(adminCoins);
                deductionModel.setDeductionType("ZeelVideo");
                deductionModel.setDeductionDate(date);
                deductionModel.setViewerStatus("");

                viewModel.deductionCoinsApi(deductionModel);

            } else {
                Toast.makeText(mContext, "Coins Insufficient !", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public int getItemCount() {
        return reelsVideoModelList.size();
    }

    public void setModelList(List<ReelsVideoModel> reelsVideoModelList) {
        this.reelsVideoModelList = reelsVideoModelList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.videoView)
        VideoView mVideoView;
        @BindView(R.id.tv_reels_name)
        TextView tv_reels_name;
        @BindView(R.id.progressBar)
        ProgressBar mProgressBar;
        @BindView(R.id.iv_video_like)
        ImageView iv_video_like;
        @BindView(R.id.iv_pause)
        ImageView iv_pause;
        @BindView(R.id.tv_video_like)
        TextView tv_video_like;
        @BindView(R.id.tv_video_views)
        TextView tv_video_views;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}