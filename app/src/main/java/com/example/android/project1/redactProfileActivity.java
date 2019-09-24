package com.example.android.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class redactProfileActivity extends AppCompatActivity {

    private DatabaseReference mydatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private ImageButton backBtn;
    private TextView identityText, identityInfo;
    private ImageView identityMark;
    private EditText firstname, lastname, datebirth, country, city, address;
    private RadioGroup malegr;
    private RadioButton man, woman;
    private boolean male = false;
    private boolean malecheked;
    private Button saveBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redact_profile);


        mAuth = FirebaseAuth.getInstance();
        mydatabase = FirebaseDatabase.getInstance().getReference();
        user= mAuth.getCurrentUser();


        identityInfo = (TextView) findViewById(R.id.text_info);
        identityText = (TextView)findViewById(R.id.identity_text);
        identityMark = (ImageView)findViewById(R.id.identity_mark);

        if (!(Buffer.verification == null)){
            if (Buffer.verification.isVerifyCheck() && !Buffer.user.getVerifyEnd()){
                identityInfo.setVisibility(View.VISIBLE);
                identityInfo.setText("Верификация находится на этапе проверки. Данные обновятся автоматически.");
            }
            if (Buffer.user.getVerifyEnd()){
                Picasso.get().load(R.drawable.checkmark)
                        .into(identityMark);
                identityText.setText("Идентифицирован");
                identityInfo.setVisibility(View.GONE);
            }

        }


        firstname = (EditText) findViewById(R.id.firstnameText);
        firstname.setText(Buffer.user.getFirstname());
        lastname = (EditText) findViewById(R.id.secondnameText);
        lastname.setText(Buffer.user.getLastname());
        datebirth = (EditText)findViewById(R.id.datebirthText);
        datebirth.setText(Buffer.user.getDatebirth());
        man = (RadioButton)findViewById(R.id.radio_man);
        woman = (RadioButton)findViewById(R.id.radio_woman);
        country = (EditText)findViewById(R.id.countryText);
        country.setText(Buffer.user.getCountry());
        city = (EditText) findViewById(R.id.cityText);
        city.setText(Buffer.user.getCity());
        address = (EditText) findViewById(R.id.addressText);
        address.setText(Buffer.user.getStreet());
        malegr = (RadioGroup) findViewById(R.id.radio_male);
        malecheked = false;
        malegr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        malecheked = false;
                        break;
                    case R.id.radio_man:
                        male = true;
                        malecheked = true;
                        break;
                    case R.id.radio_woman:
                        male = false;
                        malecheked = true;

                        break;


                    default:
                        break;
                }
            }
        });
        if (Buffer.user.isMale()){
            malegr.check(R.id.radio_man);
        }else
            malegr.check(R.id.radio_woman);


        backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(redactProfileActivity.this, profileActivity.class);
                startActivity(intent);
            }
        });

        saveBtn = (Button)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFieldsProfile();


            }
        });



    }

    private void checkFieldsProfile() {

        if (checkMale()) {

            if (firstname.getText().toString().isEmpty())
                Toast.makeText(redactProfileActivity.this, "Поле имя пустое",
                        Toast.LENGTH_SHORT).show();
            else if (lastname.getText().toString().isEmpty())
                Toast.makeText(redactProfileActivity.this, "Поле фамилия пустое",
                        Toast.LENGTH_SHORT).show();
            else if (datebirth.getText().toString().isEmpty())
                Toast.makeText(redactProfileActivity.this, "Поле дата рождения пустое",
                        Toast.LENGTH_SHORT).show();
            else if (country.getText().toString().isEmpty())
                Toast.makeText(redactProfileActivity.this, "Поле страна пустое",
                        Toast.LENGTH_SHORT).show();
            else if (city.getText().toString().isEmpty())
                Toast.makeText(redactProfileActivity.this, "Поле город пустое",
                        Toast.LENGTH_SHORT).show();
            else if (address.getText().toString().isEmpty())
                Toast.makeText(redactProfileActivity.this, "Поле адрес пустое",
                        Toast.LENGTH_SHORT).show();
            else if (lastname.getText().toString().isEmpty())
                Toast.makeText(redactProfileActivity.this, "Поле фамилия пустое",
                        Toast.LENGTH_SHORT).show();

            else
                redactUserProfile();
        }


    }

    private void redactUserProfile(){

                FirebaseUser currentUser = mAuth.getCurrentUser();
                Buffer.user.setFirstname(firstname.getText().toString());
                Buffer.user.setLastname(lastname.getText().toString());
                Buffer.user.setDatebirth(datebirth.getText().toString());
                Buffer.user.setCountry(country.getText().toString());
                Buffer.user.setCity(city.getText().toString());
                Buffer.user.setStreet(address.getText().toString());
                Buffer.user.setAddress(String.format("%s,%s,%s", country.getText().toString(), city.getText().toString(), address.getText().toString()));
                Buffer.user.setMale(male);
                Buffer.user.setProfileFilled(true);



        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(Buffer.user.getFirstname()).build();


        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            updateUI();


                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(redactProfileActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }


    private void updateUI() {

        Map<String, Object> neweventValues = Buffer.user.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/users/" + Buffer.user.getUseruid(), neweventValues);
        //childUpdates.put("/partners/" + spronsorid + "/" + Buffer.user.getUseruid(), neweventValues);
        //childUpdates.put("/logins/" + Buffer.user.getLogin(), Buffer.user.getUseruid());


        mydatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Write was successful!
                // ...
                Toast.makeText(redactProfileActivity.this, "Профиль успешно отредактирован",
                        Toast.LENGTH_SHORT).show();
                if (Buffer.verification == null) {
                    Intent intent = new Intent(redactProfileActivity.this, verificationActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(redactProfileActivity.this, profileActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                        Toast.makeText(redactProfileActivity.this, "Ошибка.Профиль не отредактирован",
                                Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private boolean checkMale() {
        if (malecheked) {
            return true;

        } else {
            Toast.makeText(redactProfileActivity.this, "Пол не указан",
                    Toast.LENGTH_SHORT).show();
            return false;

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


