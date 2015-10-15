package com.siot.sss.hsgallery.util.view.navigator;

/**
 * Created by SSS on 2015-10-15.
 */
public interface ToolbarCallback {
    void getCurrentAction(int item);
    void getCurrentAction(boolean isRun, int item);
    public static interface ToolbarSimpleCallback{
        void getCurrentAction(boolean isRun, int item);
    }
}
