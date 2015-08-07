package com.siot.sss.hsgallery.app.model;

import android.content.ContentValues;

import com.siot.sss.hsgallery.util.database.table.Tables;

/**
 * Created by SSS on 2015-08-06.
 */
public class UseLog extends DBModel {
    public Integer id;
    public String date;
    public String name;
    public String pictureId;
    public String type;

    public enum Type{
        SAVE, READ, UPDATE, DELETE
    }
    public static String getType(Type type){
        switch (type) {
            case SAVE:      return "save";
            case UPDATE:    return "update";
            case READ:      return "read";
            case DELETE:    return "delete";
            default:        return null;
        }
    }

    public UseLog(Integer id, String date, String name, String pictureId, String type){
        this.id = id;
        this.date = date;
        this.name= name;
        this.pictureId = pictureId;
        this.type = type;
    }
    public UseLog(String date, String name, String pictureId, String type){
        this.date = date;
        this.name= name;
        this.pictureId = pictureId;
        this.type = type;
    }

    @Override
    public ContentValues getValue() {
        ContentValues values = new ContentValues();
        values.put(Tables.UseLog.NAME, this.name);
        values.put(Tables.UseLog.DATE, this.date);
        values.put(Tables.UseLog.PICTUREID, this.pictureId);
        return null;
    }

    @Override
    public String getTableName() {
        return Tables.UseLog._TABLENAME;
    }
}
