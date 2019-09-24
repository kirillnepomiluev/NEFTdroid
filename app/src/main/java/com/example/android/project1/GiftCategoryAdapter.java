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

public class GiftCategoryAdapter extends RecyclerView.Adapter <GiftCategoryAdapter.GiftCategoryViewHolder> {

    private List <GiftCategory> giftCategories;
    private OnGiftCategoryClickListener onGiftCategoryClickListener;

    public GiftCategoryAdapter(List<GiftCategory> giftCategories, OnGiftCategoryClickListener onGiftCategoryClickListener) {
        this.onGiftCategoryClickListener = onGiftCategoryClickListener;
        this.giftCategories = giftCategories;
    }

    @NonNull
    @Override
    public GiftCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new GiftCategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_gift_category, parent, false));
    }

    @Override
    public void onBindViewHolder(GiftCategoryViewHolder holder, int position) {

        GiftCategory giftCategory = giftCategories.get(position);

        Picasso.get().load(giftCategory.getPicture())
                .error(R.drawable.ic_insert_photo_black_24dp)
                .placeholder(R.drawable.ic_insert_photo_black_24dp)
                .into(holder.pic);
        holder.titleText.setText(giftCategory.getTitle());

    }



    @Override
    public int getItemCount() {
        return giftCategories.size();
    }

    public class GiftCategoryViewHolder extends RecyclerView.ViewHolder {

        protected ImageView pic;
        protected TextView titleText;
        protected LinearLayout giftCard;

        public GiftCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            this.pic = itemView.findViewById(R.id.gift_category_pic);
            this.titleText = itemView.findViewById(R.id.gift_category_tv);
            this.giftCard = itemView.findViewById(R.id.gift_card_category);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GiftCategory giftCategory = giftCategories.get(getLayoutPosition());
                    onGiftCategoryClickListener.onGiftCategoryClick(giftCategory);
                }
            });

        }

    }
    public interface OnGiftCategoryClickListener {
        void onGiftCategoryClick(GiftCategory giftCategory);
    }
}
