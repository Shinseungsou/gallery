package com.siot.sss.hsgallery.util.database.table;

import android.provider.BaseColumns;

/**
 * Created by SSS on 2015-08-06.
 */
public class Tables {

    public static final class UseLog implements BaseColumns{
        public static final String DATE = "date";
        public static final String NAME = "name";
        public static final String TITLE = "title";
        public static final String PICTUREID = "picture_id";
        public static final String TYPE = "type";
        public static final String DATA = "data";
        public static final String BUCKET = "bucket";
        public static final String BUCKETNAME = "bucketname";
        public static final String WIDTH = "width";
        public static final String HEIGHT = "height";



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
                HEIGHT + " integer" +
                ");";
    }
}
