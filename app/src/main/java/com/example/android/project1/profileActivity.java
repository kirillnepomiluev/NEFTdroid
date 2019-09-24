package com.example.android.project1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class profileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DatabaseReference mydatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private StorageReference usersref;
    private StorageReference picref;

    private TextView nameText, phonenumberText, datebirthText, passportText, addressText, identityText, textInfo;
    private ImageButton redactProfileBtn, menuBtn;
    private Button orderCardBtn;
    private ImageView photoUser, identityMark;
    //private Switch aSwitch;
    String key;
    String userimageurl;
    private final static int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE =1;
    static final int GALLERY_REQUEST = 1;
    //private RelativeLayout verificationLayout;
    private LinearLayout identityLayout;
    final String TAG = "States";




    /*public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, profileActivity.class);
            startActivity(intent);
            finish();
        }
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        BottomMenu.setBottomMenuListener(this);

        mAuth = FirebaseAuth.getInstance();
        mydatabase = FirebaseDatabase.getInstance().getReference();
        user= mAuth.getCurrentUser();


        nameText = (TextView) findViewById(R.id.name_user_text);
        nameText.setText(String.format(Buffer.user.getFirstname()+ " " + Buffer.user.getLastname()));
        phonenumberText = (TextView) findViewById(R.id.phonenumber_tv);
        phonenumberText.setText(Buffer.user.getPhone());
        datebirthText = (TextView) findViewById(R.id.dateBirth_tv);
        datebirthText.setText(Buffer.user.getDatebirth());
        passportText = (TextView) findViewById(R.id.passport_tv);

        addressText = (TextView) findViewById(R.id.address_tv);
        addressText.setText(Buffer.user.getAddress());

        /*
        aSwitch = (Switch)findViewById(R.id.verification_switch);
        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profileActivity.this, verificationActivity.class);
                startActivity(intent);
                finish();
            }
        });
        */

        //verificationLayout= (RelativeLayout)findViewById(R.id.verification_layout);
        identityLayout = (LinearLayout)findViewById(R.id.identity_layout);
        identityText = (TextView)findViewById(R.id.identity_text);
        textInfo = (TextView)findViewById(R.id.text_info);
        identityMark = (ImageView)findViewById(R.id.identity_mark);


        if (!(Buffer.verification == null)){
            if (Buffer.verification.isVerifyCheck() && !Buffer.user.getVerifyEnd()){
                textInfo.setVisibility(View.VISIBLE);
                textInfo.setText("Верификация находится на этапе проверки. Данные обновятся автоматически.");
            }
            if (Buffer.user.getVerifyEnd()){
                identityLayout.setBackground(getResources().getDrawable(R.drawable.rectangle_identity));
                Picasso.get().load(R.drawable.checkmark)
                        .into(identityMark);
                identityText.setText("Идентифицирован");
                textInfo.setVisibility(View.GONE);
            }

        }
        orderCardBtn = (Button)findViewById(R.id.order_btn);
        if (Buffer.fuelCard == null){
            orderCardBtn.setVisibility(View.VISIBLE);
        }else {
            orderCardBtn.setVisibility(View.GONE);
        }
        orderCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profileActivity.this, OrderCardActivity.class));
            }
        });





        redactProfileBtn = (ImageButton) findViewById(R.id.redact_profile_btn);
        redactProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profileActivity.this, redactProfileActivity.class);
                startActivity(intent);
            }
        });

        menuBtn = (ImageButton) findViewById(R.id.menu_btn);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);

            }
        });

        photoUser = (ImageView) findViewById(R.id.profile_iv);


        key = user.getUid();

        String sr = Buffer.user.getUseruid();

        try {
            userimageurl = user.getPhotoUrl().toString();
        } catch (Exception ex) {
            userimageurl = "R.drawable.loginim";
        }

        storage = FirebaseStorage.getInstance();
        usersref = storage.getReference();

        Picasso.get().load(Buffer.photourlpart1 + sr + Buffer.photourlpart2)
                .error(R.drawable.profile_user)
                .placeholder(R.drawable.profile_user)
                .transform(new CropCircleTransformation())
                .into(photoUser);

        photoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permissionStatus = ContextCompat.checkSelfPermission(profileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                    showimagechoser();
                } else {
                    ActivityCompat.requestPermissions(profileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE);
                }


            }
        });
    }

        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
        {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        showimagechoser();
                    }
                    break;
            }
        }

        private void showimagechoser(){

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        }

        protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
            super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
            switch (requestCode) {
                case GALLERY_REQUEST:
                    if (resultCode == RESULT_OK) {

                        Uri selectedImageUri = imageReturnedIntent.getData();

                        Picasso.get()
                                .load(selectedImageUri)
                                .error(R.drawable.profile_user)
                                .resize(200, 200)
                                .centerCrop()
                                .into(photoUser, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        photoUser.setVisibility(View.VISIBLE);

                                        uploadpic(key);


                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Toast.makeText(profileActivity.this, "ошибка.изображение не загружено",
                                                Toast.LENGTH_SHORT).show();


                                    }


                                });


                    }
            }
        }

        private void uploadpic(String key) {

            picref= usersref.child("userspics").child(key+".jpg");

            // Get the data from an ImageView as bytes
            photoUser.setDrawingCacheEnabled(true);
            photoUser.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) photoUser.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();



            UploadTask uploadTask = picref.putBytes(data);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();

                    }

                    // Continue with the task to get the download URL
                    return picref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        userimageurl =task.getResult().toString();
                        getimageURL(userimageurl);




                    } else {


                        Toast.makeText(profileActivity.this, "ошибка! Изображение  не обновлено!",
                                Toast.LENGTH_SHORT).show();

                        // Handle failures
                        // ...
                    }
                }
            });


        }

        private void getimageURL (String url)  {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(url))
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                mydatabase = FirebaseDatabase.getInstance().getReference();
                                mydatabase.child("users").child(key).child("photoUrl").setValue(userimageurl);



                                Toast.makeText(profileActivity.this, "изображение обновлено",
                                        Toast.LENGTH_SHORT).show();


                                Picasso.get().load(userimageurl)
                                        .error(R.drawable.profile_user)
                                        .placeholder(R.drawable.profile_user)
                                        .into(photoUser);

                            }
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
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
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
            CustomDialogClass cdd = new CustomDialogClass(profileActivity.this, title, info, yesText, noText);
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cdd.show();

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