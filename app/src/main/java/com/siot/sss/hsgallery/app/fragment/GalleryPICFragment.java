package com.siot.sss.hsgallery.app.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.activity.MainActivity;
import com.siot.sss.hsgallery.app.model.ImageData;
import com.siot.sss.hsgallery.app.model.UseLog;
import com.siot.sss.hsgallery.app.recycler.adapter.GalleryAdapter;
import com.siot.sss.hsgallery.util.data.db.UseLogManager;
import com.siot.sss.hsgallery.util.data.image.ImageController;
import com.siot.sss.hsgallery.util.data.image.ImageShow;
import com.siot.sss.hsgallery.util.view.MenuItemManager;
import com.siot.sss.hsgallery.util.view.navigator.Navigator;
import com.siot.sss.hsgallery.util.view.navigator.OnBack;
import com.siot.sss.hsgallery.util.view.recyclerview.OnMenuChange;
import com.siot.sss.hsgallery.util.view.recyclerview.RecyclerViewFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by SSS on 2015-08-04.
 */
public class GalleryPICFragment extends RecyclerViewFragment<GalleryAdapter, ImageData> implements OnBack, OnMenuChange {
    @InjectView(R.id.gallery) protected RecyclerView gallery;
    @InjectView(R.id.sidebar) protected LinearLayout sidebar;

    private CompositeSubscription subscription;
    private Toolbar toolbar;
    private Navigator navigator;
    private ImageController imageController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.inject(this, view);
        this.navigator = (MainActivity) this.getActivity();

        this.toolbar = ((MainActivity)this.getActivity()).getToolbar();
        this.toolbar.setTitle(R.string.gallery);
        this.setupRecyclerView(this.gallery);
        imageController = ImageController.getInstance();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(this.subscription != null && !this.subscription.isUnsubscribed()) this.subscription.unsubscribe();
        ButterKnife.reset(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MenuItemManager.getInstance().menuItemVisible(1);
        this.notifyDataChange(ImageShow.getInstance().getBucketId());
        this.sidebar.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.slide_right_show);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) { Timber.d("end!");setOnMenuListener(); }
            @Override public void onAnimationRepeat(Animation animation) { }
        });
        this.sidebar.setAnimation(anim);
        this.getFragmentManager()
            .beginTransaction()
            .add(this.sidebar.getId(), ((MainActivity)getActivity()).getFragmentNavigator().instantiateFragment(SideBarFragment.class))
            .commit();
    }


    public void notifyDataChange(String id){
        this.items.clear();
        this.items.addAll(imageController.getImageData(id));
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
        ImageShow.getInstance().setImageData(this.items.get(position));
        ImageShow.getInstance().setPosition(position);
        UseLogManager.getInstance().addLog(UseLog.Type.READ);
//        if(mode.equals(Configuration.GalleryMode.DIR))
//            ImageShow.getInstance().setBucketId(this.items.get(position).bucketId);
//        else
//            ImageShow.getInstance().setBucketId(null);
        this.navigator.navigate(ImageFragment.class, true);
    }

    @Override
    public boolean onBack() {
        Animation anim = AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.slide_right_hide);
        this.sidebar.startAnimation(anim);

        return false;
    }

    public void setOnMenuListener(){
        ((SideBarFragment)getFragmentManager().findFragmentById(sidebar.getId())).setOnMenuChange(this);
    }

    @Override
    public void onMenuChange(String id) {
        this.notifyDataChange(id);
    }
}
