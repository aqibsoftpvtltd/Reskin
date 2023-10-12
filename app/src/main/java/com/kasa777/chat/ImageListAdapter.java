package com.kasa777.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.kasa777.R;
import com.kasa777.chat.ui.FullScreenImageActivity;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {
    public Context context;
    public List<String> imagelist;
    public ViewPager viewPager;


    public ImageListAdapter(Context context, List<String> imagelist, ViewPager viewPager) {
        this.context = context;
        this.imagelist = imagelist;
        this.viewPager = viewPager;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View rootView = li.inflate(R.layout.layout_multi_image, null);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(context)
                .load(imagelist.get(position))
                .placeholder(R.drawable.image_placeholder)
                .into(holder.image_view);
        if(position== FullScreenImageActivity.currentPosition)
        {
            holder.rl_selected.setBackground(context.getResources().getDrawable(R.drawable.image_selected));
        }else {
            holder.rl_selected.setBackground(null);
        }
    }

    @Override
    public int getItemCount() {
        return imagelist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rl_selected;
        ImageView image_view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rl_selected = itemView.findViewById(R.id.rl_selected);
            image_view = itemView.findViewById(R.id.image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FullScreenImageActivity.currentPosition = getAdapterPosition();
                    viewPager.setCurrentItem(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }

    }


}
