package com.kasa77.adapter.bids_history_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa77.R;

import java.util.List;

public class StarlineResultAdapter extends RecyclerView.Adapter<StarlineResultAdapter.ViewHolder> {
    private Context mContext;
    private List<com.kasa77.modal.history_data_modal.starlineresulthistory.Datum> fundData;
    private View.OnClickListener onClickListener;

    public StarlineResultAdapter(Context mContext, List<com.kasa77.modal.history_data_modal.starlineresulthistory.Datum> fundData ) {
        this.mContext = mContext;
        this.fundData = fundData;
    }

    @NonNull
    @Override
    public StarlineResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View rootView = li.inflate(R.layout.row_bid__result_history, null);
        return new ViewHolder(rootView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StarlineResultAdapter.ViewHolder holder, int position) {
        com.kasa77.modal.history_data_modal.starlineresulthistory.Datum datum = fundData.get(position);
        holder.tvGameResult.setText(datum.getWinningDigit().toString()+"-"+datum.getWinningDigitFamily().toString());
        holder.tvProviderName.setText(datum.getProviderName());
    }

    @Override
    public int getItemCount() {
        return fundData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvProviderName,tvGameResult;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProviderName = itemView.findViewById(R.id.tvProviderName);
            tvGameResult = itemView.findViewById(R.id.tvGameResult);

        }
    }
}
