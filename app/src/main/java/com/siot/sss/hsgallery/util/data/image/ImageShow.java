package com.siot.sss.hsgallery.util.data.image;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.widget.Toast;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.model.ImageBucket;
import com.siot.sss.hsgallery.app.model.ImageData;
import com.siot.sss.hsgallery.app.model.ThumbnailData;
import com.siot.sss.hsgallery.app.model.UseLog;
import com.siot.sss.hsgallery.util.data.db.UseLogManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
    @Getter private List<ImageData> images;
    @Getter private List<ImageBucket> buckets;
    @Getter @Setter private int position;
    @Getter @Setter private String bucketId;

    private static ImageShow instance;
    public static synchronized ImageShow getInstance(){
        if(ImageShow.instance == null) ImageShow.instance = new ImageShow();
        return ImageShow.instance;
    }
    public ImageShow(){
        this.images = new ArrayList<>();
        this.buckets = new ArrayList<>();
    }

    public void setImages(List<ImageData> images){
        this.images.clear();
        this.images.addAll(images);
        Timber.d("&&setImage : %s %s", images.size(), this.images.size());
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

    public void renameImagedata(Context context, Bitmap bitmap, String name, int position){
        renameImagedata(context, bitmap, name, position, false);
    }
    public void renameImagedata(Context context, Bitmap bitmap, String name, int position, boolean isPrivate){
        File from = new File(this.getImages().get(position).data);
        renameImagedata(context, this.getImages().get(position).id, name, isPrivate);
    }
    public void renameImagedata(Context context, String imageId, String toName, boolean isPrivate){
        ImageData fromImage = ImageController.getInstance().getImageData(context, imageId);
        File from = new File(fromImage.data);

        String[] path = from.getPath().split("\\.");

        File to = new File(from.getParent(), toName+ "." + path[path.length-1]);
        if(!to.exists()) {
            from.renameTo(to);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, to.getPath());
            values.put(MediaStore.Images.Media.DISPLAY_NAME, replace(fromImage.data, toName));
            values.put(MediaStore.Images.Media.TITLE, toName+"."+path[path.length-1]);
            if(isPrivate)
                values.put(MediaStore.Images.Media.IS_PRIVATE, true);

            context.getContentResolver().update(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values, "_id=" + imageId, null);
        }else{
            Timber.d("file exist!!!!");
        }
//        Timber.d("to file %s", to.getPath());
        UseLogManager.getInstance().addLog(UseLog.Type.UPDATE);
        UseLogManager.getInstance().addLogUpdate(to.getName(),UseLog.Type.UPDATE);
    }
    public String replace(String before, String toName){
        String[] splits = before.split("/");
        String[] suffix = before.split("\\.");

        splits[splits.length-1] = "";
        String parent = "";
        for(int i = 0; i < splits.length-1; i++){
            parent += splits[i] + "/";
        }

        return parent +toName+"."+suffix[suffix.length - 1];
    }

    public void moveImagedata(Context context, String toPath){
        File from = new File(this.getImageData().data);
        Timber.d("path : %s %s", from.getName(), from.getParent());

        Timber.d("path2 : %s", "/storage/emulated/0/DCIM/Camera"+from.getName());
        String toFile = "/storage/emulated/0/DCIM/Camera/"+from.getName();
        if(from.renameTo(new File(toFile))){
            moveImagedata(context, getImageData().id, toFile);
        }
    }

    public void moveImagedata(Context context, String id, String toPath){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, toPath);

        context.getContentResolver().update(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values, "_id=" + id, null);
        context.getContentResolver().notifyChange(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null);
    }

    public void copyImagedata(Context context, String id, String toPath){
        File from = new File(this.getImageData().data);
        Timber.d("path : %s %s", from.getName(), from.getParent());

        Timber.d("path2 : %s", "/storage/emulated/0/DCIM/Camera"+from.getName());
        File toFile = new File("/storage/emulated/0/DCIM/Camera/"+from.getName());

        (new FileController()).copyFile(from.getPath(), toFile.getPath());
        Timber.d("copy %s %s %s %s", from.getPath(), toFile.getPath(), from.getName(), toFile.getName());
        try {
            MediaStore.Images.Media.insertImage(
                context.getContentResolver(),
                toFile.getPath(),
                toFile.getName(),
                this.getImageData().description
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.error_copy), Toast.LENGTH_LONG).show();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.error_copy), Toast.LENGTH_LONG).show();
        }

        context.getContentResolver().notifyChange(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null);
    }

    public void deleteImagedata(Context context, int position){
        Timber.d("&&image position : %s %s", images.size(), position);
        renameImagedata(context, null, "."+this.getImageData().title, position, true);
    }

    public void removeImagedata(Context context, int position){
        this.removeImagedata(context, this.getImageData().id);
    }

    public void removeImagedata(Context context, String id){
        context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, BaseColumns._ID + " = " + id, null);
    }

    public void addImagedata(Context context, Bitmap bitmap, String name){
        MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, name, null);
    }

    public void relocateImagedata(Context context, int position) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.ORIENTATION, relocateValue(images.get(position).orientation));

        context.getContentResolver().update(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values, "_id=" + images.get(position).id, null);
    }

    public void insertBucket(Context context, String name, String imageId){
        File mImageDir = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), name);
//Retrieve the path with the folder/filename concatenated
        String mImageFilePath = new File(mImageDir, "NameOfImage").getAbsolutePath();

        this.moveImagedata(context, imageId, mImageFilePath);
//Create new content values
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.ImageColumns.DATA, mImageFilePath);
//Add whatever other content values you need

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public Integer relocateValue(String orientation){
        if(orientation != null){
            Integer value = Integer.parseInt(orientation) + 90;
            if(value > 270){
                return 0;
            }else{
                return value;
            }
        }
        return null;
    }

    public void initImageShow(){
        List<ImageData> lists = ImageController.getInstance().getImageDataList();
        Timber.d("&&IC size %s", lists.size());
        this.buckets.clear();
        if(!lists.isEmpty()) {
            this.setImages(lists);
            this.buckets.add(new ImageBucket("-1", null, lists.get(0)));
            for (ImageData image : lists) {
                if (!this.containsBucket(image.bucketId))
                    this.buckets.add(new ImageBucket(image));
            }
        }
    }
    public boolean containsBucket(Cursor cursor){
        for(ImageBucket ib : this.buckets){
            if(ib.id.equals(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID))))
                return true;
        }
        return false;
    }

    public boolean containsBucket(String bucketId){
        for (ImageBucket ib : this.buckets) {
            if (ib.id.equals(bucketId))
                return true;
        }

        return false;
    }

    public void clear(){
        this.images.clear();
        this.buckets.clear();
    }
}
