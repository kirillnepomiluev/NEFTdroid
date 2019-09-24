package com.example.android.project1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerActivity extends AppCompatActivity{

    private FirebaseDatabase database;
    private DatabaseReference mydatabase;
    private ImageButton backBtn;
    private RelativeLayout mainLayout, myQrLayout;
    private ImageView myQrCode;
    private Button shareBtn, qrScannerBtn, myQrBtn;
    private TextView title;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int REQUEST_CODE_SCAN = 0x0000c0de;
    private int maxQrSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        database = FirebaseDatabase.getInstance();
        mydatabase= database.getReference("users");

        backBtn = (ImageButton) findViewById(R.id.back_btn);
        mainLayout = (RelativeLayout)findViewById(R.id.main_layout);
        myQrLayout = (RelativeLayout)findViewById(R.id.generate_qr_layout);
        myQrCode = (ImageView) findViewById(R.id.qr_code);
        shareBtn = (Button) findViewById(R.id.share_btn);
        qrScannerBtn = (Button) findViewById(R.id.qr_scanner_btn);
        myQrBtn = (Button) findViewById(R.id.my_qr_btn);
        title = (TextView) findViewById(R.id.title);
        maxQrSize = LayoutUtils.calculateMaxQrCodeSize(getResources());
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScannerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mainLayout.setBackgroundColor(getResources().getColor(R.color.background));
        qrScannerBtn.setBackgroundResource(R.drawable.button_right);
        myQrBtn.setBackgroundResource(R.drawable.button_left_active);
        myQrLayout.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.my_qr));
        final String qrContent = Buffer.user.getUseruid();
        Bitmap qrCodeBitmap = Qr.bitmap(qrContent, maxQrSize);
        myQrCode.setImageBitmap(qrCodeBitmap);

        qrScannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
                myQrLayout.setVisibility(View.GONE);
                qrScannerBtn.setBackgroundResource(R.drawable.button_right_active);
                myQrBtn.setBackgroundResource(R.drawable.button_left);
                title.setText(getResources().getString(R.string.qr_scanner));
                int permissionStatus = ContextCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA);

                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                    handleScan();
                }
                else {
                    ActivityCompat.requestPermissions(ScannerActivity.this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                }

            }
        });
        myQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainLayout.setBackgroundColor(getResources().getColor(R.color.background));
                qrScannerBtn.setBackgroundResource(R.drawable.button_right);
                myQrBtn.setBackgroundResource(R.drawable.button_left_active);
                myQrLayout.setVisibility(View.VISIBLE);
                title.setText(getResources().getString(R.string.my_qr));
                //final String qrContent = CoinURI.convertToCoinURI(receiveAddress, amount, label, message);
                final String qrContent = Buffer.user.getUseruid();
                Bitmap qrCodeBitmap = Qr.bitmap(qrContent, maxQrSize);
                myQrCode.setImageBitmap(qrCodeBitmap);

            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.TEXT", R.string.share_text);
                startActivity(Intent.createChooser(intent, "Поделиться"));
            }
        });




    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    handleScan();
                }
                break;

        }
    }
    private void handleScan() {


        IntentIntegrator integrator = new IntentIntegrator(this).setRequestCode(REQUEST_CODE_SCAN);
        integrator.setOrientationLocked(false);
        integrator.setCaptureActivity(CustomScannerActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        //integrator.addExtra(Intents.Scan.SCAN_TYPE, Intents.Scan.QR_CODE_MODE);
        integrator.initiateScan();

        //startActivityForResult(new Intent(getActivity(), ScanActivity.class), REQUEST_CODE_SCAN);
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE_SCAN) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if(result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(ScannerActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                        /*try {
                            //converting the data to json
                            JSONObject obj = new JSONObject(result.getContents());
                            //setting values to textviews

                            String  input =obj.getString("address");


                            ((SendFragment) pageradapter.getItem(SEND)).parseResultfromQR(input);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //if control comes here
                            //that means the encoded format not matches
                            //in this case you can display whatever data is available on the qrcode
                            //to a toast
                            Toast.makeText(WalletActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                        }*/

                    String input = result.getContents();

                    mydatabase.child(input).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot snapshot) {

                            Buffer.receiverFromQr = snapshot.getValue(User.class);
                            if (Buffer.receiverFromQr != null){
                                Intent intent = new Intent(ScannerActivity.this, TransferActivity.class);
                                startActivity(intent);
                                finish();
                            } else
                                Toast.makeText(ScannerActivity.this, "Пользователь с таким ID не найден", Toast.LENGTH_LONG).show();




                        }

                        public void onCancelled(DatabaseError databaseError) {
                            //Log.i(TAG, "loadPost:onCancelled", databaseError.toException());
                        }

                    });



                }

                    /*if (resultCode == Activity.RESULT_OK) {
                        try {
                            processInput(intent.getStringExtra(ScanActivity.INTENT_EXTRA_RESULT));
                        } catch (final Exception e) {
                            showScanFailedMessage(e);
                        }
                    }*/

            }

        }
    }



    private void showScanFailedMessage(Exception e) {
        //String error = getResources().getString(R.string.scan_error, e.getMessage());
        //Toast.makeText(this, error, Toast.LENGTH_LONG).show();
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

