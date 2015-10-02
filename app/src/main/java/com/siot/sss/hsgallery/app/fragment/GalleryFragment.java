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
import com.siot.sss.hsgallery.app.recycler.adapter.GalleryAdapter;
import com.siot.sss.hsgallery.app.model.ImageBucket;
import com.siot.sss.hsgallery.app.model.ImageData;
import com.siot.sss.hsgallery.app.model.UseLog;
import com.siot.sss.hsgallery.app.model.unique.Configuration;
import com.siot.sss.hsgallery.app.model.unique.ImageShow;
import com.siot.sss.hsgallery.util.data.db.UseLogManager;
import com.siot.sss.hsgallery.util.data.image.ImageController;
import com.siot.sss.hsgallery.util.view.MenuItemManager;
import com.siot.sss.hsgallery.util.view.navigator.Navigator;
import com.siot.sss.hsgallery.util.view.recyclerview.RecyclerViewFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by SSS on 2015-08-04.
 */
public class GalleryFragment extends RecyclerViewFragment<GalleryAdapter, ImageData>{
    @InjectView(R.id.gallery) protected RecyclerView gallery;
    private CompositeSubscription subscription;
    private Toolbar toolbar;
    private Navigator navigator;
    private ImageController imageController;

    private Configuration.GalleryMode mode;

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
        this.getImageCursor();

        this.modeNotify();
    }

    public void getImageCursor(){
        this.items.clear();
        this.items.addAll(imageController.getImageData());
        this.imageController.setImageShow();
        this.adapter.notifyDataSetChanged();
    }

    public void modeNotify(){
        mode = Configuration.getInstance().getGalleryMode();
        switch (mode){
            case DIR:
                this.items.clear();
                for(ImageBucket IB : ImageShow.getInstance().getBuckets()){
                    this.items.add(IB.imageData);
                }
                break;
            case PIC:
                this.items.clear();
                this.items.addAll(ImageShow.getInstance().getImages());
                break;
        }
        this.adapter.setMode(mode);
        this.adapter.notifyDataSetChanged();
        (getActivity()).findViewById(R.id.menu_layout).setVisibility(View.INVISIBLE);
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
        if(mode.equals(Configuration.GalleryMode.DIR))
            ImageShow.getInstance().setBucketId(this.items.get(position).bucketId);
        else
            ImageShow.getInstance().setBucketId(null);
        this.navigator.navigate(ImageFragment.class, true);
    }
}
