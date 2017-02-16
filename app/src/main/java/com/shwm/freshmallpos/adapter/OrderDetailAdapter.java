package com.shwm.freshmallpos.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.OrderDetailAdapter.MyViewHolder;
import com.shwm.freshmallpos.been.OrderEntity;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.util.UtilMath;

public class OrderDetailAdapter extends Adapter<MyViewHolder> {
	private Context context;
	private List<String> listKey;
	private OrderEntity order;

	public OrderDetailAdapter(Context context) {
		this.context = context;
		listKey = new ArrayList<String>();
		listKey.add("交易时间");
		listKey.add("流水号");
		listKey.add("收款方式");
		listKey.add("金额");
	}

	public void setData(OrderEntity order) {
		this.order = order;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		UL.e("orderdetaladapter", "" + listKey.size());
		if (listKey == null) {
			return 0;
		}
		return listKey.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder viewholder, int position) {
		// TODO Auto-generated method stub
		viewholder.tvLeft.setText(listKey.get(position));
		if (order != null) {
			String value = "";
			switch (position) {
			case 0:
				value = order.getTime();
				break;
			case 1:
				value = order.getOrderno();
				break;
			case 2:
				value = order.getPayTypeTag();
				break;
			case 3:
				value = UtilMath.currency(order.getMoney());
				break;

			default:
				break;
			}
			viewholder.tvRight.setText(value);
		}
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_orderdetail_order, null));
	}

	protected class MyViewHolder extends ViewHolder {
		TextView tvLeft, tvRight;
		public MyViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
			tvLeft = (TextView) view.findViewById(R.id.tv_item_orderdetail_order_left);
			tvRight = (TextView) view.findViewById(R.id.tv_item_orderdetail_order_right);
		}
	}

}
