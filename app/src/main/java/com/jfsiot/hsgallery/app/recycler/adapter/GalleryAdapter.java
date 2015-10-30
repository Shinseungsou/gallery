package com.jfsiot.hsgallery.app.recycler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.recycler.viewholder.ImageViewHolder;
import com.jfsiot.hsgallery.app.model.ImageData;
import com.jfsiot.hsgallery.app.model.unique.Configuration;
import com.jfsiot.hsgallery.util.view.recyclerview.RecyclerViewItemClickListener;

import java.util.List;

import timber.log.Timber;

/**
 * Created by SSS on 2015-08-04.
 */
public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ImageData> imageList;
    private RecyclerViewItemClickListener listener;
    private Configuration.GalleryMode mode;

    public GalleryAdapter(List<ImageData> imageList, RecyclerViewItemClickListener listener){
        this.imageList = imageList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new ImageViewHolder(inflater.inflate(R.layout.item_image, viewGroup,false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        try {
            ((ImageViewHolder) viewHolder).bind(this.imageList.get(i), this.mode);
        }catch (NullPointerException e){
            Timber.d("error path : %s", this.imageList.get(i).data);
            Timber.d("<%s> <%s> <%s> <%s> ", imageList.get(i).data, imageList.get(i).title, imageList.get(i).displayName, imageList.get(i).id);
            e.printStackTrace();
        }
    }

    public void setMode(Configuration.GalleryMode mode){
        this.mode = mode;
    }

    @Override
    public int getItemCount() {
        return this.imageList == null? 0 : this.imageList.size();
    }

}
