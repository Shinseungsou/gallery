package com.jfsiot.hsgallery.app.activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.AppConfig;
import com.jfsiot.hsgallery.app.AppConst;
import com.jfsiot.hsgallery.app.AppManager;
import com.jfsiot.hsgallery.app.fragment.GalleryPICFragment;
import com.jfsiot.hsgallery.app.fragment.LogFragment;
import com.jfsiot.hsgallery.app.fragment.MenuFragment;
import com.jfsiot.hsgallery.app.fragment.OptionFragment;
import com.jfsiot.hsgallery.app.model.unique.Configuration;
import com.jfsiot.hsgallery.util.data.db.UseLogManager;
import com.jfsiot.hsgallery.util.data.image.ImageController;
import com.jfsiot.hsgallery.util.data.image.ImageShow;
import com.jfsiot.hsgallery.util.view.MenuItemManager;
import com.jfsiot.hsgallery.util.view.navigator.FragmentNavigator;
import com.jfsiot.hsgallery.util.view.navigator.Navigator;
import com.jfsiot.hsgallery.util.view.navigator.OnBack;
import com.jfsiot.hsgallery.util.view.navigator.ToolbarCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity implements Navigator{
    @InjectView(R.id.container) protected LinearLayout container;
    @InjectView(R.id.toolbar) protected Toolbar toolbar;
    @InjectView(R.id.menu_layout) protected LinearLayout menuLayout;

    private ToolbarCallback.ToolbarSimpleCallback toolbarSimpleCallback;
    private List<ToolbarCallback.ToolbarSimpleCallback> toolbarSimpleCallbackList;

    private FragmentNavigator navigator;
    private OnBack onBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        this.toolbar.inflateMenu(R.menu.menu_main);
        MenuItemManager.getInstance().setManager(this.toolbar);
        toolbar.setOnMenuItemClickListener(super::onOptionsItemSelected);
        this.setToolbarItem(this.toolbar.getMenu());
        ImageController.getInstance().init(this.getBaseContext());

        AppConfig.Option.SUPER_USER = false;
        AppConfig.Option.MULTISELECT = false;
        this.toolbarSimpleCallbackList = new ArrayList<>();

        UseLogManager.getInstance().setContext(getBaseContext());

        navigator = new FragmentNavigator(this.getFragmentManager(), R.id.container, GalleryPICFragment.class);
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
        Configuration.getInstance().setWidth(this.getResources().getDisplayMetrics().widthPixels);
        Configuration.getInstance().setHeight(this.getResources().getDisplayMetrics().heightPixels);
        Configuration.getInstance().setDensity(this.getResources().getDisplayMetrics().density);
        AppManager.getInstance().setContext(this);
        if(AppManager.getInstance().getString(AppConst.Preference.USER_NAME, null) == null)
            AppManager.getInstance().setUserName();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        this.getFragmentManager()
            .beginTransaction()
            .add(this.menuLayout.getId(), navigator.instantiateFragment(MenuFragment.class))
            .commit();
    }

    private MenuItem menuLog;
    private MenuItem menuMore;
    private void setToolbarItem(Menu menu){
        this.menuLog = menu.findItem(R.id.menu_log);
        this.menuMore = menu.findItem(R.id.menu_more);
        toolbar.setOnMenuItemClickListener(
            item -> {
                    /*LOG*/
                if (item.getItemId() == menuLog.getItemId()) {
                    this.navigate(LogFragment.class, true);

                    /*MOVE*/
                } else if (item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.MOVE).getItemId()) {
                    if(toolbarSimpleCallback != null)
                        toolbarSimpleCallback.getCurrentAction(true, MenuItemManager.Item.MOVE);

                    /*RENAME*/
                } else if (item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.RENAME).getItemId()) {
                    MaterialDialog renameDialog = new MaterialDialog.Builder(this)
                        .title("rename")
                        .content(ImageShow.getInstance().getImageData().displayName)
                        .input("rename to", "", false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {

                            }
                        })
                        .positiveText("rename")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                                dialog.getInputEditText().setText("");
                            }

                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                ImageShow.getInstance().renameImagedata(getBaseContext(), ImageShow.getInstance().getImageData().id, dialog.getInputEditText().getText().toString(), false);
                                if(toolbarSimpleCallback != null)
                                    toolbarSimpleCallback.getCurrentAction(true, MenuItemManager.Item.RENAME);
                            }
                        })
                        .show();

                    /* DELETE */
                } else if (item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.DELETE).getItemId()) {
                    if(toolbarSimpleCallback != null)
                        toolbarSimpleCallback.getCurrentAction(true, MenuItemManager.Item.DELETE);

                    /*SELECT*/
                } else if (item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.MULTISELECT).getItemId()) {
                    if(toolbarSimpleCallback != null) {
                        AppConfig.Option.MULTISELECT = !AppConfig.Option.MULTISELECT;
                        this.toolbarSimpleCallback.getCurrentAction(AppConfig.Option.MULTISELECT, MenuItemManager.Item.MULTISELECT);
                    }
                    /*ROTATE*/
                } else if (item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.ROTATE).getItemId()) {
                    if(toolbarSimpleCallback != null)
                        toolbarSimpleCallback.getCurrentAction(true, MenuItemManager.Item.ROTATE);

                    /*EDIT*/
                } else if (item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.EDIT).getItemId()) {
                    if(toolbarSimpleCallback != null)
                        toolbarSimpleCallback.getCurrentAction(true, MenuItemManager.Item.EDIT);
                    /*MORE*/
                } else if (item.getItemId() == menuMore.getItemId()) {
                    if (this.menuLayout.getVisibility() == View.VISIBLE)
                        this.menuLayout.setVisibility(View.GONE);
                    else
                        this.menuLayout.setVisibility(View.VISIBLE);
                    /* OPTION */
                } else if (item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.SETTING).getItemId()) {
                    this.navigate(OptionFragment.class, true);

                    /*NEW DIRECTORY*/
                } else if (item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.NEW_DIR).getItemId()) {
                    MaterialDialog newDirDialog = new MaterialDialog.Builder(this)
                        .title("rename")
                        .content(getString(R.string.dialog_new_dir))
                        .input("rename to", "", false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {

                            }
                        })
                        .positiveText(getString(R.string.create_upper))
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                                dialog.getInputEditText().setText("");
                            }

                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                ImageShow.getInstance().insertBucket(getBaseContext(), dialog.getInputEditText().getText().toString());
                                if(toolbarSimpleCallback != null)
                                    toolbarSimpleCallback.getCurrentAction(true, MenuItemManager.Item.NEW_DIR);
                            }
                        })
                        .show();
                } else if(item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.KAKAO).getItemId()){
                    if(toolbarSimpleCallback != null) {
                        this.toolbarSimpleCallback.getCurrentAction(true, MenuItemManager.Item.KAKAO);
                    }
                } else if(item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.FACEBOOK).getItemId()){
                    if(toolbarSimpleCallback != null) {
                        this.toolbarSimpleCallback.getCurrentAction(true, MenuItemManager.Item.FACEBOOK);
                    }
                } else if(item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.INSTAGRAM).getItemId()){
                    if(toolbarSimpleCallback != null) {
                        this.toolbarSimpleCallback.getCurrentAction(true, MenuItemManager.Item.INSTAGRAM);
                    }
                }
                return true;
            }
        );
    }

    private void initShareIntent(String type, String title, String filePath) {
        String fileName = "image-3116.jpg";//Name of an image
        String externalStorageDirectory = Environment.getExternalStorageDirectory().toString();
        String myDir = externalStorageDirectory + "/saved_images/"; // the file will be in saved_images
        Uri uri = Uri.parse("file:///" + myDir + fileName);
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/html");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Test Mail");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Launcher");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, "Share Deal"));
    }

    public String getCurrentFragmentName(){
        return this.getFragmentManager().findFragmentById(container.getId()).getClass().getSimpleName();
    }

    public Fragment getCurrentFragment(){
        return this.getFragmentManager().findFragmentById(container.getId());
    }

    public FragmentNavigator getFragmentNavigator(){
        return this.navigator;
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
        Timber.d("activity back!");
//        if(this.onBack != null)
//            return onBack.onBack();
        if(navigator.getCurrentFragment() instanceof OnBack)
            ((OnBack) navigator.getCurrentFragment()).onBack();
        return this.navigator.back();
    }

    @Override
    public String getBackStackNameAt(int index) {
        return this.navigator.getBackStackNameAt(index);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            this.getToolbar().getMenu().findItem(R.id.action_settings).setVisible(true);
            this.getToolbar();
            return true;
        }else if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if(this.menuLayout.getVisibility() == View.VISIBLE){
                this.menuLayout.setVisibility(View.INVISIBLE);
                return true;
            }
            Timber.d("back!");
        }
        return super.onKeyUp(keyCode, event);
    }

    public void setToolbarSimpleCallback(ToolbarCallback.ToolbarSimpleCallback callback){
        this.toolbarSimpleCallback = callback;
        if(callback == null){
            AppConfig.Option.MULTISELECT = false;
        }
    }
    public void addToolbarSimpleCallback(ToolbarCallback.ToolbarSimpleCallback callback){
        this.toolbarSimpleCallback = callback;
        this.toolbarSimpleCallbackList.add(callback);
    }

    public void removeToolbarSimpleCallback(ToolbarCallback.ToolbarSimpleCallback callback){
        this.toolbarSimpleCallbackList.remove(callback);
    }

    public void toolbarSimpleCallbackNotify(boolean isRun, int item){
        toolbarSimpleCallback.getCurrentAction(isRun, item);
        for(ToolbarCallback.ToolbarSimpleCallback callback : toolbarSimpleCallbackList){
            callback.getCurrentAction(isRun, item);
        }
    }

    public void setOnBack(OnBack onBack){
        this.onBack = onBack;
    }
}
