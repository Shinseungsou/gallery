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
import com.jfsiot.hsgallery.util.dialog.ShareDialog;
import com.jfsiot.hsgallery.util.helper.ToolbarHelper;
import com.jfsiot.hsgallery.util.view.navigator.Navigator;
import com.jfsiot.hsgallery.util.view.navigator.OnBack;
import com.jfsiot.hsgallery.util.view.navigator.ToolbarCallback;
import com.jfsiot.hsgallery.util.view.recyclerview.OnMenuChange;
import com.jfsiot.hsgallery.util.view.recyclerview.RecyclerViewFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by SSS on 2015-08-04.
 */
public class GalleryPICFragment extends RecyclerViewFragment<GalleryAdapter, ImageData> implements OnBack, OnMenuChange, ToolbarCallback.ToolbarSimpleCallback {
    @InjectView(R.id.gallery) protected RecyclerView gallery;
    @InjectView(R.id.sidebar) protected LinearLayout sidebar;
    @InjectView(R.id.progress_bar) protected MaterialProgressBar progressBar;

    private CompositeSubscription subscription;
    private Toolbar mToolbar;
    private Navigator mNavigator;
    private ImageController imageController;

    private List<ImageData> selectList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.inject(this, view);
        this.mNavigator = (MainActivity) this.getActivity();

        this.mToolbar = ((MainActivity)this.getActivity()).getToolbar();
        this.mToolbar.setTitle(R.string.gallery);

        this.setupRecyclerView(this.gallery);

        imageController = ImageController.getInstance();

        this.sidebar.setVisibility(View.VISIBLE);

        Animation anim = AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.slide_right_show);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Timber.d("end!");
                setOnMenuListener();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        this.sidebar.setAnimation(anim);
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
        AppConfig.Option.MULTISELECT = false;
        this.selectList.clear();

        ((MainActivity) this.getActivity()).setToolbarSimpleCallback(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) this.getActivity()).setToolbarSimpleCallback(this);
        this.notifyDataChange(ImageShow.getInstance().getBucketId());

        ToolbarHelper.getInstance().clear().setEnable(ToolbarHelper.State.DEFAULT, ToolbarHelper.State.UNSELECTED);
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
            this.mNavigator.navigate(ImageFragment.class, true);
        }else{
            int i;
            if((i = contains(this.items.get(position).id)) >= 0)
                this.selectList.remove(i);
            else {
                this.selectList.add(this.items.get(position));
            }
            if(this.selectList.isEmpty()) {
                ToolbarHelper.getInstance().clear().setEnable(ToolbarHelper.State.DEFAULT, ToolbarHelper.State.UNSELECTED);
            }else if(this.selectList.size() == 1){
                ToolbarHelper.getInstance().clear().setEnable(ToolbarHelper.State.DEFAULT, ToolbarHelper.State.IMAGE, ToolbarHelper.State.OPERATOR, ToolbarHelper.State.SHARE);
            }else{
                ToolbarHelper.getInstance().clear().setEnable(ToolbarHelper.State.DEFAULT, ToolbarHelper.State.SHARE, ToolbarHelper.State.OPERATOR);
            }

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
            this.getCurrentAction(false, ToolbarHelper.Item.MULTISELECT);
            AppConfig.Option.MULTISELECT = false;
            ToolbarHelper.getInstance().clear().setEnable(ToolbarHelper.State.UNSELECTED, ToolbarHelper.State.DEFAULT);
            this.selectList.clear();
            return true;
        }
        return false;
    }

    public void setOnMenuListener(){
        ((SideBarFragment)getFragmentManager().findFragmentById(sidebar.getId())).setOnMenuChange(this);
    }

    @Override
    public void onMenuChange(String id) {
        progressBar.setVisibility(View.VISIBLE);
        ImageShow.getInstance().setBucketId(id);
        this.notifyDataChange(id);
        this.selectList.clear();
        Observable.timer(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        timer -> {
                            progressBar.setVisibility(View.GONE);
                        }
                );
    }

    @Override
    public void getCurrentAction(boolean isRun, int item) {
        if (item == ToolbarHelper.Item.MULTISELECT){
            this.adapter.notifyDataSetChanged();
        }else if (item == ToolbarHelper.Item.MOVE){
            ImageShow.getInstance().move(this.getActivity(), selectList);
            this.notifyDataChange(ImageShow.getInstance().getBucketId());
        }else if (item == ToolbarHelper.Item.DELETE){
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
        }else if(item == ToolbarHelper.Item.SHARE){
            ShareDialog.build(getActivity(), selectList).show();
        }else if(item == ToolbarHelper.Item.EDIT){
            ImageShow.getInstance().setImageData(selectList.get(0));
            mNavigator.navigate(ImageEditFragment.class, true);
        }

        this.notifyDataChange(ImageShow.getInstance().getBucketId());
    }

}
