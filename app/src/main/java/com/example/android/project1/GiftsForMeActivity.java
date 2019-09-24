package com.example.android.project1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GiftsForMeActivity extends AppCompatActivity {

    private TextView category, giftsForMe, giftsFromMe;
    private ImageButton backBtn;
    private FirebaseDatabase database;
    private DatabaseReference mydatabase;
    private RecyclerView recyclerView;
    private List<Present> presentsForMe;
    private PresentAdapter presentAdapter;
    private static final String TAG = "RecyclerViewExample";

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts_for_me);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mydatabase= database.getReference("presents").child(user.getUid());

        backBtn = (ImageButton)findViewById(R.id.back_btn);
        category = (TextView)findViewById(R.id.category);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GiftsForMeActivity.this, GiftsActivity.class);
                startActivity(intent);
            }
        });
        giftsForMe = (TextView)findViewById(R.id.for_me);
        giftsForMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GiftsForMeActivity.this, GiftsForMeActivity.class);
                startActivity(intent);
            }
        });

        giftsFromMe = (TextView)findViewById(R.id.from_me);
        giftsFromMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GiftsForMeActivity.this, GiftsFromMeActivity.class);
                startActivity(intent);
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GiftsForMeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.presents_for_me);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);

        presentsForMe = new ArrayList<>();
        mydatabase.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                presentsForMe = new ArrayList<>();

                if (snapshot.exists()) {

                    for (DataSnapshot snapshot1 :
                            snapshot.getChildren()) {

                        Present element = snapshot1.getValue(Present.class);
                        if (element!= null && element.getReceiverId().equals(user.getUid()))

                            presentsForMe.add(element);



                    }
                }

                updateList();

            }

            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "loadPost:onCancelled", databaseError.toException());
            }

        });

    }

    private void updateList() {

        PresentAdapter.OnPresentClickListener onPresentClickListener = new PresentAdapter.OnPresentClickListener() {
            @Override
            public void onPresentClick(Present present) {
                Buffer.present = present;
                Intent intent = new Intent(GiftsForMeActivity.this, PresentActivity.class);
                startActivity(intent);
            }
        };

            presentAdapter = new PresentAdapter(presentsForMe, onPresentClickListener);
            recyclerView.setAdapter(presentAdapter);
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

