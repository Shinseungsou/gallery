package com.jfsiot.hsgallery.app.model;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

/**
 * Created by SSS on 2015-08-08.
 */
public class ThumbnailData {
    public String id;
    public String data;
    public String height;
    public String width;
    public String kind;
    public String thumbData;
    public String imageId;

    public ThumbnailData(String id, String data, String height, String width, String kind, String thumbData, String imageId){
        this.id = id;
        this.data = data;
        this.height = height;
        this.width = width;
        this.kind = kind;
        this.thumbData = thumbData;
        this.imageId = imageId;

    }
    public ThumbnailData(Cursor cursor){

        id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails._ID));
        data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
        height = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.HEIGHT));
        width = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.WIDTH));
        kind = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.KIND));
        thumbData = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.THUMB_DATA));
        imageId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID));
    }

    public Bitmap getImageBitmap(){
        BitmapFactory.Options bo = new BitmapFactory.Options();
        bo.inSampleSize = 8;

        return BitmapFactory.decodeFile(data, bo);
    }
}
