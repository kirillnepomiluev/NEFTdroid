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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GiftsActivity extends AppCompatActivity {

    private TextView category, giftsForMe, giftsFromMe;
    private ImageButton backBtn;
    private FirebaseDatabase database;
    private DatabaseReference mydatabase;
    private RecyclerView recyclerView;
    private List<GiftCategory> giftCategories;
    private GiftCategoryAdapter giftCategoryAdapter;
    private static final String TAG = "RecyclerViewExample";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts);

        database = FirebaseDatabase.getInstance();
        mydatabase= database.getReference("gifts").child("gifts_category").child("categories");

        backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GiftsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        category = (TextView)findViewById(R.id.category);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GiftsActivity.this, GiftsActivity.class);
                startActivity(intent);
            }
        });
        giftsForMe = (TextView)findViewById(R.id.for_me);
        giftsForMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GiftsActivity.this, GiftsForMeActivity.class);
                startActivity(intent);
            }
        });

        giftsFromMe = (TextView)findViewById(R.id.from_me);
        giftsFromMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GiftsActivity.this, GiftsFromMeActivity.class);
                startActivity(intent);
            }
        });


        recyclerView = (RecyclerView)findViewById(R.id.gifts_category_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);

        giftCategories = new ArrayList<>();
        mydatabase.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                giftCategories = new ArrayList<>();

                if (snapshot.exists()) {

                    for (DataSnapshot snapshot1 :
                            snapshot.getChildren()) {

                        GiftCategory element = snapshot1.getValue(GiftCategory.class);
                        if (element!= null)

                            giftCategories.add(element);


                    }
                }

                updateList();

            }

            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "loadPost:onCancelled", databaseError.toException());
            }

        });

    }

    private void updateList(){
        GiftCategoryAdapter.OnGiftCategoryClickListener onGiftCategoryClickListener = new GiftCategoryAdapter.OnGiftCategoryClickListener() {
            @Override
            public void onGiftCategoryClick(GiftCategory giftCategory) {
                Buffer.giftCategory = giftCategory;
                Intent intent = new Intent(GiftsActivity.this, GiftCategoryActivity.class);
                startActivity(intent);
            }
        };

        giftCategoryAdapter = new GiftCategoryAdapter(giftCategories, onGiftCategoryClickListener);
        recyclerView.setAdapter(giftCategoryAdapter);




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
