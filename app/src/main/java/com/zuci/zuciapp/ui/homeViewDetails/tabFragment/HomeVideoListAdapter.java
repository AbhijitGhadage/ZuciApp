package com.zuci.zuciapp.ui.homeViewDetails.tabFragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zuci.zuciapp.R;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zuci.zuciapp.di.modules.NetworkModule.IMAGE_URL;

public class HomeVideoListAdapter extends RecyclerView.Adapter<HomeVideoListAdapter.MyViewHolder> {
    private Context mContext;
    private List<MediaVideoListModel> mediaVideoListModelList;
    private View.OnClickListener onClickListener;

    public HomeVideoListAdapter(Context mContext, List<MediaVideoListModel> mediaVideoListModelList, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.mediaVideoListModelList = mediaVideoListModelList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_video_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        MediaVideoListModel mediaModel = mediaVideoListModelList.get(position);

        holder.tv_like_video.setText("" + mediaModel.getLikesCount());
        holder.tv_views_video.setText("" + mediaModel.getTotalView() + " Views");

        try {
            Uri uri = Uri.fromFile(new File(IMAGE_URL + mediaModel.getName()));
            holder.iv_home_video.setTag(uri);
            Glide.with(mContext)
                    .load(uri)
                    .thumbnail(0.1f)
                    .into(holder.iv_home_video);
        } catch (Exception ignored) {
        }
  /*      try {
            // process slow to create image
            Bitmap bitmap = retriveVideoFrameFromVideo(IMAGE_URL + mediaModel.getName());
            holder.iv_home_video.setImageBitmap(bitmap);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }*/

        try {
            if (mediaModel.getStatus().equalsIgnoreCase("Private")
                    && mediaModel.getViewerStatus() == null) {
                holder.iv_home_video.setAlpha(0.5f);
                holder.btn_video_private.setVisibility(View.VISIBLE);

            } else if (mediaModel.getStatus().equalsIgnoreCase("Private")
                    && mediaModel.getViewerStatus().equalsIgnoreCase("Public")) {
                holder.iv_home_video.setAlpha(0.9f);
                holder.btn_video_private.setVisibility(View.GONE);

            } else {
                holder.iv_home_video.setAlpha(0.9f);
                holder.btn_video_private.setVisibility(View.GONE);
            }
        } catch (Exception ignored) {
        }

        // duration cal
//        MediaPlayer mp = MediaPlayer.create(mContext, video);
//        int duration = mp.getDuration();
//        mp.release();

    /*
       // convert millis to appropriate time
        return String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );*/

        holder.iv_home_video.setTag(mediaModel);
        holder.iv_home_video.setOnClickListener(onClickListener);
        holder.btn_video_private.setTag(mediaModel);
        holder.btn_video_private.setOnClickListener(onClickListener);

    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    @Override
    public int getItemCount() {
        return mediaVideoListModelList.size();
    }

    public void setGalleryListModelList(List<MediaVideoListModel> mediaVideoListModelList) {
        this.mediaVideoListModelList = mediaVideoListModelList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btn_video_private)
        Button btn_video_private;
        @BindView(R.id.iv_home_video)
        ImageView iv_home_video;
        @BindView(R.id.tv_like_video)
        TextView tv_like_video;
        @BindView(R.id.tv_views_video)
        TextView tv_views_video;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
