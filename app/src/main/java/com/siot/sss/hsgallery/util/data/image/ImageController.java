package com.siot.sss.hsgallery.util.data.image;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.siot.sss.hsgallery.app.model.ImageBucket;
import com.siot.sss.hsgallery.app.model.ImageData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ImageController {
    private ContentResolver contentResolver;
    public ImageController(){    }
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
                    if(!this.isPrivate(imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA))) ){

                        list.add(new ImageData(imageCursor));
                    }
            }while (imageCursor.moveToNext());
        }
        imageCursor.close();
        return list;
    }

    public void setImageShow(){
        List<ImageData> lists = this.getImageData();
        Timber.d("&&IC size %s", lists.size());
        ImageShow.getInstance().getBuckets().clear();
        if(!lists.isEmpty()) {
            ImageShow.getInstance().setImages(lists);
            ImageShow.getInstance().getBuckets().add(new ImageBucket(lists.get(0)));
            for (ImageData image : lists) {
                if (!ImageShow.getInstance().containsBucket(image.bucketId))
                    ImageShow.getInstance().getBuckets().add(new ImageBucket(image));
            }
        }
    }

    public boolean isPrivate(ImageData image){
        return this.isPrivate(image.data);
    }

    public boolean isPrivate(String data){
        String name = this.getRealName(data);

        if (name.charAt(0) == '.')
            return true;

        return false;
    }

    public String getRealName(ImageData image){
        return getRealName(image.data);
    }

    public String getRealName(String data){
        File from = new File(data);
        String[] path = from.getPath().split("/");

        return path[path.length -1];
    }

}
