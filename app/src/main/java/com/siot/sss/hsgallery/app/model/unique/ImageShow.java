package com.siot.sss.hsgallery.app.model.unique;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.siot.sss.hsgallery.app.model.ImageData;
import com.siot.sss.hsgallery.app.model.ThumbnailData;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import timber.log.Timber;

/**
 * Created by SSS on 2015-08-06.
 */
@Data
public class ImageShow {
    @Getter @Setter private ImageData imageData;
    @Getter @Setter private List<ImageData> images;
    @Getter @Setter private int position;
    @Getter @Setter private String buketId;

    private static ImageShow instance = null;
    public static synchronized ImageShow getInstance(){
        if(ImageShow.instance == null) ImageShow.instance = new ImageShow();
        return ImageShow.instance;
    }
    public void selectImageData(ThumbnailData thumbnailData, ContentResolver contentResolver){

        String[] proj = {MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE};
//        String[] proj = {MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        String selection = MediaStore.Images.Media._ID +" like "+ thumbnailData.imageId;
        Cursor imageCursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        int num = 0;

        if (imageCursor != null && imageCursor.moveToFirst()){
            num++;
            if (imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA)) != null){
                this.imageData.setImageData(imageCursor);
            }
            Timber.d("num : %s", num);
        }
        imageCursor.close();
    }
    public void renameImagedata(int position, String name){

    }
    public void deleteImagedata(int position){

    }
    public void addImagedata(int position){

    }
}
