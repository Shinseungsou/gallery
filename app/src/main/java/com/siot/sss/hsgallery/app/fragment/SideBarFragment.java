package com.siot.sss.hsgallery.app.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.model.ImageBucket;
import com.siot.sss.hsgallery.app.recycler.adapter.GalleryDIRAdapter;
import com.siot.sss.hsgallery.util.data.image.ImageController;
import com.siot.sss.hsgallery.util.data.image.ImageShow;
import com.siot.sss.hsgallery.util.view.recyclerview.OnMenuChange;
import com.siot.sss.hsgallery.util.view.recyclerview.RecyclerViewFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

public class SideBarFragment extends RecyclerViewFragment<GalleryDIRAdapter, ImageBucket> {
    @InjectView(R.id.simple_recycler) protected RecyclerView gallery;
    private OnMenuChange onMenuChange;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple_list, container, false);
        ButterKnife.inject(this, view);
        this.setupRecyclerView(this.gallery);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.items.clear();
        this.items.addAll(ImageShow.getInstance().getBuckets());
        this.adapter.notifyDataSetChanged();
    }

    @Override
    protected GalleryDIRAdapter getAdapter() {
        return new GalleryDIRAdapter(this.items, this);
    }

    @Override
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override
    public void onRecyclerViewItemClick(View view, int position) {
        if(onMenuChange != null)
            this.onMenuChange.onMenuChange(this.items.get(position).id);
        else
            Timber.d("Listener isn't exist");
    }

    public void setOnMenuChange(OnMenuChange onMenuChange){
        Timber.d("regist!");
        this.onMenuChange = onMenuChange;
    }
}
