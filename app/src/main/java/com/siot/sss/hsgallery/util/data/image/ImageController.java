package com.siot.sss.hsgallery.util.data.image;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.siot.sss.hsgallery.app.model.ImageData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageController {
    private Context context;
    public ImageController(){    }
    private static ImageController instance;

    public static synchronized ImageController getInstance(){
        if(ImageController.instance == null) ImageController.instance = new ImageController();
        return ImageController.instance;
    }
    public void init(Context context){
        this.context = context;
    }

    public List<ImageData> getImageDataList(){
        return this.getImageDataList(null);
    }

    public List<ImageData> getImageDataList(String bucketId){
        if(bucketId != null && bucketId.equals("-1"))
            return getImageDataList();
        List<ImageData> list = new ArrayList<>();

        String selection = null;
        if(bucketId != null)
            selection= MediaStore.Images.Media.BUCKET_ID + " == " + bucketId;

        Cursor imageCursor = this.context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, selection, null, null);

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

    public ImageData getImageData(Context context, String imageId){
        String selection = null;
        if(imageId != null)
            selection = String.format("%s == %s", MediaStore.Images.Media._ID, imageId);

        Cursor imageCursor = this.context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, selection, null, null);

        if(imageCursor != null && imageCursor.moveToFirst() && imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)) != null){
            return new ImageData(imageCursor);
        }
        return null;
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
