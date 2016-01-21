package com.bogdan.phototags;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bogdan on 18.01.2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE = "photos";
    public static final String COLUMN_LAT = "latitude";
    public static final String COLUMN_LNG = "longitude";
    public static final String COLUMN_IMAGE_URL = "image";
    public static final String COLUMN_DATE = "date";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_TABLE + " ( " +
                "_id integer primary key autoincrement , " + COLUMN_LAT + " double , " + COLUMN_LNG
                + " double , " + COLUMN_IMAGE_URL + " text , " + COLUMN_DATE + " text " + " ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DATABASE_TABLE);
        onCreate(db);
    }

//    public long insert(ContentValues contentValues) {
//        return db.insert(DATABASE_TABLE, null, contentValues);
//    }
//
//    public Cursor getAllRows() {
//        return db.query(DATABASE_TABLE, new String[]{"_id", COLUMN_LAT, COLUMN_LNG, COLUMN_IMAGE_URL}, null, null, null, null, null);
//    }


}
