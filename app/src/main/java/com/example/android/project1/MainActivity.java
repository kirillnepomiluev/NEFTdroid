package com.example.android.project1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    private ImageButton menu, notifications;
    private TextView currency, balance, paidTill;
    private Button signoutBtn;
    private LinearLayout gift, topUp, transferMoney, withdrawMoney, friends, map, home, history, scanPay, cards, chat;

    private DatabaseReference mydatabase;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private ProgressBar progressBar;
    private ArrayList<User> users;
    private FirebaseUser user;
    private static final String TAG = "ex";
    boolean usersload;

    public MainActivity() {
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        user = mAuth.getCurrentUser();
        if (user == null) {
            mAuth.signOut();
            Intent intent = new Intent(this, signupActivity.class);
            startActivity(intent);
            finish();
        } else {

            mydatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot snapshot) {

                    Buffer.user = snapshot.getValue(User.class);

                    if (Buffer.user == null) {
                        startActivity(new Intent(MainActivity.this, signupActivity2.class));
                        finish();
                    } else {
                        mydatabase.child("verifications").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Buffer.verification = dataSnapshot.getValue(Verification.class);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        mydatabase.child("FuelCards").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Buffer.fuelCard = dataSnapshot.getValue(FuelCard.class);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        if (!Buffer.user.getIsProfileFilled()) {
                            String title = "Заполните профиль";
                            String info = "Для получения полной функциональности обновите свой профиль!";
                            String yesText = "Профиль";
                            String noText = "Пропустить";
                            CustomDialogClass cdd = new CustomDialogClass(MainActivity.this, title, info, yesText, noText);
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            cdd.show();

                        }else {
                            if (Buffer.verification == null){
                                String title = "Завершите верификацию!";
                                String info = "Вы не полностью заполнили профиль! Завершите верификацию для получения полного функционала.";
                                String yesText = "Верификация";
                                String noText = "Пропустить";
                                CustomDialogClass cdd = new CustomDialogClass(MainActivity.this, title, info, yesText, noText);
                                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                cdd.show();
                            } else if (Buffer.user.getEmail() == null){
                                String title = "Подтвердите email!";
                                String info = "Вы не подтвердили свой email. Завершите операцию для получения полного функционала. ";
                                String yesText = "Подтвердить email";
                                String noText = "Пропустить";
                                CustomDialogClass cdd = new CustomDialogClass(MainActivity.this, title, info, yesText, noText);
                                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                cdd.show();
                            } else if (!Buffer.user.getStatus()){
                                String title = "Оплатите обслуживание!";
                                String info = "Для получения полной функциональности оплатите обслуживание.";
                                String yesText = "Оплатить";
                                String noText = "Пропустить";
                                CustomDialogClass cdd = new CustomDialogClass(MainActivity.this, title, info, yesText, noText);
                                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                cdd.show();

                            } else if (Buffer.fuelCard == null && Buffer.isOrderDialogShow==false){
                                String title = "Заказать топливную карту?";
                                String info = "Закажите свою бесплатную топливную карту и начисляйте на неё литры с баланса. ";
                                String yesText = "Заказать";
                                String noText = "Пропустить";
                                CustomDialogClass cdd = new CustomDialogClass(MainActivity.this, title, info, yesText, noText);
                                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                cdd.show();
                                Buffer.isOrderDialogShow = true;
                            }
                        }


                    }
                    if (!Buffer.checkPassword) {
                        Intent intent = new Intent(MainActivity.this, SighInActivity.class);
                        startActivity(intent);
                        finish();

                    }


                    if (Buffer.user.getPassword() == null) {
                        startActivity(new Intent(MainActivity.this, signupActivity3.class));
                        finish();

                    }




                    balanceupdate();
                    hasNotif();
                    setPaidTill();

                    FirebaseMessaging.getInstance().subscribeToTopic(mAuth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                String msg = "not subscribed";
                                Log.d(TAG, msg);
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }

                public void onCancelled(DatabaseError databaseError) {
                    //Log.i(TAG, "loadPost:onCancelled", databaseError.toException());
                }

            });


        }


    }

    private void balanceupdate() {
        progressBar.setVisibility(View.VISIBLE);
        mydatabase.child("history").child(Buffer.user.getUseruid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Buffer.history= new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Operation op = postSnapshot.getValue(Operation.class);
                    Buffer.history.add(op);
                }
                Buffer.moneyupdate();
                balance = (TextView) findViewById(R.id.balance_tv);
                balance.setText(Buffer.money + "");
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    private void setPaidTill() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy");
        if (Buffer.user.getStatusend() < new Date().getTime()) {
            paidTill.setText("Ваша подписка истекла");
        } else {
            paidTill.setText("Подписка оформлена до " + dateFormat.format(new Date(Buffer.user.getStatusend())));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        menu = (ImageButton) findViewById(R.id.menu_btn);
        menu.setOnClickListener(this);
        notifications = (ImageButton) findViewById(R.id.notification_btn);
        notifications.setOnClickListener(this);
        currency = (TextView) findViewById(R.id.currency);
        balance = (TextView) findViewById(R.id.balance_tv);
        topUp = (LinearLayout) findViewById(R.id.top_up);
        topUp.setOnClickListener(this);
        transferMoney = (LinearLayout) findViewById(R.id.transfer_money);
        transferMoney.setOnClickListener(this);
        withdrawMoney = (LinearLayout) findViewById(R.id.withdrow);
        withdrawMoney.setOnClickListener(this);
        friends = (LinearLayout) findViewById(R.id.friends);
        friends.setOnClickListener(this);
        gift = (LinearLayout) findViewById(R.id.gift_linear);
        gift.setOnClickListener(this);
        map = (LinearLayout) findViewById(R.id.map);
        map.setOnClickListener(this);
        home = (LinearLayout) findViewById(R.id.home_btn);
        home.setOnClickListener(this);
        history = (LinearLayout) findViewById(R.id.history_btn);
        history.setOnClickListener(this);
        scanPay = (LinearLayout) findViewById(R.id.scan_pay_btn);
        scanPay.setOnClickListener(this);
        cards = (LinearLayout) findViewById(R.id.cards_btn);
        cards.setOnClickListener(this);
        chat = (LinearLayout) findViewById(R.id.chat_btn);
        chat.setOnClickListener(this);
        signoutBtn = (Button) findViewById(R.id.signout_btn);
        signoutBtn.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        paidTill = findViewById(R.id.paid_till);




        mAuth = FirebaseAuth.getInstance();
        mydatabase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();

        progressBar.setVisibility(View.GONE);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_exit) {
            String title = "Выйти из профиля?";
            String info = "";
            String yesText = "Выйти";
            String noText = "Пропустить";
            CustomDialogClass cdd = new CustomDialogClass(MainActivity.this, title, info, yesText, noText);
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cdd.show();

        }
        if (!Buffer.user.getIsProfileFilled()) {
            String title = "Заполните профиль";
            String info = "Для получения полной функциональности обновите свой профиль!";
            String yesText = "Профиль";
            String noText = "Пропустить";
            CustomDialogClass cdd = new CustomDialogClass(MainActivity.this, title, info, yesText, noText);
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cdd.show();
        } else {
            if (Buffer.verification == null) {
                String title = "Завершите верификацию!";
                String info = "Вы не полностью заполнили профиль! Завершите верификацию для получения полного функционала.";
                String yesText = "Верификация";
                String noText = "Пропустить";
                CustomDialogClass cdd = new CustomDialogClass(MainActivity.this, title, info, yesText, noText);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            } else if (Buffer.user.getEmail() == null) {
                String title = "Подтвердите email!";
                String info = "Вы не подтвердили свой email. Завершите операцию для получения полного функционала. ";
                String yesText = "Подтвердить email";
                String noText = "Пропустить";
                CustomDialogClass cdd = new CustomDialogClass(MainActivity.this, title, info, yesText, noText);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            } else if (!Buffer.user.getStatus()) {
                String title = "Оплатите обслуживание!";
                String info = "Для получения полной функциональности оплатите обслуживание.";
                String yesText = "Оплатить";
                String noText = "Пропустить";
                CustomDialogClass cdd = new CustomDialogClass(MainActivity.this, title, info, yesText, noText);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();

            } else {
                if (id == R.id.nav_home) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_profile) {
                    Intent intent = new Intent(this, profileActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_news) {
                    Intent intent = new Intent(this, NewsActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_ref) {
                    Intent intent = new Intent(this, InviteActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_feedback) {
                    Intent intent = new Intent(this, FeedbackActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_tarif) {
                    Intent intent = new Intent(this, PaymentActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_faq) {
                    Intent intent = new Intent(this, FAQActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_chat) {
                    Intent intent = new Intent(this, ChatBoxActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_settings) {
                    Intent intent = new Intent(this, SettingsActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_about) {
                    Intent intent = new Intent(this, AboutActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_offer) {
                    Intent intent = new Intent(this, OfferActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_partners) {
                    Intent intent = new Intent(this, PartnersActivity.class);
                    startActivity(intent);
                }
            }
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    @Override
    public void onClick(View view) {
        if (!Buffer.user.getIsProfileFilled()) {
            String title = "";
            String info = "Для получения полной функциональности обновите свой профиль!";
            String yesText = "Профиль";
            String noText = "Пропустить";
            CustomDialogClass cdd = new CustomDialogClass(MainActivity.this, title, info, yesText, noText);
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cdd.show();
        }else if (Buffer.verification == null){
                String title = "";
                String info = "Вы не полностью заполнили профиль! Завершите верификацию для получения полного функционала.";
                String yesText = "Верификация";
                String noText = "Пропустить";
                CustomDialogClass cdd = new CustomDialogClass(MainActivity.this, title, info, yesText, noText);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            } else if (Buffer.user.getEmail() == null){
                String title = "";
                String info = "Вы не подтвердили свой email. Завершите операцию для получения полного функционала. ";
                String yesText = "Подтвердить email";
                String noText = "Пропустить";
                CustomDialogClass cdd = new CustomDialogClass(MainActivity.this, title, info, yesText, noText);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            } else if (!Buffer.user.getStatus()){
            String title = "Оплатите обслуживание!";
            String info = "Для получения полной функциональности оплатите обслуживание.";
            String yesText = "Оплатить";
            String noText = "Пропустить";
            CustomDialogClass cdd = new CustomDialogClass(MainActivity.this, title, info, yesText, noText);
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cdd.show();

        } else
        switch (view.getId()) {
                case R.id.menu_btn:
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.openDrawer(GravityCompat.START);
                    break;
                case R.id.notification_btn:
                    startActivity(new Intent(MainActivity.this, NotificationsActivity.class));
                    break;
                case R.id.top_up:
                    startActivity(new Intent(MainActivity.this, RefillActivity.class));
                    break;
                case R.id.withdrow:
                    break;
                case R.id.transfer_money:
                    startActivity(new Intent(MainActivity.this, TransferActivity.class));
                    break;
                case R.id.friends:
                    startActivity(new Intent(MainActivity.this, FriendsActivity.class));
                    break;
                case R.id.gift_linear:
                    startActivity(new Intent(MainActivity.this, GiftsActivity.class));
                    break;
                case R.id.map:
                    startActivity(new Intent(MainActivity.this, MapActivity.class));
                    break;
                case R.id.home_btn:
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    break;
                case R.id.history_btn:
                    startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                    break;
                case R.id.scan_pay_btn:
                    startActivity(new Intent(MainActivity.this, ScannerActivity.class));
                    break;
                case R.id.cards_btn:

                    break;
                case R.id.chat_btn:
                    startActivity(new Intent(MainActivity.this, ChatBoxActivity.class));
                    break;
                case R.id.signout_btn:
                    mAuth.signOut();
                    startActivity(new Intent(MainActivity.this, signupActivity.class));
                    finish();
                    break;
            }
        }
    private void hasNotif() {
        mydatabase.child("notifications").child(Buffer.user.getUseruid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Notification notification = postSnapshot.getValue(Notification.class);
                    if(!notification.getRead()) {
                        notifications.setImageResource(R.drawable.bell_has_notif);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}

