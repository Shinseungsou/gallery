package com.siot.sss.hsgallery.app.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.activity.MainActivity;
import com.siot.sss.hsgallery.app.adapter.GalleryAdapter;
import com.siot.sss.hsgallery.app.adapter.LogAdapter;
import com.siot.sss.hsgallery.app.model.ImageSource;
import com.siot.sss.hsgallery.app.model.UseLog;
import com.siot.sss.hsgallery.util.database.table.DBOpenHelper;
import com.siot.sss.hsgallery.util.recyclerview.RecyclerViewFragment;

import java.sql.SQLException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by SSS on 2015-08-04.
 */
public class LogFragment extends RecyclerViewFragment<LogAdapter, UseLog>{
    @InjectView(R.id.log_list) protected RecyclerView gallery;
    private CompositeSubscription subscription;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);
        ButterKnife.inject(this, view);
        this.toolbar = ((MainActivity)this.getActivity()).getToolbar();
        this.toolbar.setTitle(R.string.log);
        this.setupRecyclerView(this.gallery);
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
        DBOpenHelper helper = new DBOpenHelper(this.getActivity().getBaseContext());
        this.items.clear();
        helper.open();
        Cursor cursor = helper.getAllColumnsUseLog();
        cursor.moveToFirst();
        do {
            this.items.add(
                new UseLog(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("date")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("picture_id"))
                )
            );
        }while (cursor.moveToNext());

        helper.close();
    }

    @Override
    protected LogAdapter getAdapter() {
        return new LogAdapter(this.items, this);
    }

    @Override
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return new LinearLayoutManager(this.getActivity());
    }

    @Override
    public void onRecyclerViewOtemClick(View view, int position) {
    }
}
