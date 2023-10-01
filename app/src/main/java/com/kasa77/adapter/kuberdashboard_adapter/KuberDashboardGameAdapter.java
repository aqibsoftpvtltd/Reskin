package com.kasa77.adapter.kuberdashboard_adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa77.R;
import com.kasa77.databinding.RowLiveResultDashboardBinding;
import com.kasa77.modal.dashboard_gamelist.Result;
import com.kasa77.ui.activity.GameListFromDashboardActivity;
import com.kasa77.utils.Alerts;

import java.util.List;

public class KuberDashboardGameAdapter extends RecyclerView.Adapter<KuberDashboardGameAdapter.ViewHolder> {

    List<Result> resultArrayList;

    public KuberDashboardGameAdapter(List<Result> resultArrayList) {
        this.resultArrayList = resultArrayList;
    }

    @NonNull
    @Override
    public KuberDashboardGameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowLiveResultDashboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.row_live_result_dashboard, parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull KuberDashboardGameAdapter.ViewHolder holder, int position) {

        Result result = (Result) resultArrayList.get(position);



        holder.binding.tvProviderName.setText(""+result.providerName);
        holder.binding.tvGameResult.setText(""+result.providerResult);
        if (result.openBidTime!=null && !(result.openBidTime.isEmpty()))
        {
            holder.binding.tvBidOpenTime.setText(""+result.openBidTime);
        }
        else
        {
            holder.binding.tvBidOpenTime.setText("---");
        }
        if (result.closeBidTime!=null && !(result.closeBidTime.isEmpty()))
        {
            holder.binding.tvBidCloseTime.setText(""+result.closeBidTime);
        }
        else
        {
            holder.binding.tvBidCloseTime.setText("---");
        }
        holder.binding.tvBattingStatus.setText(""+result.displayText);

        try {
            holder.binding.tvBattingStatus.setTextColor(Color.parseColor(result.colorCode));
        } catch (Exception e) {
            holder.binding.tvBattingStatus.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(),R.color.grayColor));
        }

        if (result.openBidTime.equals("") || result.closeBidTime.equals("") || result.displayText.contains("Betting Is Closed For Today")) {
            holder.binding.tabInnerPlayGame.setBackgroundResource(R.drawable.ractengle_gradian_round_edge_disabled);
            holder.binding.ivPlayGame.setColorFilter(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.gray_f), PorterDuff.Mode.MULTIPLY);
        }
        else{
            holder.binding.tabInnerPlayGame.setBackgroundResource(R.drawable.ractengle_gradian_round_edge);
            holder.binding.ivPlayGame.setColorFilter(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.appColorLight),PorterDuff.Mode.MULTIPLY);
        }
        holder.binding.tabPlayGame.setOnClickListener(v -> {
            if (result.openBidTime.equals("") || result.closeBidTime.equals("") || result.displayText.contains("Betting Is Closed For Today")) {
                Alerts.AlertDialogWarning(holder.binding.getRoot().getContext(), "Betting Is Closed For Today");
            } else {
                Intent intent = new Intent(holder.binding.getRoot().getContext(), GameListFromDashboardActivity.class);
                intent.putExtra("PROVIDER", result);
                holder.binding.getRoot().getContext().startActivity(intent);
            }
        });

        holder.binding.tabSelection.setOnClickListener(v -> {
            Alerts.infoDialog(holder.binding.getRoot().getContext(), result);
        });

    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        RowLiveResultDashboardBinding binding;

        public ViewHolder(RowLiveResultDashboardBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }


}

