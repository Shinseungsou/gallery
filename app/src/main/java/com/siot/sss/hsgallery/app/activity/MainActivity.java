package com.siot.sss.hsgallery.app.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.LinearLayout;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.fragment.GalleryFragment;
import com.siot.sss.hsgallery.util.navigator.FragmentNavigator;
import com.siot.sss.hsgallery.util.navigator.Navigator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;


public class MainActivity extends Activity implements Navigator {
    @InjectView(R.id.container) protected LinearLayout container;
    @InjectView(R.id.toolbar) protected Toolbar toolbar;

    private FragmentNavigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        navigator = new FragmentNavigator(this.getFragmentManager(), R.id.container, GalleryFragment.class);
        toolbar.setOnMenuItemClickListener( super::onOptionsItemSelected );
    }
    public Toolbar getToolbar(){
        return this.toolbar;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private boolean terminate = false;
    @Override
    public void onBackPressed() {
        if(this.navigator.back()) terminate = false;
        else if(terminate) super.onBackPressed();
        else terminate = true;

    }

    @Override
    public void navigate(Class<? extends Fragment> target, boolean addToBackStack) {
        this.navigator.navigate(target, addToBackStack);
    }

    @Override
    public void navigate(Class<? extends Fragment> target, boolean addToBackStack, boolean clear) {
        this.navigator.navigate(target, addToBackStack, clear);
    }

    @Override
    public void navigate(Class<? extends Fragment> target, Bundle bundle, boolean addToBackStack) {
        this.navigator.navigate(target, bundle, addToBackStack);
    }

    @Override
    public void navigate(Class<? extends Fragment> target, Bundle bundle, boolean addToBackStack, boolean clear) {
        this.navigator.navigate(target, bundle, addToBackStack, clear);
    }

    @Override
    public boolean back() {
        return this.navigator.back();
    }

    @Override
    public String getBackStackNameAt(int index) {
        return this.navigator.getBackStackNameAt(index);
    }
}
