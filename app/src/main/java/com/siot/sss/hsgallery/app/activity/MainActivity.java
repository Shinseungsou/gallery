package com.siot.sss.hsgallery.app.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.fragment.GalleryFragment;
import com.siot.sss.hsgallery.app.fragment.LogFragment;
import com.siot.sss.hsgallery.app.model.unique.DisplayWindow;
import com.siot.sss.hsgallery.app.model.unique.ImageShow;
import com.siot.sss.hsgallery.util.view.MenuItemManager;
import com.siot.sss.hsgallery.util.view.navigator.FragmentNavigator;
import com.siot.sss.hsgallery.util.view.navigator.Navigator;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity implements Navigator, View.OnKeyListener {
    @InjectView(R.id.container) protected LinearLayout container;
    @InjectView(R.id.toolbar) protected Toolbar toolbar;

    private FragmentNavigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        this.toolbar.inflateMenu(R.menu.menu_main);
        MenuItemManager.getInstance().setManager(this.toolbar);
        toolbar.setOnMenuItemClickListener(super::onOptionsItemSelected);
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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        navigator = new FragmentNavigator(this.getFragmentManager(), R.id.container, GalleryFragment.class);
    }

    private MenuItem seeLog;
    private void setToolbarItem(Menu menu){
        this.seeLog = menu.findItem(R.id.menu_log);
        toolbar.setOnMenuItemClickListener(
            item->{
                if(item.getItemId() == seeLog.getItemId()){
                    this.navigate(LogFragment.class, true);
                }else if(item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.COPY).getItemId()){

                }else if(item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.RENAME).getItemId()){

                }else if(item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.PASTE).getItemId()){

                }else if(item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.CUT).getItemId()){

                }else if(item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.DELETE).getItemId()){
                    ImageShow.getInstance().deleteImagedata(this.getContentResolver(), ImageShow.getInstance().getPosition());
                    this.navigate(GalleryFragment.class, false);
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

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_MENU){
            this.getToolbar().getMenu().findItem(R.id.action_settings).setVisible(true);
            this.getToolbar();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
