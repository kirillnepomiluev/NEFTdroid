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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String singleDate = "", beginDate = "", endDate = "";
    private SimpleDateFormat dateFormat;
    private long digitSingleDate = 0;
    private long digitBeginDate = 0;
    private long digitEndDate = 0;

    private TextView moneytv;
    private DatabaseReference mydatabase;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView mRecyclerView;
    private static final String TAG = "RecyclerViewExample";

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private ArrayList<Operation> history;
    private HistoryRecyclerViewAdapter adapter;
    private FirebaseUser user;
    private Button setDate;
    private ImageButton menuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                //this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.addDrawerListener(toggle);
        //toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        menuBtn = (ImageButton)findViewById(R.id.menu_btn);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);

            }
        });
        BottomMenu.setBottomMenuListener(this);


        mAuth = FirebaseAuth.getInstance();
        mydatabase= FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        moneytv=(TextView) findViewById(R.id.moneytv);
        progressBar=(ProgressBar) findViewById(R.id.progress_bar);
        moneytv.setText("Баллы: "+Buffer.money);

        setDate = findViewById(R.id.set_date);
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryActivity.this, CalendarActivity.class);
                startActivity(intent);
                finish();
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecView);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        Intent intent = getIntent();
        dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
        singleDate = intent.getStringExtra("singleDate");
        beginDate = intent.getStringExtra("beginDate");
        endDate = intent.getStringExtra("endDate");


        if (singleDate != null) {
            digitSingleDate = Long.parseLong(singleDate);
            setDate.setText(dateFormat.format(digitSingleDate));
        }

        if ((beginDate != null) && (endDate != null)) {
            digitBeginDate = Long.parseLong(beginDate);
            digitEndDate = Long.parseLong(endDate);
            setDate.setText(dateFormat.format(digitBeginDate) +
                    " - " + dateFormat.format(digitEndDate));
        }


        history = new ArrayList<>();

        mydatabase.child("history").child(Buffer.user.getUseruid()).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                history = new ArrayList<>();

                if (snapshot.exists()) {
                    if(dateIsSet()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Operation element = snapshot1.getValue(Operation.class);
                            if ((element!= null)&&
                                    (   //проверка на соответствие выбранным датам
                                            (element.getTime() >= digitBeginDate) &&
                                                    (element.getTime() <= digitEndDate)
                                    ) ||
                                    (
                                            (element.getTime() >= digitSingleDate) &&
                                                    (element.getTime() <= digitSingleDate + 86400000)
                                    )
                            )
                                history.add(element);
                        }
                    } else {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Operation element = snapshot1.getValue(Operation.class);
                            if (element != null)
                                history.add(element);
                        }
                    }
                }
                updateUI();
                progressBar.setVisibility(View.GONE);
                Buffer.history=history;
                Buffer.moneyupdate();
                moneytv.setText("Баллы: "+Buffer.money);

            }

            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "loadPost:onCancelled", databaseError.toException());
            }

        });
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
        getMenuInflater().inflate(R.menu.history, menu);
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
            String title = "Выйти из профиля?";
            String info = "";
            String yesText = "Выйти";
            String noText = "Пропустить";
            CustomDialogClass cdd = new CustomDialogClass(HistoryActivity.this, title, info, yesText, noText);
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cdd.show();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateUI() {

        if (history.size()==0)  return;


        adapter = new HistoryRecyclerViewAdapter(HistoryActivity.this, history);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItem2ClickListener() {

            @Override
            public void onItemClick( Object item) {
                if(Buffer.checkstatus(HistoryActivity.this)) {
                }
            }


        });

    }

    private boolean dateIsSet() {
        if ((singleDate != null) || (beginDate != null)) return true;
        else return false;
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
