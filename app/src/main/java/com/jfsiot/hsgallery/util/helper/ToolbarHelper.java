package com.jfsiot.hsgallery.util.helper;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.AppConfig;
import com.jfsiot.hsgallery.app.fragment.LogFragment;
import com.jfsiot.hsgallery.app.fragment.OptionFragment;
import com.jfsiot.hsgallery.util.data.image.ImageShow;
import com.jfsiot.hsgallery.util.view.navigator.Navigator;
import com.jfsiot.hsgallery.util.view.navigator.ToolbarCallback;

/**
 * Created by SSS on 2015-08-09.
 */
public class ToolbarHelper {
    private Toolbar toolbar;
    private Navigator navigator;
    private Context context;
    private static ToolbarHelper instance;
    public static synchronized ToolbarHelper getInstance(){
        if(ToolbarHelper.instance == null) ToolbarHelper.instance = new ToolbarHelper();
        return ToolbarHelper.instance;
    }
    public void setManager(Context context, Toolbar toolbar, Navigator navigator){
        this.toolbar = toolbar;
        this.navigator = navigator;
        this.context = context;
        this.toolbar.setOnMenuItemClickListener(
            item -> {
                    /*LOG*/
                if (item.getItemId() == Item.getItem(toolbar, Item.USELOG).getItemId()) {
                    this.navigator.navigate(LogFragment.class, true);

                    /*MOVE*/
                } else if (item.getItemId() == ToolbarHelper.Item.getItem(toolbar, ToolbarHelper.Item.MOVE).getItemId()) {
                    if(navigator.getCurrentFragment() instanceof ToolbarCallback.ToolbarSimpleCallback)
                        ((ToolbarCallback.ToolbarSimpleCallback) navigator.getCurrentFragment()).getCurrentAction(true, Item.MOVE);

                    /*RENAME*/
                } else if (item.getItemId() == ToolbarHelper.Item.getItem(toolbar, ToolbarHelper.Item.RENAME).getItemId()) {
                    MaterialDialog renameDialog = new MaterialDialog.Builder(context)
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
                                ImageShow.getInstance().renameImagedata(context, ImageShow.getInstance().getImageData().id, dialog.getInputEditText().getText().toString(), false);

                                if(navigator.getCurrentFragment() instanceof ToolbarCallback.ToolbarSimpleCallback)
                                    ((ToolbarCallback.ToolbarSimpleCallback) navigator.getCurrentFragment()).getCurrentAction(true, Item.RENAME);
                            }
                        })
                        .show();

                    /* DELETE */
                } else if (item.getItemId() == ToolbarHelper.Item.getItem(toolbar, ToolbarHelper.Item.DELETE).getItemId()) {
                    if(navigator.getCurrentFragment() instanceof ToolbarCallback.ToolbarSimpleCallback)
                        ((ToolbarCallback.ToolbarSimpleCallback) navigator.getCurrentFragment()).getCurrentAction(true, Item.DELETE);

                    /*SELECT*/
                } else if (item.getItemId() == ToolbarHelper.Item.getItem(toolbar, ToolbarHelper.Item.MULTISELECT).getItemId()) {
                    if(navigator.getCurrentFragment() instanceof ToolbarCallback.ToolbarSimpleCallback) {
                        AppConfig.Option.MULTISELECT = !AppConfig.Option.MULTISELECT;
                        ((ToolbarCallback.ToolbarSimpleCallback) navigator.getCurrentFragment()).getCurrentAction(true, Item.MULTISELECT);
                    }
                    /*ROTATE*/
                } else if (item.getItemId() == ToolbarHelper.Item.getItem(toolbar, ToolbarHelper.Item.ROTATE).getItemId()) {
                    if(navigator.getCurrentFragment() instanceof ToolbarCallback.ToolbarSimpleCallback)
                        ((ToolbarCallback.ToolbarSimpleCallback) navigator.getCurrentFragment()).getCurrentAction(true, Item.ROTATE);

                    /*EDIT*/
                } else if (item.getItemId() == ToolbarHelper.Item.getItem(toolbar, ToolbarHelper.Item.EDIT).getItemId()) {
                    if(navigator.getCurrentFragment() instanceof ToolbarCallback.ToolbarSimpleCallback)
                        ((ToolbarCallback.ToolbarSimpleCallback) navigator.getCurrentFragment()).getCurrentAction(true, Item.EDIT);
                    /*MORE*/
                } else if (item.getItemId() == ToolbarHelper.Item.getItem(toolbar, Item.MORE).getItemId()) {
                    if(navigator.getCurrentFragment() instanceof ToolbarCallback.ToolbarSimpleCallback)
                        ((ToolbarCallback.ToolbarSimpleCallback) navigator.getCurrentFragment()).getCurrentAction(true, Item.MORE);

                    /* OPTION */
                } else if (item.getItemId() == ToolbarHelper.Item.getItem(toolbar, ToolbarHelper.Item.SETTING).getItemId()) {
                    this.navigator.navigate(OptionFragment.class, true);

                    /*NEW DIRECTORY*/
                } else if (item.getItemId() == ToolbarHelper.Item.getItem(toolbar, ToolbarHelper.Item.NEW_DIR).getItemId()) {
                    MaterialDialog newDirDialog = new MaterialDialog.Builder(context)
                        .title("rename")
                        .content(context.getString(R.string.dialog_new_dir))
                        .input("rename to", "", false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {

                            }
                        })
                        .positiveText(context.getString(R.string.create_upper))
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                                dialog.getInputEditText().setText("");
                            }

                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                ImageShow.getInstance().insertBucket(context, dialog.getInputEditText().getText().toString());
                                if(navigator.getCurrentFragment() instanceof ToolbarCallback.ToolbarSimpleCallback)
                                    ((ToolbarCallback.ToolbarSimpleCallback) navigator.getCurrentFragment()).getCurrentAction(true, Item.NEW_DIR);
                            }
                        })
                        .show();
                } else if (item.getItemId() == ToolbarHelper.Item.getItem(toolbar, ToolbarHelper.Item.SHARE).getItemId()) {
                    if(navigator.getCurrentFragment() instanceof ToolbarCallback.ToolbarSimpleCallback)
                        ((ToolbarCallback.ToolbarSimpleCallback) navigator.getCurrentFragment()).getCurrentAction(true, Item.SHARE);
                }
                return true;
            }
        );
    }

    public enum State{
        IMAGE, OPERATOR, SHARE, UNSELECTED,DEFAULT
    }

    public ToolbarHelper clear(){
        Item.getItem(toolbar, Item.USELOG).setVisible(true);

        Item.getItem(toolbar, Item.SETTING).setVisible(false);
        Item.getItem(toolbar, Item.DELETE).setVisible(false);
        Item.getItem(toolbar, Item.MOVE).setVisible(false);
        Item.getItem(toolbar, Item.RENAME).setVisible(false);
        Item.getItem(toolbar, Item.ROTATE).setVisible(false);
        Item.getItem(toolbar, Item.EDIT).setVisible(false);
        Item.getItem(toolbar, Item.MORE).setVisible(false);
        Item.getItem(toolbar, Item.MULTISELECT).setVisible(false);
        Item.getItem(toolbar, Item.NEW_DIR).setVisible(true);
        Item.getItem(toolbar, Item.SHARE).setVisible(false);

        return this;
    }
    public ToolbarHelper setEnable(State... state){
        for (State stateItem : state){
            setEnable(stateItem);
        }
        return this;
    }
    public ToolbarHelper setEnable(State state){
        switch (state){
            case IMAGE: //PIC(selected), Image
                Item.getItem(toolbar, Item.EDIT).setVisible(true);
                Item.getItem(toolbar, Item.ROTATE).setVisible(false);
                break;
            case OPERATOR://PIC(selected)
                Item.getItem(toolbar, Item.MOVE).setVisible(true);
                Item.getItem(toolbar, Item.DELETE).setVisible(true);
                Item.getItem(toolbar, Item.RENAME).setVisible(true);
                break;
            case SHARE: //PIC(selected), Image
                Item.getItem(toolbar, Item.SHARE).setVisible(true);
                break;
            case UNSELECTED: //PIC(unselected)
                Item.getItem(toolbar, Item.MULTISELECT).setVisible(true);
                Item.getItem(toolbar, Item.NEW_DIR).setVisible(true);
                break;
            case DEFAULT: //all
                Item.getItem(toolbar, Item.SETTING).setVisible(true);
                Item.getItem(toolbar, Item.MORE).setVisible(false);
                break;
        }
        return this;
    }
    public ToolbarHelper setDisable(State... state){
        for (State stateItem : state){
            setDisable(stateItem);
        }
        return this;
    }
    public ToolbarHelper setDisable(State state){
        switch (state){
            case IMAGE: //PIC(selected), Image
                Item.getItem(toolbar, Item.EDIT).setVisible(false);
                Item.getItem(toolbar, Item.ROTATE).setVisible(false);
                break;
            case OPERATOR://PIC(selected)
                Item.getItem(toolbar, Item.MOVE).setVisible(false);
                Item.getItem(toolbar, Item.DELETE).setVisible(false);
                Item.getItem(toolbar, Item.RENAME).setVisible(false);
                break;
            case SHARE: //PIC(selected), Image
                Item.getItem(toolbar, Item.SHARE).setVisible(false);
                break;
            case UNSELECTED: //PIC(unselected)
                Item.getItem(toolbar, Item.MULTISELECT).setVisible(false);
                Item.getItem(toolbar, Item.NEW_DIR).setVisible(false);
                break;
            case DEFAULT: //all
                Item.getItem(toolbar, Item.SETTING).setVisible(false);
                Item.getItem(toolbar, Item.MORE).setVisible(false);
                break;
        }
        return this;
    }

    public static class Item{
        /* main items */
        public static final int SETTING = R.id.action_settings;
        public static final int MORE = R.id.menu_more;
        public static final int USELOG = R.id.menu_log;
        public static final int MULTISELECT = R.id.multi_select;

        /* directory option */
        public static final int DELETE = R.id.delete;
        public static final int MOVE = R.id.move;
        public static final int RENAME = R.id.rename;
        public static final int NEW_DIR = R.id.menu_new_directory;

        /* image option */
        public static final int ROTATE = R.id.relocate;
        public static final int EDIT = R.id.edit;

        /* share */
        public static final int SHARE = R.id.menu_share;

        public static MenuItem getItem(Toolbar toolbar, int menuItem){
            return toolbar.getMenu().findItem(menuItem);
        }
    }
}
