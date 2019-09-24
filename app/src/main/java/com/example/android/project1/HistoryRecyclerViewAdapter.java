package com.example.android.project1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.CustomViewHolder> {
    private List<Operation> feedItemList;
    private Context mContext;
    private OnItem2ClickListener onItemClickListener;
    private DateFormat df;

    public HistoryRecyclerViewAdapter(Context context, List<Operation> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        df=new SimpleDateFormat("HH:mm d-M-yy");
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        final Operation feedItem = feedItemList.get(i);
        final int n = i;

        Date date= new Date(feedItem.getTime());
        customViewHolder.textView.setText(feedItem.getName());
        customViewHolder.textView2.setText((feedItem.getPlus())?"Доход +"+feedItem.getAmount()+"      "+df.format(date):"Расход -"+feedItem.getAmount()+"      "+df.format(date));
        customViewHolder.textView2.setTextColor(ContextCompat.getColor(mContext,R.color.white));
        customViewHolder.textView2.setBackgroundResource(feedItem.getPlus()? R.color.colorPrimary:R.color.colorAccent);
        if (feedItem.getPlus()) {
            customViewHolder.imageView.setImageResource(R.drawable.ic_action_plus);
        } else {
            customViewHolder.imageView.setImageResource(R.drawable.ic_action_minus);
        }

        customViewHolder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               customViewHolder.details.setVisibility(View.GONE);
               customViewHolder.operId.setText("ID операции: " + feedItem.getId());
               customViewHolder.operId.setVisibility(View.VISIBLE);
                if (feedItem.getSenderPhone() != null) {
                    customViewHolder.senderPhone.setText(feedItem.getSenderPhone());
                    customViewHolder.senderPhone.setVisibility(View.VISIBLE);
                }
               if (feedItem.getDescription() != null) {
                   customViewHolder.senderMessage.setText(feedItem.getDescription());
                   customViewHolder.senderMessage.setVisibility(View.VISIBLE);
               }
               customViewHolder.noDetails.setVisibility(View.VISIBLE);
            }
        });

        customViewHolder.noDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customViewHolder.operId.setVisibility(View.GONE);
                customViewHolder.senderMessage.setVisibility(View.GONE);
                customViewHolder.senderPhone.setVisibility(View.GONE);
                customViewHolder.noDetails.setVisibility(View.GONE);
                customViewHolder.details.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public OnItem2ClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItem2ClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView textView, textView2, details, operId, senderPhone, senderMessage, noDetails;
        protected RelativeLayout layout;

        public CustomViewHolder(View view) {
            super(view);
            this.layout=  view.findViewById(R.id.itemlayout);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
            this.textView = (TextView) view.findViewById(R.id.title);
            this.textView2 = (TextView) view.findViewById(R.id.phone);
            this.details = view.findViewById(R.id.details);
            this.operId = view.findViewById(R.id.oper_id);
            this.senderPhone = view.findViewById(R.id.sender_phone);
            this.senderMessage = view.findViewById(R.id.sender_message);
            this.noDetails = view.findViewById(R.id.no_details);
        }
    }
}
