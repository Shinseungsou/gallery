package com.siot.sss.hsgallery.util.view.navigator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.siot.sss.hsgallery.app.AppConst;

/**
 * Created by SSS on 2015-08-05.
 */
public class FragmentNavigator implements Navigator {

    private FragmentManager manager;
    private int containerViewId;

    public FragmentNavigator(FragmentManager manager, int containerViewId, Class<? extends Fragment> initialFragment){
        this.containerViewId = containerViewId;
        this.manager = manager;
        Fragment fragment = this.instantiateFragment(initialFragment);
        this.manager.beginTransaction()
            .add(this.containerViewId, fragment)
            .addToBackStack(fragment.getClass().getSimpleName())
            .commit();
    }

    @Override
    public void navigate(Class<? extends Fragment> target, Bundle bundle, boolean addToBackStack, boolean clear) {
        final Fragment current = this.manager.findFragmentById(this.containerViewId);
        Fragment nextFragment = this.instantiateFragment(target);
        if(bundle != null) nextFragment.setArguments(bundle);
        if(current != null && current.getClass().getSimpleName().equals(target.getSimpleName())) return;
        if (clear){
            this.manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = this.manager.beginTransaction();
        if(addToBackStack) transaction.addToBackStack(nextFragment.getClass().getSimpleName());
        transaction.replace(this.containerViewId, nextFragment, AppConst.Tag.ACTIVE_FRAGMENT).commit();

    }

    @Override
    public void navigate(Class<? extends Fragment> target, boolean addToBackStack) {
        this.navigate(target, null, addToBackStack, false);
    }

    @Override
    public void navigate(Class<? extends Fragment> target, boolean addToBackStack, boolean clear) {
        this.navigate(target, null, addToBackStack, clear);

    }

    @Override
    public void navigate(Class<? extends Fragment> target, Bundle bundle, boolean addToBackStack) {
        this.navigate(target, bundle, addToBackStack, false);
    }

    @Override
    public boolean back() {
        final Fragment current = this.manager.findFragmentById(this.containerViewId);
        if (current != null) {
            boolean backed = false;
            if(!backed && current instanceof OnBack) backed = ((OnBack) current).onBack();
            if(!backed) {
                if(this.manager.getBackStackEntryCount() > 2) {
                    this.manager.popBackStack();
                    backed = true;
                } else if(this.manager.getBackStackEntryCount() == 2) {
                    this.manager.popBackStack();
                    backed = true;
                } if(this.manager.getBackStackEntryCount() == 1){
                    backed = false;
                }
            } return backed;
        } return false;
    }

    @Override
    public String getBackStackNameAt(int index) {
        if(this.manager.getBackStackEntryCount() < index + 1) return null;
        return this.manager.getBackStackEntryAt(this.manager.getBackStackEntryCount() - index - 1).getName();
    }

    public Fragment instantiateFragment(Class<? extends Fragment> fragment){
        Fragment instance = null;
        try {
            instance = fragment.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return instance;
    }
}
