package com.siot.sss.hsgallery.util.data.db.table;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.siot.sss.hsgallery.app.model.UseLog;
import com.siot.sss.hsgallery.util.data.db.DBHelper;


/**
 * Created by SSS on 2015-08-06.
 */
public class DBOpenHelper{
    private static final String DATABASE_NAME = "gallery.db";
    private static final int DATABASE_VERSION = 6;
    public static SQLiteDatabase sqlite;
    private DBHelper dbHelper;
    private Context context;

    public DBOpenHelper(Context context){
        this.context = context;
    }

    public DBOpenHelper open() throws SQLException {
        dbHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqlite = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        sqlite.close();
    }



    // Insert DB
    public long insertColumnUseLog(UseLog useLog){
        ContentValues values = new ContentValues();
        values.put(Tables.UseLog.NAME, useLog.name);
        values.put(Tables.UseLog.DATE, useLog.date);
        values.put(Tables.UseLog.PICTUREID, useLog.pictureId);
        values.put(Tables.UseLog.TYPE, useLog.type);
        values.put(Tables.UseLog.TITLE, useLog.title);
        values.put(Tables.UseLog.DATA, useLog.data);
        values.put(Tables.UseLog.BUCKET, useLog.bucket);
        values.put(Tables.UseLog.BUCKETNAME, useLog.bucketName);
        values.put(Tables.UseLog.WIDTH, useLog.width);
        values.put(Tables.UseLog.HEIGHT, useLog.height);
        return sqlite.insert(Tables.UseLog._TABLENAME, null, values);
    }

    // Update DB
    public boolean updateColumnUseLog(long id, UseLog useLog){
        ContentValues values = new ContentValues();
        values.put(Tables.UseLog.NAME, useLog.name);
        values.put(Tables.UseLog.DATE, useLog.date);
        values.put(Tables.UseLog.PICTUREID, useLog.pictureId);
        values.put(Tables.UseLog.TYPE, useLog.type);
        return sqlite.update(Tables.UseLog._TABLENAME, values, "_id="+id, null) > 0;
    }

    // Delete ID
    public boolean deleteColumnUseLog(long id){
        return sqlite.delete(Tables.UseLog._TABLENAME, "_id="+id, null) > 0;
    }

    // Delete Contact
    public boolean deleteColumnUseLog(String number){
        return sqlite.delete(Tables.UseLog._TABLENAME, "contact="+number, null) > 0;
    }

    public Cursor getUseLog(UseLog.Type type){
        String selection = Tables.UseLog.TYPE + " like " + UseLog.getTypeString(type);
        return sqlite.query(Tables.UseLog._TABLENAME, null, selection, null, null, null, null);
    }
    // Select All
    public Cursor getAllColumnsUseLog(){
        return sqlite.query(Tables.UseLog._TABLENAME, null, null, null, null, null, null);
    }

    public Cursor getColumnUseLog(long id){
        Cursor c = sqlite.query(Tables.UseLog._TABLENAME, null,
            "_id="+id, null, null, null, null);
        if(c != null && c.getCount() != 0)
            c.moveToFirst();
        return c;
    }

    public Cursor getMatchNameUseLog(String name){
        Cursor c = sqlite.rawQuery( "select * from "+Tables.UseLog._TABLENAME+" where name=" + "'" + name + "'" , null);
        return c;
    }
}
