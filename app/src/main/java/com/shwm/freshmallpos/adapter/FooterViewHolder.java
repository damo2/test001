package com.shwm.freshmallpos.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shwm.freshmallpos.R;

/**
 * Created by as on 2017/2/22.
 */

public class FooterViewHolder extends RecyclerView.ViewHolder {
    TextView tvTag;

    public FooterViewHolder(View view) {
        super(view);
        tvTag = (TextView) view.findViewById(R.id.tv_recycle_bottom_tag);
    }

}