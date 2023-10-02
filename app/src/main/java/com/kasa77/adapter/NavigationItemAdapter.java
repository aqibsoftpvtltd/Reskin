package com.kasa77.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa77.R;
import com.kasa77.databinding.RowNavigationVerticalBinding;
import com.kasa77.interfaces.AdapterClickListener;
import com.kasa77.modal.NavigationItemModal;

import java.util.ArrayList;

public class NavigationItemAdapter extends RecyclerView.Adapter<NavigationItemAdapter.ViewHolder> {

    private ArrayList<NavigationItemModal> list;
    AdapterClickListener listener;

    public NavigationItemAdapter(ArrayList<NavigationItemModal> list,AdapterClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NavigationItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowNavigationVerticalBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.row_navigation_vertical, parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NavigationItemAdapter.ViewHolder holder, int position) {
        NavigationItemModal navModal = list.get(position);

        holder.binding.navName.setText(navModal.getName());
        holder.binding.navDetails.setText(navModal.getDetails());
        holder.binding.navIcon.setImageDrawable(ContextCompat.getDrawable(holder.binding.getRoot().getContext(),navModal.getImage()));

        holder.bind(position,listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RowNavigationVerticalBinding binding;

        public ViewHolder( RowNavigationVerticalBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bind(int position, AdapterClickListener listener)
        {
            binding.navLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onAdapterClick(position,view);
                }
            });
        }

    }

}
