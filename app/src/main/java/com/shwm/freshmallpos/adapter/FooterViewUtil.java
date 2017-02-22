package com.shwm.freshmallpos.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.base.ApplicationMy;
import com.shwm.freshmallpos.value.ValueType;

/**
 * Created by as on 2017/2/22.
 */

public class FooterViewUtil {
    public static void setFooterViewStatu(RecyclerView.ViewHolder holder, int loadType) {
        FooterViewHolder viewHolder = (FooterViewHolder) holder;
        switch (loadType) {
            case ValueType.LOAD_LOADING:
                viewHolder.tvTag.setVisibility(View.VISIBLE);
                viewHolder.tvTag.setText(ApplicationMy.getContext().getString(R.string.loading));
                break;
            case ValueType.LOAD_OVER:
                viewHolder.tvTag.setVisibility(View.VISIBLE);
                viewHolder.tvTag.setText(ApplicationMy.getContext().getString(R.string.loadover));
                break;
            case ValueType.LOAD_OVERALL:
                viewHolder.tvTag.setVisibility(View.VISIBLE);
                viewHolder.tvTag.setText(ApplicationMy.getContext().getString(R.string.loadoverAll));
                break;
            case ValueType.LOAD_FAIL:
                viewHolder.tvTag.setText(ApplicationMy.getContext().getString(R.string.loadfail));
                break;
            default:
                viewHolder.tvTag.setVisibility(View.GONE);
                break;
        }
    }
}
