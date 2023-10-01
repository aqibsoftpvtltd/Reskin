package com.kasa77.adapter.static_data_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa77.R;
import com.kasa77.modal.static_data.profilenote.Datum;

import java.util.List;

public class ProfileNoteAdapter extends RecyclerView.Adapter<ProfileNoteAdapter.ViewHolder> {
    private Context mContext;
    private List<com.kasa77.modal.static_data.profilenote.Datum> newsData;
    private View.OnClickListener onClickListener;

    public ProfileNoteAdapter(Context mContext, List<Datum> newsData) {
        this.mContext = mContext;
        this.newsData = newsData;
    }

    @NonNull
    @Override
    public ProfileNoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View rootView = li.inflate(R.layout.row_profile_note, null);
        return new ViewHolder(rootView);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProfileNoteAdapter.ViewHolder holder, int position) {
        holder.txtNewsDescription.setText("Note* : "+ newsData.get(position).getNote());

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
