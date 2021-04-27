package com.zuci.zuciapp.ui.gallery;

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
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zuci.zuciapp.di.modules.NetworkModule.IMAGE_URL;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
    private Context mContext;
    private List<MediaModel> galleryListModelList;
    private View.OnClickListener onClickListener;

    public GalleryAdapter(Context mContext, List<MediaModel> galleryListModelList, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.galleryListModelList = galleryListModelList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gallery, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        MediaModel galleryListModel = galleryListModelList.get(position);

        holder.tv_like_img.setText(""+galleryListModel.getLikesCount());

        if (!Methods.isEmptyOrNull(galleryListModel.getName()))
            Picasso.get()
                    .load(IMAGE_URL + galleryListModel.getName())
                    .error(R.drawable.profile_male)
                    .placeholder(R.drawable.profile_male)
                    .into(holder.iv_gallery_image);

        holder.iv_gallery_image.setTag(galleryListModel);
        holder.iv_gallery_image.setOnClickListener(onClickListener);

        holder.iv_gallery_image_delete.setTag(galleryListModel);
        holder.iv_gallery_image_delete.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return galleryListModelList != null ? galleryListModelList.size() : 0;
    }

    public void setGalleryListModelList(List<MediaModel> galleryListModelList) {
        this.galleryListModelList = galleryListModelList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_gallery_image) ImageView iv_gallery_image;
        @BindView(R.id.iv_gallery_image_delete) ImageView iv_gallery_image_delete;
        @BindView(R.id.tv_like_img) TextView tv_like_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
