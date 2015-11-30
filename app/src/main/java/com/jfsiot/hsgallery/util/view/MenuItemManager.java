package com.jfsiot.hsgallery.util.view;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jfsiot.hsgallery.R;

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
        Item.getItem(toolbar, Item.MOVE).setVisible(false);
        Item.getItem(toolbar, Item.RENAME).setVisible(false);
        Item.getItem(toolbar, Item.ROTATE).setVisible(false);
        Item.getItem(toolbar, Item.CROP).setVisible(false);
        Item.getItem(toolbar, Item.USELOG).setVisible(true);
        Item.getItem(toolbar, Item.MORE).setVisible(false);
        Item.getItem(toolbar, Item.MULTISELECT).setVisible(false);
        Item.getItem(toolbar, Item.NEW_DIR).setVisible(true);
        Item.getItem(toolbar, Item.KAKAO).setVisible(false);
        Item.getItem(toolbar, Item.FACEBOOK).setVisible(false);
        Item.getItem(toolbar, Item.INSTAGRAM).setVisible(false);
        switch (state){
            case 0:
                break;
            case 1:
                Item.getItem(toolbar, Item.SETTING).setVisible(true);
                Item.getItem(toolbar, Item.MORE).setVisible(false);
                Item.getItem(toolbar, Item.MULTISELECT).setVisible(true);
                break;
            case 2:
                Item.getItem(toolbar, Item.MOVE).setVisible(true);
                Item.getItem(toolbar, Item.DELETE).setVisible(true);
                Item.getItem(toolbar, Item.RENAME).setVisible(true);
                Item.getItem(toolbar, Item.CROP).setVisible(true);
                Item.getItem(toolbar, Item.ROTATE).setVisible(true);
                Item.getItem(toolbar, Item.MORE).setVisible(false);
                Item.getItem(toolbar, Item.KAKAO).setVisible(true);
                Item.getItem(toolbar, Item.FACEBOOK).setVisible(true);
                Item.getItem(toolbar, Item.INSTAGRAM).setVisible(true);
                Item.getItem(toolbar, Item.NEW_DIR).setVisible(false);
                break;
        }
    }
    enum State{
        IMAGE, OPERATOR, SHARE, DEFAULT
    }

    public MenuItemManager clear(){
        Item.getItem(toolbar, Item.SETTING).setVisible(false);
        Item.getItem(toolbar, Item.DELETE).setVisible(false);
        Item.getItem(toolbar, Item.MOVE).setVisible(false);
        Item.getItem(toolbar, Item.RENAME).setVisible(false);
        Item.getItem(toolbar, Item.ROTATE).setVisible(false);
        Item.getItem(toolbar, Item.CROP).setVisible(false);
        Item.getItem(toolbar, Item.USELOG).setVisible(true);
        Item.getItem(toolbar, Item.MORE).setVisible(false);
        Item.getItem(toolbar, Item.MULTISELECT).setVisible(false);
        Item.getItem(toolbar, Item.NEW_DIR).setVisible(true);
        Item.getItem(toolbar, Item.KAKAO).setVisible(false);
        Item.getItem(toolbar, Item.FACEBOOK).setVisible(false);
        Item.getItem(toolbar, Item.INSTAGRAM).setVisible(false);

        return this;
    }
    public MenuItemManager setVisible(State state){
        switch (state){
            case IMAGE:
                Item.getItem(toolbar, Item.CROP).setVisible(true);
                Item.getItem(toolbar, Item.ROTATE).setVisible(true);
                break;
            case OPERATOR:
                Item.getItem(toolbar, Item.MOVE).setVisible(true);
                Item.getItem(toolbar, Item.DELETE).setVisible(true);
                Item.getItem(toolbar, Item.RENAME).setVisible(true);
                Item.getItem(toolbar, Item.NEW_DIR).setVisible(false);
                break;
            case SHARE:
                Item.getItem(toolbar, Item.KAKAO).setVisible(true);
                Item.getItem(toolbar, Item.FACEBOOK).setVisible(true);
                Item.getItem(toolbar, Item.INSTAGRAM).setVisible(true);
                break;
            case DEFAULT:
                Item.getItem(toolbar, Item.SETTING).setVisible(true);
                Item.getItem(toolbar, Item.MORE).setVisible(false);
                Item.getItem(toolbar, Item.MULTISELECT).setVisible(true);
                break;
        }
        return this;
    }

    public enum MenuType{
        NORMAL, IMAGE, MOVE
    }
    public int getMenuType(MenuType type){
        return type.ordinal();
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
        public static final int CROP = R.id.crop;

        /* share */
        public static final int KAKAO = R.id.menu_share_kakao;
        public static final int FACEBOOK = R.id.menu_share_facebook;
        public static final int INSTAGRAM = R.id.menu_share_instagram;

        public static MenuItem getItem(Toolbar toolbar, int menuItem){
            return toolbar.getMenu().findItem(menuItem);
        }
    }
}
