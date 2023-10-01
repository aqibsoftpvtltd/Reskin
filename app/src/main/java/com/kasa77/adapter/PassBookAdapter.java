package com.kasa77.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa77.R;
import com.kasa77.modal.transaction_statement.RecordsItem;

import java.util.List;

public class PassBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<RecordsItem> recordsItems;
    private OnDropActionListner onDropActionListner;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private int expandedposition = -1;

    public PassBookAdapter(Context mContext, List<RecordsItem> recordsItems, OnDropActionListner onDropActionListner) {
        this.mContext = mContext;
        this.recordsItems = recordsItems;
        this.onDropActionListner = onDropActionListner;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_passbook_new, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }

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
    public int getItemViewType(int position) {
        return recordsItems.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof MyViewHolder) {
            populateItemRows((MyViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    @SuppressLint("SetTextI18n")
    private void populateItemRows(MyViewHolder holder, int position) {
        RecordsItem datum = recordsItems.get(position);
        holder.tv_pamount.setText("₹ " + datum.previousAmount.toString());
        holder.tv_camount.setText("₹ " + datum.currentAmount.toString());
        holder.tv_description.setText(datum.description);
        holder.tv_date.setText(datum.transactionDate);


//        if (datum.isExpand()) {
//            datum.setExpand(true);
//            holder.rl_inner.setVisibility(View.VISIBLE);
//            crossfade(holder, datum);
//
//        } else {
//            datum.setExpand(false);
//            holder.rl_inner.setVisibility(View.GONE);
//
//        }
        if (position == expandedposition) {
            holder.rl_inner.setVisibility(View.VISIBLE);
            crossfade(holder, datum);
        } else {
            holder.rl_inner.setVisibility(View.GONE);
        }
        holder.tv_amount.setTextColor(mContext.getResources().getColor(R.color.blue));
        holder.tv_amount.setText("₹" + datum.transactionAmount.toString());
        if (datum.filterType != null)
            switch (datum.filterType) {
                case 0:
                case 9:
                case 5:
                case 7:
                case 8:
                case 2:
                    holder.tv_amount.setTextColor(mContext.getResources().getColor(R.color.red));
                    holder.tv_amount.setText("-₹" + datum.transactionAmount.toString());
                    break;
                case 1:
                case 4:
                case 3:
                    holder.tv_amount.setTextColor(mContext.getResources().getColor(R.color.green));
                    holder.tv_amount.setText("+₹" + datum.transactionAmount.toString());
                    break;
                case 6:
                    holder.tv_amount.setTextColor(mContext.getResources().getColor(R.color.blue));
                    holder.tv_amount.setText("(₹" + datum.transactionAmount.toString() + ")");
                    break;

            }


        holder.rl_outer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (expandedposition >= 0) {
                    int prev = expandedposition;
                    recordsItems.get(prev).setExpand(false);
                    notifyItemChanged(prev);
                }
                expandedposition = holder.getAdapterPosition();
                if (!datum.isExpand()) {

                    datum.setExpand(true);
                    holder.rl_inner.setVisibility(View.VISIBLE);

                    crossfade(holder, datum);

                } else {
                    datum.setExpand(false);
                    holder.rl_inner.setVisibility(View.GONE);

                }
            }
        });
    }


    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_id, tv_amount, tv_description, tv_status, tv_date, tv_pamount, tv_camount;
        public ImageView iv_action;
        public RelativeLayout rl_inner;
        public ProgressBar loading_spinner;
        public LinearLayout rl_outer, content_bank, contentBidPlaced, contentBidWon, contentCredit, contentNoData;

        public TextView tvBidPlacedStatus, tvBidPlacedPlayedFor, tvBidPlacedDateTime, tvBidPlacedProviderName, tvBidPlacedGameName, tvBidPlacedType;
        public TextView tvBidWonStatus, tvBidWonPlayedFor, tvBidWonDateTime, tvBidWonProviderName, tvBidWonGameName, tvBidWonType, tvBidWonWinRatio, tvBidWonBidAmount;
        public TextView tvAccountHolder, tvstatus, tvdatetime, tvIFSC, tvAccountNo, tvBank;
        public TextView tvCreditStatus, tvCreditDateTime, tvCreditTransId, tvCreditAmount, tvCreditFundMode;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_id = itemView.findViewById(R.id.tv_id);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_pamount = itemView.findViewById(R.id.tv_pamount);
            tv_camount = itemView.findViewById(R.id.tv_camount);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_action = itemView.findViewById(R.id.iv_action);
            rl_outer = itemView.findViewById(R.id.rl_outer);
            rl_inner = itemView.findViewById(R.id.rl_inner);
            loading_spinner = itemView.findViewById(R.id.loading_spinner);


            contentNoData = itemView.findViewById(R.id.contentNoData);

            //withdraw request
            content_bank = itemView.findViewById(R.id.content_bank);
            tvAccountHolder = itemView.findViewById(R.id.tvAccountHolder);
            tvstatus = itemView.findViewById(R.id.tvstatus);
            tvdatetime = itemView.findViewById(R.id.tvdatetime);
            tvIFSC = itemView.findViewById(R.id.tvIFSC);
            tvAccountNo = itemView.findViewById(R.id.tvAccountNo);
            tvBank = itemView.findViewById(R.id.tvBank);

            //BId placed
            contentBidPlaced = itemView.findViewById(R.id.contentBidPlaced);
            tvBidPlacedType = itemView.findViewById(R.id.tvBidPlacedType);
            tvBidPlacedGameName = itemView.findViewById(R.id.tvBidPlacedGameName);
            tvBidPlacedProviderName = itemView.findViewById(R.id.tvBidPlacedProviderName);
            tvBidPlacedDateTime = itemView.findViewById(R.id.tvBidPlacedDateTime);
            tvBidPlacedPlayedFor = itemView.findViewById(R.id.tvBidPlacedPlayedFor);
            tvBidPlacedStatus = itemView.findViewById(R.id.tvBidPlacedStatus);

            // bid won
            contentBidWon = itemView.findViewById(R.id.contentBidWon);
            tvBidWonType = itemView.findViewById(R.id.tvBidWonType);
            tvBidWonGameName = itemView.findViewById(R.id.tvBidWonGameName);
            tvBidWonProviderName = itemView.findViewById(R.id.tvBidWonProviderName);
            tvBidWonDateTime = itemView.findViewById(R.id.tvBidWonDateTime);
            tvBidWonPlayedFor = itemView.findViewById(R.id.tvBidWonPlayedFor);
            tvBidWonStatus = itemView.findViewById(R.id.tvBidWonStatus);
            tvBidWonWinRatio = itemView.findViewById(R.id.tvBidWonWinRatio);
            tvBidWonBidAmount = itemView.findViewById(R.id.tvBidWonBidAmount);


            // bid won
            contentCredit = itemView.findViewById(R.id.contentCredit);
            tvCreditFundMode = itemView.findViewById(R.id.tvCreditFundMode);
            tvCreditAmount = itemView.findViewById(R.id.tvCreditAmount);
            tvCreditTransId = itemView.findViewById(R.id.tvCreditTransId);
            tvCreditDateTime = itemView.findViewById(R.id.tvCreditDateTime);
            tvCreditStatus = itemView.findViewById(R.id.tvCreditStatus);


        }


    }


    private void crossfade(MyViewHolder holder, RecordsItem datum) {
        holder.loading_spinner.setVisibility(View.VISIBLE);

        holder.content_bank.setVisibility(View.GONE);
        holder.contentBidPlaced.setVisibility(View.GONE);
        holder.contentBidWon.setVisibility(View.GONE);
        holder.contentCredit.setVisibility(View.GONE);
        holder.contentNoData.setVisibility(View.GONE);
        ///0: Bid Placed, 1: Bid Status Won, 2: Bid Status Loss, 3: Bid Status Refund, 4: Fund Credit, 5: Fund Debit, 6 : Fund Declined, 7: Account Update Charge
        if (datum.filterType != null) {
            switch (datum.filterType) {
                case 0:
                    onDropActionListner.onBidPlaced(holder, datum);
                    break;
                case 1:
                case 2:
                case 3:
                case 8:
                    onDropActionListner.onBidWonLossRefund(holder, datum);
                    break;
                case 4:
                case 5:
                    onDropActionListner.onAddFund(holder, datum);
                    break;
                case 9:
                case 6:
                case 7:
                    onDropActionListner.onWithdrawChangeBankFund(holder, datum);
                    break;
            }
        } else {
            onDropActionListner.onNoDataFound(holder, datum);
        }
    }


    public interface OnDropActionListner {
        public void onBidPlaced(MyViewHolder viewHolder, RecordsItem datum);

        public void onBidWonLossRefund(MyViewHolder viewHolder, RecordsItem datum);

        public void onAddFund(MyViewHolder viewHolder, RecordsItem datum);

        public void onWithdrawChangeBankFund(MyViewHolder viewHolder, RecordsItem datum);

        public void onNoDataFound(MyViewHolder viewHolder, RecordsItem datum);


    }

}
