package com.example.android.project1;

import android.content.Context;
//import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Buffer {
    public static User user;
    public static User curuser;
    public static User receiver;
    public static User receiverFromQr;
    public static User sponsor;
    public static Verification verification;
    public static final String adminUserId = "adminnew2019";
    public static Question question;
    public static Chat chat;
    public static final String photourlpart1 = "https://firebasestorage.googleapis.com/v0/b/neft-d9691.appspot.com/o/userspics%2F";
    public static final String photourlpart2 = ".jpg?alt=media&token=a4840188-3234-45f8-8710-55124af7c9bc";
    public static Class back;
    public static ArrayList<Operation> history;
    public static List<Question> questions;
    public static int money;
    public static String imageUrl;
    public static String audioUrl;
    public static GiftItem giftItem;
    public static GiftCategory giftCategory;
    public static Present present;
    public static ArrayList<GasStation> gasStations;
    public static GasStation gasStation;
    public static List<User> partners;
    public static List<User> allUsers;
    public static boolean checkPassword = false;
    public static boolean forgetPin;
    public static long stopTime;
    public static VerifyEmail verifyEmail;
    public static FuelCard fuelCard;
    public static GiftItem fuelCardItem;
    public static boolean isOrderDialogShow;


    public static boolean checkstatus (Context context) {
        if (user.getStatus()) return true;
        else {
            Toast.makeText(context, R.string.low_status, Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public static void moneyupdate() {
        int m=0;
        for (Operation op: history) {
            if (op.getPlus())
                m=m+op.getAmount();
            else m=m-op.getAmount();
        }
        money=m;
    }
    public static void setStopTime(){
        stopTime = new Date().getTime();

    }

}

