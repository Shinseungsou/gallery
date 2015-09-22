package com.siot.sss.hsgallery.app.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.activity.MainActivity;
import com.siot.sss.hsgallery.app.adapter.SimpleAdapter;
import com.siot.sss.hsgallery.app.model.SimpleItem;
import com.siot.sss.hsgallery.app.model.unique.Configuration;
import com.siot.sss.hsgallery.util.view.recyclerview.RecyclerViewFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.subscriptions.CompositeSubscription;

public class MenuFragment extends RecyclerViewFragment<SimpleAdapter, SimpleItem> {
    @InjectView(R.id.simple_recycler) protected RecyclerView recyclerView;
    private CompositeSubscription subscription;

    private Configuration.GalleryMode mode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple_list, container, false);
        ButterKnife.inject(this, view);
        this.setupRecyclerView(this.recyclerView);

        mode = Configuration.getInstance().getGalleryMode();
        this.setMenuList();
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

    }
    public void setMenuList(){
        this.items.clear();
        switch (mode){
            case DIR:
                this.items.add(new SimpleItem(R.drawable.ic_insert_photo_white_24dp, getResources().getString(R.string.view_img)));
                break;
            case PIC:
                this.items.add(new SimpleItem(R.drawable.ic_folder_white_24dp, getResources().getString(R.string.view_dir)));
                break;
        }
    }

    @Override
    protected SimpleAdapter getAdapter() {
        return new SimpleAdapter(this.items, this);
    }

    @Override
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return new LinearLayoutManager(this.getActivity());
    }

    @Override
    public void onRecyclerViewItemClick(View view, int position) {
        if(position == 0){
            switch (mode){
                case DIR:
                    Configuration.getInstance().setGalleryMode(Configuration.GalleryMode.PIC);
                    this.mode = Configuration.GalleryMode.PIC;
                    break;
                case PIC:
                    Configuration.getInstance().setGalleryMode(Configuration.GalleryMode.DIR);
                    this.mode = Configuration.GalleryMode.DIR;
                    break;
            }
            if(((MainActivity)this.getActivity()).getCurrentFragmentName().equals(GalleryFragment.class.getSimpleName())){
                ((GalleryFragment)((MainActivity)this.getActivity()).getCurrentFragment()).modeNotify();
            }
        }
        this.setMenuList();
        this.adapter.notifyDataSetChanged();
    }

}
