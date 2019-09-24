package com.example.android.project1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference mydatabase;
    private ImageButton backBtn, addFriendBtn;
    private Button activeFriends, notActiveFriends;
    private TextView notFriendsText, notFriendsCategoryText;
    private RelativeLayout friendsLayout;
    private RecyclerView recyclerView;
    private List<User> friendList;
    private List <User> partnerList;
    private FriendAdapter friendAdapter;
    private static final String TAG = "RecyclerViewExample";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        database = FirebaseDatabase.getInstance();
        mydatabase= database.getReference("users");


        recyclerView = (RecyclerView) findViewById(R.id.friends_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);

        backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        addFriendBtn = (ImageButton) findViewById(R.id.add_friend_btn);
        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendsActivity.this, InviteActivity.class);
                startActivity(intent);
                finish();
            }
        });

        friendList = new ArrayList<>();
        mydatabase.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                friendList = new ArrayList<>();
                Buffer.allUsers = new ArrayList<>();

                if (snapshot.exists()) {

                    for (DataSnapshot snapshot1 :
                            snapshot.getChildren()) {

                        User element = snapshot1.getValue(User.class);
                        if (element!= null ){
                            Buffer.allUsers.add(element);
                            if (element.getSponsorid().equals(Buffer.user.getUseruid()))
                                friendList.add(element);
                            if (element.getSponsor2id()!= null && element.getSponsor2id().equals(Buffer.user.getUseruid()))
                                friendList.add(element);
                            if (element.getSponsor3id()!= null && element.getSponsor3id().equals(Buffer.user.getUseruid()))
                                friendList.add(element);
                        }


                    }
                }
                updateList();

            }

            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "loadPost:onCancelled", databaseError.toException());
            }

        });

        friendsLayout = (RelativeLayout)findViewById(R.id.friends_layout);
        notFriendsText = (TextView) findViewById(R.id.text_info);
        notFriendsCategoryText = (TextView) findViewById(R.id.not_active_friends_text);
        activeFriends = (Button) findViewById(R.id.active_friends);
        activeFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeFriends.setBackgroundResource(R.drawable.button_left_active);
                notActiveFriends.setBackgroundResource(R.drawable.button_right);
                List<User> activeFriendsList = new ArrayList<>();
                for (User friend : friendList){
                    if (friend.getStatusend() > System.currentTimeMillis()){
                        activeFriendsList.add(friend);
                    }
                }
                if (activeFriendsList.isEmpty()){
                    notFriendsCategoryText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                }else {
                    friendAdapter = new FriendAdapter(activeFriendsList);
                    recyclerView.setAdapter(friendAdapter);
                    notFriendsCategoryText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
        notActiveFriends = (Button) findViewById(R.id.not_active_friends);
        notActiveFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeFriends.setBackgroundResource(R.drawable.button_left);
                notActiveFriends.setBackgroundResource(R.drawable.button_right_active);
                List<User> notActiveFriendsList = new ArrayList<>();
                for (User friend : friendList){
                    if (friend.getStatusend()< System.currentTimeMillis()){
                        notActiveFriendsList.add(friend);
                    }
                }
                if (notActiveFriendsList.isEmpty()){
                    notFriendsCategoryText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                }else {
                    friendAdapter = new FriendAdapter(notActiveFriendsList);
                    recyclerView.setAdapter(friendAdapter);
                    notFriendsCategoryText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });


    }



    private void updateList(){
        if (friendList.size() == 0) {
            notFriendsText.setVisibility(View.VISIBLE);
            friendsLayout.setVisibility(View.GONE);
        }else {
            notFriendsText.setVisibility(View.GONE);
            friendsLayout.setVisibility(View.VISIBLE);
        }
        List<User> activeFriendsList = new ArrayList<>();
        for (User friend : friendList){
            if (friend.getStatusend() > System.currentTimeMillis()){
                activeFriendsList.add(friend);
            }
        }
        if (activeFriendsList.isEmpty()){
            notFriendsCategoryText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        }else {
            friendAdapter = new FriendAdapter(activeFriendsList);
            recyclerView.setAdapter(friendAdapter);
            notFriendsCategoryText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
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
