package com.example.android.project1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GiftItemActivity extends AppCompatActivity {

    public static final String GIFT_ID = "giftId";
    private ImageView picGift;
    private TextView title, description,conditions, validityDays, validityDate, price, rules;
    private Button nextBtn, giveBtn;
    private ImageButton backBtn;
    private RelativeLayout receiverInfo;
    private EditText phoneReceiver, nameReceiver, wishes;
    private CheckBox forMeCheckBox, sendOnEmail, agreeCheckBox;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mydatabase;
    private DatabaseReference giftRef;
    private DatabaseReference userRef;
    private FirebaseUser user;
    private ArrayList<User> users;
    private static final String TAG = "ex";

    private GiftItem giftItem;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_item);

        giftItem = Buffer.giftItem;

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mydatabase = database.getReference();
        userRef = database.getReference("users");
        giftRef = database.getReference("gifts").child("gifts_categories").child(Buffer.giftCategory.getCategoryId()).child(giftItem.getGiftItemId());


        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        calendar.add(Calendar.DATE, giftItem.getValidity());


        backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GiftItemActivity.this, GiftCategoryActivity.class);
                startActivity(intent);
            }
        });

        picGift = (ImageView) findViewById(R.id.pic_gift);
        Picasso.get().load(giftItem.getPicUrl()).into(picGift);
        title = (TextView) findViewById(R.id.title_gift);
        title.setText(giftItem.getTitle());
        description = (TextView) findViewById(R.id.description_gift);
        description.setText(giftItem.getDescription());
        conditions = (TextView) findViewById(R.id.conditions_of_use);
        conditions.setText(giftItem.getConditions());
        validityDays = (TextView) findViewById(R.id.validity_gift_days);
        validityDays.setText(giftItem.getValidity() + "");
        validityDate = (TextView) findViewById(R.id.validity_gift_date);
        validityDate.setText(dateFormat.format(calendar.getTime()));
        price = (TextView) findViewById(R.id.price_gift);
        price.setText(giftItem.getPrice() + "");
        receiverInfo = (RelativeLayout) findViewById(R.id.buy_info);
        receiverInfo.setVisibility(View.GONE);
        phoneReceiver = (EditText) findViewById(R.id.editText_phone);
        nameReceiver = (EditText) findViewById(R.id.receiver_name_ed);
        wishes = (EditText) findViewById(R.id.comments_ed);
        forMeCheckBox = (CheckBox) findViewById(R.id.gift_for_me_checkbox);
        sendOnEmail= (CheckBox) findViewById(R.id.send_on_email);
        agreeCheckBox = (CheckBox) findViewById(R.id.agree);
        rules = (TextView) findViewById(R.id.rules);
        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GiftItemActivity.this, RulesActivity.class);
                startActivity(intent);
            }
        });



        giveBtn = (Button) findViewById(R.id.give_btn);


        nextBtn = (Button) findViewById(R.id.next_btn);
        nextBtn.setVisibility(View.VISIBLE);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextBtn.setVisibility(View.GONE);
                receiverInfo.setVisibility(View.VISIBLE);
                giveBtn.setVisibility(View.VISIBLE);
                giveBtn.setBackground(getResources().getDrawable(R.drawable.button_rectangle_transparent));

            }
        });

        forMeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CompoundButton) view).isChecked()) {
                    phoneReceiver.setText(Buffer.user.getPhone());
                    nameReceiver.setText(String.format("%s %s", Buffer.user.getFirstname(), Buffer.user.getLastname()));
                } else {
                    phoneReceiver.setText("+");
                    nameReceiver.setText("");
                }
            }
        });

        agreeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CompoundButton) view).isChecked()) {
                    giveBtn.setClickable(true);
                    giveBtn.setBackground(getResources().getDrawable(R.drawable.button_rectangle));
                } else {
                    giveBtn.setClickable(false);
                    giveBtn.setBackground(getResources().getDrawable(R.drawable.button_rectangle_transparent));
                }

            }
        });


        giveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFields();

            }
        });

        users = new ArrayList<>();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                users = new ArrayList<>();

                if (snapshot.exists()) {

                    for (DataSnapshot snapshot1 :
                            snapshot.getChildren()) {

                        User element = snapshot1.getValue(User.class);
                        if (element != null)

                            users.add(element);
                    }
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "loadPost:onCancelled", databaseError.toException());
            }

        });
    }

    private void checkFields() {
        if (phoneReceiver.getText().toString().isEmpty() || phoneReceiver.getText().toString().length() < 7) {
            Toast.makeText(GiftItemActivity.this, "Введите телефон получателя",
                    Toast.LENGTH_SHORT).show();
        }else {
                for (User user : users) {
                    if (user.getPhone().equals(phoneReceiver.getText().toString())) {
                        Buffer.receiver = user;
                        break;

                    }

                }

        } if (nameReceiver.getText().toString().isEmpty()) {
            Toast.makeText(GiftItemActivity.this, "Введите имя получателя",
                    Toast.LENGTH_SHORT).show();
        } else
            sendPresent();

    }
    private boolean checkBalance() {
        if (Buffer.money >= giftItem.getPrice()) {
            return true;

        }
        return false;
    }

    private void sendPresent() {
        if (Buffer.receiver == null){
            Toast.makeText(GiftItemActivity.this, "Пользователь с таким номером не найден",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (checkBalance()) {
            final Present present = new Present();
            present.setTitle(giftItem.getTitle());
            present.setSendorId(user.getUid());
            present.setGiftItemId(giftItem.getGiftItemId());
            dateFormat = new SimpleDateFormat("HH:mm");
            String time = dateFormat.format(Calendar.getInstance().getTime());
            present.setTime(time);
            present.setValidityDate(validityDate.getText().toString());
            present.setAgree(true);
            present.setWishes(wishes.getText().toString());
            present.setPresentId(user.getUid() + time);
            present.setPicUrl(giftItem.getPicUrl());
            present.setReceiverName(nameReceiver.getText().toString());
            present.setConditions(giftItem.getConditions());
            if (forMeCheckBox.isChecked()) {
                present.setReceiverId(user.getUid());
                present.setSendorName("от меня");


            } else {
                present.setReceiverId(Buffer.receiver.getUseruid());
                present.setSendorName(Buffer.user.getFirstname()+ " " + Buffer.user.getLastname());

            }
            if (sendOnEmail.isChecked()){
                if (forMeCheckBox.isChecked()){
                    SendMail.sendEmail(GiftItemActivity.this, Buffer.receiver.getEmail(),"Подарочный сертификат из приложения mWallet", "Вы приобрели подарочный серификат в приложении mWallet" );
                }else
                    SendMail.sendEmail(GiftItemActivity.this, Buffer.receiver.getEmail(),"Вы получили подарок в приложении mWallet", "Пользователь " + present.getSendorName() + "отправил Вам подарок. Зайдите в приложение, чтобы посмотреть!" );

            }
            String s = giftItem.getPrice() + "";
            final Integer refillSumm = Integer.parseInt(s);
            Operation sss = new Operation("Покупка подарка" + present.getPresentId(), false, refillSumm, Buffer.user.getUseruid());
            Map<String, Object> presentmap = present.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            Map<String, Object> sssmap = sss.toMap();

            childUpdates.put("/history/" + Buffer.user.getUseruid() + "/" + sss.getId(), sssmap);
            childUpdates.put("/users/" + Buffer.user.getUseruid() + "/money", Buffer.user.getMoney() - refillSumm);
            childUpdates.put("/presents/" + present.getReceiverId() + "/" + present.getPresentId(), presentmap);
            if (!forMeCheckBox.isChecked()) {
                childUpdates.put("/presents/" + present.getSendorId() + "/" + present.getPresentId(), presentmap);
            }

            mydatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Buffer.user.setMoney(Buffer.user.getMoney() - refillSumm);
                    Toast.makeText(GiftItemActivity.this, "Подарок успешно отправлен!", Toast.LENGTH_SHORT).show();
                    if (present.getReceiverId().equals(user.getUid())) {
                        Intent intent = new Intent(GiftItemActivity.this, GiftsForMeActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(GiftItemActivity.this, GiftsFromMeActivity.class);
                        startActivity(intent);
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GiftItemActivity.this, "Ошибка!", Toast.LENGTH_SHORT).show();
                }
            });


        } else

            Toast.makeText(GiftItemActivity.this, "Недостаточно средств!", Toast.LENGTH_SHORT).show();
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


