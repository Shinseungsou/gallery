package com.jfsiot.hsgallery.app.recycler.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.util.view.recyclerview.RecyclerViewItemClickListener;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by SSS on 2015-08-06.
 */
public class SimpleViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
    @InjectView(R.id.icon) protected ImageView icon;
    @InjectView(R.id.name) protected TextView name;

    private RecyclerViewItemClickListener listener;
    public SimpleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
        itemView.setOnClickListener(this);
    }

    public SimpleViewHolder(View itemView, RecyclerViewItemClickListener listener){
        this(itemView);
        this.listener = listener;
    }

    public void bind(Integer icon, String name) {
        if(icon != null)
            Picasso.with(this.itemView.getContext()).load(icon).into(this.icon);
        else
            this.icon.setVisibility(View.GONE);
        this.name.setText(name);
    }

    @Override
    public void onClick(View v) {
        this.listener.onRecyclerViewItemClick(v, this.getAdapterPosition());
    }
}
