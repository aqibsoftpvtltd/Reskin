package com.kasa77.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.kasa77.R;
import com.kasa77.databinding.GameDashboardItemViewBinding;
import com.kasa77.interfaces.AdapterClickListener;
import com.kasa77.modal.GameDashboardModel;
import java.util.ArrayList;

public class GameDashboardAdapter extends RecyclerView.Adapter<GameDashboardAdapter.ViewHolder> {

    ArrayList<GameDashboardModel> list;
    AdapterClickListener listener;

    public GameDashboardAdapter(ArrayList<GameDashboardModel> list,AdapterClickListener listener) {
        this.list = list;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GameDashboardItemViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.game_dashboard_item_view, parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GameDashboardModel item = list.get(position);

        if (position%2==1)
        {
            holder.binding.tabStartView.setVisibility(View.GONE);
            holder.binding.tabEndView.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.binding.tabStartView.setVisibility(View.VISIBLE);
            holder.binding.tabEndView.setVisibility(View.GONE);
        }

      //  holder.binding.tvGameDashboard.setText(""+item.getName());
       // holder.binding.tvGameDashboard.setImageDrawable(ContextCompat.getDrawable(holder.binding.getRoot().getContext(),item.getDrawable()));
        holder.binding.tvGameDashboard.setBackground(ContextCompat.getDrawable(holder.binding.getRoot().getContext(),item.getBackground()));

        holder.bind(listener,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        GameDashboardItemViewBinding binding;

        public ViewHolder(GameDashboardItemViewBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bind(AdapterClickListener listener,int position)
        {
            binding.tabGameDashboard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onAdapterClick(position,view);
                }
            });
        }
    }


}
