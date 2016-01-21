package com.bogdan.phototags.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SignalStrength;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bogdan.phototags.DBHelper;
import com.bogdan.phototags.GPSListener;
import com.example.bogdan.phototags.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by bogdan on 18.01.2016.
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LAYOUT = R.layout.main_activity_layout;
    private Toolbar toolbar;
    private GoogleMap googleMap;
    private FloatingActionButton btnAddPhoto;
    public static String latitude;
    public static String longitude;
    private ArrayList<Marker> markers = new ArrayList<>();
    private CameraUpdate cameraUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initToolbar();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        GPSListener.getLocation(this);
        btnAddPhoto = (FloatingActionButton) findViewById(R.id.btnTaggingPhoto);
        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latitude == null) {
                    System.out.println("ASD" + latitude);
                    Toast.makeText(getApplicationContext(), "Wait", Toast.LENGTH_SHORT).show();
                } else
                    addPhotoTagging();
            }
        });

    }

    private void addMarker(GoogleMap googleMap, double latitude, double longitude, String photoUrl) {
        if (googleMap != null) {
            View markerPhoto = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.photo_marker, null);
            ImageView photoInMarker = (ImageView) markerPhoto.findViewById(R.id.photoInMarker);
            if (photoUrl.substring(0, 9).equals("/storage/")) {
                photoInMarker.setImageBitmap(BitmapFactory.decodeFile(photoUrl));
            } else {
                Picasso.with(getApplicationContext())
                        .load(photoUrl)
                        .into(photoInMarker);
            }

            markers.add(googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .icon(BitmapDescriptorFactory.fromBitmap(createDrawablefromView(this, markerPhoto))))
            );
            System.out.println("MARKEEEESS" + markers);

        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        addPhotoTagToMap();
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.remove();

                return false;
            }
        });
        if (!markers.isEmpty())
            setMapZoom(googleMap, markers);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Photo Tagging");

        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.day_histrory_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuDayHistory) {
            Intent intent = new Intent(getApplicationContext(), DayHistoryActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void addPhotoTagging() {
        Intent intent = new Intent(getApplicationContext(), AddPhotoTaggingActivity.class);
        startActivity(intent);
    }

    private void setMapZoom(final GoogleMap googleMap, ArrayList<Marker> markers) {
        System.out.println(markers);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        final LatLngBounds bounds = builder.build();

        final int padding = 50;
        cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                googleMap.animateCamera(cameraUpdate);
            }
        });
    }

    private void addPhotoTagToMap() {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(DBHelper.DATABASE_TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int columnnIndexLatitude = cursor.getColumnIndex(DBHelper.COLUMN_LAT);
            int columnIndexLongitude = cursor.getColumnIndex(DBHelper.COLUMN_LNG);
            int columnIndexPhoto = cursor.getColumnIndex(DBHelper.COLUMN_IMAGE_URL);
            do {
                System.out.println(cursor.getString(columnIndexLongitude));
                double latitude = cursor.getDouble(columnnIndexLatitude);
                double longitude = cursor.getDouble(columnIndexLongitude);
                String photoUrl = cursor.getString(columnIndexPhoto);
                addMarker(googleMap, latitude, longitude, photoUrl);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private static Bitmap createDrawablefromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

}