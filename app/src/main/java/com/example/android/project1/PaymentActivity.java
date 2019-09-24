package com.example.android.project1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ImageButton newCard;
    private ProgressBar progressBar;
    private long time;
    private int amountSumm = 0;
    TextView summFromCard, summFromBalance, currBalance;

    private DatabaseReference mydatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mydatabase = FirebaseDatabase.getInstance().getReference();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        BottomMenu.setBottomMenuListener(this);

        newCard = findViewById(R.id.add_card);
        final Intent addNewCard = new Intent(this, NewCardActivity.class);
        newCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(addNewCard);
            }
        } );

        currBalance = findViewById(R.id.curr_balance);
        currBalance.setText(Buffer.money + "");

        final Button buttonCard = findViewById(R.id.button_card);
        final Button buttonBalance = findViewById(R.id.button_balance);
        final Button confirmAmount = findViewById(R.id.confirm_amount);
        final LinearLayout fromCard = findViewById(R.id.from_card);
        final LinearLayout fromBalance = findViewById(R.id.from_balance);

        buttonCard.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                buttonCard.setPressed(true);
                buttonBalance.setPressed(false);
                fromBalance.setVisibility(View.GONE);
                fromCard.setVisibility(View.VISIBLE);
                confirmAmount.setVisibility(View.VISIBLE);
                return true; }
        });

        buttonBalance.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                buttonBalance.setPressed(true);
                buttonCard.setPressed(false);
                fromCard.setVisibility(View.GONE);
                fromBalance.setVisibility(View.VISIBLE);
                confirmAmount.setVisibility(View.VISIBLE);
                return true; }
        });

        RadioButton refillOneMonthCard = findViewById(R.id.refill_1m_card);
        RadioButton refillThreeMonthCard = findViewById(R.id.refill_3m_card);
        RadioButton refillSixMonthCard = findViewById(R.id.refill_6m_card);
        RadioButton refillOneYearCard = findViewById(R.id.refill_1y_card);
        RadioButton refillOneMonthBalance = findViewById(R.id.refill_1m_balance);
        RadioButton refillThreeMonthBalance = findViewById(R.id.refill_3m_balance);
        RadioButton refillSixMonthBalance = findViewById(R.id.refill_6m_balance);
        RadioButton refillOneYearBalance = findViewById(R.id.refill_1y_balance);

        refillOneMonthCard.setOnClickListener(radioButtonClickListener);
        refillThreeMonthCard.setOnClickListener(radioButtonClickListener);
        refillSixMonthCard.setOnClickListener(radioButtonClickListener);
        refillOneYearCard.setOnClickListener(radioButtonClickListener);
        refillOneMonthBalance.setOnClickListener(radioButtonClickListener);
        refillThreeMonthBalance.setOnClickListener(radioButtonClickListener);
        refillSixMonthBalance.setOnClickListener(radioButtonClickListener);
        refillOneYearBalance.setOnClickListener(radioButtonClickListener);

        confirmAmount.setOnClickListener(confirmAmountListener);
        summFromCard = findViewById(R.id.summ_from_card);
        summFromBalance = findViewById(R.id.summ_from_balance);
    }



    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RadioButton rb = (RadioButton)v;
            switch (rb.getId()) {
                case R.id.refill_1m_card: summFromCard.setText("1000");
                    amountSumm=1000;
                    break;
                case R.id.refill_3m_card: summFromCard.setText("3000");
                    amountSumm=3000;
                    break;
                case R.id.refill_6m_card: summFromCard.setText("6000");
                    amountSumm=6000;
                    break;
                case R.id.refill_1y_card: summFromCard.setText("12000");
                    amountSumm=12000;
                    break;
                case R.id.refill_1m_balance: summFromBalance.setText("1000");
                    amountSumm=1000;
                    break;
                case R.id.refill_3m_balance: summFromBalance.setText("3000");
                    amountSumm=3000;
                    break;
                case R.id.refill_6m_balance: summFromBalance.setText("6000");
                    amountSumm=6000;
                    break;
                case R.id.refill_1y_balance: summFromBalance.setText("12000");
                    amountSumm=12000;
                    break;
                default:
                    break;
            }
        }
    };

    View.OnClickListener confirmAmountListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Button buttonBalance = findViewById(R.id.button_balance);
            final Button buttonCard = findViewById(R.id.button_card);
            if (((Buffer.money - amountSumm)<0)&&(buttonBalance.isPressed())) {
                Toast.makeText(PaymentActivity.this, R.string.low_balance, Toast.LENGTH_SHORT).show();
                return ;
            }

            Operation operation = new Operation();
            if (buttonBalance.isPressed()) {
                operation = new Operation(getString(R.string.abonent_payment), false, amountSumm, Buffer.user.getUseruid());
            }

            if(buttonCard.isPressed()) {
                operation = new Operation(getString(R.string.abonent_payment), false, 0, Buffer.user.getUseruid());
            }



            Map<String, Object> childUpdates = new HashMap<>();

            ArrayList<String> sponsors =  new ArrayList<>(Arrays.asList(Buffer.user.getSponsorid(),Buffer.user.getSponsor2id(),Buffer.user.getSponsor3id()));
            Notification rewardNotification = new Notification();

            for (int i=0; i < sponsors.size(); i++ ) {
                String sponsor = sponsors.get(i);
                int partnerReward = 0;
                switch (i) {
                    case (0) :
                        partnerReward = (int)Math.round(amountSumm*0.3);
                        rewardNotification = new Notification(
                                getString(R.string.reward_1st_lvl) + partnerReward,
                                sponsor);
                        break;
                    case (1):
                        partnerReward = (int)Math.round(amountSumm*0.15);
                        rewardNotification = new Notification(
                                getString(R.string.reward_2_lvl) + partnerReward,
                                sponsor);
                        break;
                    case (2):
                        partnerReward = (int)Math.round(amountSumm*0.05);
                        rewardNotification = new Notification(
                                getString(R.string.reward_3_lvl) + partnerReward,
                                sponsor);
                        break;
                }
                Operation splus = new Operation("Партнерка", true, partnerReward, sponsor);
                Map<String, Object> splusmap = splus.toMap();
                childUpdates.put("/history/" + sponsor + "/"+ operation.getId() ,splusmap);
                childUpdates.put("/notifications/" + sponsor + "/" + rewardNotification.getId(), rewardNotification.toMap());

            }
            if (Buffer.user.getStatusend()==0) Buffer.user.setStatusend(new Date().getTime());
            Calendar calendarInstanse= Calendar.getInstance();
            calendarInstanse.setTimeInMillis(Buffer.user.getStatusend()); //устанавливаем дату, с которой будет производить операции
            switch (amountSumm) {
                case 1000 :
                    calendarInstanse.add(Calendar.MONTH, 1);
                    time = calendarInstanse.getTimeInMillis();
                    break;
                case 3000 :
                    calendarInstanse.add(Calendar.MONTH, 3);
                    time = calendarInstanse.getTime().getTime();
                    break;
                case 6000 :
                    calendarInstanse.add(Calendar.MONTH, 6);
                    time = calendarInstanse.getTime().getTime();
                    break;
                case 12000 :
                    calendarInstanse.add(Calendar.MONTH, 12);
                    time = calendarInstanse.getTime().getTime();
                    break;

            }

            Map<String, Object> operationMap=operation.toMap();
            childUpdates.put("/history/" + Buffer.user.getUseruid() + "/"+operation.getId(),operationMap);
            childUpdates.put("/users/" + Buffer.user.getUseruid() + "/statusend", time);
            childUpdates.put("/users/" + Buffer.user.getUseruid() + "/status", true);
            childUpdates.put("/partners/" + Buffer.user.getSponsorid() + "/" + Buffer.user.getUseruid() + "/status", true);
            childUpdates.put("/users/" + Buffer.user.getUseruid() + "/money", Buffer.money - amountSumm);
            mydatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Buffer.user.setStatus(true);
                    Buffer.user.setStatusend(time);
                    Toast.makeText(PaymentActivity.this, R.string.success,Toast.LENGTH_SHORT).show();
                    summFromBalance.setText("");
                    summFromCard.setText("");
                    currBalance.setText(Buffer.money + "");


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PaymentActivity.this, R.string.error,Toast.LENGTH_SHORT).show();
                }
            });
            }
        };


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
        getMenuInflater().inflate(R.menu.payment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_exit) {
            String title = "Выйти из профиля?";
            String info = "";
            String yesText = "Выйти";
            String noText = "Пропустить";
            CustomDialogClass cdd = new CustomDialogClass(PaymentActivity.this, title, info, yesText, noText);
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cdd.show();

        }else
        // Handle navigation view item clicks here.
        if (!Buffer.user.getIsProfileFilled()) {
            String title = "Заполните профиль";
            String info = "Для получения полной функциональности обновите свой профиль!";
            String yesText = "Профиль";
            String noText = "Пропустить";
            CustomDialogClass cdd = new CustomDialogClass(PaymentActivity.this, title, info, yesText, noText);
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cdd.show();
        } else {
            if (Buffer.verification == null) {
                String title = "Завершите верификацию!";
                String info = "Вы не полностью заполнили профиль! Завершите верификацию для получения полного функционала.";
                String yesText = "Верификация";
                String noText = "Пропустить";
                CustomDialogClass cdd = new CustomDialogClass(PaymentActivity.this, title, info, yesText, noText);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            } else if (Buffer.user.getEmail() == null) {
                String title = "Подтвердите email!";
                String info = "Вы не подтвердили свой email. Завершите операцию для получения полного функционала. ";
                String yesText = "Подтвердить email";
                String noText = "Пропустить";
                CustomDialogClass cdd = new CustomDialogClass(PaymentActivity.this, title, info, yesText, noText);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            } else if (!Buffer.user.getStatus()) {
                String title = "Оплатите обслуживание!";
                String info = "Для получения полной функциональности оплатите обслуживание.";
                String yesText = "Оплатить";
                String noText = "Пропустить";
                CustomDialogClass cdd = new CustomDialogClass(PaymentActivity.this, title, info, yesText, noText);
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
                } else if (id == R.id.nav_exit) {

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


}
