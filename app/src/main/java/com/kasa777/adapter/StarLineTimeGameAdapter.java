package com.kasa777.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa777.R;
import com.kasa777.databinding.RowStarlinegameListBinding;
import com.kasa777.ui.activity.StarLineGameListActivity;
import com.kasa777.ui.fragment.startline_game_fragment.modal.Result;
import com.kasa777.utils.Alerts;

import java.util.List;

public class StarLineTimeGameAdapter extends RecyclerView.Adapter<StarLineTimeGameAdapter.ViewHolder> {
    private List<com.kasa777.ui.fragment.startline_game_fragment.modal.Result> resultList;


    public StarLineTimeGameAdapter(List<com.kasa777.ui.fragment.startline_game_fragment.modal.Result> resultList) {
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public StarLineTimeGameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowStarlinegameListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.row_starlinegame_list, parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StarLineTimeGameAdapter.ViewHolder holder, int position) {
        Result resultData = resultList.get(position);


        holder.binding.tvBidTime.setText(resultData.providerName);
        holder.binding.tvBidResult.setText(resultData.providerResult);
        holder.binding.tvBattingStatus.setText(resultData.displayText);
        if(resultData.displayText.contains("Betting Is Closed For Today")){
            holder.binding.tabInnerPlayGame.setBackgroundResource(R.drawable.ractengle_gradian_round_edge_disabled);
            holder.binding.ivPlayGame.setColorFilter(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.gray_f), PorterDuff.Mode.MULTIPLY);
        }
        else{
            holder.binding.tabInnerPlayGame.setBackgroundResource(R.drawable.ractengle_gradian_round_edge);
            holder.binding.ivPlayGame.setColorFilter(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.appColorLight),PorterDuff.Mode.MULTIPLY);
        }
        try {
            holder.binding.tvBattingStatus.setTextColor(Color.parseColor(resultData.colorCode));
        } catch (Exception e) {
            holder.binding.tvBattingStatus.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(),R.color.grayColor));
        }


//        if (resultData.displayText.contains("Betting Is Running Now")) {
//            holder.binding.tvBattingStatus.setTextColor(ContextCompat.
//                    getColor(holder.binding.getRoot().getContext(),R.color.green));
//            if (resultData.resultStatus == 0) {
//                holder.binding.tvBattingStatus.setTextColor(ContextCompat.
//                        getColor(holder.binding.getRoot().getContext(),R.color.green));
//            } else {
//                holder.binding.tvBattingStatus.setTextColor(ContextCompat.
//                        getColor(holder.binding.getRoot().getContext(),R.color.red));
//            }
//        } else {
//            holder.binding.tvBattingStatus.setTextColor(ContextCompat.
//                    getColor(holder.binding.getRoot().getContext(),R.color.red));
//        }


        holder.binding.tabPlayGame.setOnClickListener(v -> {
            if (resultData.displayText.contains("Betting Is Closed For Today")) {
                Alerts.AlertDialogWarning(holder.binding.getRoot().getContext(), resultData.displayText);
            } else {
                holder.binding.getRoot().getContext().startActivity(new Intent(holder.binding.getRoot().getContext(), StarLineGameListActivity.class).putExtra("PROVIDER", resultList.get(position)));
            }
        });

        holder.binding.tabSelection.setOnClickListener(v -> {
            if (resultData.displayText.contains("Betting Is Closed For Today")) {
                Alerts.AlertDialogWarning(holder.binding.getRoot().getContext(), resultData.displayText);
            } else {
                holder.binding.getRoot().getContext().startActivity(new Intent(holder.binding.getRoot().getContext(), StarLineGameListActivity.class).putExtra("PROVIDER", resultList.get(position)));
            }
        });

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        RowStarlinegameListBinding binding;

        public ViewHolder(RowStarlinegameListBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }


}
