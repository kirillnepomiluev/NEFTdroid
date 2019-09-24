package com.example.android.project1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class signupActivity extends AppCompatActivity {

    private static final String TAG = "PhoneAuth";

    private FirebaseAuth mAuth;
    private EditText phone;
    private Button getsmsButton;
    private EditText codeText;
    private Button verifyButton;
    private TextView notsmstext;
    private DatabaseReference myRef;
    private DatabaseReference mydatabase;

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private final static int REQUEST_CODE_READ_CONTACTS =1;



    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser !=null) {
            mydatabase.child("users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    Buffer.user  = snapshot.getValue(User.class);

                    if (Buffer.user==null) {
                        startActivity(new Intent(signupActivity.this, signupActivity2.class));
                        finish();

                    }
                    else {

                        Intent intent = new Intent(signupActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }



                }

                public void onCancelled(DatabaseError databaseError) {
                    Log.i(TAG, "loadPost:onCancelled", databaseError.toException());
                }

            });


        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        mydatabase= FirebaseDatabase.getInstance().getReference();

        phone = (EditText) findViewById(R.id.editText_phone);
        getsmsButton= (Button) findViewById(R.id.getsmsbtn);
        codeText = (EditText) findViewById(R.id.editText_cod);
        verifyButton = (Button) findViewById(R.id.button_reg);
        notsmstext = (TextView) findViewById(R.id.notsmstext);

        verifyButton.setEnabled(false);

        getsmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionStatus = ContextCompat.checkSelfPermission(signupActivity.this, Manifest.permission.READ_CONTACTS);

                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                    sendCode();
                } else {
                    ActivityCompat.requestPermissions(signupActivity.this, new String[] {Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
                }


            }
        });

        notsmstext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendCode(v);
            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode(v);
            }
        });


    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case REQUEST_CODE_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendCode();
                }
                break;
        }
    }


    public void sendCode() {



        String phoneNumber = phone.getText().toString();

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks);
    }

    private void setUpVerificatonCallbacks() {

        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(
                            PhoneAuthCredential credential) {


                        verifyButton.setEnabled(false);
                        codeText.setText("");
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            Log.d(TAG, "Invalid credential: "
                                    + e.getLocalizedMessage());
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded
                            Log.d(TAG, "SMS Quota exceeded.");
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {

                        phoneVerificationId = verificationId;
                        resendToken = token;

                        verifyButton.setEnabled(true);
                        verifyButton.setVisibility(View.VISIBLE);
                        codeText.setVisibility(View.VISIBLE);
                        getsmsButton.setEnabled(false);

                    }
                };
    }

    public void verifyCode(View view) {

        String code = codeText.getText().toString();

        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(phoneVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            codeText.setText("");

                            verifyButton.setEnabled(false);
                            FirebaseUser user = task.getResult().getUser();
                            updateUI(user);


                        } else {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    public void resendCode(View view) {

        String phoneNumber = phone.getText().toString();

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks,
                resendToken);
    }

    private void updateUI (FirebaseUser user) {
        if (user==null) { return;}
        if (Buffer.forgetPin){
            Buffer.forgetPin = false;
            startActivity(new Intent(signupActivity.this, signupActivity3.class));
            finish();
        }






        mydatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot) {




                Buffer.user  = snapshot.getValue(User.class);


                if (Buffer.user==null) {

                startActivity(new Intent(signupActivity.this, signupActivity2.class));
                finish();

                }
                else {

                    Intent intent = new Intent(signupActivity.this, MainActivity.class);
                    startActivity(intent);
                   finish();
               }



            }

            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "loadPost:onCancelled", databaseError.toException());
            }

        });


    }

}
