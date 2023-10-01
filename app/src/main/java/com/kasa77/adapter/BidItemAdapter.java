package com.kasa77.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa77.R;
import com.kasa77.modal.BidItemModel;

import java.util.List;

public class BidItemAdapter extends RecyclerView.Adapter<BidItemAdapter.ViewHolder> {
    private Context mContext;
    private List<BidItemModel> bidItems;
    private View.OnClickListener onClickListener;

    public BidItemAdapter(Context mContext, List<BidItemModel> bidItem, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.bidItems = bidItem;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public BidItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View rootView = li.inflate(R.layout.row_bid_list_with_delete, null);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull BidItemAdapter.ViewHolder holder, int position) {
        holder.tvDigits.setText(String.valueOf(bidItems.get(position).getDigits()));
        holder.tvPoints.setText(String.valueOf(bidItems.get(position).getPoints()));
       // holder.tvGameType.setText(bidItems.get(position).getGameType());

        holder.btnDelete.setTag(position);
        holder.btnDelete.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return bidItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView btnDelete;
        private TextView tvDigits, tvPoints, tvGameType;
        private LinearLayout navLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDigits = itemView.findViewById(R.id.tvDigits);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvGameType = itemView.findViewById(R.id.tvGameType);

            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
