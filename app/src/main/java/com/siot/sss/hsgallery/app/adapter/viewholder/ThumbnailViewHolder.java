package com.siot.sss.hsgallery.app.adapter.viewholder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.model.ThumbnailData;
import com.siot.sss.hsgallery.util.view.recyclerview.RecyclerViewItemClickListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by SSS on 2015-08-04.
 */
public class ThumbnailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @InjectView(R.id.image) protected ImageView image;

    private RecyclerViewItemClickListener listener;
    public ThumbnailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
        itemView.setOnClickListener(this);
    }

    public ThumbnailViewHolder(View itemView, RecyclerViewItemClickListener listener){
        this(itemView);
        this.listener = listener;
    }

    public void bind(ThumbnailData thumbnailData) {
        BitmapFactory.Options bo = new BitmapFactory.Options();
        bo.inSampleSize = 8;
        Bitmap bmp = BitmapFactory.decodeFile(thumbnailData.data, bo);
        this.image.setImageBitmap(bmp);
//        Timber.d("image %s %s ", imageSource.width, imageSource.height);
//        float scale = Integer.parseInt(imageSource.width) / Integer.parseInt(imageSource.height);
//        ViewGroup.LayoutParams params = this.image.getLayoutParams();
//        params.height = (int)(DisplayWindow.getInstance().getHeightDP()/scale);
//        this.image.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        this.listener.onRecyclerViewOtemClick(v, this.getAdapterPosition());
    }
}
