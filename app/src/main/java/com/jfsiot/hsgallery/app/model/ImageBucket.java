package com.jfsiot.hsgallery.app.model;

import android.database.Cursor;
import android.provider.MediaStore;

import java.io.File;

import lombok.ToString;

/**
 * Created by SSS on 2015-08-04.
 */

@ToString
public class ImageBucket {
    public String id;
    public String displayName;
    public ImageData imageData;

    public ImageBucket(String id, String displayName, ImageData imageData){
        this.id = id;
        this.displayName = displayName;
        this.imageData = imageData;
    }

    public ImageBucket(ImageData imageData){
        this.id = imageData.bucketId;
        this.displayName = imageData.bucketDisplayName;
        this.imageData = imageData;
    }

    public ImageBucket(Cursor cursor){
        id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
        displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
        imageData = new ImageData(cursor);
    }

    public void setImageData(Cursor cursor){
        id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
        displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
        imageData = new ImageData(cursor);
    }
    public String getPath(){
        File file = new File(imageData.data);
        return file.getParent();
    }
}
