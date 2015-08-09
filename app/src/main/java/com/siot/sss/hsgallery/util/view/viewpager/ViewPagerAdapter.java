package com.siot.sss.hsgallery.util.view.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.model.ImageData;

import java.util.List;

import timber.log.Timber;

/**
 * Created by SSS on 2015-08-09.
 */
public class ViewPagerAdapter extends PagerAdapter {
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

    public ViewPagerAdapter(Context context, View.OnClickListener listener, List<ImageData> items) {
        super();
        this.context = context;
        this.items = items;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
        Timber.d("count : %s", items.size());
    }
    private View v;
    @Override
    public Object instantiateItem(ViewGroup pager, int position) {
        v = inflater.inflate(R.layout.fragment_image_slide, null);
        ImageView image = (ImageView) v.findViewById(R.id.image);
        TextView title = (TextView) v.findViewById(R.id.title);
//        ButterKnife.inject(v);
        image.setImageBitmap(items.get(position).getImageBitmap());
        title.setText(items.get(position).title);
//        Timber.d("image : %s", items.get(position).title);
        image.setOnClickListener(listener);

        pager.addView(v, 0);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup pager, int position, Object view) {
        pager.removeView((View) view);
//        ButterKnife.reset(view);
    }
}
