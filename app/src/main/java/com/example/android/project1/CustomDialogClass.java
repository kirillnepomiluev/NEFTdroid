package com.example.android.project1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public String title, info, yesText, noText;
    private FirebaseAuth mAuth;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }
    public CustomDialogClass(Activity a, String title, String info, String yesText, String noText) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.title = title;
        this.info = info;
        this.yesText = yesText;
        this.noText = noText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        yes = (Button) findViewById(R.id.yes_btn);
        if (yesText!=null){
            yes.setText(yesText);
        }
        no = (Button) findViewById(R.id.continue_btn);
        if (noText!=null){
            no.setText(noText);
        }
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        if (title!=null){
            ((TextView)findViewById(R.id.title)).setText(title);

        }
        if (info!=null){
            ((TextView)findViewById(R.id.info_text)).setText(info);

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes_btn:
                if (yes.getText().equals("Выйти")){
                    mAuth.signOut();
                    final Intent intent = new Intent(c, signupActivity.class);
                    c.startActivity(intent);
                    c.finish();
                }
                if (yes.getText().equals("Заказать")){
                    final Intent intent = new Intent(c, OrderCardActivity.class);
                    c.startActivity(intent);
                }
                if (yes.getText().equals("Оплатить")){
                    final Intent intent = new Intent(c, PaymentActivity.class);
                    c.startActivity(intent);
                }
                if (yes.getText().equals("Подтвердить email")){
                    final Intent intent = new Intent(c, EmailActivity.class);
                    c.startActivity(intent);
                }
                if (yes.getText().equals("Верификация")){
                    final Intent intent = new Intent(c, verificationActivity.class);
                    c.startActivity(intent);
                }
                if (yes.getText().equals("Профиль")){
                    final Intent intent = new Intent(c, redactProfileActivity.class);
                    c.startActivity(intent);
                }
                c.finish();


                break;
            case R.id.continue_btn:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

}
