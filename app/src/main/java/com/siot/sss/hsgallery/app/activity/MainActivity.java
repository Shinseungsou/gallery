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

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.AppConfig;
import com.siot.sss.hsgallery.app.fragment.GalleryDIRFragment;
import com.siot.sss.hsgallery.app.fragment.GalleryPICFragment;
import com.siot.sss.hsgallery.app.fragment.LogFragment;
import com.siot.sss.hsgallery.app.fragment.MenuFragment;
import com.siot.sss.hsgallery.app.model.UseLog;
import com.siot.sss.hsgallery.app.model.unique.Configuration;
import com.siot.sss.hsgallery.util.data.db.UseLogManager;
import com.siot.sss.hsgallery.util.data.image.ImageController;
import com.siot.sss.hsgallery.util.data.image.ImageShow;
import com.siot.sss.hsgallery.util.view.MenuItemManager;
import com.siot.sss.hsgallery.util.view.navigator.FragmentNavigator;
import com.siot.sss.hsgallery.util.view.navigator.Navigator;
import com.siot.sss.hsgallery.util.view.navigator.OnBack;
import com.siot.sss.hsgallery.util.view.navigator.ToolbarCallback;

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

        UseLogManager.getInstance().setContext(getBaseContext());

        navigator = new FragmentNavigator(this.getFragmentManager(), R.id.container, GalleryDIRFragment.class);
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
                if (item.getItemId() == menuLog.getItemId()) {
                    this.navigate(LogFragment.class, true);

                } else if (item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.COPY).getItemId()) {
//                    ImageShow.getInstance().copyImagedata(getBaseContext(), null, null);
                    List<CharSequence> names = new ArrayList<>();
                    CharSequence[] names2 = new CharSequence[ImageShow.getInstance().getBuckets().size() - 1];
                    for (int i = 1; i <= names2.length; i++) {
                        if (!ImageShow.getInstance().getBuckets().get(i).id.equals("-1")) {
                            if(ImageShow.getInstance().getBuckets().get(i).displayName != null)
                                names2[i-1] = ImageShow.getInstance().getBuckets().get(i).displayName;
                            else
                                names2[i-1] = "Unknown Directory";
                        }
//                        names2[i] = "hello";
                    }
                    MaterialDialog.Builder moveDialog = new MaterialDialog.Builder(this)
                        .items(names2)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {

                            }
                        });
                    new MaterialDialog.Builder(this)
                        .content("Select Action")
                        .buttonsGravity(GravityEnum.CENTER)
                        .positiveText("MOVE")
                        .neutralText("COPY")
                        .negativeText("CANCEL")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                moveDialog.show();
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);

                            }

                            @Override
                            public void onNeutral(MaterialDialog dialog) {
                                super.onNeutral(dialog);
                                moveDialog.show();
                            }
                        }).show();
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


                } else if (item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.PASTE).getItemId()) {

                } else if (item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.CUT).getItemId()) {
                    ImageShow.getInstance().moveImagedata(getBaseContext(), null);

                } else if (item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.DELETE).getItemId()) {
                    ImageShow.getInstance().deleteImagedata(this.getBaseContext(), ImageShow.getInstance().getPosition());
                    UseLogManager.getInstance().addLog(UseLog.Type.DELETE);
                    this.navigate(GalleryPICFragment.class, false);

                } else if (item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.MULTISELECT).getItemId()) {
                    if(toolbarSimpleCallback != null)
                        if(!AppConfig.Option.MULTISELECT) {
                            this.toolbarSimpleCallback.getCurrentAction(true, MenuItemManager.Item.MULTISELECT);
                            AppConfig.Option.MULTISELECT = true;
                        }else{
                            this.toolbarSimpleCallback.getCurrentAction(false, MenuItemManager.Item.MULTISELECT);
                            AppConfig.Option.MULTISELECT = false;
                        }
                } else if (item.getItemId() == MenuItemManager.Item.getItem(toolbar, MenuItemManager.Item.RELOCATE).getItemId()) {
                    ImageShow.getInstance().relocateImagedata(this.getBaseContext(), ImageShow.getInstance().getPosition());

                } else if (item.getItemId() == menuMore.getItemId()) {
                    if (this.menuLayout.getVisibility() == View.VISIBLE)
                        this.menuLayout.setVisibility(View.GONE);
                    else
                        this.menuLayout.setVisibility(View.VISIBLE);
                }
                return true;
            }
        );
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
        if(this.onBack != null)
            return onBack.onBack();
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
    public void setOnBack(OnBack onBack){
        this.onBack = onBack;
    }
}
