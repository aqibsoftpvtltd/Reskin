package com.kasa777.adapter.submit_bid_data_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa777.R;
import com.kasa777.modal.GameBidModal;

import java.util.List;

public class SubmitGameBidDialogAdapter extends RecyclerView.Adapter<SubmitGameBidDialogAdapter.ViewHolder> {
    private Context mContext;
    private List<GameBidModal> bidItems;
    private String gameSession;

    public SubmitGameBidDialogAdapter(Context mContext, List<GameBidModal> bidItem) {
        this.mContext = mContext;
        this.bidItems = bidItem;
        this.gameSession = gameSession;
    }

    @NonNull
    @Override
    public SubmitGameBidDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View rootView = li.inflate(R.layout.row_submit_single_digit_data, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubmitGameBidDialogAdapter.ViewHolder holder, int position) {
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
