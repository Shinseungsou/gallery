package com.jfsiot.hsgallery.app.fragment;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.activity.MainActivity;
import com.jfsiot.hsgallery.app.model.ImageData;
import com.jfsiot.hsgallery.app.model.unique.CurrentUseLog;
import com.jfsiot.hsgallery.util.data.image.ImageController;
import com.jfsiot.hsgallery.util.data.image.ImageShow;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;
import uk.co.senab.photoview.PhotoView;

public class EditImageFragment extends Fragment implements View.OnClickListener{

    @InjectView(R.id.image) protected ImageView imageView;
    @InjectView(R.id.title) protected TextView titleView;

    private Toolbar toolbar;

    private ImageData image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_slide, container, false);
        ButterKnife.inject(this, view);
        this.toolbar = ((MainActivity)this.getActivity()).getToolbar();
        this.toolbar.setTitle(R.string.image);
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroyView();
    }
    @Override
    public void onResume() {
        super.onResume();
        image = ImageShow.getInstance().getImageData();
//        Uri uri = Uri.fromFile(new File( image.to_data == null ? image.data : image.to_data));
        Uri uri = Uri.fromFile(new File(image.data));

        Picasso.with(getActivity()).load(uri).into(this.imageView);
        this.titleView.setText(image.title);
    }

    @Override
    public void onClick(View v) {

    }

}
