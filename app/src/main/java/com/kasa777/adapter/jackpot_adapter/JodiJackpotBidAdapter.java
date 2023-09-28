package com.kasa777.adapter.jackpot_adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.kasa777.R;
import com.kasa777.constant.Constant;
import com.kasa777.modal.game_bid_data.BidData;
import com.kasa777.utils.Alerts;
import com.kasa777.utils.AppPreference;
import com.kasa777.utils.ItemOnclickListener;
import com.kasa777.utils.OnItemTextChangedListener;

import java.util.List;

public class JodiJackpotBidAdapter extends RecyclerView.Adapter<JodiJackpotBidAdapter.ViewHolder> {
    private Context mContext;
    private List<BidData> bidItems;
    private String[] etValArr;
    private ItemOnclickListener onClickListener;
    private OnItemTextChangedListener onTextChaged;
    private Activity activity;
    private int bidAddedPoints = 0;

    public JodiJackpotBidAdapter(Context mContext, List<BidData> bidItems, ItemOnclickListener onClickListener, OnItemTextChangedListener onTextChaged, Activity activity) {
        this.mContext = mContext;
        this.bidItems = bidItems;
        this.onClickListener = onClickListener;
        this.onTextChaged=onTextChaged;
        etValArr = new String[bidItems.size()];
        this.activity = activity;
    }

    @NonNull
    @Override
    public JodiJackpotBidAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View rootView = li.inflate(R.layout.row_jodi_jackpot, null);
        return new ViewHolder(rootView, new CustomEtListener());
    }

    @Override
    public void onBindViewHolder(@NonNull JodiJackpotBidAdapter.ViewHolder holder, int position) {
        holder.tvDigits.setText(bidItems.get(position).getDigits());

        holder.myCustomEditTextListener.updatePosition(position);
        holder.etPoints.setText(bidItems.get(position).getPoints());
        holder.btnDelete.setTag(position);
        holder.etPoints.setTag(position);
//        holder.btnDelete.setOnClickListener(view -> onClickListener.onItemClicked(view,position));
    }

    @Override
    public int getItemCount() {
        return bidItems.size();
    }

  public    class ViewHolder extends RecyclerView.ViewHolder {
      private AppCompatImageView btnDelete;
        private TextView tvDigits;
        public EditText etPoints;
        private LinearLayout navLayout;
        CustomEtListener myCustomEditTextListener;

        ViewHolder(@NonNull View itemView, CustomEtListener myList) {
            super(itemView);
            tvDigits = itemView.findViewById(R.id.tvDigits);
            etPoints = itemView.findViewById(R.id.etPoints);
            myCustomEditTextListener = myList;
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnDelete.setOnClickListener(view -> onClickListener.onItemClicked(view,getAdapterPosition()));
            etPoints.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    onTextChaged.onItemTextChanged(getAdapterPosition(), charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

        }
    }


    private class CustomEtListener implements TextWatcher {
        private int position;

        /**
         * Updates the position according to onBindViewHolder
         *
         * @param position - position of the focused item
         */
        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            etValArr[position] = charSequence.toString();
            if (etValArr[position] != null) {
                bidItems.get(position).setPoints(etValArr[position]);
                if (!etValArr[position].isEmpty()) {
                    bidAddedPoints = bidAddedPoints + Integer.parseInt(etValArr[position]);
                    int wallet = AppPreference.getIntegerPreference(mContext, Constant.USER_WALLET_BALANCE);
                    if (wallet < bidAddedPoints) {
                        Alerts.show(mContext, "Insufficient wallet balance");
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }


}
