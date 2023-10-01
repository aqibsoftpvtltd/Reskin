package com.kasa77.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa77.R;
import com.kasa77.modal.filterdata.DataItem;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private Context mContext;
    private static List<DataItem> filterLists;


    public FilterAdapter(Context mContext, List<DataItem> filterLists) {
        this.mContext = mContext;
        this.filterLists = filterLists;
    }

    @NonNull
    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View rootView = li.inflate(R.layout.row_filter_list, parent, false);
        return new ViewHolder(rootView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.ViewHolder holder, int position) {
        holder.tv_provider_name.setText(filterLists.get(position).providerName);

    }

    @Override
    public int getItemCount() {
        return filterLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox checkbox;
        TextView tv_provider_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox);
            tv_provider_name = itemView.findViewById(R.id.tv_provider_name);

            itemView.setOnClickListener(this);
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        filterLists.get(getAdapterPosition()).checked = isChecked;
                }
            });
        }

        @Override
        public void onClick(View v) {
            if(filterLists.get(getAdapterPosition()).checked)
            {
                checkbox.setChecked(false);
                filterLists.get(getAdapterPosition()).checked = false;
            }else {
                checkbox.setChecked(true);
                filterLists.get(getAdapterPosition()).checked = true
                ;
            }
        }
    }

    public ArrayList<String> getCheckedIds(){
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i = 0; i < filterLists.size() ; i++) {
            if(filterLists.get(i).checked)
            {
                stringArrayList.add(filterLists.get(i).id);
            }
        }
        return stringArrayList;
    }
}

