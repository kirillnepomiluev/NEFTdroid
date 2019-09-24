package com.example.android.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class PresentActivity extends AppCompatActivity {

    private Present present;
    private ImageButton closeBtn;
    private ImageView picPresent;
    private TextView title,conditions, validityDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present);

        present = Buffer.present;

        closeBtn = (ImageButton) findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (present.getReceiverId().equals(Buffer.user.getUseruid())) {
                    Intent intent = new Intent(PresentActivity.this, GiftsForMeActivity.class);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(PresentActivity.this, GiftsFromMeActivity.class);
                    startActivity(intent);

                }
            }
        });

        picPresent = (ImageView) findViewById(R.id.pic_gift);
        Picasso.get().load(present.getPicUrl()).into(picPresent);
        title = (TextView) findViewById(R.id.title_gift);
        title.setText(present.getTitle());
        conditions = (TextView) findViewById(R.id.conditions_of_use);
        conditions.setText(present.getConditions());
        validityDate = (TextView) findViewById(R.id.validity_gift_date);
        validityDate.setText(present.getValidityDate());



    }
 /*  @Override
    protected void onResume() {
        super.onResume();
        long currentTime = new Date().getTime();
        long timeDifference = currentTime - Buffer.stopTime;
        if (timeDifference >= 180000 || !Buffer.checkPassword){
            startActivity(new Intent(EmailActivity.this, SighInActivity.class));
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Buffer.setStopTime();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Buffer.checkPassword = false;
    }
*/
}
