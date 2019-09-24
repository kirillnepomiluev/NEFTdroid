package com.example.android.project1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class GiftItemAdapter extends RecyclerView.Adapter <GiftItemAdapter.GiftItemViewHolder> {

    private List <GiftItem> giftItemList;
    private OnGiftItemClickListener onGiftItemClickListener;

    public GiftItemAdapter(List<GiftItem> giftItemList, OnGiftItemClickListener onGiftItemClickListener) {
        this.onGiftItemClickListener = onGiftItemClickListener;
        this.giftItemList = giftItemList;
    }

    @NonNull
    @Override
    public GiftItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new GiftItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_gift_item, parent, false));
    }

    @Override
    public void onBindViewHolder(GiftItemViewHolder holder, int position) {

        GiftItem giftItem = giftItemList.get(position);

        Picasso.get().load(giftItem.getPicUrl())
                .error(R.drawable.ic_insert_photo_black_24dp)
                .placeholder(R.drawable.ic_insert_photo_black_24dp)
                .into(holder.pic);
        holder.titleText.setText(giftItem.getTitle());
        holder.price.setText(giftItem.getPrice()+" NFT");

    }



    @Override
    public int getItemCount() {
        return giftItemList.size();
    }

    public class GiftItemViewHolder extends RecyclerView.ViewHolder {

        protected ImageView pic;
        protected TextView titleText, price;
        protected LinearLayout giftCard;

        public GiftItemViewHolder(@NonNull View itemView) {
            super(itemView);

            this.pic = itemView.findViewById(R.id.gift_pic);
            this.titleText = itemView.findViewById(R.id.gift_title);
            this.price = itemView.findViewById(R.id.gift_price);
            this.giftCard = itemView.findViewById(R.id.gift_card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GiftItem giftItem = giftItemList.get(getLayoutPosition());
                    onGiftItemClickListener.onGiftItemClick(giftItem);
                }
            });

        }

    }
    public interface OnGiftItemClickListener {
        void onGiftItemClick(GiftItem giftItem);
    }
}
