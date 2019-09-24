package com.example.android.project1;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SighInActivity extends AppCompatActivity {

    private EditText password;
    private TextView forgotPin;
    private ImageView fingerprint;
    private DatabaseReference mydatabase;
    private FirebaseAuth mAuth;
    private CancellationSignal cancellationSignal;

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private final static int REQUEST_CODE_READ_CONTACTS =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigh_in);

        mAuth = FirebaseAuth.getInstance();
        mydatabase = FirebaseDatabase.getInstance().getReference();

        password = (EditText)findViewById(R.id.user_pin);
        password.setImeOptions(EditorInfo.IME_ACTION_DONE);

        password.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                if (password.getText().toString().length()==4)
                checkPassword();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        forgotPin = (TextView)findViewById(R.id.forgot_pin);
        forgotPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(SighInActivity.this, ResetPasswordActivity.class));
                Buffer.forgetPin = true;
                mAuth.signOut();
                startActivity(new Intent(SighInActivity.this, signupActivity.class));
                finish();

            }
        });
        fingerprint = (ImageView)findViewById(R.id.fingerprint);

        checkBiometricSupport();
        Executor executor = Executors.newSingleThreadExecutor();

        FragmentActivity activity = this;

        final BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationError(int errorCode,
                                                      CharSequence errString) {
                //notifyUser("Ошибка аутентификации: " + errString);
                super.onAuthenticationError(errorCode, errString);
            }


            @Override
            public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Buffer.checkPassword = true;
                        startActivity(new Intent(SighInActivity.this, MainActivity.class));
                        finish();
                    }
                });
                final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Touch ID")
                        .setSubtitle("Для продолжения требуется аутентификация")
                        .setDescription("Это приложение использует биометрическую аутентификацию для защиты ваших данных")
                        .setNegativeButtonText("Отмена")
                        .build();


                fingerprint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        biometricPrompt.authenticate(promptInfo);

            }



        });
    }

    private void checkPassword(){
        if (Buffer.user.getPassword().equals(password.getText().toString())){
            Buffer.checkPassword = true;
            Intent intent = new Intent(SighInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }else{
            Toast.makeText(SighInActivity.this, "Вы ввели неверный PIN-код",
                    Toast.LENGTH_SHORT).show();
        }

    }
    private Boolean checkBiometricSupport() {

        KeyguardManager keyguardManager =
                (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        PackageManager packageManager = this.getPackageManager();

        if (!keyguardManager.isKeyguardSecure()) {
            //notifyUser("Защита экрана не включена в настройках");
            return false;
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.USE_BIOMETRIC) !=
                PackageManager.PERMISSION_GRANTED) {

            //notifyUser("Разрешение аутентификации по отпечатку пальца не включено");
            return false;
        }

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT))
        {
            return true;
        }

        return true;
    }


    private void notifyUser(String message) {
        Toast.makeText(this,
                message,
                Toast.LENGTH_LONG).show();
    }

}
