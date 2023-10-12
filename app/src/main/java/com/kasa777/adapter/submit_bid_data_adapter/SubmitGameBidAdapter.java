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

public class SubmitGameBidAdapter extends RecyclerView.Adapter<SubmitGameBidAdapter.ViewHolder> {
    private Context mContext;
    private List<GameBidModal> bidItems;
    private String gameSession;
    private View.OnClickListener onClickListener;

    public SubmitGameBidAdapter(Context mContext, List<GameBidModal> bidItem, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.bidItems = bidItem;
        this.gameSession = gameSession;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public SubmitGameBidAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View rootView = li.inflate(R.layout.row_bid_list_with_delete, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubmitGameBidAdapter.ViewHolder holder, int position) {
        holder.tvDigits.setText(bidItems.get(position).getDigits());
        holder.tvPoints.setText(bidItems.get(position).getPoints());
        holder.tvGameType.setText(bidItems.get(position).getGemeSession());
        holder.btnDelete.setTag(position);
        holder.btnDelete.setOnClickListener(onClickListener);
        //  holder.tvGameType.setText(bidItems.get(position).setGemeSession(gameSession));

    }

    @Override
    public int getItemCount() {
        return bidItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDigits, tvPoints, tvGameType;
        private TextView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDigits = itemView.findViewById(R.id.tvDigits);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvGameType = itemView.findViewById(R.id.tvGameType);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
