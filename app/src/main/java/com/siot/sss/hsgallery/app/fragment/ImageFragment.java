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
import com.siot.sss.hsgallery.app.recycler.adapter.ImageViewPagerAdapter;
import com.siot.sss.hsgallery.app.model.ImageData;
import com.siot.sss.hsgallery.app.model.UseLog;
import com.siot.sss.hsgallery.app.model.unique.ImageShow;
import com.siot.sss.hsgallery.util.data.db.UseLogManager;
import com.siot.sss.hsgallery.util.data.image.ImageController;
import com.siot.sss.hsgallery.util.view.MenuItemManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

public class ImageFragment extends Fragment implements View.OnClickListener{
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
        List<ImageData> list = new ArrayList<>();
        if(ImageShow.getInstance().getBucketId() != null)
            list.addAll(ImageController.getInstance().getImageData(ImageShow.getInstance().getBucketId()));
        else
            list.addAll(ImageShow.getInstance().getImages());
        pager.setAdapter(new ImageViewPagerAdapter(this.getActivity().getApplicationContext(), this, list));
        pager.setCurrentItem(ImageShow.getInstance().getPosition());
        pager.setPageMargin(10);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                UseLogManager.getInstance().addLog(UseLog.Type.READ);
                ImageShow.getInstance().setPosition(position);
                Timber.d("**image data : %s", list.get(position).data);
                Timber.d("**image displayName : %s", list.get(position).displayName);
                Timber.d("**image isPrivate : %s", list.get(position).isPrivate);
                Timber.d("**image title : %s", list.get(position).title);
                Timber.d("**image realname : %s", ImageController.getInstance().information.getRealName(list.get(position)));
                Timber.d("**image real private %s", ImageController.getInstance().information.isPrivate(list.get(position)));
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
