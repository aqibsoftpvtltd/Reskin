package com.kasa77.adapter.jackpot_adapter;

import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa77.R;
import com.kasa77.databinding.RowJackpotGameIstBinding;
import com.kasa77.ui.activity.JackpotGameListActivity;
import com.kasa77.ui.fragment.jackpot_fragments.modal.Result;
import com.kasa77.utils.Alerts;
import com.kasa77.utils.Helper;

import java.util.List;

public class JackpotAdapter extends RecyclerView.Adapter<JackpotAdapter.ViewHolder> {
    private List<Result> resultList;

    public JackpotAdapter( List<Result> resultList) {
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public JackpotAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowJackpotGameIstBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.row_jackpot_game_ist, parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull JackpotAdapter.ViewHolder holder, int position) {
        Result resultData = resultList.get(position);

        SpannableString formattedTime = Helper.formatTimeString(""+resultData.providerName);
        holder.binding.tvProviderName.setText(formattedTime);
        holder.binding.tvGameResult.setText(""+resultData.providerResult);
        holder.binding.tvBettingStatus.setText(""+resultData.displayText);

        try {
            holder.binding.tvBettingStatus.setTextColor(Color.parseColor(resultData.colorCode));
        } catch (Exception e) {
            holder.binding.tvBettingStatus.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(),R.color.grayColor));
        }


        if (resultData.displayText.contains("Betting Is Running Now"))
        {
           /*// holder.binding.tabInnerSelection.setAlpha(1f);
            holder.binding.tabPlayGame.setVisibility(View.VISIBLE);
            if (resultData.resultStatus == 0)
            {
                holder.binding.tabPlayGame.setVisibility(View.VISIBLE);
            }
            else
            {
                //holder.binding.tabInnerSelection.setAlpha(0.5f);
                //holder.binding.tabPlayGame.setVisibility(View.GONE);
                holder.binding.tabPlayGame.setAlpha(0.5f);

            }*/
            holder.binding.tabPlayGame.setAlpha(1f);
            holder.binding.outerBackground.setBackgroundResource(R.drawable.green_outer_jackport);

        }
        else
        {
            //holder.binding.tabPlayGame.setVisibility(View.GONE);
            //holder.binding.tabInnerSelection.setAlpha(0.5f);
            holder.binding.tabPlayGame.setAlpha(0.5f);
            holder.binding.outerBackground.setBackgroundResource(R.drawable.white_jackport_rectangle_background);
        }


        holder.binding.tabPlayGame.setOnClickListener(v -> {
            if (resultData.openBidTime.equals("") || resultData.closeBidTime.equals("") || resultData.displayText.contains("Betting Is Closed For Today")) {
                Alerts.AlertDialogWarning(holder.binding.getRoot().getContext(), resultData.displayText,"green");
            } else {
                if (holder.binding.tabPlayGame.getVisibility() == View.VISIBLE) {
                    Intent intent = new Intent(holder.binding.getRoot().getContext(), JackpotGameListActivity.class);
                    intent.putExtra("PROVIDER", resultList.get(position));
                    holder.binding.getRoot().getContext().startActivity(intent);
                }
            }
        });

        holder.binding.tabSelection.setOnClickListener(v -> {
            if (resultData.openBidTime.equals("") || resultData.closeBidTime.equals("") || resultData.displayText.contains("Betting Is Closed For Today")) {
                Alerts.AlertDialogWarning(holder.binding.getRoot().getContext(), resultData.displayText,"green");
            } else {
                if (holder.binding.tabPlayGame.getVisibility() == View.VISIBLE) {
                    Intent intent = new Intent(holder.binding.getRoot().getContext(), JackpotGameListActivity.class);
                    intent.putExtra("PROVIDER", resultList.get(position));
                    holder.binding.getRoot().getContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RowJackpotGameIstBinding binding;

        public ViewHolder(RowJackpotGameIstBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
