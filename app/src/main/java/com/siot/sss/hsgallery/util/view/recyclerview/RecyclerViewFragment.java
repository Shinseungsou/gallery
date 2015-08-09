package com.siot.sss.hsgallery.util.view.recyclerview;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SSS on 2015-08-05.
 */
public abstract class RecyclerViewFragment<ADAPTER extends RecyclerView.Adapter<RecyclerView.ViewHolder>, ITEM> extends Fragment implements RecyclerViewItemClickListener {

    protected ADAPTER adapter;
    protected List<ITEM> items;

    protected void setupRecyclerView (RecyclerView view) {
        this.items = new ArrayList<>();
        this.adapter = this.getAdapter();
        view.setLayoutManager(this.getRecyclerViewLayoutManager());
        view.setAdapter(this.adapter);
    }

    protected abstract ADAPTER getAdapter ();
    protected abstract RecyclerView.LayoutManager getRecyclerViewLayoutManager ();
}
