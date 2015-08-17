package com.siot.sss.hsgallery.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.activity.MainActivity;
import com.siot.sss.hsgallery.app.model.UseLog;
import com.siot.sss.hsgallery.app.model.unique.ImageShow;
import com.siot.sss.hsgallery.util.database.UseLogManager;
import com.siot.sss.hsgallery.util.view.MenuItemManager;
import com.siot.sss.hsgallery.app.adapter.ImageViewPagerAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

/**
 * Created by SSS on 2015-08-06.
 */
public class ImageFragment extends Fragment implements View.OnClickListener{
//    @InjectView(R.id.image) protected ImageView image;
//    @InjectView(R.id.title) protected TextView title;
    @InjectView(R.id.viewpager) protected ViewPager pager;

    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        ButterKnife.inject(this, view);
        this.toolbar = ((MainActivity)this.getActivity()).getToolbar();
        this.toolbar.setTitle(R.string.image);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MenuItemManager.getInstance().menuItemVisible(2);
//        this.image.setImageBitmap(ImageShow.getInstance().getImageData().getImageBitmap());
//        this.title.setText(ImageShow.getInstance().getImageData().title);
        pager.setAdapter(new ImageViewPagerAdapter(this.getActivity().getApplicationContext(), this, ImageShow.getInstance().getImages()));
        pager.setCurrentItem(ImageShow.getInstance().getPosition());
        pager.setPageMargin(10);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                UseLogManager.getInstance().addLog(UseLog.Type.READ);
                ImageShow.getInstance().setPosition(position);
                Timber.d("title : %s", ImageShow.getInstance().getImages().get(position).data);
                Timber.d("isprivate : %s", ImageShow.getInstance().getImages().get(position).isPrivate);
                Timber.d("file path : %s", ImageShow.getInstance().getImages().get(position).data);
            }
        });
    }



    @Override
    public void onClick(View v) {

    }
}
