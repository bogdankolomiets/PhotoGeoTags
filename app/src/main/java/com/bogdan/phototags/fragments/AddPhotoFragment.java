package com.bogdan.phototags.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bogdan.phototags.DBHelper;
import com.bogdan.phototags.GPSListener;
//import com.bogdan.phototags.PhotoTagsContentProvider;
import com.bogdan.phototags.activities.AddPhotoTaggingActivity;
import com.bogdan.phototags.activities.MainActivity;
import com.example.bogdan.phototags.R;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bogdan on 19.01.2016.
 */
public class AddPhotoFragment extends Fragment {
    private Button btnSelectPictureFromGallery;
    private Button btnSelectPhotoFromURL;
    private Button btnAddPhotoToMap;
    private EditText txtPhotoURL;
    private ImageView circleImageView;
    private static final int RESULT_LOAD_IMAGE = 1;
    private String imagePath;
    private DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_photo_fragment_layout, container, false);
        circleImageView = (ImageView) view.findViewById(R.id.imgSelected);
        btnSelectPictureFromGallery = (Button) view.findViewById(R.id.btnPhotoGallery);
        btnAddPhotoToMap = (Button) view.findViewById(R.id.btnAddPhotoToMap);
        System.out.println("LAATITUDDDEEEE" + MainActivity.latitude);
        btnSelectPictureFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        txtPhotoURL = (EditText) view.findViewById(R.id.txtPhotoURL);
        btnSelectPhotoFromURL = (Button) view.findViewById(R.id.btnPhotoURLSearch);
        btnSelectPhotoFromURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhotoFromURL(getPhotoURL());
                System.out.println("!!!!!!!!!!" + imagePath);
            }
        });

        btnAddPhotoToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DBHelper(getContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.COLUMN_LAT, MainActivity.latitude);
                contentValues.put(DBHelper.COLUMN_LNG, MainActivity.longitude);
                contentValues.put(DBHelper.COLUMN_IMAGE_URL, imagePath);
                contentValues.put(DBHelper.COLUMN_DATE, getData());
                long ID = db.insert(DBHelper.DATABASE_TABLE, null, contentValues);
                Log.d("INSRETTTTTTTTTT", "row inserted, ID = " + ID);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void openGallery() {
        Intent intentGallery = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentGallery, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
            imagePath = cursor.getString(columnIndex);
            System.out.println("!!!!!!!!!!" + imagePath);
            cursor.close();

            circleImageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        } else
            super.onActivityResult(requestCode, resultCode, data);

    }

    private String getPhotoURL() {
        return txtPhotoURL.getText().toString();
    }

    private void uploadPhotoFromURL(String Url) {
        Picasso.with(getContext())
                .load(Url)
                .into(circleImageView);
        imagePath = Url;
    }

    private String getData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(new Date());
    }



}
