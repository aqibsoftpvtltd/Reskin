package com.kasa77.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.kasa77.R;
import com.kasa77.databinding.RowFundRequestesBinding;
import com.kasa77.modal.fund_pagination.RecordsItem;
import java.util.List;

public class FundHistoryPaginationAdapter extends RecyclerView.Adapter<FundHistoryPaginationAdapter.ViewHolder> {
    private List<RecordsItem> recordsItems;

    public FundHistoryPaginationAdapter(List<RecordsItem> recordsItems) {
        this.recordsItems = recordsItems;
    }

    @NonNull
    @Override
    public FundHistoryPaginationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowFundRequestesBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.row_fund_requestes, parent,false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FundHistoryPaginationAdapter.ViewHolder holder, int position) {
        RecordsItem datum = recordsItems.get(position);
        holder.binding.tvId.setText(datum.id);
        holder.binding.tvAmount.setText("₹" + datum.reqAmount);
        holder.binding.tvStatus.setText(datum.reqStatus);
        holder.binding.tvType.setText(datum.reqType);
        holder.binding.tvDate.setText(datum.reqDate + " " + datum.reqTime);

        if(datum.reqType.equals("Debit")){
            holder.binding.tvAmount.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(),R.color.red));
            holder.binding.tabMode.setVisibility(View.VISIBLE);
            holder.binding.tabViewMode.setVisibility(View.VISIBLE);
            holder.binding.tvMode.setText(datum.withdrawalMode);
        }else {
            holder.binding.tabMode.setVisibility(View.GONE);
            holder.binding.tabViewMode.setVisibility(View.GONE);
            holder.binding.tvAmount.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(),R.color.green));
        }

    }

    public void addItems(List<RecordsItem> postItems) {
        int lastItem = recordsItems.size();
        recordsItems.addAll(postItems);
        notifyItemRangeInserted(lastItem, postItems.size() - 1);
    }

    @Override
    public int getItemCount() {
        return recordsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RowFundRequestesBinding binding;

        public ViewHolder(RowFundRequestesBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
