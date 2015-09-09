package com.siot.sss.hsgallery.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.adapter.viewholder.SimpleViewHolder;
import com.siot.sss.hsgallery.app.adapter.viewholder.UseLogViewHolder;
import com.siot.sss.hsgallery.app.model.SimpleItem;
import com.siot.sss.hsgallery.app.model.UseLog;
import com.siot.sss.hsgallery.util.view.recyclerview.RecyclerViewItemClickListener;

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
