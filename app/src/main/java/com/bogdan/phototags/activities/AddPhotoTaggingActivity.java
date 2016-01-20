package com.bogdan.phototags.activities;

import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.bogdan.phototags.GPSListener;
import com.bogdan.phototags.fragments.AddPhotoFragment;
import com.example.bogdan.phototags.R;

/**
 * Created by bogdan on 18.01.2016.
 */
public class AddPhotoTaggingActivity extends AppCompatActivity {
    private static final int LAYOUT = R.layout.add_photo_activity_layout;
    private AddPhotoFragment addPhotoFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        setAddPhotoFragment();

    }

    private void setAddPhotoFragment() {
        addPhotoFragment = new AddPhotoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.addPhotoFragmentContainer, addPhotoFragment)
                .commit();
    }
}
