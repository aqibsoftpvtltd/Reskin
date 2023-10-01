package com.kasa77.adapter.history_adapter_pagination;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa77.R;
import com.kasa77.modal.history_pagination.RecordsItem;
import com.kasa77.utils.DateFormatToDisplay;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BidHistoryPaginationAdapter extends RecyclerView.Adapter<BidHistoryPaginationAdapter.MyViewHolder> {

    private Context mContext;
    private List<RecordsItem> recordsItems;

    public BidHistoryPaginationAdapter(Context mContext, List<RecordsItem> recordsItems) {
        this.mContext = mContext;
        this.recordsItems = recordsItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bid_history, parent, false));
    }


    @Override
    public int getItemCount() {
        return recordsItems == null ? 0 : recordsItems.size();
    }

    public void addItems(List<RecordsItem> postItems) {
        int lastItem = recordsItems.size();
        recordsItems.addAll(postItems);
        notifyItemRangeInserted(lastItem, postItems.size() - 1);
    }


    public void clear() {
        recordsItems.clear();
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecordsItem datum = recordsItems.get(position);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy hh:mm a");



        Date createdDateDay = null;
        try {
            createdDateDay = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").parse(datum.createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String gameDate = new DateFormatToDisplay().parseDateToddMMyyyy(datum.gameDate);
        String playOnDate = sdf.format(createdDateDay);

        Date dt1= null;
        Date dt2= null;
        Date dt3= null;
        String gameDateDay= null;
        String playOnDateDay= null;
        try {
            dt1 = sdf.parse(gameDate);
            DateFormat format2=new SimpleDateFormat("EEEE");
             gameDateDay=format2.format(dt1);

            dt2 = sdf.parse(playOnDate);
            DateFormat format3=new SimpleDateFormat("EEEE");
             playOnDateDay=format3.format(dt2);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        holder.tv_bidtime.setText( fmtOut.format(createdDateDay));

        holder.tvProviderName.setText(datum.providerName +"("+ datum.gameTypeName+","+ datum.gameSession+")");
        holder.tvGameDate.setText(playOnDate + "("+playOnDateDay+")");
        holder.tvPlayFor.setText(gameDate + "("+gameDateDay+")");
        if (datum.winStatus == 0) {
            holder.imgStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_wait));
            holder.tvWinStatus.setText("Bid Placed Successfully, Wait For The Result!!!");
        }
        if (datum.winStatus == 1) {
            holder.imgStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_excited));
            holder.tvWinStatus.setText("You Won Rs. " + datum.gameWinPoints.toString());
        }
        if (datum.winStatus == 2) {
            holder.imgStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_unhappy));
            holder.tvWinStatus.setText("Better Luck Next Time!!!");
        }
        if (datum.winStatus == 5) {
            holder.imgStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_excited));
            holder.tvWinStatus.setText("Game Declined Payment Refunded!!!");
        }
        holder.tvBidDigit.setText(datum.bidDigit);
        holder.tvBidPoint.setText(datum.biddingPoints + "");
        holder.tvBidId.setText(datum.id);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvProviderName, tvBidId, tvPlayFor,  tvGameDate, tvWinStatus, tvName,
                tvBidDigit, tvBidPoint,tv_bidtime;

        private ImageView imgStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            tvName = itemView.findViewById(R.id.tvName);
            tvProviderName = itemView.findViewById(R.id.tvProviderName);
//            tvGameType = itemView.findViewById(R.id.tvGameType);
            tvGameDate = itemView.findViewById(R.id.tvGameDate);
            tvWinStatus = itemView.findViewById(R.id.tvWinStatus);
            tvBidDigit = itemView.findViewById(R.id.tvBidDigit);
            tvBidPoint = itemView.findViewById(R.id.tvBidPoint);
//            tvBidTime = itemView.findViewById(R.id.tvBidTime);
            tvBidId = itemView.findViewById(R.id.tvBidId);
            tvPlayFor = itemView.findViewById(R.id.tvPlayFor);
            tv_bidtime = itemView.findViewById(R.id.tv_bidtime);
        }


    }


}
