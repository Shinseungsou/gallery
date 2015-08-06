package com.siot.sss.hsgallery.util.database.table;

import android.provider.BaseColumns;

/**
 * Created by SSS on 2015-08-06.
 */
public class Tables {

    public static final class UseLog implements BaseColumns{
        public static final String DATE = "date";
        public static final String NAME = "name";
        public static final String PICTUREID = "picture_id";
        public static final String _TABLENAME = "uselog";
        public static final String _CREATE =
            "create table " + _TABLENAME +"( "+
                _ID + " integer primary key autoincrement, " +
                DATE + " text not null, " +
                NAME +" text not null, " +
                PICTUREID + " text not null );";

    }
}
