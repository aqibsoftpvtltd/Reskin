package com.kasa777.adapter.static_data_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa777.R;
import com.kasa777.modal.static_data.news.Datum;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context mContext;
    private List<Datum> newsData;
    private View.OnClickListener onClickListener;

    public NewsAdapter(Context mContext, List<Datum> newsData) {
        this.mContext = mContext;
        this.newsData = newsData;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View rootView = li.inflate(R.layout.row_news, null);
        return new ViewHolder(rootView);
    }
    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        holder.txtNewsDescription.setText(String.valueOf(newsData.get(position).getDescription()));

    }

    @Override
    public int getItemCount() {
        return newsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNewsDescription, tvPoints;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNewsDescription = itemView.findViewById(R.id.txtNewsDescription);

        }
    }
}
