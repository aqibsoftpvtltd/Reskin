package com.kasa777.adapter.static_data_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa777.R;
import com.kasa777.modal.broadcast.OldBroadcastMessageItem;

import java.util.List;

public class BroadCastAdapter extends RecyclerView.Adapter<BroadCastAdapter.ViewHolder> {
    private Context mContext;
    private List<OldBroadcastMessageItem> oldBroadcastMessageItems;
    private View.OnClickListener onClickListener;

    public BroadCastAdapter(Context mContext, List<OldBroadcastMessageItem> oldBroadcastMessageItems) {
        this.mContext = mContext;
        this.oldBroadcastMessageItems = oldBroadcastMessageItems;
    }

    @NonNull
    @Override
    public BroadCastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View rootView = li.inflate(R.layout.row_broadcast, null);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull BroadCastAdapter.ViewHolder holder, int position) {
        holder.txtTitleNotification.setText(String.valueOf(oldBroadcastMessageItems.get(position).message));
        holder.txtTime.setText(String.valueOf(oldBroadcastMessageItems.get(position).dateTime));


    }

    @Override
    public int getItemCount() {
        return oldBroadcastMessageItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView  txtTitleNotification,txtTime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitleNotification = itemView.findViewById(R.id.txtTitleNotification);
            txtTime = itemView.findViewById(R.id.txtTime);



        }
    }
}
