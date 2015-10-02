package com.siot.sss.hsgallery.util.data.image;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import com.siot.sss.hsgallery.app.model.ImageBucket;
import com.siot.sss.hsgallery.app.model.ImageData;
import com.siot.sss.hsgallery.app.model.ThumbnailData;
import com.siot.sss.hsgallery.app.model.UseLog;
import com.siot.sss.hsgallery.util.data.db.UseLogManager;

import java.io.File;
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

    public void renameImagedata(ContentResolver contentResolver, Bitmap bitmap, String name, int position){
        renameImagedata(contentResolver, bitmap, name, position, false);
    }
    public void renameImagedata(ContentResolver contentResolver, Bitmap bitmap, String name, int position, boolean isPrivate){
        File from = new File(this.getImages().get(position).data);
        String[] path = from.getPath().split("\\.");

        File to = new File(from.getParent(), name+ "." + path[path.length-1]);
        if(!to.exists()) {
            from.renameTo(to);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, to.getPath());
            if(isPrivate)
                values.put(MediaStore.Images.Media.IS_PRIVATE, true);


            contentResolver.update(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values, "_id=" + images.get(position).id, null);
        }else{
            Timber.d("file exist!!!!");
        }
//        Timber.d("to file %s", to.getPath());
        UseLogManager.getInstance().addLog(UseLog.Type.UPDATE);
        UseLogManager.getInstance().addLogUpdate(to.getName(),UseLog.Type.UPDATE);
    }

    public void deleteImagedata(ContentResolver contentResolver, int position){
        Timber.d("&&image position : %s %s", images.size(), position);
        renameImagedata(contentResolver, null, "."+images.get(position).title, position, true);
    }

    public void removeImagedata(ContentResolver contentResolver, int position){
        this.removeImagedata(contentResolver, this.images.get(position).id);
    }

    public void removeImagedata(ContentResolver contentResolver, String id){
        contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, BaseColumns._ID + " = " + id, null);
    }

    public void addImagedata(ContentResolver contentResolver, Bitmap bitmap, String name){
        MediaStore.Images.Media.insertImage(contentResolver, bitmap, name, null);
    }

    public void relocateImagedata(ContentResolver contentResolver, int position) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.ORIENTATION, relocateValue(images.get(position).orientation));

        contentResolver.update(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values, "_id=" + images.get(position).id, null);
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
        List<ImageData> lists = ImageController.getInstance().getImageData();
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
