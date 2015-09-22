package com.siot.sss.hsgallery.app.model;

import android.content.ContentValues;

import com.siot.sss.hsgallery.util.data.db.table.Tables;

/**
 * Created by SSS on 2015-08-06.
 */
public class UseLog extends DBModel {
    public Integer id;
    public String date;
    public String name;
    public String pictureId;
    public String type;
    public String title;
    public String data;
    public String bucket;
    public String bucketName;
    public Integer width;
    public Integer height;

    public enum Type{
        SAVE, READ, UPDATE, DELETE, ALL
    }
    public static String getTypeString(Type type){
        switch (type) {
            case SAVE:      return "save";
            case UPDATE:    return "update";
            case READ:      return "read";
            case DELETE:    return "delete";
            case ALL:       return "all";
            default:        return null;
        }
    }

    public UseLog(Integer id, String date, String name, String pictureId, String type, String title, String data, String bucket, String bucketName, Integer width, Integer height){
        this.id = id;
        this.date = date;
        this.name= name;
        this.pictureId = pictureId;
        this.type = type;
        this.title = title;
        this.data = data;
        this.bucket = bucket;
        this.bucketName = bucketName;
        this.width = width;
        this.height = height;
    }
    public UseLog(String date, String name, String pictureId, String type, String title, String data, String bucket, String bucketName, Integer width, Integer height){
        this.date = date;
        this.name= name;
        this.pictureId = pictureId;
        this.type = type;
        this.title = title;
        this.data = data;
        this.bucket = bucket;
        this.bucketName = bucketName;
        this.width = width;
        this.height = height;
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
        return values;
    }

    @Override
    public String getTableName() {
        return Tables.UseLog._TABLENAME;
    }
}
