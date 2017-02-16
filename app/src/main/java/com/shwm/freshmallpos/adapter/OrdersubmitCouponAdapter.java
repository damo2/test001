package com.shwm.freshmallpos.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.been.CouponEntity;
import com.shwm.freshmallpos.value.ValueType;

public class OrdersubmitCouponAdapter extends Adapter<OrdersubmitCouponAdapter.MyViewHolder> {
	private Context context;
	private List<CouponEntity> listCoupon;
	private List<Integer> listWidth = new ArrayList<Integer>();;

	public OrdersubmitCouponAdapter(Context context) {
		this.context = context;
	}

	public void setData(List<CouponEntity> listCoupon) {
		this.listCoupon = listCoupon;
		for (int i = 0; i < listCoupon.size(); i++) {
			String tag = listCoupon.get(i).getTag();
			int len = tag.length();
			listWidth.add(len + 20);
		}
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return listCoupon == null ? 0 : listCoupon.size();
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void onBindViewHolder(MyViewHolder viewholder, int position) {
		// TODO Auto-generated method stub
		ViewGroup.LayoutParams params = viewholder.tv.getLayoutParams();
		params.width = listWidth.get(position);
		params.height = 30;
		viewholder.tv.setLayoutParams(params);
		CouponEntity couponEntity = listCoupon.get(position);
		viewholder.tv.setText(couponEntity.getTag());
		if (couponEntity.getType() == ValueType.CouponType_Discount) {
			viewholder.viewMain.setBackgroundResource(R.color.bg_red);
		} else {
			viewholder.viewMain.setBackgroundResource(R.color.bg_orange);
		}
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.item_ordersubmitcoupon, null);
		return new MyViewHolder(view);
	}

	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView tv;
		View viewMain;

		public MyViewHolder(View view) {
			super(view);
			tv = (TextView) view.findViewById(R.id.tv_item_ordersubmitcoupon);
			viewMain = view.findViewById(R.id.ll_item_ordersubmitcoupon_main);
		}
	}

	/**
	 * 向指定位置添加元素
	 */
	public void addItem(int position, CouponEntity coupon) {
		if (position > listCoupon.size()) {
			position = listCoupon.size();
		}
		if (position < 0) {
			position = 0;
		}
		/**
		 * 使用notifyItemInserted/notifyItemRemoved会有动画效果 而使用notifyDataSetChanged()则没有
		 */
		listCoupon.add(position, coupon);// 在集合中添加这条数据
		listWidth.add(position, new Random().nextInt(200) + 100);// 添加一个随机高度,会在onBindViewHolder方法中得到设置
		notifyItemInserted(position);// 通知插入了数据
	}

	/**
	 * 移除指定位置元素
	 */
	public CouponEntity removeItem(int position) {
		if (position > listCoupon.size() - 1) {
			return null;
		}
		listWidth.remove(position);// 删除添加的高度
		CouponEntity coupon = listCoupon.remove(position);// 所以还需要手动在集合中删除一次
		notifyItemRemoved(position);// 通知删除了数据,但是没有删除list集合中的数据
		return coupon;
	}
}
