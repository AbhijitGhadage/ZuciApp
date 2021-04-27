package com.zuci.zuciapp.ui.galleryVideo;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zuci.zuciapp.R;
import com.zuci.zuciapp.ui.gallery.MediaModel;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zuci.zuciapp.di.modules.NetworkModule.IMAGE_URL;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyViewHolder> {
    private Context mContext;
    private List<MediaModel> videoListModelList;
    private View.OnClickListener onClickListener;

    public VideoListAdapter(Context mContext, List<MediaModel> videoListModelList, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.videoListModelList = videoListModelList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_video_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        MediaModel mediaModel = videoListModelList.get(position);

        holder.tv_like_video.setText("" + mediaModel.getLikesCount());
        holder.tv_views_video.setText("" + mediaModel.getTotalView() + " Views");

        try {
            Uri uri = Uri.fromFile(new File(IMAGE_URL + mediaModel.getName()));
            holder.video_view.setTag(uri);
            Glide.with(mContext)
                    .load(uri)
                    .thumbnail(0.1f)
                    .into(holder.video_view);
        } catch (Exception ignored) {
        }

/*
        try {
            // process slow to create image
            Bitmap bitmap = retriveVideoFrameFromVideo(IMAGE_URL + mediaModel.getName());
            holder.video_view.setImageBitmap(bitmap);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
*/


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

        holder.video_view.setTag(mediaModel);
        holder.video_view.setOnClickListener(onClickListener);

        holder.iv_gallery_image_delete.setTag(mediaModel);
        holder.iv_gallery_image_delete.setOnClickListener(onClickListener);
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
        return videoListModelList.size();
    }

    public void setGalleryListModelList(List<MediaModel> videoListModelList) {
        this.videoListModelList = videoListModelList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.video_view)
        ImageView video_view;
        @BindView(R.id.iv_gallery_image_delete)
        ImageView iv_gallery_image_delete;
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
