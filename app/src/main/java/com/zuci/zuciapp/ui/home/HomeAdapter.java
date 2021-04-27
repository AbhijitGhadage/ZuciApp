package com.zuci.zuciapp.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.zuci.zuciapp.R;
import com.zuci.zuciapp.utils.Methods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> implements Filterable {
    private Context mContext;
    private List<HomeListModel> homeListModelList;
    private List<HomeListModel> contactListFiltered;
    private View.OnClickListener onClickListener;

    public HomeAdapter(Context mContext, List<HomeListModel> homeListModelList, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.homeListModelList = homeListModelList;
        this.contactListFiltered = homeListModelList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public HomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.MyViewHolder holder, final int position) {
        HomeListModel homeListModel = homeListModelList.get(position);
        holder.tv_home_userName.setText(homeListModel.getProfileName() + ", " + homeListModel.getAge());

        holder.setIsRecyclable(false);

        try {
            if (!Methods.isEmptyOrNull(homeListModel.getProfileImage())) {
                Picasso.get()
                        .load(homeListModel.getProfileImage())
                        .placeholder(R.drawable.profile_male)
                        .error(R.drawable.profile_male)
                        .into(holder.iv_home_image);
            } else {
                if (homeListModel.getGender().equalsIgnoreCase("Male")) {
                    holder.iv_home_image.setImageResource(R.drawable.profile_male);
                } else {
                    holder.iv_home_image.setImageResource(R.drawable.profile_female);
                }
            }
        } catch (Exception e) {
            Log.e("bindViewHolder", e.getMessage());
        }
        holder.iv_home_image.setTag(homeListModel);
        holder.iv_home_image.setOnClickListener(onClickListener);
        holder.iv_voice_call.setTag(homeListModel);
        holder.iv_voice_call.setOnClickListener(onClickListener);
        holder.iv_video_call.setTag(homeListModel);
        holder.iv_video_call.setOnClickListener(onClickListener);
        holder.iv_chat_message.setTag(homeListModel);
        holder.iv_chat_message.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return homeListModelList == null ? 0 : homeListModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_home_image)
        CircleImageView iv_home_image;
        @BindView(R.id.iv_voice_call)
        ImageView iv_voice_call;
        @BindView(R.id.iv_video_call)
        ImageView iv_video_call;
        @BindView(R.id.tv_home_userName)
        AppCompatTextView tv_home_userName;
        @BindView(R.id.iv_chat_message)
        ImageView iv_chat_message;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public void updateList(List<HomeListModel> list) {
        homeListModelList = new ArrayList<>();
        homeListModelList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().replaceAll("\\s", "").toLowerCase().trim();
                if (charString.isEmpty() || charSequence.equals("")) {
                    homeListModelList = contactListFiltered;
                } else {
                    ArrayList<HomeListModel> filteredList = new ArrayList<>();
                    for (int i = 0; i < homeListModelList.size(); i++) {
                        String name = homeListModelList.get(i).getProfileName().replaceAll("\\s", "").toLowerCase().trim();
                        String mobile_no = homeListModelList.get(i).getEmailId().replaceAll("\\s", "").toLowerCase().trim();
                        if ((name.contains(charString)) || (mobile_no.contains(charString))) {
                            filteredList.add(homeListModelList.get(i));
                        }
                    }
                    if (filteredList.size() > 0) {
                        homeListModelList = filteredList;
                    } else {
                        homeListModelList = contactListFiltered;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = homeListModelList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                homeListModelList = (ArrayList<HomeListModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
