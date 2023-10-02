package com.kasa77.adapter.bid_list_submit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa77.R;
import com.kasa77.modal.game_bid_data.BidData;

import java.util.List;

public class BidListToSubmitAdapter extends RecyclerView.Adapter<BidListToSubmitAdapter.ViewHolder> {
    private Context mContext;
    private List<BidData> bidItems;
    private String gameSession;
    private View.OnClickListener onClickListener;

    public BidListToSubmitAdapter(Context mContext, List<BidData> bidItem, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.bidItems = bidItem;
        this.gameSession = gameSession;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public BidListToSubmitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View rootView = li.inflate(R.layout.row_bid_list_with_delete, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvDigits.setText(bidItems.get(position).getDigits());
        holder.tvPoints.setText(bidItems.get(position).getPoints());
        holder.tvGameType.setText(bidItems.get(position).getGemeSession());
        if (bidItems.get(position).getGemeSession().equals("Open")){
            holder.tvGameType.setTextColor(mContext.getResources().getColor(R.color.openColor));
        }
        else {
            holder.tvGameType.setTextColor(mContext.getResources().getColor(R.color.closeColor));
        }
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
        private ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDigits = itemView.findViewById(R.id.tvDigits);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvGameType = itemView.findViewById(R.id.tvGameType);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
