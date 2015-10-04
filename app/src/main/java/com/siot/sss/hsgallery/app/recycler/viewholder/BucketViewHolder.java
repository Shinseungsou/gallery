package com.siot.sss.hsgallery.app.recycler.viewholder;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.model.ImageBucket;
import com.siot.sss.hsgallery.app.model.ImageData;
import com.siot.sss.hsgallery.app.model.unique.Configuration;
import com.siot.sss.hsgallery.util.view.recyclerview.RecyclerViewItemClickListener;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

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
    }

    @Override
    public void onClick(View v) {
        this.listener.onRecyclerViewItemClick(v, this.getAdapterPosition());
    }
}
