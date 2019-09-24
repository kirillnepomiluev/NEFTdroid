package com.example.android.project1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

public class BottomMenu {
    public static void setBottomMenuListener(Activity activity) {
        View.OnClickListener bottommenulistener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomMenuClick(view.getContext(), view);
            }
        };

        ((LinearLayout) activity.findViewById(R.id.home_btn)).setOnClickListener(bottommenulistener);
        ((LinearLayout) activity.findViewById(R.id.history_btn)).setOnClickListener(bottommenulistener);
        ((LinearLayout) activity.findViewById(R.id.scan_pay_btn)).setOnClickListener(bottommenulistener);
        ((LinearLayout) activity.findViewById(R.id.cards_btn)).setOnClickListener(bottommenulistener);
        ((LinearLayout) activity.findViewById(R.id.chat_btn)).setOnClickListener(bottommenulistener);
    }


    public static void bottomMenuClick(Context context, View view) {
        switch (view.getId()) {
            case R.id.home_btn:
                context.startActivity(new Intent(context, MainActivity.class));
                break;
            case R.id.history_btn:
                context.startActivity(new Intent(context, HistoryActivity.class));
                break;
            case R.id.scan_pay_btn:
                context.startActivity(new Intent(context, ScannerActivity.class));
                break;
            case R.id.cards_btn:
                break;
            case R.id.chat_btn:
                context.startActivity(new Intent(context, ChatBoxActivity.class));
                break;

        }
    }
}
