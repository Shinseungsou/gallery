package com.siot.sss.hsgallery.app.model;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

/**
 * Created by SSS on 2015-08-04.
 */

public class ImageSource {
    public String id;
    public String displayName;
    public String data;
    public String size;
    public String count;
    public String dateAdded;
    public String dateModified;
    public String dateTaken;
    public String bucketId;
    public String bucketDisplayName;
    public String title;
    public String width;
    public String height;
    public String mimeType;
    public String orientation;
    public String description;
    public String isPrivate;
    public String latitude;
    public String longitude;
    public String miniThumbMagic;

    public ImageSource(String id, String data, String displayName, String size){
        this.id = id;
        this.data = data;
        this.displayName = displayName;
        this.size = size;
    }

    public ImageSource(Cursor cursor){
        id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
        displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
        data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        size = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
//        count = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._COUNT));
        bucketDisplayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
        bucketId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
        dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
        dateModified = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
        dateTaken = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
        title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
        width = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
        height = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));
        mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
        orientation = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION));
        description = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION));
        isPrivate = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.IS_PRIVATE));
        latitude = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE));
        longitude = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
        miniThumbMagic = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MINI_THUMB_MAGIC));
    }

    public Bitmap getImageBitmap(){
        BitmapFactory.Options bo = new BitmapFactory.Options();
        bo.inSampleSize = 8;

        return BitmapFactory.decodeFile(data, bo);
    }
}
