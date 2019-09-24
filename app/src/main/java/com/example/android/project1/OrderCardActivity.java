package com.example.android.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OrderCardActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton endBtn;
    private Button nextBtn, orderBtn;
    private RelativeLayout cardInfo, orderInfo;
    private LinearLayout identityLayout;
    private ImageView picCard, identityMark;
    private TextView titleCard, descriptionCard, conditionsOfUse, validityDays, userName, identityText, userPhone, dateBirth, userPassport;
    private EditText userAddress;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private DatabaseReference mydatabase;

    @Override
    protected void onStart() {
        super.onStart();


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_card);

        mydatabase = FirebaseDatabase.getInstance().getReference();


        endBtn = (ImageButton) findViewById(R.id.close_btn);
        nextBtn = (Button) findViewById(R.id.next_btn);
        orderBtn = (Button) findViewById(R.id.order_btn);
        cardInfo = (RelativeLayout) findViewById(R.id.card_layout);
        orderInfo = (RelativeLayout) findViewById(R.id.order_info);
        picCard = (ImageView) findViewById(R.id.pic_card);
        identityMark = (ImageView) findViewById(R.id.identity_mark);
        titleCard = (TextView) findViewById(R.id.title_card);
        descriptionCard = (TextView) findViewById(R.id.description_card);
        conditionsOfUse = (TextView) findViewById(R.id.conditions_of_use);
        validityDays = (TextView) findViewById(R.id.validity_card_days);
        userName = (TextView) findViewById(R.id.name_user_text);
        identityText = (TextView) findViewById(R.id.identity_text);
        userPhone = (TextView) findViewById(R.id.phonenumber_tv);
        dateBirth = (TextView) findViewById(R.id.dateBirth_tv);
        userPassport = (TextView) findViewById(R.id.passport_tv);
        userAddress = (EditText) findViewById(R.id.address_et);
        identityLayout = (LinearLayout)findViewById(R.id.identity_layout);
        mydatabase.child("gifts").child("gifts_category").child("fuelcard").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Buffer.fuelCardItem = dataSnapshot.getValue(GiftItem.class);
                if (Buffer.fuelCardItem != null){
                    Picasso.get()
                            .load(Buffer.fuelCardItem.getPicUrl())
                            .into(picCard);
                    titleCard.setText(Buffer.fuelCardItem.getTitle());
                    descriptionCard.setText(Buffer.fuelCardItem.getDescription());
                    conditionsOfUse.setText(Buffer.fuelCardItem.getConditions());
                    validityDays.setText(Buffer.fuelCardItem.getValidity()+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        cardInfo.setVisibility(View.VISIBLE);
        orderInfo.setVisibility(View.GONE);

        endBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        orderBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_btn:
                startActivity(new Intent(OrderCardActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.next_btn:
                orderInfo.setVisibility(View.VISIBLE);
                if (Buffer.user.getVerifyEnd()){
                    identityLayout.setBackground(getResources().getDrawable(R.drawable.rectangle_identity));
                    Picasso.get().load(R.drawable.checkmark)
                            .into(identityMark);
                    identityText.setText("Идентифицирован");
                }
                userName.setText(Buffer.user.getFirstname() + " " + Buffer.user.getLastname());
                userPhone.setText(Buffer.user.getPhone());
                dateBirth.setText(Buffer.user.getDatebirth());
                if (Buffer.user.getPassportnumber()!=null) {
                    userPassport.setText(Buffer.user.getPassportnumber());
                }
                userAddress.setText(Buffer.user.getAddress());
                break;
            case R.id.order_btn:
                if (userAddress.getText().toString().isEmpty()) {
                    Toast.makeText(OrderCardActivity.this, "Для заказа необходимо ввести адрес!", Toast.LENGTH_SHORT).show();

                }else {
                    String currentDate = dateFormat.format(calendar.getTime());
                    final FuelCard fuelCard = new FuelCard(Buffer.user.getUseruid(), Buffer.user.getUseruid(), userAddress.getText().toString(), currentDate);
                    Map<String, Object> updates = fuelCard.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/FuelCards/" + Buffer.user.getUseruid(), updates);
                    mydatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Buffer.fuelCard = fuelCard;
                            Toast.makeText(OrderCardActivity.this, " Заказ успешно отправлен!",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(OrderCardActivity.this, MainActivity.class));
                            finish();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(OrderCardActivity.this, " Ошибка!!! Заказ не отправлен.",
                                    Toast.LENGTH_SHORT).show();


                        }
                    });
                }


                break;
        }
    }
}
