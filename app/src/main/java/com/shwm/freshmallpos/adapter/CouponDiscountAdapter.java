package com.shwm.freshmallpos.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.been.CouponEntity;
import com.shwm.freshmallpos.value.ValueType;

public class CouponDiscountAdapter extends Adapter<ViewHolder> {
	private Context context;
	private int couponType;
	private boolean couponOpenStatu;
	private static final int TYPE_ADD = 1;
	private List<CouponEntity> listCoupon;

	private ICouponSetListener iCouponSetListener;

	public CouponDiscountAdapter(Context context, int couponType) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.couponType = couponType;
	}

	public void setData(List<CouponEntity> listCoupon) {
		this.listCoupon = listCoupon;
	}

	public void setCouponOpenStatu(boolean couponOpenStatu) {
		this.couponOpenStatu = couponOpenStatu;
	}

	public void setIOnCouponSetListener(ICouponSetListener iCouponSetListener) {
		this.iCouponSetListener = iCouponSetListener;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		// 打开优惠劵才能看到添加
		if (couponOpenStatu) {
			return listCoupon == null ? 1 : listCoupon.size() + 1;
		} else {
			return listCoupon == null ? 0 : listCoupon.size();
		}
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if ((couponOpenStatu && listCoupon == null) || position == listCoupon.size()) {
			return TYPE_ADD;
		}
		return couponType;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewholder, int position) {
		// TODO Auto-generated method stub
		if (viewholder instanceof DiscountViewHolder) {
			DiscountViewHolder viewHolder = (DiscountViewHolder) viewholder;
			CouponEntity coupon = listCoupon.get(position);
			if (coupon.getType() == ValueType.CouponType_Discount) {
				viewHolder.tvDiscount.setText(coupon.getDiscount() + "折");
			}
			if (coupon.getType() == ValueType.CouponType_Moneydown) {
				viewHolder.tvDiscount.setText("减" + coupon.getMoneydown() + "");
			}

			if (!couponOpenStatu) {
				// 关闭优惠
				viewHolder.tvDiscount.setBackgroundResource(R.color.coupon_add);
				viewHolder.ivLeft.setImageResource(R.drawable.coupon_lac_4);
			} else {
				if (coupon.getType() == ValueType.CouponType_Discount) {
					viewHolder.tvDiscount.setBackgroundResource(R.color.coupon_discount);
					viewHolder.ivLeft.setImageResource(R.drawable.coupon_lac_1);
				}
				if (coupon.getType() == ValueType.CouponType_Moneydown) {
					viewHolder.tvDiscount.setBackgroundResource(R.color.coupon_moneydown);
					viewHolder.ivLeft.setImageResource(R.drawable.coupon_lac_2);
				}
			}
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
		// TODO Auto-generated method stub
		if (type == TYPE_ADD) {
			View view = LayoutInflater.from(context).inflate(R.layout.item_coupon_add, parent, false);
			return new AddViewHolder(view);
		}
		if (type == ValueType.CouponType_Discount || type == ValueType.CouponType_Moneydown) {
			View view = LayoutInflater.from(context).inflate(R.layout.item_coupon_discount, parent, false);
			return new DiscountViewHolder(view);
		}
		return null;
	}

	protected class DiscountViewHolder extends ViewHolder {
		private TextView tvDiscount;
		private ImageView ivLeft;
		private ImageView ivDelete;

		public DiscountViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
			tvDiscount = (TextView) view.findViewById(R.id.tv_item_coupon_discount_text);
			ivLeft = (ImageView) view.findViewById(R.id.tv_item_coupon_left);
			ivDelete = (ImageView) view.findViewById(R.id.iv_item_coupon_discount_del);
			ivDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if (couponOpenStatu && iCouponSetListener != null) {
						iCouponSetListener.onDelete(view, getPosition(), couponType);
					}
				}
			});
		}
	}

	protected class AddViewHolder extends ViewHolder {

		public AddViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if (iCouponSetListener != null) {
						iCouponSetListener.onAdd(view, getPosition(), couponType);
					}
				}
			});
		}
	}

	public interface ICouponSetListener {
		void onAdd(View view, int posiont, int couponType);

		void onDelete(View view, int posiont, int couponType);
	}

}
