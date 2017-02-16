package com.shwm.freshmallpos.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.CouponAdapter.CouponViewHolder;
import com.shwm.freshmallpos.been.CouponEntity;
import com.shwm.freshmallpos.inter.IOnItemClickListener;
import com.shwm.freshmallpos.util.StringFormatUtil;
import com.shwm.freshmallpos.value.ValueType;

public class CouponAdapter extends Adapter<CouponViewHolder> {
	private Context context;
	private List<CouponEntity> listCoupon;
	private IOnItemClickListener<CouponEntity> iOnItemClickListener;

	public CouponAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void setData(List<CouponEntity> listCoupon) {
		this.listCoupon = listCoupon;
	}

	public void setIOnItemClickListener(IOnItemClickListener<CouponEntity> iOnItemClickListener) {
		this.iOnItemClickListener = iOnItemClickListener;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stubF
		return listCoupon == null ? 0 : listCoupon.size();
	}

	@Override
	public void onBindViewHolder(CouponViewHolder viewHolder, int position) {
		// TODO Auto-generated method stub
		CouponEntity coupon = listCoupon.get(position);
		if (coupon.getType() == ValueType.CouponType_Discount) {
			viewHolder.tvTag.setText(context.getString(R.string.coupon_popup_discount));
			viewHolder.tvValue.setText(StringFormatUtil.couponDiscount(coupon.getDiscount()));
			viewHolder.viewContent.setBackgroundResource(R.color.coupon_discount);
			viewHolder.ivLeft.setImageResource(R.drawable.coupon_lac_1);
		}
		if (coupon.getType() == ValueType.CouponType_Moneydown) {
			viewHolder.tvTag.setText(context.getString(R.string.coupon_popup_moneydown));
			viewHolder.tvValue.setText(StringFormatUtil.couponMoneydown(coupon.getMoneydown()));
			viewHolder.viewContent.setBackgroundResource(R.color.coupon_moneydown);
			viewHolder.ivLeft.setImageResource(R.drawable.coupon_lac_2);
		}
	}

	@Override
	public CouponViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.item_coupon, parent, false);
		return new CouponViewHolder(view);
	}

	protected class CouponViewHolder extends ViewHolder {
		private TextView tvValue;
		private TextView tvTag;
		private View viewContent;
		private ImageView ivLeft;

		public CouponViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
			viewContent = view.findViewById(R.id.rl_item_coupon);
			ivLeft = (ImageView) view.findViewById(R.id.tv_item_coupon_left);
			tvValue = (TextView) view.findViewById(R.id.tv_item_coupon_value);
			tvTag = (TextView) view.findViewById(R.id.tv_item_coupon_tag);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if (iOnItemClickListener != null) {
						int positon = getPosition();
						iOnItemClickListener.onItemClick(view, listCoupon.get(positon), positon);
					}
				}
			});
		}
	}
}
