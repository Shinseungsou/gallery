package com.siot.sss.hsgallery.app.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.siot.sss.hsgallery.R;
import com.siot.sss.hsgallery.app.model.UseLog;
import com.siot.sss.hsgallery.util.recyclerview.RecyclerViewItemClickListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by SSS on 2015-08-06.
 */
public class UseLogViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
    @InjectView(R.id.date) protected TextView date;
    @InjectView(R.id.name) protected TextView name;
    @InjectView(R.id.picture_id) protected TextView pictureId;

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
        this.date.setText(useLog.date);
        this.name.setText(useLog.name);
        this.pictureId.setText(useLog.pictureId);
    }

    @Override
    public void onClick(View v) {
        this.listener.onRecyclerViewOtemClick(v, this.getAdapterPosition());
    }
}
