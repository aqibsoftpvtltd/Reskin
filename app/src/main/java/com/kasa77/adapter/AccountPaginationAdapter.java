package com.kasa77.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa77.R;
import com.kasa77.modal.transaction_statement.RecordsItem;

import java.util.List;

public class AccountPaginationAdapter extends RecyclerView.Adapter<AccountPaginationAdapter.MyViewHolder> {

    private Context mContext;
    private List<RecordsItem> recordsItems;

    public AccountPaginationAdapter(Context mContext, List<RecordsItem> recordsItems) {
        this.mContext = mContext;
        this.recordsItems = recordsItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transaction_statement, parent, false));
    }


    @Override
    public int getItemCount() {
        return recordsItems == null ? 0 : recordsItems.size();
    }

    public void addItems(List<RecordsItem> postItems) {
        int lastItem = recordsItems.size();
        recordsItems.addAll(postItems);
        notifyItemRangeInserted(lastItem, postItems.size() - 1);
    }


    public void clear() {
        recordsItems.clear();
        notifyDataSetChanged();
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecordsItem datum = recordsItems.get(position);
        holder.tv_id.setText(datum.id);
        holder.tv_amount.setText("₹" +datum.transactionAmount.toString());
        holder.tv_description.setText(datum.description);
        holder.tv_status.setText(datum.transactionStatus);
        holder.tv_date.setText(datum.transactionDate);

        switch (datum.transactionStatus) {
            case "Pending":
                holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.orange));

                break;
            case "Success":
                holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.colorAccent));

                break;
            case "Cancel":
                holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));

                break;
        }



    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
      TextView tv_id,tv_amount,tv_description,tv_status,tv_date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_id = itemView.findViewById(R.id.tv_id);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_date = itemView.findViewById(R.id.tv_date);

        }


    }


}
