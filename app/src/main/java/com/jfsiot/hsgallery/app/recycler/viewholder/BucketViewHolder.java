package com.jfsiot.hsgallery.app.recycler.viewholder;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.model.ImageBucket;
import com.jfsiot.hsgallery.app.model.unique.Configuration;
import com.jfsiot.hsgallery.util.view.recyclerview.RecyclerViewItemClickListener;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by SSS on 2015-08-04.
 */
public class BucketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @InjectView(R.id.image) protected ImageView image;
    @InjectView(R.id.title) protected TextView title;

    private RecyclerViewItemClickListener listener;
    public BucketViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
        itemView.setOnClickListener(this);
    }

    public BucketViewHolder(View itemView, RecyclerViewItemClickListener listener){
        this(itemView);
        this.listener = listener;
    }

    public void bind(ImageBucket imageData) {
        this.title.setText(imageData.displayName != null ? imageData.displayName : itemView.getContext().getString(R.string.view_all));
        Uri uri = Uri.fromFile(new File(imageData.imageData.data));
        Picasso.with(this.itemView.getContext()).load(uri).fit().into(this.image);
    }

    public void bind(ImageBucket imageData, Configuration.GalleryMode mode){
        this.title.setVisibility(View.VISIBLE);
        this.bind(imageData);
        setSelected(false);
    }

    @Override
    public void onClick(View v) {
        this.listener.onRecyclerViewItemClick(v, this.getAdapterPosition());
    }

    public void setSelected(boolean select){
        this.itemView.setSelected(select);
        if(select) {
            this.title.setBackgroundColor(itemView.getResources().getColor(R.color.color_chip_light_green_a50));
            this.title.setTextColor(itemView.getResources().getColor(R.color.white));
        }else {
            this.title.setBackgroundColor(itemView.getResources().getColor(R.color.white_a70));
            this.title.setTextColor(itemView.getResources().getColor(R.color.black));
        }
    }
}
