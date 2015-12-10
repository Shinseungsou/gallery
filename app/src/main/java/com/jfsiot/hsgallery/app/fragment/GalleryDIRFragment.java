package com.jfsiot.hsgallery.app.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.activity.MainActivity;
import com.jfsiot.hsgallery.app.model.ImageBucket;
import com.jfsiot.hsgallery.app.recycler.adapter.GalleryDIRAdapter;
import com.jfsiot.hsgallery.util.data.image.ImageShow;
import com.jfsiot.hsgallery.util.view.MenuItemManager;
import com.jfsiot.hsgallery.util.view.navigator.Navigator;
import com.jfsiot.hsgallery.util.view.recyclerview.RecyclerViewFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by SSS on 2015-08-04.
 */
public class GalleryDIRFragment extends RecyclerViewFragment<GalleryDIRAdapter, ImageBucket>{
    @InjectView(R.id.gallery) protected RecyclerView gallery;

    private CompositeSubscription subscription;
    private Toolbar toolbar;
    private Navigator navigator;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.inject(this, view);
        this.navigator = (MainActivity) this.getActivity();

        this.toolbar = ((MainActivity)this.getActivity()).getToolbar();
        this.toolbar.setTitle(R.string.gallery);
        MenuItemManager.getInstance().clear().setEnable(MenuItemManager.State.UNSELECTED, MenuItemManager.State.DEFAULT);
        ImageShow.getInstance().initImageShow();
        this.setupRecyclerView(this.gallery);
        this.items.clear();
        this.items.addAll(ImageShow.getInstance().getBuckets());
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
        this.adapter.notifyDataSetChanged();
    }

    @Override
    protected GalleryDIRAdapter getAdapter() {
        return new GalleryDIRAdapter(this.items, this);
    }

    @Override
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return new GridLayoutManager(this.getActivity(), 2);
    }

    @Override
    public void onRecyclerViewItemClick(View view, int position) {
        ImageShow.getInstance().setBucketId(this.items.get(position).id);
        this.navigator.navigate(GalleryPICFragment.class, true);
    }
}
