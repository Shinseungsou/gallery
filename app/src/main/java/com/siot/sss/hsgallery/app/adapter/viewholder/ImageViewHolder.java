package com.siot.sss.hsgallery.app.adapter.viewholder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.model.ImageData;
import com.siot.sss.hsgallery.app.model.unique.Configuration;
import com.siot.sss.hsgallery.util.view.recyclerview.RecyclerViewItemClickListener;

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

    private final int WIDTHSIZE = 300;

    public ImageViewHolder(View itemView, RecyclerViewItemClickListener listener){
        this(itemView);
        this.listener = listener;
    }

    public void bind(ImageData imageData) {
        BitmapFactory.Options bo = new BitmapFactory.Options();
        bo.inSampleSize = 8;
        Bitmap bmp = BitmapFactory.decodeFile(imageData.data, bo);
        this.title.setText(imageData.bucketDisplayName);
        if(imageData.width != null && imageData.height != null && imageData.width > 0) {
            int width = imageData.width;
            int height =imageData.height;
            this.image.setImageBitmap(Bitmap.createScaledBitmap(bmp, WIDTHSIZE, WIDTHSIZE * height / width, true));
        }else
            this.image.setImageBitmap(bmp);
    }

    public void bind(ImageData imageData, Configuration.GalleryMode mode){
        if(mode.equals(Configuration.GalleryMode.DIR))
            this.title.setVisibility(View.VISIBLE);
        else if(mode.equals(Configuration.GalleryMode.PIC))
            this.title.setVisibility(View.INVISIBLE);
        this.bind(imageData);
    }

    @Override
    public void onClick(View v) {
        this.listener.onRecyclerViewItemClick(v, this.getAdapterPosition());
    }
}
