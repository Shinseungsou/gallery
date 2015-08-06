package com.siot.sss.hsgallery.app.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.activity.MainActivity;
import com.siot.sss.hsgallery.app.adapter.GalleryAdapter;
import com.siot.sss.hsgallery.app.model.ImageSource;
import com.siot.sss.hsgallery.app.model.unique.ImageShow;
import com.siot.sss.hsgallery.util.navigator.Navigator;
import com.siot.sss.hsgallery.util.recyclerview.RecyclerViewFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by SSS on 2015-08-04.
 */
public class GalleryFragment extends RecyclerViewFragment<GalleryAdapter, ImageSource>{
    @InjectView(R.id.gallery) protected RecyclerView gallery;
    private CompositeSubscription subscription;
    private Toolbar toolbar;
    private Navigator navigator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.inject(this, view);
        this.navigator = (MainActivity) this.getActivity();

        this.toolbar = ((MainActivity)this.getActivity()).getToolbar();
        this.toolbar.setTitle(R.string.gallery);
        this.setupRecyclerView(this.gallery);
        this.getImageCursor();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(this.subscription != null && !this.subscription.isUnsubscribed()) this.subscription.unsubscribe();
        ButterKnife.reset(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getImageCursor(){
        String[] proj = {MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE};
//        String[] proj = {MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        Cursor imageCursor = this.getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

//        Cursor imageCursor = this.getActivity().managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            proj, null, null, null);
//        Cursor imageCursor = (new CursorLoader(this.getActivity().getBaseContext(), MediaStore.Images.Media.INTERNAL_CONTENT_URI, proj, null, null, null)).loadInBackground();

        int num = 0;

        if (imageCursor != null && imageCursor.moveToFirst()){
            do {
                num++;
                if (imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)) != null){
                    this.items.add(new ImageSource(imageCursor));
//                    Timber.d("name : %s", imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)));
                }
            }while (imageCursor.moveToNext());
            Timber.d("num : %s", num);
            this.adapter.notifyDataSetChanged();
        }
        imageCursor.close();
    }

    @Override
    protected GalleryAdapter getAdapter() {
        return new GalleryAdapter(this.items, this);
    }

    @Override
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return new GridLayoutManager(this.getActivity(), 2);
    }

    @Override
    public void onRecyclerViewOtemClick(View view, int position) {
        ImageShow.getInstance().setImageSource(this.items.get(position));
        this.navigator.navigate(ImageFragment.class, true);
    }
}
