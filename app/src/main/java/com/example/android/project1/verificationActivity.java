package com.example.android.project1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class verificationActivity extends AppCompatActivity {

    private DatabaseReference mydatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private StorageReference usersref;
    private StorageReference picref;

    private ImageButton backBtn, endBtn, addPhotoBtn;;
    private Button nextBtn;
    private ImageView selfiePhoto;
    private static final int CAMERA_REQUEST = 0;
    String key;
    String userimageurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        mAuth = FirebaseAuth.getInstance();
        mydatabase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();

        backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(verificationActivity.this, redactProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        endBtn = (ImageButton) findViewById(R.id.end_btn);
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogClass cdd=new CustomDialogClass(verificationActivity.this);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();

            }
        });
        nextBtn = (Button) findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(Buffer.user.getSelfiePhotoUrl() == null)) {

                    final Verification verification = new Verification();
                    verification.setSelfiePhotoUrl(Buffer.user.getSelfiePhotoUrl());
                    verification.setUserId(user.getUid());
                    verification.setVerificationId(user.getUid());
                    verification.setVerifyCheck(true);
                    verification.setComment("");

                    Map<String, Object> updates = verification.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/verifications/" + user.getUid(), updates);

                    mydatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Buffer.verification = verification;
                            Toast.makeText(verificationActivity.this, " Данные успешно отправлены на обработку! После проверки администратор изменит ваш статус.",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(verificationActivity.this, EmailActivity.class));
                            finish();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(verificationActivity.this, " Ошибка!!! Данные не отправлены.",
                                    Toast.LENGTH_SHORT).show();


                        }
                    });



                } else
                    startActivity(new Intent(verificationActivity.this, EmailActivity.class));
                Toast.makeText(verificationActivity.this, " Для прохождения верификации необходимо загрузить изображние паспорта и ваше фото с паспортом!",
                        Toast.LENGTH_SHORT).show();

            }
        });
        addPhotoBtn = (ImageButton)findViewById(R.id.add_photo);

        key = user.getUid();

        storage = FirebaseStorage.getInstance();
        usersref = storage.getReference();
        selfiePhoto = (ImageView) findViewById(R.id.selfie_iv);
        selfiePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            selfiePhoto.setImageBitmap(photo);
            addPhotoBtn.setVisibility(View.GONE);
            uploadpic(key);

        }

    }

    private void uploadpic(String key) {

        picref= usersref.child("userspics").child(key).child("selfiePhoto.jpg");

        // Get the data from an ImageView as bytes
        selfiePhoto.setDrawingCacheEnabled(true);
        selfiePhoto.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) selfiePhoto.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();


        UploadTask uploadTask = picref.putBytes(data);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();

                }

                // Continue with the task to get the download URL
                return picref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {

                    userimageurl =task.getResult().toString();
                    mydatabase = FirebaseDatabase.getInstance().getReference();
                    mydatabase.child("users").child(user.getUid()).child("selfiePhoto").setValue(userimageurl);
                    Buffer.user.setSelfiePhotoUrl(userimageurl);
                } else {


                    Toast.makeText(verificationActivity.this, "ошибка! Изображение  не обновлено!",
                            Toast.LENGTH_SHORT).show();

                    // Handle failures
                    // ...
                }
            }
        });


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


