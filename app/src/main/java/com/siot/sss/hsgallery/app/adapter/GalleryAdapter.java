package com.siot.sss.hsgallery.app.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.adapter.viewholder.ImageViewHolder;
import com.siot.sss.hsgallery.app.model.ImageSource;
import com.siot.sss.hsgallery.util.recyclerview.RecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by SSS on 2015-08-04.
 */
public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ImageSource> imageList;
    private RecyclerViewItemClickListener listener;

    public GalleryAdapter(List<ImageSource> imageList, RecyclerViewItemClickListener listener){
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
        ((ImageViewHolder) viewHolder).bind(this.imageList.get(i));
    }

    @Override
    public int getItemCount() {
        return this.imageList == null? 0 : this.imageList.size();
    }

}
