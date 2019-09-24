package com.example.android.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class signupActivity3 extends AppCompatActivity {

    public static String pincode;

    private Button nextBtn;
    private EditText password;
    private DatabaseReference mydatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);

        mAuth = FirebaseAuth.getInstance();
        mydatabase = FirebaseDatabase.getInstance().getReference();
        password = (EditText)findViewById(R.id.user_pin);

        nextBtn = (Button)findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createUserPassword();

            }
        });
    }

    private void createUserPassword() {


        if (password.getText().toString().isEmpty())
            Toast.makeText(signupActivity3.this, "Поле PIN - код пустое",
                    Toast.LENGTH_SHORT).show();
        else
            if (password.getText().toString().length() < 4)
                Toast.makeText(signupActivity3.this, "PIN - код менее 4 символов",
                        Toast.LENGTH_SHORT).show();
            else
            if (password.getText().toString().length() > 4)
                Toast.makeText(signupActivity3.this, "PIN - код более 4 символов",
                        Toast.LENGTH_SHORT).show();

        else {
            pincode = password.getText().toString();
                Intent intent = new Intent(signupActivity3.this, signupActivity4.class);
                startActivity(intent);
                finish();
        }


    }


}
