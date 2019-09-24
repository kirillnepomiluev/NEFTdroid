package com.example.android.project1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    private DatabaseReference mydatabase;
    private MapView mapview;
    private final static int REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION = 11;
    private LocationManager locationManager;
    private boolean firstlocation;
    private LocationListener locationListener;
    private ArrayList<GasStation> gasStations;
    MapObject userplace;
    MapObjectCollection mcollection;
    MapObjectCollection mcollectionloc;
    private ProgressBar progressBar;
    private ImageButton backBtn;


    @Override
    protected void onStart() {
        super.onStart();
        mapview.onStart();
        MapKitFactory.getInstance().onStart();
        gasStationsFromDB ();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("6363c35d-430c-4582-9b36-0f0adb7aa8b2");
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_map);

        mydatabase= FirebaseDatabase.getInstance().getReference();

        mapview = (MapView)findViewById(R.id.mapview);
        firstlocation = false;
        FloatingActionButton tolocationbtn = (FloatingActionButton) findViewById(R.id.tomylocation);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        checkpermisions();

        mcollection = mapview.getMap().getMapObjects().addCollection();
        mcollectionloc = mapview.getMap().getMapObjects().addCollection();

        tolocationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkpermisions();
                if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if
                (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) )

                    showLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
                else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    showLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));


            }
        });

    }
    private void gasStationsFromDB () {
        mydatabase.child("gasstations").addValueEventListener(new ValueEventListener(){
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            progressBar.setVisibility(View.VISIBLE);
            gasStations = new ArrayList<>();

            if (snapshot.exists()) {

                for (DataSnapshot snapshot1 :
                        snapshot.getChildren()) {

                    GasStation element = snapshot1.getValue(GasStation.class);
                    if (element!= null)

                        gasStations.add(element);


                }
            }
            progressBar.setVisibility(View.GONE);
            updateMap();

        }

        public void onCancelled(DatabaseError databaseError) {
            //Log.i(TAG, "loadPost:onCancelled", databaseError.toException());
        }

    });

}
    private void updateMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000 * 10, 10, locationListener);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                    locationListener);
        }
        Buffer.gasStations = gasStations;

        mcollection.clear();

        for (final GasStation station : gasStations) {
            String iconname = "gasicon";
            int resID = getResources().getIdentifier(iconname,
                    "drawable", getPackageName());

            final GasStation gasStation = station;


            mcollection.addPlacemark(new Point(station.getLatitude(), station.getLongitude()), ImageProvider.fromResource(this, resID)).addTapListener(new MapObjectTapListener() {
                @Override
                public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {

                    Buffer.gasStation = gasStation;

                    BottomSheet bottomSheet = new BottomSheet();
                    bottomSheet.show(getSupportFragmentManager(), "");
                    updateMap();

                    return false;
                }
            });

        }

    }



    private void checkpermisions() {
        int permissionStatus = ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            maptoLocation();
        } else {
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION);
        }



    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    maptoLocation();
                }
                break;
        }
    }
    private void maptoLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                if (firstlocation) return;
                showLocation(location);
            }

            @Override
            public void onProviderDisabled(String provider) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                if (firstlocation) return;

                if (ActivityCompat.checkSelfPermission( getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                showLocation(locationManager.getLastKnownLocation(provider));

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                if (provider.equals(LocationManager.GPS_PROVIDER)) {
                    // tvStatusGPS.setText("Status: " + String.valueOf(status));
                } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                    //tvStatusNet.setText("Status: " + String.valueOf(status));
                }
            }
        };


    }

    private void showLocation(Location location) {

        if (location == null)
            return;
        //if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {

        mcollectionloc.clear();

        mapview.getMap().move(
                new CameraPosition(new Point(location.getLatitude(), location.getLongitude()), 14.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);


        mcollectionloc.addPlacemark(new Point(location.getLatitude(), location.getLongitude()), ImageProvider.fromResource(this, R.drawable.ic_userlocation));
        firstlocation=true;


        /*} else if (location.getProvider().equals(
                LocationManager.NETWORK_PROVIDER)) {

            mapview.getMap().move(
                    new CameraPosition(new Point(location.getLatitude(), location.getLongitude()), 14.0f, 0.0f, 0.0f),
                    new Animation(Animation.Type.SMOOTH, 0),
                    null);

            mapview.getMap().getMapObjects().addPlacemark(new Point(location.getLatitude(), location.getLongitude()), ImageProvider.fromResource(this, R.drawable.ic_userlocation));

        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
//        long currentTime = new Date().getTime();
//        long timeDifference = currentTime - Buffer.stopTime;
//        if (timeDifference >= 180000 || !Buffer.checkPassword){
//            startActivity(new Intent(MapActivity.this, SighInActivity.class));
//            finish();
//        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000 * 10, 10, locationListener);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                    locationListener);
        }
        // checkEnabled();
    }

   @Override
    protected void onPause() {
        super.onPause();
        //Buffer.setStopTime();
        if (locationManager != null)
            locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Buffer.checkPassword = false;
        mapview.onStop();
        MapKitFactory.getInstance().onStop();
    }


}
