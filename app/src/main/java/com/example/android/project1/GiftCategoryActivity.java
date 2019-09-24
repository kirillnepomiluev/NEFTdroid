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

public class GiftCategoryActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private TextView categoryText;
    private FirebaseDatabase database;
    private DatabaseReference mydatabase;
    private RecyclerView recyclerView;
    private List <GiftItem> giftItems;
    private GiftItemAdapter giftItemAdapter;
    private static final String TAG = "RecyclerViewExample";
    private GiftCategory giftCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_category);
        giftCategory=Buffer.giftCategory;

        database = FirebaseDatabase.getInstance();
        mydatabase= database.getReference("gifts").child("gifts_category").child(giftCategory.getCategoryId());

        backBtn = (ImageButton)findViewById(R.id.back_btn);
        categoryText = (TextView)findViewById(R.id.category_text);
        categoryText.setText(Buffer.giftCategory.getTitle());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GiftCategoryActivity.this, GiftsActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.gift_item_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);

        giftItems = new ArrayList<>();
        mydatabase.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                giftItems = new ArrayList<>();

                if (snapshot.exists()) {

                    for (DataSnapshot snapshot1 :
                            snapshot.getChildren()) {

                        GiftItem element = snapshot1.getValue(GiftItem.class);
                        if (element!= null)

                            giftItems.add(element);


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
         GiftItemAdapter.OnGiftItemClickListener onGiftItemClickListener = new GiftItemAdapter.OnGiftItemClickListener() {
             @Override
             public void onGiftItemClick(GiftItem giftItem) {
                 Buffer.giftItem = giftItem;
                 Intent intent = new Intent(GiftCategoryActivity.this, GiftItemActivity.class);
                 intent.putExtra(GiftItemActivity.GIFT_ID, giftItem.getGiftItemId());
                 startActivity(intent);
             }
         };

        giftItemAdapter = new GiftItemAdapter(giftItems, onGiftItemClickListener);
        recyclerView.setAdapter(giftItemAdapter);




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
