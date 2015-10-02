package com.siot.sss.hsgallery.app;

import android.app.Application;
import android.util.Log;

import timber.log.Timber;

/**
 * Created by SSS on 2015-08-05.
 */
public class HSGalleryApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }
}
