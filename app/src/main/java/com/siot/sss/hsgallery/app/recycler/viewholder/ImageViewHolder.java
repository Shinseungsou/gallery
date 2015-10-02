package com.siot.sss.hsgallery.app.recycler.viewholder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.model.ImageData;
import com.siot.sss.hsgallery.app.model.unique.Configuration;
import com.siot.sss.hsgallery.util.view.recyclerview.RecyclerViewItemClickListener;
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
    }

    @Override
    public void onClick(View v) {
        this.listener.onRecyclerViewItemClick(v, this.getAdapterPosition());
    }
}
