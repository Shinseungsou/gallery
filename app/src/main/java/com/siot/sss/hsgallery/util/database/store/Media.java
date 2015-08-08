package com.siot.sss.hsgallery.util.database.store;

import android.database.Cursor;

/**
 * Created by SSS on 2015-08-09.
 */
public class Media {
    private static Media instance;
    public static synchronized Media getInstance(){
        if(Media.instance == null) Media.instance = new Media();
        return Media.instance;
    }

    public Cursor getImage(){
        return null;
    }

    public Cursor getThumbnail(){
        return null;
    }
}
