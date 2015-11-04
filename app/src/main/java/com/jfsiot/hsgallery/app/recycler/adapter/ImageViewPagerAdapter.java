package com.jfsiot.hsgallery.app.recycler.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.model.ImageData;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by SSS on 2015-08-09.
 */
public class ImageViewPagerAdapter extends PagerAdapter {
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
        views = new SparseArray<>();
        Timber.d("count : %s", items.size());
    }
    private View v;

    @InjectView(R.id.image) protected PhotoView imageView;
    @InjectView(R.id.title) protected TextView titleView;
    private SparseArray<View> views;
    @Override
    public Object instantiateItem(ViewGroup pager, int position) {
        v = inflater.inflate(R.layout.fragment_image_slide, null);
        ButterKnife.inject(this, v);

        Uri uri = Uri.fromFile(new File(items.get(position).data));
        imageView.setImageBitmap(items.get(position).getImageBitmap());

        Picasso.with(this.v.getContext()).load(uri).fit().centerInside().into(this.imageView);
        titleView.setText(items.get(position).title);

        imageView.setOnClickListener(listener);
        pager.addView(v, 0);
        views.put(position, v);
        return v;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        super.destroyItem(container, position, object);
        views.remove(position);

    }

    private boolean isChange;
    public void update(){
        isChange = true;
        this.notifyDataSetChanged();
        isChange = false;
    }

    @Override
    public int getItemPosition(Object object) {
        Timber.d("call!! position !! %s", isChange);
//        if(isChange) {
//            return POSITION_NONE;
//        }
        return super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        for(int i = 0; i < views.size(); i++){
            int key = views.keyAt(i);
            View view = views.get(key);
            ((TextView)view.findViewById(titleView.getId())).setText(items.get(i).title);
        }
    }

    @Override
    public void destroyItem(ViewGroup pager, int position, Object view) {
        pager.removeView((View) view);
        ButterKnife.reset(view);
    }

}
