package com.jfsiot.hsgallery.app.recycler.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.model.UseLog;
import com.jfsiot.hsgallery.util.view.recyclerview.RecyclerViewItemClickListener;


import java.util.Date;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by SSS on 2015-08-06.
 */
public class UseLogViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
    @InjectView(R.id.date) protected TextView date;
    @InjectView(R.id.name) protected TextView name;
    @InjectView(R.id.title) protected TextView title;
    @InjectView(R.id.picture_id) protected TextView pictureId;
    @InjectView(R.id.directory) protected TextView bucket;
    @InjectView(R.id.type) protected TextView type;
    @InjectView(R.id.to_path) protected TextView toPath;
    @InjectView(R.id.share) protected TextView share;
    @InjectView(R.id.note) protected TextView note;

    private RecyclerViewItemClickListener listener;
    public UseLogViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
        itemView.setOnClickListener(this);
    }

    public UseLogViewHolder(View itemView, RecyclerViewItemClickListener listener){
        this(itemView);
        this.listener = listener;
    }

    public void bind(UseLog useLog) {
//        Calendar myCal =Calendar.getInstance();
//        myCal.setTimeInMillis(Long.parseLong(useLog.date)*1000);
//        Date dateText = new Date(myCal.get(Calendar.YEAR)-1900,
//            myCal.get(Calendar.MONTH),
//            myCal.get(Calendar.DAY_OF_MONTH),
//            myCal.get(Calendar.HOUR_OF_DAY),
//            myCal.get(Calendar.MINUTE));
        Date date = new Date(useLog.date);
        TimeZone timeZone = TimeZone.getTimeZone(useLog.date);
        this.date.setText(useLog.date);
        this.name.setText(useLog.name);
        this.title.setText(useLog.title);
        this.pictureId.setText(useLog.pictureId);
        this.bucket.setText(useLog.bucketName);
        this.type.setText(useLog.type);
        this.toPath.setText(useLog.to_data);
        this.share.setText(useLog.share);
        this.note.setText(useLog.note);
    }

    @Override
    public void onClick(View v) {
        this.listener.onRecyclerViewItemClick(v, this.getAdapterPosition());
    }
}
