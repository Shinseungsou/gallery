package com.siot.sss.hsgallery.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.activity.MainActivity;
import com.siot.sss.hsgallery.app.model.UseLog;
import com.siot.sss.hsgallery.app.model.unique.ImageShow;
import com.siot.sss.hsgallery.util.database.table.DBOpenHelper;
import com.siot.sss.hsgallery.util.viewpager.ViewPagerAdapter;

import java.util.Calendar;
import java.util.Date;

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
//        this.image.setImageBitmap(ImageShow.getInstance().getImageData().getImageBitmap());
//        this.title.setText(ImageShow.getInstance().getImageData().title);
        pager.setAdapter(new ViewPagerAdapter(this.getActivity().getApplicationContext(), this, ImageShow.getInstance().getImages()));
        pager.setCurrentItem(ImageShow.getInstance().getPosition());
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                addLog();
            }
        });
    }

    private void addLog(){
        DBOpenHelper helper = new DBOpenHelper(this.getActivity().getBaseContext());
        helper.open();
        Calendar c = Calendar.getInstance();
        Date date = new Date(c.get(Calendar.YEAR)-1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
        helper.insertColumnUseLog(
            new UseLog(
                android.text.format.DateFormat.format("yyyy/MM/dd hh:mm", date).toString(),
                ImageShow.getInstance().getImageData().displayName,
                ImageShow.getInstance().getImageData().id,
                UseLog.getType(UseLog.Type.READ)
            ));
        helper.close();
    }

    @Override
    public void onClick(View v) {

    }
}
