package com.kasa777.adapter.bids_history_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa777.R;

import java.util.List;

public class AndarBaharHistoryAdapter extends RecyclerView.Adapter<AndarBaharHistoryAdapter.ViewHolder> {
    private Context mContext;
    private List<com.kasa777.modal.history_data_modal.jackpotresulthistory.Datum> fundData;
    private View.OnClickListener onClickListener;

    public AndarBaharHistoryAdapter(Context mContext, List<com.kasa777.modal.history_data_modal.jackpotresulthistory.Datum> fundData ) {
        this.mContext = mContext;
        this.fundData = fundData;
    }

    @NonNull
    @Override
    public AndarBaharHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View rootView = li.inflate(R.layout.row_bid__result_history, null);
        return new ViewHolder(rootView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AndarBaharHistoryAdapter.ViewHolder holder, int position) {
        com.kasa777.modal.history_data_modal.jackpotresulthistory.Datum datum = fundData.get(position);
        holder.tvGameResult.setText(datum.getWinningDigit().toString());
        holder.tvProviderName.setText(datum.getProviderName());

    }

    @Override
    public int getItemCount() {
        return fundData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvGameResult, tvProviderName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProviderName = itemView.findViewById(R.id.tvProviderName);
            tvGameResult = itemView.findViewById(R.id.tvGameResult);

        }
    }
}
