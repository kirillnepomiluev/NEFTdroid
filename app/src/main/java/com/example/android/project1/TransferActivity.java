package com.example.android.project1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TransferActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int CONTACTS = 999;
    Integer transferSumm = 0;
    String phone;
    String recipientPhone;
    private DatabaseReference mydatabase;
    User recipientUser;
    EditText message;
    String messageForRecipient;
    private ImageButton menuBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                //this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
       // drawer.addDrawerListener(toggle);
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
        mydatabase = FirebaseDatabase.getInstance().getReference();

        ImageButton getContacts = findViewById(R.id.get_contacts);
        message = findViewById(R.id.message);
        getContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContact();
            }
        });

        final EditText transferSummView = findViewById(R.id.transfer_summ);
        final EditText editPhone = findViewById(R.id.edit_phone);
        editPhone.setSelection(2);
        Button confirmTransfer = findViewById(R.id.transfer_confirm);
        confirmTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((!transferSummView.getText().toString().equals("")) && (!editPhone.getText().toString().equals(""))) {
                    transferSumm = Integer.parseInt(transferSummView.getText().toString());
                    phone = editPhone.getText().toString();
                    recipientPhone = phone;
                } else {
                    Toast.makeText(TransferActivity.this, R.string.fields_not_fill, Toast.LENGTH_SHORT).show();
                    return;
                }

                if ((Buffer.money - transferSumm)<0) {
                    Toast.makeText(TransferActivity.this, R.string.low_balance, Toast.LENGTH_SHORT).show();
                    return ;
                }


                mydatabase.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object>  operationMap;
                        Map<String, Object> childUpdates = new HashMap<>();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            User user = postSnapshot.getValue(User.class);
                            if (user.getPhone().equals(recipientPhone)) {
                                recipientUser = user;
                            }
                            else {
                                // логика отправки sms для нового юзера
                            }
                        }
                        if (recipientUser==null) {
                            Toast.makeText(TransferActivity.this, R.string.user_not_found, Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(TransferActivity.this, "recipientUser != null", Toast.LENGTH_SHORT).show();
                            if (!message.getText().equals("")) {
                                messageForRecipient = message.getText().toString();
                            }
                            Operation sentTransfer = new Operation("Перевод "
                                    + recipientUser.getFirstname() + " " + recipientUser.getLastname(),
                                    false, transferSumm, Buffer.user.getUseruid());
                            Operation getTransfer = new Operation("Получение перевода от "
                                    + Buffer.user.getFirstname() + " " + Buffer.user.getLastname(), messageForRecipient,
                                    Buffer.user.getPhone(),true, transferSumm, recipientUser.getUseruid());
                            Notification getTransfetNotif = new Notification("Перевод от "
                                    + Buffer.user.getFirstname() + " " + Buffer.user.getLastname(),
                                    recipientUser.getUseruid()+ " " + transferSumm);
                            operationMap = sentTransfer.toMap();
                            childUpdates.put("/history/" + Buffer.user.getUseruid() + "/" + sentTransfer.getId(), operationMap);
                            operationMap = getTransfer.toMap();
                            childUpdates.put("/history/" + recipientUser.getUseruid() + "/" + getTransfer.getId(), operationMap);
                            operationMap = getTransfetNotif.toMap();
                            childUpdates.put("/notifications/" + recipientUser.getUseruid() + "/" + getTransfetNotif.getId(), operationMap);
                            mydatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(TransferActivity.this, R.string.success,Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(TransferActivity.this, R.string.error,Toast.LENGTH_SHORT).show();
                                }
                            });
                            transferSummView.setText("");
                            editPhone.setText("+7");
                            editPhone.setSelection(2);
                            message.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                });
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
        getMenuInflater().inflate(R.menu.transfer, menu);
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
            CustomDialogClass cdd = new CustomDialogClass(TransferActivity.this, title, info, yesText, noText);
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cdd.show();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getContact() {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        contactPickerIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(contactPickerIntent, CONTACTS);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
            if (requestCode == CONTACTS) {
                // Make sure the request was successful
                if (resultCode == RESULT_OK) {
                    Uri contactUri = intent.getData();

                    String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                    Cursor cursor = getContentResolver()
                            .query(contactUri, projection, null, null, null);
                    cursor.moveToFirst();
                    int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String number = cursor.getString(column);
                    number = number.replaceAll("[^0-9+]*", "");
                    EditText editPhoneNumber = findViewById(R.id.edit_phone);
                    editPhoneNumber.setText(number);
                }
            }

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
