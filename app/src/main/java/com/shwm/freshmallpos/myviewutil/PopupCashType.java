package com.shwm.freshmallpos.myviewutil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.PaintDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.CashTypePopupAdapter;
import com.shwm.freshmallpos.base.BasePopupWindow;
import com.shwm.freshmallpos.been.IconEntity;
import com.shwm.freshmallpos.inter.IOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class PopupCashType extends BasePopupWindow {
    private View mContentView;
    private RecyclerView mRecycleView;
    private CashTypePopupAdapter mAdapter;
    private List<IconEntity> listIcon;
    private Context context;

    public PopupCashType(Activity context) {
        this(context, LayoutParams.MATCH_PARENT);
    }

    public PopupCashType(Activity context, int height) {
        super(context);// 没有会出错
        this.context = context;
        mContentView = LayoutInflater.from(context).inflate(R.layout.popup_cashtype, null);
        this.setContentView(mContentView);
        init();
        initView();
        setValue();
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(height);
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new PaintDrawable());
        this.setAnimationStyle(R.style.PopupAnimationBottom);
        this.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    private void init() {
        listIcon = new ArrayList<IconEntity>();
        listIcon.add(new IconEntity(R.drawable.icon_gathering_tablecard, context.getString(R.string.paytype_taib)));
        listIcon.add(new IconEntity(R.drawable.icon_gathering_wechat, context.getString(R.string.paytype_wechat)));
        listIcon.add(new IconEntity(R.drawable.icon_gathering_alipay, context.getString(R.string.paytype_alipay)));
        listIcon.add(new IconEntity(R.drawable.icon_gathering_cash, context.getString(R.string.paytype_cash)));
    }

    private void initView() {
        mRecycleView = (RecyclerView) mContentView.findViewById(R.id.recycleview_popup_cashtype);
    }

    private void setValue() {
        FullyGridLayoutManager fullyGridLayoutManager = new FullyGridLayoutManager(context, 3);
        fullyGridLayoutManager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(fullyGridLayoutManager);
        mRecycleView.addItemDecoration(new SpaceItemDecoration(16));
        mAdapter = new CashTypePopupAdapter(context);
        mAdapter.setData(listIcon);
        mRecycleView.setAdapter(mAdapter);
    }

    public void setIOnItemClickListener(IOnItemClickListener iOnItemClickListener) {
        mAdapter.setIOnItemClickListener(iOnItemClickListener);
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            // 不是第一个的格子都设一个左边和底部的间距
            outRect.left = space;
            outRect.bottom = space;
            // 由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
            if (parent.getChildLayoutPosition(view) % 3 == 0) {
                outRect.left = 0;
            }
        }
    }

}
