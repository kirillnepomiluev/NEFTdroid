package com.example.android.project1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class InviteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ImageButton menuBtn;
    LinearLayout bottomMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        bottomMenu = findViewById(R.id.bottom_menu);
        NavigationView navigationView = findViewById(R.id.nav_view);

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
        getMenuInflater().inflate(R.menu.invite, menu);
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
        // Handle navigation view item clicks here.
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
            CustomDialogClass cdd = new CustomDialogClass(InviteActivity.this, title, info, yesText, noText);
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cdd.show();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void share(View view) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", R.string.share_text);
        startActivity(Intent.createChooser(intent, "Поделиться"));
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
