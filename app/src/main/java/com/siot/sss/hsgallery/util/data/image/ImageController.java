package com.siot.sss.hsgallery.util.data.image;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.siot.sss.hsgallery.app.model.ImageBucket;
import com.siot.sss.hsgallery.app.model.ImageData;
import com.siot.sss.hsgallery.app.model.unique.ImageShow;

import java.util.ArrayList;
import java.util.List;

public class ImageController {
    private ContentResolver contentResolver;
    public ImageController(){
    }
    private static ImageController instance;

    public static synchronized ImageController getInstance(){
        if(ImageController.instance == null) ImageController.instance = new ImageController();
        return ImageController.instance;
    }
    public void init(ContentResolver contentResolver){
        this.contentResolver = contentResolver;
    }

    public List<ImageData> getImageData(){
        return this.getImageData(null);
    }

    public List<ImageData> getImageData(String bucketId){
        List<ImageData> list = new ArrayList<>();

        String selection = null;
        if(bucketId != null)
            selection= MediaStore.Images.Media.BUCKET_ID + " == " + bucketId;

        Cursor imageCursor = this.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, selection, null, null);

        if (imageCursor != null && imageCursor.moveToFirst()){
            ImageShow.getInstance().clear();
            do {
                if (imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)) != null)
                    if(imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.IS_PRIVATE)) == null || !imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.IS_PRIVATE)).equals("1")){
                        list.add(new ImageData(imageCursor));
                    }
            }while (imageCursor.moveToNext());
        }
        imageCursor.close();
        return list;
    }

    public void setImageShow(List<ImageData> lists){
        ImageShow.getInstance().getImages().clear();
        ImageShow.getInstance().getBuckets().clear();
        ImageShow.getInstance().getImages().addAll(lists);
        for(ImageData image : lists){
            if(!ImageShow.getInstance().containsBucket(image.bucketId))
                ImageShow.getInstance().getBuckets().add(new ImageBucket(image));
        }
    }
}
