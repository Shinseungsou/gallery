package com.jfsiot.hsgallery.app.fragment;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isseiaoki.simplecropview.CropImageView;
import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.activity.MainActivity;
import com.jfsiot.hsgallery.app.model.ImageData;
import com.jfsiot.hsgallery.util.data.image.ImageShow;
import com.jfsiot.hsgallery.util.view.MenuItemManager;
import com.jfsiot.hsgallery.util.view.navigator.Navigator;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ImageEditFragment extends Fragment implements View.OnClickListener{

    @InjectView(R.id.cropImageView) protected CropImageView cropImageView;
    @InjectView(R.id.edit_title) protected TextView titleView;
    @InjectView(R.id.edit_save_button) protected TextView saveButton;
    @InjectView(R.id.edit_cancel_button) protected TextView cancelButton;

    @InjectView(R.id.edit_item_ok) protected RelativeLayout confirmEdit;
    @InjectView(R.id.edit_item_crop) protected RelativeLayout crop;
    @InjectView(R.id.edit_item_rotate) protected RelativeLayout rotate;


    @InjectView(R.id.edit_crop_item_1_1) protected RelativeLayout cropItem1to1;
    @InjectView(R.id.edit_crop_item_16_9) protected RelativeLayout cropItem16to9;
    @InjectView(R.id.edit_crop_item_4_3) protected RelativeLayout cropItem4to3;
    @InjectView(R.id.edit_crop_item_free) protected RelativeLayout cropItemFree;
    @InjectView(R.id.edit_crop_item_circle) protected RelativeLayout cropItemCircle;
    @InjectView(R.id.edit_crop_item_container) protected HorizontalScrollView cropItemContainer;

    private Toolbar toolbar;

    private ImageData image;
    private Navigator mNavigator;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableCrop = false;
        mNavigator = (Navigator) getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_edit, container, false);
        ButterKnife.inject(this, view);
        this.toolbar = ((MainActivity)this.getActivity()).getToolbar();
        this.toolbar.setTitle(R.string.image);
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroyView();
    }

    private boolean enableCrop;
    @Override
    public void onResume() {
        super.onResume();
        MenuItemManager.getInstance().clear().setEnable(MenuItemManager.State.DEFAULT);
        image = ImageShow.getInstance().getImageData();
//        Uri uri = Uri.fromFile(new File( image.to_data == null ? image.data : image.to_data));
        Uri uri = Uri.fromFile(new File(image.data));

        Picasso.with(getActivity()).load(uri).rotate(image.degree).into(this.cropImageView);
        this.titleView.setText(image.title);
        this.crop.setOnClickListener(v -> {
            enableCrop = !enableCrop;
            cropItemContainer.setVisibility(enableCrop ? View.VISIBLE : View.GONE);
            cropImageView.setCropEnabled(enableCrop);
        });
        this.confirmEdit.setOnClickListener(v -> {
            cropImageView.setCropEnabled(enableCrop = false);
            cropItemContainer.setVisibility(View.GONE);
            if (enableCrop) {
                cropImageView.setImageBitmap(cropImageView.getCroppedBitmap());
            }
        });
        cropImageView.setCropEnabled(false);
        this.rotate.setOnClickListener(v -> {
            if(enableCrop) {
                cropImageView.setCropEnabled(enableCrop = false);
                cropItemContainer.setVisibility(View.GONE);
            }
            cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
        });
        this.saveButton.setOnClickListener(v -> {
            save();
        });
        this.cancelButton.setOnClickListener(v -> {
            mNavigator.back();
        });

        this.cropItemFree.setOnClickListener(v->{
            cropImageView.setCropMode(CropImageView.CropMode.RATIO_FREE);
        });
        this.cropItem16to9.setOnClickListener(v->{
            cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9);
        });
        this.cropItem4to3.setOnClickListener(v->{
            cropImageView.setCropMode(CropImageView.CropMode.RATIO_4_3);
        });
        this.cropItem1to1.setOnClickListener(v->{
            cropImageView.setCropMode(CropImageView.CropMode.RATIO_1_1);
        });
        this.cropItemCircle.setOnClickListener(v->{
            cropImageView.setCropMode(CropImageView.CropMode.CIRCLE);
        });
    }

    private void save(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSSS", Locale.KOREA);
        ImageShow.getInstance().insertImage(getActivity(), String.format("%s.jpg", dateFormat.format(calendar.getTime())), cropImageView.getImageBitmap());
        mNavigator.back();
    }
    @Override
    public void onClick(View v) {

    }

}
