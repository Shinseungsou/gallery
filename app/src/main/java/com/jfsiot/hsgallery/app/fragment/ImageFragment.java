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
import com.jfsiot.hsgallery.util.helper.ToolbarHelper;
import com.jfsiot.hsgallery.util.view.navigator.ToolbarCallback;
import com.jfsiot.hsgallery.util.view.viewpager.FixedViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

public class ImageFragment extends Fragment implements View.OnClickListener, ToolbarCallback.ToolbarSimpleCallback{
    @InjectView(R.id.viewpager) protected FixedViewPager pager;

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
        ToolbarHelper.getInstance().clear()
            .setEnable(ToolbarHelper.State.DEFAULT, ToolbarHelper.State.OPERATOR, ToolbarHelper.State.IMAGE, ToolbarHelper.State.SHARE, ToolbarHelper.State.UNSELECTED);
        ((MainActivity) this.getActivity()).setToolbarSimpleCallback(this);
        pager.setCurrentItem(ImageShow.getInstance().getPosition());
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Timber.d("&& onPage");
                ImageShow.getInstance().setPosition(position);
                Timber.d("**image data : %s", list.get(position).data);
                Timber.d("**image displayName : %s", list.get(position).displayName);
                Timber.d("**image isPrivate : %s", list.get(position).isPrivate);
                Timber.d("**image title : %s", list.get(position).title);
                Timber.d("**image orientation : %s", list.get(position).orientation);
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
            if (item == ToolbarHelper.Item.RENAME) {
                if(ImageShow.getInstance().getBucketId() != null)
                    list.addAll(ImageController.getInstance().getImageDataList(ImageShow.getInstance().getBucketId()));
                else
                    list.addAll(ImageShow.getInstance().getImages());
                this.adapter.update();
            }else if (item == ToolbarHelper.Item.INSTAGRAM) {
                ImageShow.getInstance().sendInstagram(getActivity(), list.get(pager.getCurrentItem()));
                UseLogManager.getInstance().addLog(list.get(pager.getCurrentItem()), new String(), UseLog.getShareString(UseLog.Share.INSTAGRAM));
            }else if (item == ToolbarHelper.Item.KAKAO) {
                List<ImageData> images = new ArrayList<>();
                images.add(list.get(pager.getCurrentItem()));
                ImageShow.getInstance().sendKaKao(getActivity(), images);
                UseLogManager.getInstance().addLogList(images, new String(), UseLog.getShareString(UseLog.Share.KAKAO));
            }else if (item == ToolbarHelper.Item.FACEBOOK) {
                List<ImageData> images = new ArrayList<>();
                images.add(list.get(pager.getCurrentItem()));
                ImageShow.getInstance().sendFacebook(getActivity(), images);
                UseLogManager.getInstance().addLogList(images, new String(), UseLog.getShareString(UseLog.Share.FACEBOOK));
            }else if (item == ToolbarHelper.Item.ROTATE) {
                this.adapter.rotate(ImageShow.getInstance().getPosition());
            }else if (item == ToolbarHelper.Item.EDIT){
                ImageShow.getInstance().setImageData(this.list.get(ImageShow.getInstance().getPosition()));
                ((MainActivity) this.getActivity()).navigate(ImageEditFragment.class, true);
            }
        }
    }

}
