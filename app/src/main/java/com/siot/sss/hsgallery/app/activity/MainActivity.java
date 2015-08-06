package com.siot.sss.hsgallery.app.activity;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.fragment.GalleryFragment;
import com.siot.sss.hsgallery.app.fragment.LogFragment;
import com.siot.sss.hsgallery.app.model.unique.DisplayWindow;
import com.siot.sss.hsgallery.util.navigator.FragmentNavigator;
import com.siot.sss.hsgallery.util.navigator.Navigator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity implements Navigator {
    @InjectView(R.id.container) protected LinearLayout container;
    @InjectView(R.id.toolbar) protected Toolbar toolbar;

    private FragmentNavigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        this.toolbar.inflateMenu(R.menu.menu_main);
        navigator = new FragmentNavigator(this.getFragmentManager(), R.id.container, GalleryFragment.class);
        toolbar.setOnMenuItemClickListener( super::onOptionsItemSelected );
        this.setToolbarItem(this.toolbar.getMenu());
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
    protected void onResume() {
        super.onResume();
        DisplayWindow.getInstance().setWidth(this.getResources().getDisplayMetrics().widthPixels);
        DisplayWindow.getInstance().setHeight(this.getResources().getDisplayMetrics().heightPixels);
        DisplayWindow.getInstance().setDensity(this.getResources().getDisplayMetrics().density);
    }

    private MenuItem seeLog;
    private void setToolbarItem(Menu menu){
        this.seeLog = menu.findItem(R.id.menu_log);
        toolbar.setOnMenuItemClickListener(
            item->{
                if(item.getItemId() == seeLog.getItemId()){
                    this.navigate(LogFragment.class, true);
                }
                return true;
            }
        );
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
