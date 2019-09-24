package com.example.android.project1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PresentAdapter extends RecyclerView.Adapter <PresentAdapter.PresentViewHolder> {

    private List <Present> presentList;
    private OnPresentClickListener onPresentClickListener;

    public PresentAdapter(List<Present> presentList, OnPresentClickListener onPresentClickListener) {
        this.onPresentClickListener = onPresentClickListener;
        this.presentList = presentList;
    }

    @NonNull
    @Override
    public PresentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new PresentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_present, parent, false));
    }

    @Override
    public void onBindViewHolder(PresentViewHolder holder, int position) {

        Present present = presentList.get(position);

        Picasso.get().load(present.getPicUrl())
                .error(R.drawable.ic_insert_photo_black_24dp)
                .placeholder(R.drawable.ic_insert_photo_black_24dp)
                .into(holder.pic);
        holder.titleText.setText(present.getTitle());
        holder.validityDate.setText(present.getValidityDate());
        if (present.getReceiverId().equals(present.getSendorId())){
            holder.fromLayout.setVisibility(View.GONE);

        } else if (present.getReceiverId().equals(Buffer.user.getUseruid())) {
            holder.fromLayout.setVisibility(View.VISIBLE);
            holder.fromOrTo.setText("От кого: ");
            holder.userName.setText(present.getSendorName());
        } else {
            holder.fromLayout.setVisibility(View.VISIBLE);
            holder.fromOrTo.setText("Кому: ");
            holder.userName.setText(present.getReceiverName());
        }
        if (present.getWishes().equals("")){
            holder.wishesLayout.setVisibility(View.GONE);
        } else {
            holder.wishesLayout.setVisibility(View.VISIBLE);
            holder.wishes.setText(present.getWishes());
        }
    }



    @Override
    public int getItemCount() {
        return presentList.size();
    }

    public class PresentViewHolder extends RecyclerView.ViewHolder {

        protected ImageView pic;
        protected TextView titleText, fromOrTo, userName, wishes, validityDate;
        protected LinearLayout fromLayout, wishesLayout;
        protected RelativeLayout giftCard;

        public PresentViewHolder(@NonNull View itemView) {
            super(itemView);

            this.pic = itemView.findViewById(R.id.gift_pic);
            this.titleText = itemView.findViewById(R.id.gift_title);
            this.fromOrTo = itemView.findViewById(R.id.from_or_to);
            this.userName = itemView.findViewById(R.id.name_text);
            this.wishes = itemView.findViewById(R.id.wishes_text);
            this.validityDate = itemView.findViewById(R.id.validity_date);
            this.giftCard = itemView.findViewById(R.id.gift_card);
            this.wishesLayout = itemView.findViewById(R.id.wishes_layout);
            this.fromLayout = itemView.findViewById(R.id.from_or_to_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Present present = presentList.get(getLayoutPosition());
                    onPresentClickListener.onPresentClick(present);
                }
            });

        }

    }
    public interface OnPresentClickListener {
        void onPresentClick(Present present);
    }
}
