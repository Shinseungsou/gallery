package com.siot.sss.hsgallery.app.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.activity.MainActivity;
import com.siot.sss.hsgallery.app.model.ImageBucket;
import com.siot.sss.hsgallery.app.recycler.adapter.GalleryDIRAdapter;
import com.siot.sss.hsgallery.util.data.image.ImageShow;
import com.siot.sss.hsgallery.util.view.MenuItemManager;
import com.siot.sss.hsgallery.util.view.navigator.Navigator;
import com.siot.sss.hsgallery.util.view.recyclerview.RecyclerViewFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

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
        this.setupRecyclerView(this.gallery);
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
        this.items.clear();
        ImageShow.getInstance().initImageShow();
        this.items.addAll(ImageShow.getInstance().getBuckets());
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
        Timber.d("current is DIR");
        ImageShow.getInstance().setBucketId(this.items.get(position).id);
        this.navigator.navigate(GalleryPICFragment.class, true);
    }
}
