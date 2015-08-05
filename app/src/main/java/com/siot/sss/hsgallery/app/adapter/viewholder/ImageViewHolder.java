package com.siot.sss.hsgallery.app.adapter.viewholder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.model.ImageSource;
import com.siot.sss.hsgallery.util.recyclerview.RecyclerViewItemClickListener;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by SSS on 2015-08-04.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @InjectView(R.id.image) protected ImageView image;

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

    public void bind(ImageSource image) {
        Picasso.with(this.itemView.getContext()).load(image.imageId).into(this.image);
    }

    @Override
    public void onClick(View v) {
        this.listener.onRecyclerViewOtemClick(v, this.getAdapterPosition());
    }
}
