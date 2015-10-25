package com.siot.sss.hsgallery.app.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.siot.sss.hsgallery.app.AppConfig;
import com.siot.sss.hsgallery.util.data.db.table.Tables;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class UseLog extends DBModel {
    @Getter @Setter public Integer id;
    @Getter @Setter @NonNull public String date;
    @Getter @Setter @NonNull public String name;
    @Getter @Setter @NonNull public String pictureId;
    @Getter @Setter @NonNull public String type;
    @Getter @Setter @NonNull public String title;
    @Getter @Setter @NonNull public String data;
    @Getter @Setter @NonNull public String bucket;
    @Getter @Setter @NonNull public String bucketName;
    @Getter @Setter public Integer width;
    @Getter @Setter public Integer height;
    @Getter @Setter public String to_data;
    @Getter @Setter public String share;
    @Getter @Setter public String note;


    public enum Type{
        SAVE, READ, UPDATE, DELETE, ALL, DISKDEL, RENAME, COPY, MOVE, SHARE
    }
    public static String getTypeString(Type type){
        switch (type) {
            case SAVE   :   return "save";
            case UPDATE :   return "update";
            case READ   :   return "read";
            case DELETE :   return "delete";
            case ALL    :   return "all";
            case DISKDEL:   return "delete in disk";
            case COPY   :   return "copy";
            case MOVE   :   return "move";
            case RENAME :   return "rename";
            case SHARE  :   return "share";
            default     :   return null;
        }
    }

    @Override
    public ContentValues getValue() {
        ContentValues values = new ContentValues();
        values.put(Tables.UseLog.NAME, this.name);
        values.put(Tables.UseLog.DATE, this.date);
        values.put(Tables.UseLog.PICTUREID, this.pictureId);
        values.put(Tables.UseLog.TITLE, this.title);
        values.put(Tables.UseLog.TYPE, this.type);
        values.put(Tables.UseLog.DATA, this.data);
        values.put(Tables.UseLog.BUCKET, this.bucket);
        values.put(Tables.UseLog.BUCKETNAME, this.bucketName);
        values.put(Tables.UseLog.WIDTH, this.width);
        values.put(Tables.UseLog.HEIGHT, this.height);
        values.put(Tables.UseLog.TO_DATA, this.to_data);
        values.put(Tables.UseLog.SHARE, this.to_data);
        values.put(Tables.UseLog.NOTE, this.note);
        return values;
    }

    @Override
    public String getTableName() {
        return Tables.UseLog._TABLENAME;
    }

    /**
     *
     * Need to add TYPE, TO_DATE, NOTE, SHARE
     *
     */
    public static UseLog makeUseLog(ImageData imageData){
        UseLog uselog = new UseLog();
        uselog.setDate(AppConfig.currentTime());
        uselog.setPictureId(imageData.id);
        uselog.setData(imageData.data);
        uselog.setName(imageData.displayName);
        uselog.setTitle(imageData.title);
        uselog.setBucket(imageData.bucketId);
        uselog.setBucketName(imageData.bucketDisplayName);
        uselog.setHeight(imageData.height);
        uselog.setWidth(imageData.width);

        return uselog;
    }
    public static UseLog makeUseLog(Cursor cursor){
        UseLog uselog = new UseLog();

        uselog.setId(cursor.getInt(cursor.getColumnIndex(Tables.UseLog._ID)));
        uselog.setDate(cursor.getString(cursor.getColumnIndex(Tables.UseLog.DATE)));
        uselog.setPictureId(cursor.getString(cursor.getColumnIndex(Tables.UseLog.PICTUREID)));
        uselog.setData(cursor.getString(cursor.getColumnIndex(Tables.UseLog.DATA)));
        uselog.setName(cursor.getString(cursor.getColumnIndex(Tables.UseLog.NAME)));
        uselog.setType(cursor.getString(cursor.getColumnIndex(Tables.UseLog.TYPE)));
        uselog.setTitle(cursor.getString(cursor.getColumnIndex(Tables.UseLog.TITLE)));
        uselog.setBucket(cursor.getString(cursor.getColumnIndex(Tables.UseLog.BUCKET)));
        uselog.setBucketName(cursor.getString(cursor.getColumnIndex(Tables.UseLog.BUCKETNAME)));
        uselog.setHeight(cursor.getInt(cursor.getColumnIndex(Tables.UseLog.HEIGHT)));
        uselog.setWidth(cursor.getInt(cursor.getColumnIndex(Tables.UseLog.WIDTH)));
        uselog.setTo_data(cursor.getString(cursor.getColumnIndex(Tables.UseLog.TO_DATA)));
        uselog.setShare(cursor.getString(cursor.getColumnIndex(Tables.UseLog.SHARE)));
        uselog.setNote(cursor.getString(cursor.getColumnIndex(Tables.UseLog.NOTE)));

        return uselog;
    }
    public UseLog setTypeByType(Type type){
        this.type = getTypeString(type);
        return this;
    }
}
