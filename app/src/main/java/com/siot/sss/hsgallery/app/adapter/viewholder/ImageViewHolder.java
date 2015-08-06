package com.siot.sss.hsgallery.app.adapter.viewholder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.model.ImageSource;
import com.siot.sss.hsgallery.app.model.unique.DisplayWindow;
import com.siot.sss.hsgallery.util.recyclerview.RecyclerViewItemClickListener;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

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

    public void bind(ImageSource imageSource) {
        BitmapFactory.Options bo = new BitmapFactory.Options();
        bo.inSampleSize = 8;
        Bitmap bmp = BitmapFactory.decodeFile(imageSource.data, bo);
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
