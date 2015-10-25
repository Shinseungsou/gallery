package com.siot.sss.hsgallery.util.data.image;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.model.ImageBucket;
import com.siot.sss.hsgallery.app.model.ImageData;
import com.siot.sss.hsgallery.app.model.ThumbnailData;
import com.siot.sss.hsgallery.app.model.UseLog;
import com.siot.sss.hsgallery.util.data.db.UseLogManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import timber.log.Timber;

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

    public void renameImagedata(Context context, String imageId, String toName, boolean isPrivate){
        ImageData fromImage = ImageController.getInstance().getImageData(context, imageId);
        File from = new File(fromImage.data);

        String[] path = from.getPath().split("\\.");

        File to = new File(from.getParent(), toName+ "." + path[path.length-1]);
        renameImagedata(context, fromImage, to, isPrivate);
//        Timber.d("to file %s", to.getPath());
    }
    public void renameImagedata(Context context, ImageData source, String targetName, boolean isPrivate){
        File sourceFile = new File(source.data);
        String[] path = sourceFile.getPath().split("\\.");
        File target = new File(sourceFile.getParent(), targetName+ "." + path[path.length-1]);

        renameImagedata(context, source, target, isPrivate);
    }

    public void renameImagedata(Context context, ImageData source, File target, boolean isPrivate){
        File sourceFile = new File(source.data);

        if(!target.exists()) {
            sourceFile.renameTo(target);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, target.getPath());
            values.put(MediaStore.Images.Media.DISPLAY_NAME, target.getPath());
            values.put(MediaStore.Images.Media.TITLE, target.getName());
            if(isPrivate)
                values.put(MediaStore.Images.Media.IS_PRIVATE, true);

            context.getContentResolver().update(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values, MediaStore.Images.ImageColumns.DATA + "=" + "\""+sourceFile.getPath()+"\"", null);
            UseLogManager.getInstance().addLog(UseLog.Type.UPDATE);
            UseLogManager.getInstance().addLogUpdate(source, UseLog.Type.UPDATE, target.getPath());
        }else{
            Timber.d("file exist!!!!");
        }
    }

    public String replaceName(String before, String toName){
        String[] splits = before.split("/");
        String[] suffix = before.split("\\.");

        splits[splits.length-1] = "";
        String parent = "";
        for(int i = 0; i < splits.length-1; i++){
            parent += splits[i] + "/";
        }

        return parent +toName+"."+suffix[suffix.length - 1];
    }

    public void moveImagedata(Context context, String id, String toPath){
        ImageData image = ImageController.getInstance().getImageData(context, id);
        File file = new File(image.data);
        if(file.renameTo(new File(toPath))) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, toPath);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, toPath);
            values.put(MediaStore.Images.Media.TITLE, image.title);
            context.getContentResolver().update(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values, "_id=" + id, null);
        }
    }

    public void moveImagedata(Context context, List<ImageData> images, String toPath){
        for(ImageData image : images){
            moveImagedata(context, image.id, toPath+"/"+image.title);
        }
        refreshMediaStore(context, new String[]{toPath}, images.size());
    }

    public void copyImagedata(Context context, List<ImageData> images, String toDirectory){
        for(ImageData image : images){
            File fromFile = new File(image.data);
            File toFile = new File(toDirectory, image.title);
            (new FileController()).copyFile(fromFile.getPath(), toFile.getPath());
        }
        refreshMediaStore(context, new String[]{toDirectory}, images.size());
    }

    public void copyImagedata(Context context, String id, String toPath){
        ImageData image = ImageController.getInstance().getImageData(context, id);
        File fromFile = new File(image.data);
        File toFile = new File(toPath);

        (new FileController()).copyFile(fromFile.getPath(), toFile.getPath());
        refreshMediaStore(context, new String[]{toFile.getPath()}, 1);

    }

    public void refreshMediaStore(Context context, File oneOfFile, int number){
        refreshMediaStore(context, oneOfFile.getParent(), number);
    }

    public void refreshMediaStore(Context context, String directory, int number){
        refreshMediaStore(context, new String[]{directory}, number);
    }

    public void refreshMediaStore(Context context, String[] directoty, int number){
        MediaScannerConnection.scanFile(context, directoty, new String[]{"image/*"}, (path, uri)->{
            Timber.d("scan complete : %s %s", path, uri);
            Toast.makeText(context, context.getResources().getQuantityString(R.plurals.success_copy, number, number), Toast.LENGTH_LONG).show();
        });
        context.getContentResolver().notifyChange(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null);
    }


    public void deleteImagedata(Context context, ImageData image){
        File file = new File(image.data);
        renameImagedata(context, image, "."+file.getName(), true);
    }
    public void deleteImagedata(Context context, List<ImageData> images){
        for(ImageData image : images) {
            this.deleteImagedata(context, image);
        }
        refreshMediaStore(context, images.get(0).data, images.size());
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

    public void insertBucket(Context context, String name){
        File newDir = (new FileController()).makeDir("/storage/emulated/0/DCIM/"+name);
        if(newDir != null) {
            File fakeImage = newFakeImage(newDir.getPath());

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.ImageColumns.DATA, fakeImage.getPath());
            values.put(MediaStore.Images.ImageColumns.TITLE, fakeImage.getName());
            values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, fakeImage.getPath());

            context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }else{
            Toast.makeText(context, R.string.error_exist, Toast.LENGTH_LONG).show();
        }
    }

    public File newFakeImage(String path){
        File newImage = new File(path, "fakefile.png");
        OutputStream out = null;
        Bitmap fakeImage = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas white = new Canvas(fakeImage);
        white.drawColor(Color.argb(0, 255,255,255));

        try {
            newImage.createNewFile();
            out = new FileOutputStream(newImage);
            fakeImage.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                out.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return newImage;
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

    public CharSequence[] getDirectoryList(){

        CharSequence[] names = new CharSequence[ImageShow.getInstance().getBuckets().size() - 1];
        for (int i = 1; i <= names.length; i++) {
            if (!ImageShow.getInstance().getBuckets().get(i).id.equals("-1")) {
                if(ImageShow.getInstance().getBuckets().get(i).displayName != null)
                    names[i-1] = ImageShow.getInstance().getBuckets().get(i).displayName;
                else
                    names[i-1] = "Unknown Directory";
            }
        }
        return names;
    }

    public void move(Context context, List<ImageData> images){
        MaterialDialog.Builder moveDialog = new MaterialDialog.Builder(context)
            .items(getDirectoryList());

        new MaterialDialog.Builder(context)
            .content(context.getResources().getQuantityString(R.plurals.dialog_move, images.size(), images.size()))
            .buttonsGravity(GravityEnum.CENTER)
            .positiveText("MOVE")
            .neutralText("COPY")
            .negativeText("CANCEL")
            .callback(new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    super.onPositive(dialog);
                    moveDialog
                        .itemsCallback((materialDialog, view, index, charsequence) -> {
                            moveImagedata(context, images, getBuckets().get(index + 1).getPath());
                        })
                        .show();
                }

                @Override
                public void onNeutral(MaterialDialog dialog) {
                    super.onNeutral(dialog);
                    moveDialog
                        .itemsCallback((materialDialog, view, index, charsequence) -> {
                            copyImagedata(context, images.get(0).id, getBuckets().get(index + 1).getPath()+"/"+images.get(0).title);
                        })
                        .show();
                }

                @Override
                public void onNegative(MaterialDialog dialog) {
                    super.onNegative(dialog);
                }
            })
            .show();
    }
}
