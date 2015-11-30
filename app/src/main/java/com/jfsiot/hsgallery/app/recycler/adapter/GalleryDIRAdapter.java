package com.jfsiot.hsgallery.app.recycler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.model.ImageBucket;
import com.jfsiot.hsgallery.app.model.unique.Configuration;
import com.jfsiot.hsgallery.app.recycler.viewholder.BucketViewHolder;
import com.jfsiot.hsgallery.util.view.recyclerview.RecyclerViewItemClickListener;

import java.util.List;

import timber.log.Timber;

/**
 * Created by SSS on 2015-08-04.
 */
public class GalleryDIRAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ImageBucket> imageList;
    private RecyclerViewItemClickListener listener;
    private Configuration.GalleryMode mode;
    private int selectedPosition;
    private BucketViewHolder selectedViewHolder;

    public GalleryDIRAdapter(List<ImageBucket> imageList, RecyclerViewItemClickListener listener){
        this.imageList = imageList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new BucketViewHolder(inflater.inflate(R.layout.item_image, viewGroup,false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        try {
            ((BucketViewHolder) viewHolder).bind(this.imageList.get(i), this.mode);
            if(selectedPosition == i) {
                if(selectedViewHolder != null)
                    selectedViewHolder.setSelected(false);
                selectedViewHolder = ((BucketViewHolder) viewHolder);
                selectedViewHolder.setSelected(true);
            }
        }catch (NullPointerException e){
            Timber.d("error path : %s", this.imageList.get(i).imageData.data);
            Timber.d("<%s> <%s> <%s> <%s> ", imageList.get(i).imageData.data, imageList.get(i).imageData.title, imageList.get(i).displayName, imageList.get(i).id);
            e.printStackTrace();
        }
    }

    public void setMode(Configuration.GalleryMode mode){
        this.mode = mode;
    }
    public void setSelectedPosition(int position){
        selectedPosition = position;
        this.notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return this.imageList == null? 0 : this.imageList.size();
    }

}
