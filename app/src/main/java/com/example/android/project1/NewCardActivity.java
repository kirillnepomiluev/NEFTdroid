package com.example.android.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashSet;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;
import ru.yandex.money.android.sdk.Amount;
import ru.yandex.money.android.sdk.Checkout;
import ru.yandex.money.android.sdk.PaymentMethodType;
import ru.yandex.money.android.sdk.PaymentParameters;

public class NewCardActivity extends AppCompatActivity {
    private EditText cardNumber, cardDate, cardCvc;
    private DatabaseReference mydatabase;
    private Button buttonSaveCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cardNumber = findViewById(R.id.card_number);
        Slot[] slotsDate = new UnderscoreDigitSlotsParser().parseSlots("__/__");
        FormatWatcher formatWatcherDate = new MaskFormatWatcher(MaskImpl.createTerminated(slotsDate));
        FormatWatcher formatWatcherCardNumber = new MaskFormatWatcher(MaskImpl.createTerminated(PredefinedSlots.CARD_NUMBER_STANDARD));
        formatWatcherDate.installOn((EditText) findViewById(R.id.month_year));
        formatWatcherCardNumber.installOn(cardNumber);
        buttonSaveCard = findViewById(R.id.buttonSaveCard);
        cardDate = findViewById(R.id.month_year);
        cardCvc = findViewById(R.id.cvc);
        buttonSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardMask = cardNumber.getText().toString();
                //String bin = cardMask.substring(0,2);
                cardMask = cardMask.substring(cardMask.length()-4);
                mydatabase= FirebaseDatabase.getInstance().getReference();
                mydatabase.child("/cards/" + Buffer.user.getUseruid() + "/" + cardMask).setValue("");
                cardNumber.setText("");
                cardCvc.setText("");
                cardDate.setText("");
                timeToStartCheckout();
                /*Intent intent = new Intent(NewCardActivity.this, RefillActivity.class);
                startActivity(intent);
                finish();*/
            }
        });



    }

    private void timeToStartCheckout() {
        HashSet<PaymentMethodType> pmt = new HashSet<>();
        pmt.add(PaymentMethodType.BANK_CARD);
        PaymentParameters paymentParameters = new PaymentParameters(
                new Amount(BigDecimal.TEN, Currency.getInstance("RUB")),
                "Название товара",
                "Описание товара",
                "live_AAAAAAAAAAAAAAAAAAAA",
                "12345",  pmt
        );
        Intent intent = Checkout.createTokenizeIntent(this, paymentParameters);
        startActivityForResult(intent, 1);
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
