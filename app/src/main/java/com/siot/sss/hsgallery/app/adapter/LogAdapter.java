package com.siot.sss.hsgallery.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.adapter.viewholder.ImageViewHolder;
import com.siot.sss.hsgallery.app.adapter.viewholder.UseLogViewHolder;
import com.siot.sss.hsgallery.app.model.ImageSource;
import com.siot.sss.hsgallery.app.model.UseLog;
import com.siot.sss.hsgallery.util.recyclerview.RecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by SSS on 2015-08-06.
 */
public class LogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UseLog> imageList;
    private RecyclerViewItemClickListener listener;

    public LogAdapter(List<UseLog> imageList, RecyclerViewItemClickListener listener){
        this.imageList = imageList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new UseLogViewHolder(inflater.inflate(R.layout.item_log, viewGroup,false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ((UseLogViewHolder) viewHolder).bind(this.imageList.get(i));
    }

    @Override
    public int getItemCount() {
        return this.imageList == null? 0 : this.imageList.size();
    }

}
