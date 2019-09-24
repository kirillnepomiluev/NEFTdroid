package com.example.android.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class signupActivity2 extends AppCompatActivity {

    private Button nextBtn;
    private EditText sponsorPhone;
    private DatabaseReference mydatabase;
    private FirebaseAuth mAuth;
    private String sponsorid;
    private User sponsor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        mAuth = FirebaseAuth.getInstance();
        mydatabase = FirebaseDatabase.getInstance().getReference();

        sponsorPhone = (EditText)findViewById(R.id.sponsorphone_et);
        sponsorid = "";
        nextBtn = (Button)findViewById(R.id.next_btn);


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userreg();
//                Intent intent = new Intent(signupActivity2.this, signupActivity3.class);
//                startActivity(intent);
            }
        });
    }

    private void userreg() {


        if (sponsorPhone.getText().toString().isEmpty())
                Toast.makeText(signupActivity2.this, "Поле номер телефона спонсора пустое",
                        Toast.LENGTH_SHORT).show();
            else
                mydatabase.child("logins").child(sponsorPhone.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String s = dataSnapshot.getValue(String.class);
                        if (s == null) {
                            Toast.makeText(signupActivity2.this, "Спонсор с таким номером не найден",
                            Toast.LENGTH_SHORT).show();
                        } else {
                            sponsorid = s;
                            mydatabase.child("users").child(sponsorid).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    sponsor = dataSnapshot.getValue(User.class);
                                    if (sponsor == null) {
                                        Toast.makeText(signupActivity2.this, "Спонсор с таким номером телефона не найден",
                                        Toast.LENGTH_SHORT).show();

                                    } else
                                        userregstep1();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(signupActivity2.this, "Ошибка соединения",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(signupActivity2.this, "Ошибка соединения",
                        Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void userregstep1() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Buffer.user = new User(currentUser.getUid(), currentUser.getPhoneNumber());
        Buffer.user.setSponsorlogin(sponsorPhone.getText().toString());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        Buffer.user.setDateOfReg(date);
        Buffer.user.setSponsorid(sponsorid);
        Buffer.user.setSponsor2id(sponsor.getSponsorid());
        Buffer.user.setSponsor3id(sponsor.getSponsor2id());
        Buffer.user.setSponsor4id(sponsor.getSponsor3id());
        Buffer.user.setSponsor5id(sponsor.getSponsor4id());

        updateUI();
        /*UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
               .setDisplayName(Buffer.user.getSponsorlogin()).build();


        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            updateUI();


                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(signupActivity2.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                }); */


    }


    private void updateUI() {

        Map<String, Object> neweventValues = Buffer.user.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/users/" + Buffer.user.getUseruid(), neweventValues);
        //childUpdates.put("/partners/" + spronsorid + "/" + Buffer.user.getUseruid(), neweventValues);
        childUpdates.put("/logins/" + Buffer.user.getLogin(), Buffer.user.getUseruid());


        mydatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Write was successful!
                // ...
                Toast.makeText(signupActivity2.this, "Успешно создан профиль!",
                        Toast.LENGTH_SHORT).show();

                startActivity(new Intent(signupActivity2.this, signupActivity3.class));


                finish();


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                        Toast.makeText(signupActivity2.this, "Ошибка.Профиль не создан",
                                Toast.LENGTH_SHORT).show();
                    }
                });


    }

}

