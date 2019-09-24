package com.example.android.project1;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

public class BottomSheet extends BottomSheetDialogFragment {

    protected ImageView imageView;
    protected TextView title;
    protected TextView phone;
    protected TextView address;
    protected TextView info;
    private MapActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout,container, false);
        this.title=(TextView) view.findViewById(R.id.title);
        this.imageView = (ImageView) view.findViewById(R.id.pic);
        this.phone = (TextView) view.findViewById(R.id.phone);
        this.address = (TextView) view.findViewById(R.id.address);
        this.info = (TextView) view.findViewById(R.id.info);

        GasStation gasStation = Buffer.gasStation;

        if (!TextUtils.isEmpty(gasStation.getPhotoURL())) {
            String sr = gasStation.getPhotoURL();


            Picasso.get().load(sr)
                    .error(R.drawable.ic_insert_photo_black_24dp)
                    .placeholder(R.drawable.ic_insert_photo_black_24dp)
                    .into(imageView);
        }

        title.setText(gasStation.getTitle());
        title.setTextIsSelectable(true);
        phone.setText(gasStation.getPhone());
        phone.setTextIsSelectable(true);
        address.setText(gasStation.getAddress());
        address.setTextIsSelectable(true);
        info.setText(gasStation.getInfo());
        info.setTextIsSelectable(true);


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (context instanceof MapActivity) {
            activity = (MapActivity) context;

        }
    }
}
