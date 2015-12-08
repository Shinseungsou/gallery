package com.jfsiot.hsgallery.app.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.AppConfig;
import com.jfsiot.hsgallery.app.activity.MainActivity;
import com.jfsiot.hsgallery.app.model.ImageData;
import com.jfsiot.hsgallery.app.model.UseLog;
import com.jfsiot.hsgallery.app.recycler.adapter.GalleryAdapter;
import com.jfsiot.hsgallery.util.data.db.UseLogManager;
import com.jfsiot.hsgallery.util.data.image.ImageController;
import com.jfsiot.hsgallery.util.data.image.ImageShow;
import com.jfsiot.hsgallery.util.view.MenuItemManager;
import com.jfsiot.hsgallery.util.view.navigator.Navigator;
import com.jfsiot.hsgallery.util.view.navigator.OnBack;
import com.jfsiot.hsgallery.util.view.navigator.ToolbarCallback;
import com.jfsiot.hsgallery.util.view.recyclerview.OnMenuChange;
import com.jfsiot.hsgallery.util.view.recyclerview.RecyclerViewFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by SSS on 2015-08-04.
 */
public class GalleryPICFragment extends RecyclerViewFragment<GalleryAdapter, ImageData> implements OnBack, OnMenuChange, ToolbarCallback.ToolbarSimpleCallback {
    @InjectView(R.id.gallery) protected RecyclerView gallery;
    @InjectView(R.id.sidebar) protected LinearLayout sidebar;

    private CompositeSubscription subscription;
    private Toolbar toolbar;
    private Navigator navigator;
    private ImageController imageController;
    private boolean isMultiSelect;

    private List<ImageData> selectList;
    private int menuitemstate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.inject(this, view);
        this.navigator = (MainActivity) this.getActivity();

        this.toolbar = ((MainActivity)this.getActivity()).getToolbar();
        this.toolbar.setTitle(R.string.gallery);

        this.setupRecyclerView(this.gallery);

        imageController = ImageController.getInstance();

        this.sidebar.setVisibility(View.VISIBLE);

        Animation anim = AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.slide_right_show);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) { Timber.d("end!");setOnMenuListener(); }
            @Override public void onAnimationRepeat(Animation animation) { }
        });
        this.sidebar.setAnimation(anim);
        this.isMultiSelect = false;
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(this.subscription != null && !this.subscription.isUnsubscribed()) this.subscription.unsubscribe();
        ButterKnife.reset(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.selectList = new ArrayList<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroyView();

        ((MainActivity) this.getActivity()).setOnBack(null);
        ((MainActivity) this.getActivity()).setToolbarSimpleCallback(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) this.getActivity()).setToolbarSimpleCallback(this);
        ((MainActivity) this.getActivity()).setOnBack(this);
        this.notifyDataChange(ImageShow.getInstance().getBucketId());
        this.menuitemstate = 1;
        MenuItemManager.getInstance().setEnable(MenuItemManager.State.DEFAULT)
            .setEnable(MenuItemManager.State.UNSELECTED)
            .setEnable(MenuItemManager.State.IMAGE);
        this.getFragmentManager()
            .beginTransaction()
            .add(this.sidebar.getId(), ((MainActivity)getActivity()).getFragmentNavigator().instantiateFragment(SideBarFragment.class))
            .commit();
        gallery.setItemAnimator(new DefaultItemAnimator());
    }


    public void notifyDataChange(String id){
        this.items.clear();
        this.items.addAll(imageController.getImageDataList(id));
        this.adapter.notifyDataSetChanged();
        ImageShow.getInstance().initImageShow();
    }

    @Override
    protected GalleryAdapter getAdapter() {
        return new GalleryAdapter(this.items, this);
    }

    @Override
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return new GridLayoutManager(this.getActivity(), 2);
    }

    @Override
    public void onRecyclerViewItemClick(View view, int position) {

        if(!AppConfig.Option.MULTISELECT) {
            ImageShow.getInstance().setImageData(this.items.get(position));
            ImageShow.getInstance().setPosition(position);
            UseLogManager.getInstance().addLog(this.items.get(position), UseLog.Type.READ);
            this.navigator.navigate(ImageFragment.class, true);
        }else{
            int i;
            if((i = contains(this.items.get(position).id)) >= 0)
                this.selectList.remove(i);
            else {
                this.selectList.add(this.items.get(position));
            }
            if(!this.selectList.isEmpty()){
                this.menuitemstate = 2;
            }else{
                this.menuitemstate = 1;
            }
            MenuItemManager.getInstance().menuItemVisible(this.menuitemstate);

            Timber.d("list : %s", this.selectList.toString());
        }
    }

    private Integer contains(String id){
        for(int i = 0; i < selectList.size(); i++){
            if(selectList.get(i).id.equals(id))
                return i;
        }
        return -1;
    }

    @Override
    public boolean onBack() {
        Timber.d("fragment back!");
        if(AppConfig.Option.MULTISELECT) {
            this.getCurrentAction(false, MenuItemManager.Item.MULTISELECT);
            AppConfig.Option.MULTISELECT = false;
            MenuItemManager.getInstance().menuItemVisible(1);
            this.selectList.clear();
            return true;
        }else {
            Animation anim = AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.slide_right_hide);
            this.sidebar.startAnimation(anim);

            return false;
        }
    }

    public void setOnMenuListener(){
        ((SideBarFragment)getFragmentManager().findFragmentById(sidebar.getId())).setOnMenuChange(this);
    }

    @Override
    public void onMenuChange(String id) {
        ImageShow.getInstance().setBucketId(id);
        this.notifyDataChange(id);
        this.selectList.clear();
    }

    @Override
    public void getCurrentAction(boolean isRun, int item) {
        if(item == MenuItemManager.Item.MULTISELECT){
            this.adapter.notifyDataSetChanged();
        }else if (item == MenuItemManager.Item.MOVE){
            ImageShow.getInstance().move(this.getActivity(), selectList);
            this.notifyDataChange(ImageShow.getInstance().getBucketId());
        }else if(item == MenuItemManager.Item.DELETE){
            MaterialDialog renameDialog = new MaterialDialog.Builder(getActivity())
            .title(R.string.delete_upper)
                .content(getResources().getQuantityString(R.plurals.dialog_select, selectList.size(), selectList.size()))
                .positiveText(R.string.confirm_upper)
                .negativeText(R.string.cancel_upper)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        ImageShow.getInstance().deleteImagedata(getActivity(), selectList);
                        notifyDataChange(ImageShow.getInstance().getBucketId());
                    }
                })
                .show();
        }else if(item == MenuItemManager.Item.FACEBOOK){
            ImageShow.getInstance().sendFacebook(this.getActivity(), selectList);
            UseLogManager.getInstance().addLogList(selectList, new String(), UseLog.getShareString(UseLog.Share.FACEBOOK));
        }else if(item == MenuItemManager.Item.KAKAO){
            ImageShow.getInstance().sendKaKao(this.getActivity(), selectList);
            UseLogManager.getInstance().addLogList(selectList, new String(), UseLog.getShareString(UseLog.Share.KAKAO));
        }else if(item == MenuItemManager.Item.INSTAGRAM){
            ImageShow.getInstance().sendInstagram(this.getActivity(), selectList.get(0));
            UseLogManager.getInstance().addLog(selectList.get(0), new String(), UseLog.getShareString(UseLog.Share.INSTAGRAM));
        }

        this.notifyDataChange(ImageShow.getInstance().getBucketId());
    }

}
