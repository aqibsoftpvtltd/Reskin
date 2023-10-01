package com.kasa77.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa77.R;

import com.kasa77.modal.game_date_list.DateObject;
import com.kasa77.utils.DateFormatToDisplay;

import java.util.List;

public class GameDateAdapter extends RecyclerView.Adapter<GameDateAdapter.MyViewHolder> {
    public Context context;
    public List<DateObject> date;
    public OnSelectDateFromGameDates onSelectDateFromGameDates;

    public GameDateAdapter(Context context, List<DateObject> date, OnSelectDateFromGameDates onSelectDateFromGameDates) {
        this.context = context;
        this.date = date;
        this.onSelectDateFromGameDates = onSelectDateFromGameDates;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View rootView = li.inflate(R.layout.layout_game_dates, parent,false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DateObject dateObject = date.get(position);
        holder.tv_date.setText(new DateFormatToDisplay().parseDateToddMMyyyy(dateObject.date));
        holder.tv_day.setText(dateObject.dayname);

        if (dateObject.status == 1) {
            holder.tv_status.setText("Open");
        }
        if (dateObject.status == 2) {
            holder.tv_status.setText("Open");
        }
        if (dateObject.status == 3) {
            holder.tv_status.setText("Close");
        }
        if (dateObject.isSelected())
        {
            holder.ivStatus.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.ivStatus.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return date.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_date;
        TextView tv_day;
        TextView tv_status;
        ImageView ivStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_day = itemView.findViewById(R.id.tv_day);
            tv_status = itemView.findViewById(R.id.tv_status);
            ivStatus= itemView.findViewById(R.id.ivStatus);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onSelectDateFromGameDates != null)
                onSelectDateFromGameDates.onSelectDate(getAdapterPosition());
        }
    }

    public interface OnSelectDateFromGameDates {
        public void onSelectDate(int position);
    }
}
