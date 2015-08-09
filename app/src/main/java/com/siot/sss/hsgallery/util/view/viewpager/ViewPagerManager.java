package com.siot.sss.hsgallery.util.view.viewpager;

import android.view.View;

import com.siot.sss.hsgallery.app.model.ImageData;

import java.util.List;

/**
 * Created by SSS on 2015-08-09.
 */
public class ViewPagerManager {

    private List<ImageData> items;
    public ViewPagerManager(List<ImageData> items){
        this.items = items;
    }

    public View getView(int position){
        return null;
    }
}
