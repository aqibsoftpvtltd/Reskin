package com.kasa777.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kasa777.R;
import com.kasa777.databinding.RowNavigationVerticalBinding;
import com.kasa777.interfaces.AdapterClickListener;
import com.kasa777.modal.NavigationItemModal;
import com.kasa777.utils.Helper;

import java.util.ArrayList;
import java.util.List;

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
