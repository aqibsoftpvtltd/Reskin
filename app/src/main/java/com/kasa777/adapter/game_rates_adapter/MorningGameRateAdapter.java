package com.kasa777.adapter.game_rates_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa777.R;
import com.kasa777.modal.game_rates.GameRate;

import java.util.List;

public class MorningGameRateAdapter extends RecyclerView.Adapter<MorningGameRateAdapter.ViewHolder> {
    private Context mContext;
    private List<GameRate> fundData;
    private View.OnClickListener onClickListener;

    public MorningGameRateAdapter(Context mContext, List<GameRate> fundData) {
        this.mContext = mContext;
        this.fundData = fundData;
    }

    @NonNull
    @Override
    public MorningGameRateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View rootView = li.inflate(R.layout.row_game_rates, null);
        return new ViewHolder(rootView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MorningGameRateAdapter.ViewHolder holder, int position) {
        GameRate datum = fundData.get(position);
        holder.tvGameNameRate.setText(datum.getGameName() + " :- " + datum.getGamePrice());

    }

    @Override
    public int getItemCount() {
        return fundData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvGameNameRate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGameNameRate = itemView.findViewById(R.id.tvGameNameRate);
        }
    }
}
