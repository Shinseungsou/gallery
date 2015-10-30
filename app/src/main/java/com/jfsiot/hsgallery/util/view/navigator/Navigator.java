package com.jfsiot.hsgallery.util.view.navigator;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by SSS on 2015-08-05.
 */
public interface Navigator {
    public void navigate(Class<? extends Fragment> target, boolean addToBackStack);
    public void navigate(Class<? extends Fragment> target,  boolean addToBackStack, boolean clear);
    public void navigate(Class<? extends Fragment> target, Bundle bundle, boolean addToBackStack);
    public void navigate(Class<? extends Fragment> target, Bundle bundle, boolean addToBackStack,  boolean clear);
    public boolean back();
    public String getBackStackNameAt(int index);
}
