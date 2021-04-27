package com.zuci.zuciapp.ui.transaction;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zuci.zuciapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
    private Context mContext;
    private List<TransactionLogModel> transactionLogModels;
    private View.OnClickListener onClickListener;
    private Integer registerId;

    public TransactionAdapter(Context mContext, List<TransactionLogModel> transactionLogModels,
                              View.OnClickListener onClickListener, Integer registerId) {
        this.mContext = mContext;
        this.transactionLogModels = transactionLogModels;
        this.onClickListener = onClickListener;
        this.registerId = registerId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_trasaction_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        TransactionLogModel transactionLogModel = transactionLogModels.get(position);
        holder.setIsRecyclable(false);

        try {
            if (transactionLogModel.getDeductionType() == null) {
                holder.tv_tra_amt.setText("₹ " + transactionLogModel.getAmount());
                holder.tv_tra_amt.setTextColor(Color.parseColor("#000"));
                holder.tv_tra_date.setText(transactionLogModel.getTransactionDate());
                holder.tv_paid.setText("Paid to \nZuciApp");
                holder.tv_tran_dr_cr.setText("Credited");

            } else if (transactionLogModel.getDeductionType().equalsIgnoreCase("Call")) {

                holder.tv_tra_amt.setText(" - " + transactionLogModel.getDeductCoins() + " Coins");
                holder.tv_tra_amt.setTextColor(Color.parseColor("#ff0000"));
                holder.tv_tra_date.setText(transactionLogModel.getDeductionDate());
                holder.tv_paid.setText("Paid to \nZuciApp");
                holder.tv_tran_dr_cr.setText("Debited");

            } else if (transactionLogModel.getDeductionType().equalsIgnoreCase("Image")
                    || transactionLogModel.getDeductionType().equalsIgnoreCase("Video")) {
                if (registerId == transactionLogModel.getMediaOwnerRegistrationId()) {
                    holder.tv_tra_amt.setText(" + " + transactionLogModel.getDeductCoins() + " Coins");
                    holder.tv_tra_amt.setTextColor(Color.parseColor("#00ff00"));
                    holder.tv_tra_date.setText(transactionLogModel.getDeductionDate());
                    holder.tv_paid.setText("Received From \n" + transactionLogModel.getViewerUserName());
                    holder.tv_tran_dr_cr.setText("Credited");

                } else {
                    long add = transactionLogModel.getDeductCoins() + transactionLogModel.getAdminDeductCoins();
                    holder.tv_tra_amt.setText(" - " + add + " Coins");
                    holder.tv_tra_amt.setTextColor(Color.parseColor("#ff0000"));
                    holder.tv_tra_date.setText(transactionLogModel.getDeductionDate());
                    holder.tv_paid.setText("Paid to \n" + transactionLogModel.getOwnerUserName());
                    holder.tv_tran_dr_cr.setText("Debited");
                }
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public int getItemCount() {
//        return transactionLogModels != null ? transactionLogModels.size() : 0;
        return transactionLogModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_tra_amt)
        TextView tv_tra_amt;
        @BindView(R.id.tv_tra_date)
        TextView tv_tra_date;
        @BindView(R.id.tv_paid)
        TextView tv_paid;
        @BindView(R.id.tv_tran_dr_cr)
        TextView tv_tran_dr_cr;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
