package com.siot.sss.hsgallery.app.recycler.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.model.ImageData;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

/**
 * Created by SSS on 2015-08-09.
 */
public class ImageViewPagerAdapter extends PagerAdapter {
//    @InjectView(R.id.image) protected ImageView image;
//    @InjectView(R.id.title) protected TextView title;
    @Override
    public int getCount() { return items == null ? 0: items.size(); }

    @Override
    public boolean isViewFromObject(View view, Object obj) { return view == obj; }

    private Context context;
    private List<ImageData> items;
    private LayoutInflater inflater;
    private View.OnClickListener listener;
//    ViewPagerManager pagerManager;

    public ImageViewPagerAdapter(Context context, View.OnClickListener listener, List<ImageData> items) {
        super();
        this.context = context;
        this.items = items;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
        Timber.d("count : %s", items.size());
    }
    private View v;

    @InjectView(R.id.image) protected ImageView imageView;
    @InjectView(R.id.title) protected TextView titleView;
    @Override
    public Object instantiateItem(ViewGroup pager, int position) {
        v = inflater.inflate(R.layout.fragment_image_slide, null);
        ButterKnife.inject(this, v);
        titleView.setText(items.get(position).displayName);

        imageView.setImageBitmap(items.get(position).getImageBitmap());

        Uri uri = Uri.fromFile(new File(items.get(position).data));
        Picasso.with(this.v.getContext()).load(uri).fit().centerInside().into(this.imageView);
        imageView.setOnClickListener(listener);

        pager.addView(v, 0);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup pager, int position, Object view) {
        pager.removeView((View) view);
        ButterKnife.reset(view);
    }
}
