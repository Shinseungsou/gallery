package com.jfsiot.hsgallery.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.activity.MainActivity;
import com.jfsiot.hsgallery.app.recycler.adapter.ImageViewPagerAdapter;
import com.jfsiot.hsgallery.app.model.ImageData;
import com.jfsiot.hsgallery.app.model.UseLog;
import com.jfsiot.hsgallery.util.data.image.ImageShow;
import com.jfsiot.hsgallery.util.data.db.UseLogManager;
import com.jfsiot.hsgallery.util.data.image.ImageController;
import com.jfsiot.hsgallery.util.view.MenuItemManager;
import com.jfsiot.hsgallery.util.view.navigator.ToolbarCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

public class ImageFragment extends Fragment implements View.OnClickListener, ToolbarCallback.ToolbarSimpleCallback{
    @InjectView(R.id.viewpager) protected ViewPager pager;

    private Toolbar toolbar;
    private ImageViewPagerAdapter adapter;

    private List<ImageData> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        ButterKnife.inject(this, view);
        this.toolbar = ((MainActivity)this.getActivity()).getToolbar();
        this.toolbar.setTitle(R.string.image);
        this.adapter = new ImageViewPagerAdapter(this.getActivity().getApplicationContext(), this, list);
        pager.setAdapter(adapter);
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) this.getActivity()).setToolbarSimpleCallback(this);
        list = new ArrayList<>();
        if(ImageShow.getInstance().getBucketId() != null)
            list.addAll(ImageController.getInstance().getImageDataList(ImageShow.getInstance().getBucketId()));
        else
            list.addAll(ImageShow.getInstance().getImages());
    }

    @Override
    public void onDestroy() {
        super.onDestroyView();

        ((MainActivity) this.getActivity()).setToolbarSimpleCallback(null);
    }
    @Override
    public void onResume() {
        super.onResume();
        MenuItemManager.getInstance().menuItemVisible(2);
//        this.image.setImageBitmap(ImageShow.getInstance().getImageData().getImageBitmap());
//        this.title.setText(ImageShow.getInstance().getImageData().title);
        pager.setCurrentItem(ImageShow.getInstance().getPosition());
//        pager.setPageMargin(10);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ImageShow.getInstance().setPosition(position);
                Timber.d("**image data : %s", list.get(position).data);
                Timber.d("**image displayName : %s", list.get(position).displayName);
                Timber.d("**image isPrivate : %s", list.get(position).isPrivate);
                Timber.d("**image title : %s", list.get(position).title);
                Timber.d("**image realname : %s", ImageController.getInstance().getRealName(list.get(position)));
                Timber.d("**image real private %s", ImageController.getInstance().isPrivate(list.get(position)));
            }
        });
        Timber.d("image data %s", ImageShow.getInstance().getImageData().toString());
    }

    @Override
    public void onClick(View v) {

    }
    @Override
    public void getCurrentAction(boolean isRun, int item) {
        if (isRun) {
            if (item == MenuItemManager.Item.RENAME) {
                if(ImageShow.getInstance().getBucketId() != null)
                    list.addAll(ImageController.getInstance().getImageDataList(ImageShow.getInstance().getBucketId()));
                else
                    list.addAll(ImageShow.getInstance().getImages());
                this.adapter.update();
            }
        }
    }

}
