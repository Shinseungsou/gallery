package com.jfsiot.hsgallery.app.recycler.viewholder;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.AppConfig;
import com.jfsiot.hsgallery.app.model.ImageData;
import com.jfsiot.hsgallery.app.model.unique.Configuration;
import com.jfsiot.hsgallery.util.view.recyclerview.RecyclerViewItemClickListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by SSS on 2015-08-04.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @InjectView(R.id.image) protected ImageView image;
    @InjectView(R.id.title) protected TextView title;
    @InjectView(R.id.multi_select) protected CheckBox multiSelect;

    private RecyclerViewItemClickListener listener;
    public ImageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
        itemView.setOnClickListener(this);
    }

    public ImageViewHolder(View itemView, RecyclerViewItemClickListener listener){
        this(itemView);
        this.listener = listener;
    }

    public void bind(ImageData imageData) {
        this.title.setText(imageData.bucketDisplayName);
        Uri uri = Uri.fromFile(new File(imageData.data));
        Picasso.with(this.itemView.getContext()).load(uri).fit().into(this.image);
    }

    public void bind(ImageData imageData, Configuration.GalleryMode mode){
        this.title.setVisibility(View.INVISIBLE);
        this.bind(imageData);
        if(AppConfig.Option.MULTISELECT) {
            this.multiSelect.setVisibility(View.VISIBLE);
            this.multiSelect.setOnClickListener(this);
        }else {
            this.multiSelect.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        if(this.multiSelect.isChecked())
            this.multiSelect.setChecked(false);
        else
            this.multiSelect.setChecked(true);
        this.listener.onRecyclerViewItemClick(v, this.getAdapterPosition());
    }
}
