package com.jfsiot.hsgallery.app.recycler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.recycler.viewholder.SimpleViewHolder;
import com.jfsiot.hsgallery.app.model.SimpleItem;
import com.jfsiot.hsgallery.util.view.recyclerview.RecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by SSS on 2015-08-06.
 */
public class SimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SimpleItem> itemList;
    private RecyclerViewItemClickListener listener;

    public SimpleAdapter(List<SimpleItem> imageList, RecyclerViewItemClickListener listener){
        this.itemList = imageList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new SimpleViewHolder(inflater.inflate(R.layout.item_simple, viewGroup,false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ((SimpleViewHolder) viewHolder).bind(this.itemList.get(i).icon, this.itemList.get(i).name);
    }

    @Override
    public int getItemCount() {
        return this.itemList == null? 0 : this.itemList.size();
    }

}
