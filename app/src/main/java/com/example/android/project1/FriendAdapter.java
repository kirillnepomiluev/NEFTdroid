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

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class FriendAdapter extends RecyclerView.Adapter <com.example.android.project1.FriendAdapter.FriendViewHolder> {

        private List<User> friendList;



        public FriendAdapter(List<User> friendList) {
            this.friendList = friendList;
        }

        @NonNull
        @Override
        public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FriendViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_friend, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final FriendAdapter.FriendViewHolder holder, int position) {
            User friend = friendList.get(position);
            int partners = 0;
            String sponsorName = new String();
            for (User user : Buffer.allUsers){
                if (user.getSponsorid().equals(friend.getUseruid()))
                    partners++;
                if (user.getSponsor2id()!= null && user.getSponsor2id().equals(friend.getUseruid()))
                    partners++;
                if (user.getSponsor3id()!= null && user.getSponsor3id().equals(friend.getUseruid()))
                    partners++;
                if (user.getUseruid().equals(friend.getSponsorid()))
                    sponsorName = user.getFirstname() + " " + user.getLastname();

            }
            Picasso.get().load(friend.getPhotoUrl())
                    .error(R.drawable.profile_user)
                    .placeholder(R.drawable.profile_user)
                    .transform(new CropCircleTransformation())
                    .into(holder.photo);
            holder.name.setText(String.format("%s %s", friend.getFirstname(), friend.getLastname()));
            holder.address.setText(String.format("%s, %s", friend.getCountry(), friend.getCity()));
            if (Buffer.user.getUseruid().equals(friend.getSponsorid()))
            holder.level.setText("1");
            if (Buffer.user.getUseruid().equals(friend.getSponsor2id()))
                holder.level.setText("2");
            if (Buffer.user.getUseruid().equals(friend.getSponsor3id()))
                holder.level.setText("3");
            holder.dateReg.setText(friend.getDateOfReg());
            holder.sponsor.setText(sponsorName);
            holder.phone.setText(friend.getPhone());
            holder.partner.setText(partners+ "");

            holder.friendLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.infoLayout.getVisibility() == View.GONE) {
                        holder.infoLayout.setVisibility(View.VISIBLE);

                    }else if (holder.infoLayout.getVisibility() == View.VISIBLE){
                        holder.infoLayout.setVisibility(View.GONE);
                    }
                }
            });

            holder.infoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.infoLayout.setVisibility(View.GONE);
                }
            });

        }

        @Override
        public int getItemCount() {
            return friendList.size();
        }

        public class FriendViewHolder extends RecyclerView.ViewHolder {

            protected TextView name, address, level, dateReg, sponsor, phone, partner;
            protected ImageView photo;
            protected RelativeLayout friendLayout;
            protected LinearLayout infoLayout;

            public FriendViewHolder(@NonNull View itemView) {
                super(itemView);

                this.friendLayout = itemView.findViewById(R.id.friend_layout);
                this.name = itemView.findViewById(R.id.name);
                this.address = itemView.findViewById(R.id.address);
                this.level = itemView.findViewById(R.id.level);
                this.infoLayout = itemView.findViewById(R.id.info_layout);
                this.dateReg = itemView.findViewById(R.id.dateReg);
                this.sponsor = itemView.findViewById(R.id.sponsor);
                this.phone = itemView.findViewById(R.id.phone);
                this.partner = itemView.findViewById(R.id.partners);
                this.photo = itemView.findViewById(R.id.photo);
            }
        }
    }
