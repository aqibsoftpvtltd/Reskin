package com.kasa77.adapter.bid_list_submit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa77.R;
import com.kasa77.modal.game_bid_data.BidData;

import java.util.List;

public class BidListFinalAdapter extends RecyclerView.Adapter<BidListFinalAdapter.ViewHolder> {
    private Context mContext;
    private List<BidData> bidItems;
    private String gameSession;

    public BidListFinalAdapter(Context mContext, List<BidData> bidItem) {
        this.mContext = mContext;
        this.bidItems = bidItem;
        this.gameSession = gameSession;
    }

    @NonNull
    @Override
    public BidListFinalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View rootView = li.inflate(R.layout.row_submit_single_digit_data, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull BidListFinalAdapter.ViewHolder holder, int position) {
        holder.tvDigits.setText(bidItems.get(position).getDigits());
        holder.tvPoints.setText(bidItems.get(position).getPoints());
        holder.tvGameType.setText(bidItems.get(position).getGemeSession());
        //  holder.tvGameType.setText(bidItems.get(position).setGemeSession(gameSession));

    }

    @Override
    public int getItemCount() {
        return bidItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDigits, tvPoints, tvGameType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDigits = itemView.findViewById(R.id.tvDigits);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvGameType = itemView.findViewById(R.id.tvGameType);
        }
    }
}
