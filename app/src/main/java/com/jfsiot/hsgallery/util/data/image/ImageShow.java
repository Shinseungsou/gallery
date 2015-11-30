package com.jfsiot.hsgallery.util.data.image;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.model.ImageBucket;
import com.jfsiot.hsgallery.app.model.ImageData;
import com.jfsiot.hsgallery.app.model.ThumbnailData;
import com.jfsiot.hsgallery.app.model.UseLog;
import com.jfsiot.hsgallery.util.data.db.UseLogManager;

import java.io.File;
import java.io.FileNotFoundException;
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
                values, MediaStore.Images.ImageColumns.DATA + "=" + "\"" + sourceFile.getPath() + "\"", null);
            if(!isPrivate)
                UseLogManager.getInstance().addLog(source, target.getPath(), UseLog.Type.RENAME);
        }else{
            Timber.d("file exist!!!!");
        }
    }

    public String addNewNameAndSuffix(String before, String toName){

        String[] suffixCandidates = before.split("\\.");
        String suffix = suffixCandidates[suffixCandidates.length - 1];
        String[] cSuffixCandidates = toName.split("\\.");
        String currentSuffix = cSuffixCandidates[cSuffixCandidates.length - 1];

        if(currentSuffix.equals(suffix))
            return toName;
        else if(suffixCandidates.length < 2 && suffix.length() > 5)
            return toName;
        return toName+"."+suffix;
    }
    public String addNewNameAndSuffix2(String before, String toName){
        String[] splits = before.split("/");
        String[] suffix = before.split("\\.");

        splits[splits.length-1] = "";
        String parent = "";
        for(int i = 0; i < splits.length-1; i++){
            parent += splits[i] + "/";
        }

        return parent + toName+"."+suffix[suffix.length - 1];
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
            UseLogManager.getInstance().addLog(image, toPath, UseLog.Type.MOVE);
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
            Timber.d("copy start : %s", image.toString());
            File fromFile = new File(image.data);
            String newName = addNewNameAndSuffix(fromFile.getPath(), image.title);
            File targetFile = new File(toDirectory, newName);
            boolean iscopy = (new FileController()).copyFile(fromFile.getPath(), targetFile.getPath());
            if(iscopy){
                ContentValues value = new ContentValues();
                value.put(MediaStore.Images.Media.TITLE, image.title);
                value.put(MediaStore.Images.Media.DATA, targetFile.getPath());
                value.put(MediaStore.Images.Media.DISPLAY_NAME, image.displayName);
                context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value);
//                    MediaStore.Images.Media.insertImage(context.getContentResolver(), targetFile.getAbsolutePath(), targetFile.getName(), targetFile.getName());
            }
            Timber.d("copy result : %s %s %s", toDirectory, newName, toDirectory + "/" + newName);
            UseLogManager.getInstance().addLog(image, newName, UseLog.Type.COPY);
        }
        refreshMediaStore(context, new String[]{toDirectory}, images.size());
        Toast.makeText(context, context.getResources().getQuantityString(R.plurals.success_copy, images.size(), images.size()), Toast.LENGTH_LONG).show();
    }

    @Deprecated
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
        MediaScannerConnection.scanFile(context, directoty, new String[]{"image/*"}, (path, uri) -> {
            Timber.d("scan complete : %s %s", path, uri);
        });
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(directoty[0]))));
        context.getContentResolver().notifyChange(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null);
    }


    public void deleteImagedata(Context context, ImageData image){
        File file = new File(image.data);
        File toPath = new File(file.getParent(), ".del_siot_"+file.getName());
        renameImagedata(context, image, toPath, true);
        UseLogManager.getInstance().addLog(image, UseLog.Type.DELETE, toPath.getPath(), null);
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
            UseLogManager.getInstance().addLog((new ImageData().fakeImageFile()), newDir.getPath(), UseLog.Type.NEWDIR);
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
    public Integer relocateValue(Integer orientation){
        if(orientation != null){
            Integer value = orientation + 90;
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
                            copyImagedata(context, images, getBuckets().get(index + 1).getPath());
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

    public void sendInstagram(Context context, ImageData imageDatas){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.ACTION_SEND_MULTIPLE, true);
// intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imageDatas.data)));
        intent.setType("image/jpeg");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.instagram.android")) {
                intent.setPackage(info.activityInfo.packageName);
                break;
            }
        }

        context.startActivity(intent);
    }

    public void sendKaKao(Context context, List<ImageData> imageDatas){
        Intent intent = getIntentAddImages(imageDatas);

        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.kakao.talk")) {
                intent.setPackage(info.activityInfo.packageName);
                break;
            }
        }

        context.startActivity(intent);
    }

    public Intent getIntentAddImages(List<ImageData> imageDatas){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.ACTION_SEND_MULTIPLE, true);
// intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
        ArrayList<Uri> uriList = new ArrayList<>();

        for(ImageData image : imageDatas){
            uriList.add(Uri.fromFile(new File(image.data)));
        }
        intent.putExtra(Intent.EXTRA_STREAM, uriList);
        intent.setType("image/jpeg");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        return intent;
    }

    public Intent getIntentAddImagesParcelable(List<ImageData> imageDatas){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.ACTION_SEND_MULTIPLE, true);
// intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
        ArrayList<Uri> uriList = new ArrayList<>();

        for(ImageData image : imageDatas){
            uriList.add(Uri.fromFile(new File(image.data)));
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        intent.setType("image/jpeg");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        return intent;
    }

    public void sendFacebook(Context context, List<ImageData> imageDatas){
        Intent intent = getIntentAddImages(imageDatas);

        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.setPackage(info.activityInfo.packageName);
                break;
            }
        }

        context.startActivity(intent);
    }
}
