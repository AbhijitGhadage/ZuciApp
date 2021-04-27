package com.zuci.zuciapp.ui.homeViewDetails.tabFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zuci.zuciapp.utils.Methods;
import com.zuci.zuciapp.R;
import com.zuci.zuciapp.ui.gallery.MediaModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zuci.zuciapp.di.modules.NetworkModule.IMAGE_URL;

public class HomeImageListAdapter extends RecyclerView.Adapter<HomeImageListAdapter.MyViewHolder> {
    Context mContext;
    List<MediaModel> homeDetailsListModelList;
    View.OnClickListener onClickListener;

    public HomeImageListAdapter(Context mContext, List<MediaModel> homeDetailsListModelList, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.homeDetailsListModelList = homeDetailsListModelList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_image_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        MediaModel mediaModel = homeDetailsListModelList.get(position);

        holder.tv_like_img.setText(""+mediaModel.getLikesCount());

        if (!Methods.isEmptyOrNull(mediaModel.getName()))
            Picasso.get()
                    .load(IMAGE_URL + mediaModel.getName())
                    .error(R.drawable.profile_male)
                    .placeholder(R.drawable.profile_male)
                    .into(holder.iv_home_ad_image);

        try {
            if (mediaModel.getStatus().equalsIgnoreCase("Private")
                    && mediaModel.getViewerStatus()==null) {
                holder.iv_home_ad_image.setAlpha(0.1f);
                holder.btn_image_private.setVisibility(View.VISIBLE);

            } else if (mediaModel.getStatus().equalsIgnoreCase("Private")
                    && mediaModel.getViewerStatus().equalsIgnoreCase("Public")) {
                holder.iv_home_ad_image.setAlpha(0.9f);
                holder.btn_image_private.setVisibility(View.GONE);

            } else {
                holder.iv_home_ad_image.setAlpha(0.9f);
                holder.btn_image_private.setVisibility(View.GONE);
            }
        }catch (Exception ignored){}

        holder.iv_home_ad_image.setTag(mediaModel);
        holder.iv_home_ad_image.setOnClickListener(onClickListener);
        holder.btn_image_private.setTag(mediaModel);
        holder.btn_image_private.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return homeDetailsListModelList.size();
    }

    public void setGalleryListModelList(List<MediaModel> homeDetailsListModelList) {
        this.homeDetailsListModelList=homeDetailsListModelList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_home_ad_image) ImageView iv_home_ad_image;
        @BindView(R.id.btn_image_private) Button btn_image_private;
        @BindView(R.id.tv_like_img) TextView tv_like_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
