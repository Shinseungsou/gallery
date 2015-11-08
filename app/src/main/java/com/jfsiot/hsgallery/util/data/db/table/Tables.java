package com.jfsiot.hsgallery.util.data.db.table;

import android.provider.BaseColumns;

/**
 * Created by SSS on 2015-08-06.
 */
public class Tables {

    public static final class UseLog implements BaseColumns{
        public static enum COLUMN {
            ID, DATE , NAME, TITLE, TYPE, PICTUREID, DATA, BUCKET, BUCKETNAME, WIDTH, HEIGHT, TO_DATA, NOTE, SHARE;
        }
        public static String getString(COLUMN column){
            switch (column){
                case ID : return "id";
                case DATE : return "date";
                case NAME : return "name";
                case TITLE : return "title";
                case TYPE : return "type";
                case PICTUREID : return "picture_id";
                case DATA : return "data";
                case BUCKET : return "bucket";
                case BUCKETNAME : return "bucketname";
                case WIDTH : return "width";
                case HEIGHT : return "height";
                case TO_DATA : return "to_data";
                case NOTE : return "note";
                case SHARE : return "share";
            }
            return null;
        }

        public static final String DATE = "date";
        public static final String NAME = "name";
        public static final String TITLE = "title";
        public static final String TYPE = "type";
        public static final String PICTUREID = "picture_id";
        public static final String DATA = "data";
        public static final String BUCKET = "bucket";
        public static final String BUCKETNAME = "bucketname";
        public static final String WIDTH = "width";
        public static final String HEIGHT = "height";
        public static final String TO_DATA = "to_data";
        public static final String NOTE = "note";
        public static final String SHARE = "share";

        public static final String _TABLENAME = "uselog";
        public static final String _CREATE =
            "create table " + _TABLENAME +"( "+
                _ID + " integer primary key autoincrement, " +
                DATE + " text not null , " +
                NAME +" text not null, " +
                TITLE + " text not null, " +
                TYPE + " text not null, " +
                PICTUREID + " text not null, " +
                DATA + " text not null, " +
                BUCKET + " text not null, " +
                BUCKETNAME + " text not null, " +
                WIDTH + " integer, " +
                HEIGHT + " integer, " +
                TO_DATA + " text, " +
                NOTE + " text, " +
                SHARE + " text " +
                ");";
    }
}
