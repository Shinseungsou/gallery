package com.siot.sss.hsgallery.app.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.activity.MainActivity;
import com.siot.sss.hsgallery.app.adapter.LogAdapter;
import com.siot.sss.hsgallery.app.model.UseLog;
import com.siot.sss.hsgallery.util.database.table.DBOpenHelper;
import com.siot.sss.hsgallery.util.recyclerview.RecyclerViewFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by SSS on 2015-08-04.
 */
public class LogFragment extends RecyclerViewFragment<LogAdapter, UseLog>{
    @InjectView(R.id.log_list) protected RecyclerView gallery;
    private CompositeSubscription subscription;
    private Toolbar toolbar;

    private enum State{
        ALL, SAVE, READ, UPDATE, DELETE
    }
    private State state;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);
        ButterKnife.inject(this, view);
        this.toolbar = ((MainActivity)this.getActivity()).getToolbar();
        this.toolbar.setTitle(R.string.log);
        this.setupRecyclerView(this.gallery);
        this.state = State.ALL;
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
                    cursor.getString(cursor.getColumnIndex("picture_id")),
                    cursor.getString(cursor.getColumnIndex("type"))
                )
            );
        }while (cursor.moveToNext());

        helper.close();
    }

    private void toolbarMenuItemChange(State state){
        int id = 12;
        switch (state){
            case ALL:
                this.toolbar.getMenu().findItem(id).setVisible(true);
                break;
            case SAVE:
                this.toolbar.getMenu().findItem(id).setVisible(false);
                break;
            case DELETE:
                break;
            case READ:
                break;
            case UPDATE:
                break;
        }

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
