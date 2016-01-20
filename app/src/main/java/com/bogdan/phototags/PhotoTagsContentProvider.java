package com.bogdan.phototags;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by bogdan on 19.01.2016.
 */
//public class PhotoTagsContentProvider extends ContentProvider {
//    public static final String PROVIDER_NAME = "com.example.bogdan.phototags.location";
//    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/locations");
//    private static final int LOCATIONS = 1;
//    private static final UriMatcher uriMatcher;
//
//    static {
//        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//        uriMatcher.addURI(PROVIDER_NAME, "photos", LOCATIONS);
//    }
//
//    DBHelper photoTagsDataBase;
//
//    @Override
//    public boolean onCreate() {
//        photoTagsDataBase = new DBHelper(getContext());
//        return true;
//    }
//
//    @Nullable
//    @Override
//    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//        if (uriMatcher.match(uri) == LOCATIONS) {
//            return photoTagsDataBase.getAllRows();
//        }
//        return null;
//    }
//
//    @Nullable
//    @Override
//    public String getType(Uri uri) {
//        return null;
//    }
//
//    @Nullable
//    @Override
//    public Uri insert(Uri uri, ContentValues values) {
//        long rowID = photoTagsDataBase.insert(values);
//        Uri _uri = null;
//        if (rowID > 0) {
//            _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
//        }
//        return _uri;
//    }
//
//    @Override
//    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        return  0;
//    }
//
//    @Override
//    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
//        return 0;
//    }
//}
