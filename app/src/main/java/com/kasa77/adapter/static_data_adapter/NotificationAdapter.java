package com.kasa77.adapter.static_data_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa77.R;
import com.kasa77.modal.static_data.notification.Datum;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context mContext;
    private List<com.kasa77.modal.static_data.notification.Datum> newsData;
    private View.OnClickListener onClickListener;

    public NotificationAdapter(Context mContext, List<Datum> newsData) {
        this.mContext = mContext;
        this.newsData = newsData;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View rootView = li.inflate(R.layout.row_news, parent,false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        holder.txtNewsDescription.setText(String.valueOf(newsData.get(position).getMessage()));
        holder.txtTitleNotification.setText(String.valueOf(newsData.get(position).getTitle()));
        holder.tv_time.setText(newsData.get(position).getModified());
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
//        Date d = null;
//        try {
//            d = sdf.parse(newsData.get(position).getModified());
//            String formattedTime = output.format(d);
//            holder.tv_time.setText(formattedTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }




    }

    @Override
    public int getItemCount() {
        return newsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNewsDescription, txtTitleNotification,tv_time;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitleNotification = itemView.findViewById(R.id.txtTitleNotification);
            txtNewsDescription = itemView.findViewById(R.id.txtNewsDescription);
            tv_time = itemView.findViewById(R.id.tv_time);


        }
    }
}
