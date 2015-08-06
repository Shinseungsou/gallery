package com.siot.sss.hsgallery.app.fragment;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
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

import java.sql.SQLException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

/**
 * Created by SSS on 2015-08-06.
 */
public class ImageFragment extends Fragment {
    @InjectView(R.id.image) protected ImageView image;
    @InjectView(R.id.title) protected TextView title;

    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        ButterKnife.inject(this, view);
        this.toolbar = ((MainActivity)this.getActivity()).getToolbar();
        this.toolbar.setTitle(R.string.log);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.image.setImageBitmap(ImageShow.getInstance().getImageSource().getImageBitmap());
        this.title.setText(ImageShow.getInstance().getImageSource().title);
        this.addLog();
    }
    private void addLog(){
        DBOpenHelper helper = new DBOpenHelper(this.getActivity().getBaseContext());
        helper.open();
        Timber.d("add Log");
        helper.insertColumnUseLog(new UseLog(
            ImageShow.getInstance().getImageSource().dateAdded,
            ImageShow.getInstance().getImageSource().displayName,
            ImageShow.getInstance().getImageSource().id
        ));
        Timber.d("add Log2");
        helper.close();

    }
}
