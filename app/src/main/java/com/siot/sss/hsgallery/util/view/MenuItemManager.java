package com.siot.sss.hsgallery.util.view;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.siot.sss.hsgallery.R;

/**
 * Created by SSS on 2015-08-09.
 */
public class MenuItemManager {
    private Toolbar toolbar;
    private static MenuItemManager instance;
    public static synchronized MenuItemManager getInstance(){
        if(MenuItemManager.instance == null) MenuItemManager.instance = new MenuItemManager();
        return MenuItemManager.instance;
    }
    public void setManager(Toolbar toolbar){
        this.toolbar = toolbar;
    }

    public void menuItemVisible(int state){
        Item.getItem(toolbar, Item.SETTING).setVisible(false);
        Item.getItem(toolbar, Item.DELETE).setVisible(false);
        Item.getItem(toolbar, Item.COPY).setVisible(false);
        Item.getItem(toolbar, Item.CUT).setVisible(false);
        Item.getItem(toolbar, Item.PASTE).setVisible(false);
        Item.getItem(toolbar, Item.RENAME).setVisible(false);
        Item.getItem(toolbar, Item.RELOCATE).setVisible(false);
        Item.getItem(toolbar, Item.CROP).setVisible(false);
        Item.getItem(toolbar, Item.USELOG).setVisible(true);
        Item.getItem(toolbar, Item.MORE).setVisible(true);
        Item.getItem(toolbar, Item.MULTISELECT).setVisible(false);
        switch (state){
            case 0:
                break;
            case 1:
                Item.getItem(toolbar, Item.SETTING).setVisible(true);
                Item.getItem(toolbar, Item.MORE).setVisible(true);
                Item.getItem(toolbar, Item.MULTISELECT).setVisible(true);
                break;
            case 2:
                Item.getItem(toolbar, Item.COPY).setVisible(true);
                Item.getItem(toolbar, Item.DELETE).setVisible(true);
                Item.getItem(toolbar, Item.CUT).setVisible(true);
                Item.getItem(toolbar, Item.RENAME).setVisible(true);
                Item.getItem(toolbar, Item.CROP).setVisible(true);
                Item.getItem(toolbar, Item.MORE).setVisible(false);
                break;
        }
    }

    public enum MenuType{
        NORMAL, IMAGE, MOVE
    }
    public int getMenuType(MenuType type){
        return type.ordinal();
    }

    public static class Item{
        public static final int SETTING = R.id.action_settings;
        public static final int DELETE = R.id.delete;
        public static final int COPY = R.id.copy;
        public static final int CUT = R.id.cut;
        public static final int PASTE = R.id.paste;
        public static final int RENAME = R.id.rename;
        public static final int USELOG = R.id.menu_log;
        public static final int RELOCATE = R.id.relocate;
        public static final int CROP = R.id.crop;
        public static final int MULTISELECT = R.id.multi_select;
        public static final int MORE = R.id.menu_more;

        public static MenuItem getItem(Toolbar toolbar, int menuItem){
            return toolbar.getMenu().findItem(menuItem);
        }
    }
}
