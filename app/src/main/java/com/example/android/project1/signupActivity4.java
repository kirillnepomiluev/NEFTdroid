package com.example.android.project1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class signupActivity4 extends AppCompatActivity {


    private ImageButton backBtn;
    private Button endBtn;
    private EditText password;
    private DatabaseReference mydatabase;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup4);

        mAuth = FirebaseAuth.getInstance();
        mydatabase = FirebaseDatabase.getInstance().getReference();

        password = (EditText)findViewById(R.id.user_pin);
        backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signupActivity4.this, signupActivity3.class);
                startActivity(intent);
            }
        });

        endBtn = (Button)findViewById(R.id.end_btn);
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createUserPassword();


            }
        });
    }


    private void createUserPassword() {


        if (password.getText().toString().isEmpty())
            Toast.makeText(signupActivity4.this, "Поле PIN - код пустое",
                    Toast.LENGTH_SHORT).show();

        else
            if (!password.getText().toString().equals(signupActivity3.pincode)){
                Toast.makeText(signupActivity4.this, "Поле PIN - код не совпадает",
                        Toast.LENGTH_SHORT).show();

        }
        else
            userregfinish();


    }



    private void userregfinish() {
        if (Buffer.user != null){
            Buffer.user.setPassword(password.getText().toString());
            updateUI();
        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Buffer.user = new User(currentUser.getUid(), currentUser.getPhoneNumber());
        Buffer.user.setPassword(password.getText().toString());



        updateUI();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
               .setDisplayName(Buffer.user.getUseruid()).build();


        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            updateUI();


                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(signupActivity4.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }


    private void updateUI() {

        mydatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("password").setValue(Buffer.user.getPassword()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Write was successful!
                // ...
                Toast.makeText(signupActivity4.this, "Успешно установлен PIN-код!",
                        Toast.LENGTH_SHORT).show();

                //Check Android version should be greater or equal to Pie
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                    startActivity(new Intent(signupActivity4.this, fingerprintActivity.class));
                    finish();
                }else

                startActivity(new Intent(signupActivity4.this, MainActivity.class));


                finish();


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                        Toast.makeText(signupActivity4.this, "Ошибка.PIN-код не установлен!",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(signupActivity4.this, signupActivity3.class));


                        finish();

                    }
                });

    }


}
