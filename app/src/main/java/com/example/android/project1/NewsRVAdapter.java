package com.example.android.project1;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class NewsRVAdapter extends RecyclerView.Adapter<NewsRVAdapter.NewsViewHolder> {

    private List<News> newsList;
    private Context context;
    //private TextView likeImg, dislikeImg;
    private DatabaseReference mydatabase;
    public NewsRVAdapter(List newsList) {
        this.newsList = newsList;
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news, viewGroup, false);
        NewsViewHolder nvh = new NewsViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder newsViewHolder, int i) {
        final NewsViewHolder innerNewsViewHolder = newsViewHolder;
        TextView onBindLikeImg = newsViewHolder.likeImg;
        TextView onBindDislikeImg = newsViewHolder.dislikeImg;
        newsViewHolder.date.setText(newsList.get(i).getDate());
        newsViewHolder.title.setText(newsList.get(i).getTitle());
        newsViewHolder.text.setText(newsList.get(i).getText());
        Glide.with(context)
                .load(newsList.get(i).getImage())
                .into(newsViewHolder.image);
        final int item = i;
        mydatabase= FirebaseDatabase.getInstance().getReference();
        onBindLikeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydatabase.child("/news/" + newsList.get(item).getNewsId() + "/likes/" + Buffer.user.getUseruid()).setValue("");
                if (innerNewsViewHolder.likeCount.getCurrentTextColor()==Color.RED) {
                    mydatabase.child("/news/" + newsList.get(item).getNewsId() + "/likes/" + Buffer.user.getUseruid()).removeValue();
                }
                mydatabase.child("/news/" + newsList.get(item).getNewsId() + "/dislikes/" + Buffer.user.getUseruid()).removeValue();
            }
        });
        onBindDislikeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydatabase.child("/news/" + newsList.get(item).getNewsId() + "/dislikes/" + Buffer.user.getUseruid()).setValue("");
                if (innerNewsViewHolder.dislikeCount.getCurrentTextColor()==Color.RED) {
                    mydatabase.child("/news/" + newsList.get(item).getNewsId() + "/dislikes/" + Buffer.user.getUseruid()).removeValue();
                }
                mydatabase.child("/news/" + newsList.get(item).getNewsId() + "/likes/" + Buffer.user.getUseruid()).removeValue();
            }
        });

        mydatabase.child("/news/" + newsList.get(item).getNewsId() + "/likes/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String likesCount = Long.toString(dataSnapshot.getChildrenCount());
                innerNewsViewHolder.likeCount.setText(likesCount);
                List<String> usersLikes = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String userLike = postSnapshot.getKey();
                    usersLikes.add(userLike);
                }
                if (usersLikes.contains(Buffer.user.getUseruid())) {
                    innerNewsViewHolder.likeCount.setTextColor(Color.RED);
                    innerNewsViewHolder.likeImg.setTextColor(Color.RED);
                } else {
                    innerNewsViewHolder.likeCount.setTextColor(Color.WHITE);
                    innerNewsViewHolder.likeImg.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
        mydatabase.child("/news/" + newsList.get(item).getNewsId() + "/dislikes/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dislikesCount = Long.toString(dataSnapshot.getChildrenCount());
                innerNewsViewHolder.dislikeCount.setText(dislikesCount);
                List<String> usersDislikes = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String userDislike = postSnapshot.getKey();
                    usersDislikes.add(userDislike);
                }
                if (usersDislikes.contains(Buffer.user.getUseruid())) {
                    //Log.v("My Project", "usersLikes.contains UseruID");
                    innerNewsViewHolder.dislikeCount.setTextColor(Color.RED);
                    innerNewsViewHolder.dislikeImg.setTextColor(Color.RED);
                }else {
                    innerNewsViewHolder.dislikeCount.setTextColor(Color.WHITE);
                    innerNewsViewHolder.dislikeImg.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView title;
        private TextView text;
        private ImageView image;
        private TextView likeCount, dislikeCount;
        private TextView likeImg, dislikeImg;
        public NewsViewHolder(View view) {
            super(view);

            date = (TextView)itemView.findViewById(R.id.date_news);
            title = (TextView)itemView.findViewById(R.id.title_news);
            text = (TextView)itemView.findViewById(R.id.text_news);
            image = (ImageView)itemView.findViewById(R.id.image_news);
            likeCount = itemView.findViewById(R.id.like_count);
            dislikeCount = itemView.findViewById(R.id.dislike_count);
            likeImg = itemView.findViewById(R.id.like_img);
            dislikeImg = itemView.findViewById(R.id.dislike_img);

            final Button btShowmore = itemView.findViewById(R.id.btShowmore);

            btShowmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (btShowmore.getText().toString().equalsIgnoreCase(context.getString(R.string.expand)))
                    {
                        text.setMaxLines(Integer.MAX_VALUE);//your TextView
                        btShowmore.setText(R.string.roll_up);
                    }
                    else
                    {
                        text.setMaxLines(3);//your TextView
                        btShowmore.setText(context.getString(R.string.expand));
                    }
                }
            });
        }

    }



}
