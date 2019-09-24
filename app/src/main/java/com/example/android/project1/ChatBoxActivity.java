package com.example.android.project1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatBoxActivity extends AppCompatActivity
     implements NavigationView.OnNavigationItemSelectedListener{

    private DatabaseReference mydatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private StorageReference usersref;
    private StorageReference picref;
    private StorageReference audioref;

    private ImageButton cameraBtn,menuBtn,pointsBtn;
    private EditText textfield;
    private Button sendBtn, voiceBtn;
    private LinearLayout mlayout;
    private String id;
    private Chat chat;
    private ArrayList<Message> messages;
    private ProgressBar progressBar;
    private static final String TAG = "RecyclerViewExample";
    private static final String LOG_TAG = "Record_log";
    private SimpleDateFormat df;
    private static final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE =1;
    private static final int REQUEST_PERMISSION_RECORD = 15;
    private static final int GALLERY_REQUEST = 1;
    private static final int RECORD_AUDIO = 2;

    String key;
    private String typeMessage;
    private String imageUrl;
    private Uri fileUri;
    private StorageTask uploadTask;
    private ImageDialog dlgimage;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private String recordFilePath;
    private String audioUrl;
    private int displayWidth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        mydatabase= FirebaseDatabase.getInstance().getReference();
        user= mAuth.getCurrentUser();

        isPlaying= false;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        displayWidth = size.x;

        menuBtn = (ImageButton)findViewById(R.id.menu_btn);
        menuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.openDrawer(GravityCompat.START);

                }
            });



        chat = new Chat();

        Buffer.curuser = new User();
        Buffer.curuser.setLogin("admin");
        Buffer.curuser.setUseruid(Buffer.adminUserId);
        df= new SimpleDateFormat("HH:mm");


        mlayout = (LinearLayout) findViewById(R.id.messageboxla);
        cameraBtn = (ImageButton) findViewById(R.id.iconcamera);
        pointsBtn =(ImageButton) findViewById(R.id.pointsicon);
        sendBtn = (Button) findViewById(R.id.send_btn);
        sendBtn.setVisibility(View.INVISIBLE);
        voiceBtn = (Button) findViewById(R.id.voice_btn);
        voiceBtn.setVisibility(View.VISIBLE);
        textfield =(EditText) findViewById(R.id.textfield);
        textfield.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String val=charSequence.toString();
                if (val.isEmpty()){
                    sendBtn.setVisibility(View.GONE);
                    voiceBtn.setVisibility(View.VISIBLE);
                } else {
                    sendBtn.setVisibility(View.VISIBLE);
                    voiceBtn.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        textfield.setOnEditorActionListener(editorListener);

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);




        String sr = Buffer.curuser.getUseruid();


        key = Buffer.user.getUseruid();
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionStatus = ContextCompat.checkSelfPermission(ChatBoxActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                    showImageChooser();
                } else {
                    ActivityCompat.requestPermissions(ChatBoxActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE);
                }

            }
        });



        pointsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessage();
                sendBtn.setVisibility(View.INVISIBLE);
                voiceBtn.setVisibility(View.VISIBLE);

            }
        });

        voiceBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                    int x = (int) event.getX();
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        startRecording();
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        Log.i("TAG", "moving: (" + displayWidth + ", " + x + ")");
                        if (mRecorder != null && textfield.getText().toString().trim().isEmpty()) {
                            if (Math.abs(event.getX()) / displayWidth > 0.35f) {
                                stopRecording(false);
                                Toast.makeText(ChatBoxActivity.this, "Запись отменена", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        stopRecording(true);
                    }


                return false;
                }

        });

        id = Buffer.user.getUseruid().compareTo(Buffer.curuser.getUseruid())>0 ?Buffer.user.getUseruid()+Buffer.curuser.getUseruid():Buffer.curuser.getUseruid()+Buffer.user.getUseruid();

        messages = new ArrayList<>();

        mydatabase.child("messages").child(id).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                messages = new ArrayList<>();

                if (snapshot.exists()) {

                    for (DataSnapshot snapshot1 :
                            snapshot.getChildren()) {

                        Message element = snapshot1.getValue(Message.class);
                        if (element!= null)

                            messages.add(element);


                    }
                }
                updateUI();
                progressBar.setVisibility(View.GONE);

            }

            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "loadPost:onCancelled", databaseError.toException());
            }

        });




    }

    private TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            switch (i){
                case EditorInfo.IME_ACTION_SEND:
                    sendmessage();
                    break;
            }
            return false;
        }
    };



    private void updateUI() {
        mlayout.removeAllViews();
        for (final Message mes : messages)
            if (mes.getType().equals("text")) {

                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);

                TextView tv = new TextView(this);
                tv.setTextIsSelectable(true);
                tv.setText(mes.getMessage());
                TextView time = new TextView(this);
                time.setText(df.format(new Date(mes.getTime())));


                LinearLayout.LayoutParams lParamstv = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                LinearLayout.LayoutParams lParamstime = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                lParamstv.gravity = Gravity.LEFT;
                lParamstime.gravity = Gravity.RIGHT;


                int dip = 270;
                float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
                dip = (int) px;

                int pad = 18;
                px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pad, getResources().getDisplayMetrics());
                pad = (int) px;

                lParamstv.setMargins(pad, pad, pad, 0);
                lParamstime.setMargins(0, 0, pad / 4, pad / 4);

                layout.addView(tv, lParamstv);
                layout.addView(time, lParamstime);


                // try {
                //     Typeface typeface = getResources().getFont(R.font.montserratregular);
                //    tv.setTypeface(typeface);
                //  } catch (Exception e) {}


                tv.setTextColor(ContextCompat.getColor(this, R.color.Gray));
                tv.setTextSize(15);
                time.setTextSize(10);


                LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                mlayout.addView(layout, lParams);

                if (Buffer.user.getUseruid().equals(mes.getSendorId())) {
                    lParams.gravity = Gravity.RIGHT;
                    lParams.setMargins(4 * pad, pad, pad, 0);
                    layout.setLayoutParams(lParams);
                    layout.setBackgroundResource(R.drawable.background_right);

                } else {
                    lParams.setMargins(pad, pad, 4 * pad, 0);
                    lParams.gravity = Gravity.START;
                    layout.setLayoutParams(lParams);
                    layout.setBackgroundResource(R.drawable.background_left);
                }
                tv.setFocusableInTouchMode(true);
                tv.requestFocus();
                textfield.setFocusableInTouchMode(true);
                textfield.requestFocus();


            } else if (mes.getType().equals("image")) {

                LinearLayout layout = new LinearLayout(this);
                //layout.setBackgroundResource(R.color.transparent);
                layout.setOrientation(LinearLayout.VERTICAL);

                final ImageView iv = new ImageView(this);
                iv.setClickable(true);
                Picasso.get().load(mes.getMessage())
                        .into(iv, new Callback() {
                            @Override
                            public void onSuccess() {
                                iv.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(ChatBoxActivity.this, "ошибка.изображение не загружено",
                                        Toast.LENGTH_SHORT).show();


                            }

                        });


                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Buffer.imageUrl = mes.getMessage();
                        dlgimage = new ImageDialog();
                        dlgimage.show(getFragmentManager(), imageUrl);

                    }
                });
                TextView time = new TextView(this);
                time.setText(df.format(new Date(mes.getTime())));

                int dip = 200;
                float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
                dip = (int) px;

                LinearLayout.LayoutParams lParamsiv = new LinearLayout.LayoutParams(dip,
                        dip);

                LinearLayout.LayoutParams lParamstime = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                lParamsiv.gravity = Gravity.LEFT;
                lParamstime.gravity = Gravity.RIGHT;


                int pad = 18;
                px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pad, getResources().getDisplayMetrics());
                pad = (int) px;

                lParamsiv.setMargins(pad, pad, pad, 0);
                lParamstime.setMargins(0, 0, pad / 4, pad / 4);

                layout.addView(iv, lParamsiv);
                layout.addView(time, lParamstime);
                time.setTextSize(10);

                LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);


                mlayout.addView(layout, lParams);


                if (Buffer.user.getUseruid().equals(mes.getSendorId())) {
                    lParams.gravity = Gravity.RIGHT;
                    lParams.setMargins(4 * pad, pad, pad, 0);
                    layout.setLayoutParams(lParams);
                    layout.setBackgroundResource(R.drawable.background_right);


                } else {
                    lParams.setMargins(pad, pad, 4 * pad, 0);
                    lParams.gravity = Gravity.START;
                    layout.setLayoutParams(lParams);
                    layout.setBackgroundResource(R.drawable.background_left);

                }
                iv.setFocusableInTouchMode(true);
                iv.requestFocus();
                textfield.setFocusableInTouchMode(true);
                textfield.requestFocus();

            } else if (mes.getType().equals("audio")) {

                LinearLayout layout = new LinearLayout(this);
                //layout.setBackgroundResource(R.color.transparent);
                layout.setOrientation(LinearLayout.VERTICAL);
                final ImageButton button = new ImageButton(this);
                button.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                button.setBackgroundResource(R.color.transparent);
                button.setPadding(20, 10, 20, 10);





                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isPlaying) {
                            try {
                                button.setImageResource(R.drawable.ic_stop_black_24dp);
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setDataSource(Buffer.audioUrl = mes.getMessage());



                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        button.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                                        isPlaying = false;
                                    }
                                });
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                isPlaying = true;

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if(mediaPlayer != null && isPlaying) {
                                mediaPlayer.stop();
                                mediaPlayer.release();
                                mediaPlayer = null;
                                isPlaying = false;
                                button.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                        }

                    }
                });
                TextView time = new TextView(this);
                time.setText(df.format(new Date(mes.getTime())));

                int dip = 50;
                float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
                dip = (int) px;

                LinearLayout.LayoutParams lParamsib = new LinearLayout.LayoutParams(dip,
                        dip);

                LinearLayout.LayoutParams lParamstime = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                lParamsib.gravity = Gravity.LEFT;
                lParamstime.gravity = Gravity.RIGHT;


                int pad = 18;
                px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pad, getResources().getDisplayMetrics());
                pad = (int) px;

                lParamsib.setMargins(pad, pad, pad, 0);
                lParamstime.setMargins(0, 0, pad / 4, pad / 4);

                layout.addView(button, lParamsib);
                layout.addView(time, lParamstime);
                time.setTextSize(10);

                LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);


                mlayout.addView(layout, lParams);


                if (Buffer.user.getUseruid().equals(mes.getSendorId())) {
                    lParams.gravity = Gravity.RIGHT;
                    lParams.setMargins(4 * pad, pad, pad, 0);
                    layout.setLayoutParams(lParams);
                    layout.setBackgroundResource(R.drawable.background_right);


                } else {
                    lParams.setMargins(pad, pad, 4 * pad, 0);
                    lParams.gravity = Gravity.START;
                    layout.setLayoutParams(lParams);
                    layout.setBackgroundResource(R.drawable.background_left);

                }
                textfield.setFocusableInTouchMode(true);
                textfield.requestFocus();

            }
    }

    private void sendmessage() {
        typeMessage = "text";
        if (!textfield.getText().toString().isEmpty()) {
            Message message = new Message(Buffer.user.getUseruid(), textfield.getText().toString(), typeMessage);
            Map<String, Object> messagemap = message.toMap();

            Chat chat = new Chat(Buffer.user, Buffer.curuser, message);
            Chat chat1 = new Chat(Buffer.curuser, Buffer.user, message);
            Map<String, Object> chatmap = chat.toMap();
            Map<String, Object> chat1map = chat1.toMap();

            Map<String, Object> childUpdates = new HashMap<>();

            childUpdates.put("/messages/" + id + "/" + message.getId(), messagemap);
            childUpdates.put("/chats/" + Buffer.user.getUseruid() + "/" + id, chatmap);
            childUpdates.put("/chats/" + Buffer.curuser.getUseruid() + "/" + id, chat1map);

            mydatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    textfield.setText("");
                    // Write was successful!
                    // ...

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            // Write failed
                            // ...
                            Toast.makeText(ChatBoxActivity.this, "ошибка.Сообщение не отправлено",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        } else
            Toast.makeText(ChatBoxActivity.this, "Введите сообщение",
                    Toast.LENGTH_SHORT).show();
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showImageChooser();
                }
                break;
            case REQUEST_PERMISSION_RECORD:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startRecording();
                }
                break;
        }
    }

    private void showImageChooser(){

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {

                    fileUri = imageReturnedIntent.getData();

                    uploadpic();

                }
        }
    }

    private void uploadpic() {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        picref = FirebaseStorage.getInstance().getReference().child("Message media").child(id).child("image").child(timeStamp + ".jpg");

        uploadTask = picref.putFile(fileUri);
        uploadTask.continueWithTask(new Continuation(){
            @Override
            public Object then(@NonNull Task task) throws Exception {

                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return picref.getDownloadUrl();
            }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task <Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();

                    typeMessage = "image";
                    Message message = new Message(Buffer.user.getUseruid() , imageUrl, typeMessage);
                    Map<String, Object> messagemap = message.toMap();

                    Chat chat = new Chat(Buffer.user, Buffer.curuser, message);
                    Chat chat1 = new Chat(Buffer.curuser , Buffer.user, message);
                    Map<String, Object> chatmap = chat.toMap();
                    Map<String, Object> chat1map = chat1.toMap();

                    Map<String, Object> childUpdates = new HashMap<>();

                    childUpdates.put("/messages/" + id+"/"+message.getId(), messagemap);
                    childUpdates.put("/chats/" +Buffer.user.getUseruid()+"/"+ id, chatmap);
                    childUpdates.put("/chats/" +Buffer.curuser.getUseruid()+"/"+ id, chat1map);

                    mydatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            textfield.setText("");
                            Toast.makeText(ChatBoxActivity.this, "Изображение отправлено",
                                    Toast.LENGTH_SHORT).show();

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    // Write failed
                                    // ...
                                    Toast.makeText(ChatBoxActivity.this, "ошибка.Изображение не отправлено",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }

    private void startRecording(){
        int permissionStatus = ContextCompat.checkSelfPermission(ChatBoxActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            File recordFile = new File(this.getExternalFilesDir(null), "/" + getString(R.string.app_name) + "/audio/.sent/");
            boolean dirExists = recordFile.exists();
            if (!dirExists)
                dirExists = recordFile.mkdirs();
            if (dirExists) {
                try {
                    recordFile = new File(this.getExternalFilesDir(null) + "/" + getString(R.string.app_name) + "/audio/.sent/", System.currentTimeMillis() + ".mp3");
                    if (!recordFile.exists())
                        recordFile.createNewFile();
                    recordFilePath = recordFile.getAbsolutePath();
                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mRecorder.setOutputFile(recordFilePath);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    mRecorder.prepare();
                    mRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    mRecorder = null;
                } catch (IllegalStateException ex) {
                    ex.printStackTrace();
                    mRecorder = null;
                }
            }
        }else {
            ActivityCompat.requestPermissions(ChatBoxActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION_RECORD);
        }

    }

    private void stopRecording(boolean send) {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        } catch (IllegalStateException ex) {
            mRecorder = null;
        } catch (RuntimeException ex) {
            mRecorder = null;
        }
        if (send) {
            uploadAudio(recordFilePath);
        } else {
            new File(recordFilePath).delete();
        }
    }




    private void uploadAudio(String filePath){
        String timeStamp = String.valueOf(System.currentTimeMillis());
        audioref = FirebaseStorage.getInstance().getReference().child("Message media").child(id).child("audio").child(timeStamp + ".mp4");

        Uri uri = Uri.fromFile(new File(filePath));
        uploadTask = audioref.putFile(uri);
        uploadTask.continueWithTask(new Continuation(){
            @Override
            public Object then(@NonNull Task task) throws Exception {

                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return audioref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task <Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    audioUrl = downloadUri.toString();

                    typeMessage = "audio";
                    Message message = new Message(Buffer.user.getUseruid() , audioUrl, typeMessage);
                    Map<String, Object> messagemap = message.toMap();

                    Chat chat = new Chat(Buffer.user, Buffer.curuser, message);
                    Chat chat1 = new Chat(Buffer.curuser , Buffer.user, message);
                    Map<String, Object> chatmap = chat.toMap();
                    Map<String, Object> chat1map = chat1.toMap();

                    Map<String, Object> childUpdates = new HashMap<>();

                    childUpdates.put("/messages/" + id+"/"+message.getId(), messagemap);
                    childUpdates.put("/chats/" +Buffer.user.getUseruid()+"/"+ id, chatmap);
                    childUpdates.put("/chats/" +Buffer.curuser.getUseruid()+"/"+ id, chat1map);

                    mydatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            textfield.setText("");
                            Toast.makeText(ChatBoxActivity.this, "Голосовое сообщение отправлено",
                                    Toast.LENGTH_SHORT).show();

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    // Write failed
                                    // ...
                                    Toast.makeText(ChatBoxActivity.this, "ошибка.Сообщение не отправлено",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });


    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, profileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_news) {
            Intent intent = new Intent(this, NewsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_ref) {
            Intent intent = new Intent(this, InviteActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(this, FeedbackActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_tarif) {
            Intent intent = new Intent(this, PaymentActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_faq) {
            Intent intent = new Intent(this, FAQActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_chat) {
            Intent intent = new Intent(this, ChatBoxActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_offer) {
            Intent intent = new Intent(this, OfferActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_partners) {
            Intent intent = new Intent(this, PartnersActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
            String title = "Выйти из профиля?";
            String info = "";
            String yesText = "Выйти";
            String noText = "Пропустить";
            CustomDialogClass cdd = new CustomDialogClass(ChatBoxActivity.this, title, info, yesText, noText);
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cdd.show();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
  /*    @Override
    protected void onResume() {
        super.onResume();
        long currentTime = new Date().getTime();
        long timeDifference = currentTime - Buffer.stopTime;
        if (timeDifference >= 180000 || !Buffer.checkPassword){
            startActivity(new Intent(AboutActivity.this, SighInActivity.class));
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
