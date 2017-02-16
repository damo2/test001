package com.shwm.freshmallpos.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.been.OrderEntity;
import com.shwm.freshmallpos.inter.IOnItemClickListener;
import com.shwm.freshmallpos.util.UtilMath;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.base.ApplicationMy;

/**
 * 
 */
public class MainOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	// 设置底部布局
	private static final int TYPE_FOOTER = 0;
	// 设置默认布局
	private static final int TYPE_DEFAULT = 1;

	private List<OrderEntity> listOrder;
	private Context mContext;
	// 判断是不是最后一个item，默认是false
	private boolean mShowFooter = true;

	private int loadType = ValueType.LOAD_NO;

	private IOnItemClickListener iOnItemClickListener;

	// 构造函数
	public MainOrderAdapter(Context mContext) {
		this.mContext = mContext;
	}

	// 设置数据并刷新
	public void setData(List<OrderEntity> listOrder) {
		this.listOrder = listOrder;
	}

	public void setLoadType(int loadtype) {
		if (this.loadType != loadtype) {
			this.loadType = loadtype;
			notifyItemChanged(getItemCount());
		}
	}

	// 设置不同的item
	@Override
	public int getItemViewType(int position) {
		// 判断当前位置+1是不是等于数据总数（因为数组从0开始计数），是的就加载底部布局刷新，不是就加载默认布局
		if (position + 1 == getItemCount()) {
			return TYPE_FOOTER;
		} else {
			return TYPE_DEFAULT;
		}
	}

	// 设置布局（相当于设置convertView）
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_DEFAULT) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_order, parent, false);
			DefaultViewHolder vh = new DefaultViewHolder(v);
			return vh;
		} else {
			// 实例化布局
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recycle_bottom, null);
			// 代码实现加载布局
			view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			return new FooterViewHolder(view);
		}
	}

	// 在布局中加载数据（绑定数据）
	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		// 用来在运行时指出对象是某一个对象
		if (holder instanceof DefaultViewHolder) {
			OrderEntity order = listOrder.get(position);
			if (position > 0 && listOrder.get(position - 1).getDate().equals(order.getDate())) {
				((DefaultViewHolder) holder).rlTop.setVisibility(View.GONE);
			} else {
				((DefaultViewHolder) holder).rlTop.setVisibility(View.VISIBLE);
				((DefaultViewHolder) holder).tvDate.setText(order.getDate());
				String format = ApplicationMy.getContext().getString(R.string.order_totalDay);
				String money = UtilMath.currency(order.getTotalDay());
				((DefaultViewHolder) holder).tvTotalDay.setText(String.format(format, money));
			}
			if (position == 0) {
				((DefaultViewHolder) holder).rlTop.setVisibility(View.VISIBLE);
				((DefaultViewHolder) holder).tvDate.setText(order.getDate());
			}
			int payType = order.getPayType();
			String pagTypeText = "";
			if (payType == 4) {
				pagTypeText = "现金支付";
			}
			((DefaultViewHolder) holder).tvPaytype.setText(order.getPayTypeTag());
			SpannableString spanMoney = new SpannableString(UtilMath.currency(order.getMoney()));
			if (order.getRefund().equals("已退款")) {
				StrikethroughSpan span = new StrikethroughSpan();
				spanMoney.setSpan(span, 0, spanMoney.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			((DefaultViewHolder) holder).tvMoney.setText(spanMoney);
			((DefaultViewHolder) holder).tvTime.setText(order.getTime() + "");
			((DefaultViewHolder) holder).tvRefund.setText(order.getRefund() + "");
		}

		if (holder instanceof FooterViewHolder) {
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

	// 获取数据数目
	@Override
	public int getItemCount() {
		// 判断是不是显示底部，是就返回1，不是返回0
		int begin = mShowFooter ? 1 : 0;
		// 没有数据的时候，直接返回begin
		if (listOrder == null) {
			return begin;
		}
		// 因为底部布局要占一个位置，所以总数目要+1
		return listOrder.size() + begin;
	}

	// 设置是否显示底部加载提示（将值传递给全局变量）
	public void isShowFooter(boolean showFooter) {
		this.mShowFooter = showFooter;
	}

	// 判断是否显示底部，数据来自全局变量
	public boolean isShowFooter() {
		return this.mShowFooter;
	}

	// 底部布局的ViewHolder
	private class FooterViewHolder extends RecyclerView.ViewHolder {
		TextView tvTag;

		public FooterViewHolder(View view) {
			super(view);
			tvTag = (TextView) view.findViewById(R.id.tv_recycle_bottom_tag);
		}

	}

	// 默认布局的ViewHolder
	protected class DefaultViewHolder extends RecyclerView.ViewHolder {
		private TextView tvDate;
		private RelativeLayout rlTop;
		private TextView tvTotalDay;
		private TextView tvPaytype;
		private TextView tvMoney;
		private TextView tvTime;
		private TextView tvRefund;

		public DefaultViewHolder(View itemView) {
			super(itemView);
			tvDate = (TextView) itemView.findViewById(R.id.tv_item_mainOrder_date);
			rlTop = (RelativeLayout) itemView.findViewById(R.id.rl_item_mainOrder_top);
			tvPaytype = (TextView) itemView.findViewById(R.id.tv_item_mainOrder_type);
			tvMoney = (TextView) itemView.findViewById(R.id.tv_item_mainOrder_money);
			tvTime = (TextView) itemView.findViewById(R.id.tv_item_mainOrder_time);
			tvTotalDay = (TextView) itemView.findViewById(R.id.tv_item_mainOrder_totalDay);
			tvRefund = (TextView) itemView.findViewById(R.id.tv_item_mainOrder_refund);
			itemView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if (iOnItemClickListener != null) {
						iOnItemClickListener.onItemClick(view,null, getPosition());
					}
				}
			});
		}
	}

	public void setIOnItemClick(IOnItemClickListener iOnItemClickListener) {
		this.iOnItemClickListener = iOnItemClickListener;
	}

}
