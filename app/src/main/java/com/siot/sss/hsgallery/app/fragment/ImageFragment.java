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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
        Calendar c = Calendar.getInstance();
        Date date = new Date(c.get(Calendar.YEAR)-1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
        Timber.d("currnet : %s", android.text.format.DateFormat.format("yyyy/MM/dd hh:mm", date));
        helper.insertColumnUseLog(
            new UseLog(
                android.text.format.DateFormat.format("yyyy/MM/dd hh:mm", date).toString(),
                ImageShow.getInstance().getImageSource().displayName,
                ImageShow.getInstance().getImageSource().id,
                UseLog.getType(UseLog.Type.READ)
            ));
        Timber.d("add Log2");
        helper.close();

    }
}
