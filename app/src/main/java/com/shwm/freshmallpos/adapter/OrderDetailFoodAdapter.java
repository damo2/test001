package com.shwm.freshmallpos.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.OrderDetailFoodAdapter.MyViewHolder;
import com.shwm.freshmallpos.been.FoodEntity;

public class OrderDetailFoodAdapter extends Adapter<MyViewHolder> {
	private Context context;
	private List<FoodEntity> listFood;

	public OrderDetailFoodAdapter(Context context) {
		this.context = context;
	}

	public void setData(List<FoodEntity> listFood) {
		this.listFood = listFood;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		if (listFood == null) {
			return 0;
		}
		return listFood.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder viewholder, int position) {
		// TODO Auto-generated method stub
		FoodEntity food = listFood.get(position);
		viewholder.tvLeft.setText(food.getName());
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
