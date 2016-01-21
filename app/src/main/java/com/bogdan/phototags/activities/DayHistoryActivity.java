package com.bogdan.phototags.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.TextView;

import com.bogdan.phototags.DBHelper;
import com.bogdan.phototags.ImageGallelryAdapter;
import com.example.bogdan.phototags.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bogdan on 21.01.2016.
 */
public class DayHistoryActivity extends AppCompatActivity{

    private static final int LAYOUT = R.layout.day_history_activity_layout;
    private GridView gridView;
    private String currentDate;
    private TextView txtTotalDistance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        gridView = (GridView) findViewById(R.id.dayPictureGallery);
        txtTotalDistance = (TextView) findViewById(R.id.txtTotalDistance);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        currentDate = dateFormat.format(new Date());
        String totalDistance = getDistanceForDay();
        txtTotalDistance.setText("Дистанция пройденная за день : " + totalDistance + " км.");
        gridView.setAdapter(new ImageGallelryAdapter(getApplicationContext(), currentDate));
    }

    private String getDistanceForDay() {
        ArrayList<Location> locations = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        double totalDistance = 0;
        Cursor cursor = db.query(DBHelper.DATABASE_TABLE,
                null,
                DBHelper.COLUMN_DATE + " = ?",
                new String[] {currentDate}, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndexLat = cursor.getColumnIndex(DBHelper.COLUMN_LAT);
            int columnIndexLng = cursor.getColumnIndex(DBHelper.COLUMN_LNG);

            do {
                Location location = new Location("locationPoint");
                location.setLatitude(cursor.getDouble(columnIndexLat));
                location.setLongitude(cursor.getDouble(columnIndexLng));
                locations.add(location);
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (int i = 0; i < locations.size() - 1; i++) {
            totalDistance += locations.get(i).distanceTo(locations.get(i + 1));
        }

        return String.format("%8.2f", totalDistance / 1000) + "";
    }
}
