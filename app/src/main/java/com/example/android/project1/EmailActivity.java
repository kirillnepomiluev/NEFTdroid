package com.example.android.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EmailActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mydatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Button checkEmail, confirmEmail;
    private ImageButton endBtn;
    private EditText email, code;
    private TextView emailText, sendAgain;
    private LinearLayout enterEmailLayout, confirmationLayout;
    private String userEmail;
    private int userCode;

    @Override
    public void onStart() {
        super.onStart();
        mydatabase.child("verifyEmail").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Buffer.verifyEmail = dataSnapshot.getValue(VerifyEmail.class);
                if (Buffer.verifyEmail != null){
                    enterEmailLayout.setVisibility(View.GONE);
                    confirmationLayout.setVisibility(View.VISIBLE);
                    emailText.setText(Buffer.verifyEmail.getEmail());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        mAuth = FirebaseAuth.getInstance();
        mydatabase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();


        endBtn = (ImageButton) findViewById(R.id.end_btn);
        checkEmail = (Button) findViewById(R.id.check_email_btn);
        confirmEmail = (Button) findViewById(R.id.confirmation_email_btn);
        email = (EditText) findViewById(R.id.email);
        code = (EditText) findViewById(R.id.code);
        enterEmailLayout = (LinearLayout) findViewById(R.id.enter_email_layout);
        confirmationLayout = (LinearLayout) findViewById(R.id.confirmation_layout);
        confirmationLayout.setVisibility(View.GONE);
        emailText = (TextView)findViewById(R.id.email_text);
        sendAgain = (TextView)findViewById(R.id.send_again_tv);

        checkEmail.setOnClickListener(this);
        confirmEmail.setOnClickListener(this);
        endBtn.setOnClickListener(this);
        sendAgain.setOnClickListener(this);
        email.setOnEditorActionListener(editorListener);
        code.setOnEditorActionListener(editorListener);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.check_email_btn:
                if (email.getText().toString().isEmpty()) {
                    Toast.makeText(EmailActivity.this, "Введите свой email!", Toast.LENGTH_LONG).show();
                } else {
                    userEmail = email.getText().toString();
                    final VerifyEmail verifyEmail = new VerifyEmail(userEmail, generateCode());
                    Map<String, Object> updates = verifyEmail.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/verifyEmail/" + user.getUid(), updates);
                    mydatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Buffer.verifyEmail = verifyEmail;
                            SendMail.sendEmail(EmailActivity.this, userEmail,"Подтверждение почты", "Для подтверждения почты, введите в приложении код \n " + Buffer.verifyEmail.getCode());
                            enterEmailLayout.setVisibility(View.GONE);
                            confirmationLayout.setVisibility(View.VISIBLE);
                            emailText.setText(Buffer.verifyEmail.getEmail());
                            code.requestFocus();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(EmailActivity.this, " Ошибка!!! Данные не отправлены.",
                                    Toast.LENGTH_SHORT).show();


                        }
                    });


                }

                break;
            case R.id.confirmation_email_btn:
                if (code.getText().toString().isEmpty()) {
                    Toast.makeText(EmailActivity.this, "Введите полученный код из сообщения!" + Buffer.verifyEmail.getCode(), Toast.LENGTH_LONG).show();
                } else {
                    userCode = Integer.parseInt(code.getText().toString());
                    if (userCode == Buffer.verifyEmail.getCode()){
                        Buffer.user.setEmail(Buffer.verifyEmail.getEmail());
                        Map<String, Object> neweventValues = Buffer.user.toMap();

                        Map<String, Object> childUpdates = new HashMap<>();

                        childUpdates.put("/users/" + Buffer.user.getUseruid(), neweventValues);
                        mydatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Write was successful!
                                // ...
                                Toast.makeText(EmailActivity.this, "Почта успешно подтверждена",
                                        Toast.LENGTH_SHORT).show();


                                Intent intent = new Intent(EmailActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();


                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Write failed
                                        // ...
                                        Toast.makeText(EmailActivity.this, "Ошибка.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }else {
                        Toast.makeText(EmailActivity.this, "Код не совпадает. Попробуйте еще раз.",
                                Toast.LENGTH_SHORT).show();
                        code.setText("");
                    }
                }
                break;
            case R.id.send_again_tv:
                enterEmailLayout.setVisibility(View.VISIBLE);
                confirmationLayout.setVisibility(View.GONE);
                email.setText(Buffer.verifyEmail.getEmail());
                break;
            case R.id.end_btn:
                finish();

                break;

                }



    }
    private static int generateCode(){
        int code = 1000 * onlyPlus() + 100 * onlyPlus() + 10 * onlyPlus() + onlyPlus();
        return code;
    }

    private static int onlyPlus() {
        Random random = new Random();
        return random.nextInt(9)+1;
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
    private TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            switch (i){
                case EditorInfo.IME_ACTION_NEXT:
                    onClick(checkEmail);
                    break;
                case EditorInfo.IME_ACTION_SEND:
                    onClick(confirmEmail);
                    break;
            }
            return false;
        }
    };
}
