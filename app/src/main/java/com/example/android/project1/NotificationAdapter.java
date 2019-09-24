package com.example.android.project1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notifList;
    private Context context;
    private Calendar calendar;

    public NotificationAdapter (List newsList) {
        this.notifList = newsList;
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notifications_list, viewGroup, false);
        NotificationViewHolder nvh = new NotificationViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.NotificationViewHolder notifViewHolder, int i) {
        notifViewHolder.title.setText(notifList.get(i).getName());
        if (notifList.get(i).getDescr()!=null)  {
            notifViewHolder.descr.setVisibility(View.VISIBLE);
        }
            calendar = new GregorianCalendar();
            calendar.setTimeInMillis(notifList.get(i).getTime());
            notifViewHolder.date.setText(calendar.getTime().toString());

    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView descr;
        private TextView date;

        public NotificationViewHolder(View view) {
            super(view);
            title = (TextView)itemView.findViewById(R.id.notif_title);
            descr = (TextView)itemView.findViewById(R.id.notif_descr);
            date = (TextView)itemView.findViewById(R.id.notif_date);
        }
    }
}
