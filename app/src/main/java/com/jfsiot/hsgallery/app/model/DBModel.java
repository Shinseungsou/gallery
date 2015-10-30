package com.jfsiot.hsgallery.app.model;

import android.content.ContentValues;

/**
 * Created by SSS on 2015-08-07.
 */
public abstract class DBModel {
    public abstract ContentValues getValue();
    public abstract String getTableName();
}
