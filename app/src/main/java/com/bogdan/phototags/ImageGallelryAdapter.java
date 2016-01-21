package com.bogdan.phototags;


import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.bogdan.phototags.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ClipData.*;

/**
 * Created by bogdan on 21.01.2016.
 */
public class ImageGallelryAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;
    private LayoutInflater inflater;
    private ArrayList<String> items = new ArrayList<>();

    public ImageGallelryAdapter(Context context, String currentDate) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        cursor = sqLiteDatabase.query(DBHelper.DATABASE_TABLE,
                new String[] {DBHelper.COLUMN_IMAGE_URL} ,
                DBHelper.COLUMN_DATE + " = ?",
                new String[] {currentDate} , null, null, null);
        if(cursor.moveToFirst()) {
            int columnIndexImage = cursor.getColumnIndex(DBHelper.COLUMN_IMAGE_URL);
            do {
                items.add(cursor.getString(columnIndexImage));
            } while (cursor.moveToNext());
        }
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        cursor.moveToPosition(position);
        return cursor.getLong(cursor.getColumnIndex("_id"));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gallery_item, parent, false);
            convertView.setTag(R.id.pictureFromGallery, convertView.findViewById(R.id.pictureFromGallery));
        }

        imageView = (ImageView) convertView.getTag(R.id.pictureFromGallery);
        System.out.println("!!!!!!!!!!!" + getItem(position));
        if (getItem(position).substring(0, 9).equals("/storage/")) {
            Bitmap bmp = decodeBitmap(getItem(position), 300, 600);
            imageView.setImageBitmap(bmp);
        } else {
            Picasso.with(context)
                    .load(getItem(position))
                    .into(imageView);
        }

        return convertView;
    }

    private static Bitmap decodeBitmap(String imagePath, int rWidth, int rHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        options.inSampleSize = calculateSampleSize(options, rWidth, rHeight);

        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(imagePath, options);
    }

    private static int calculateSampleSize(BitmapFactory.Options options, int rWidth, int rHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (width > rWidth || height > rHeight) {
            final int halfWidth = width / 2;
            final int halfHeight = height / 2;

            while((halfHeight / inSampleSize) > rHeight && (halfWidth / inSampleSize) > rWidth)
                inSampleSize *= 2;
        }

        return inSampleSize;
    }
}
