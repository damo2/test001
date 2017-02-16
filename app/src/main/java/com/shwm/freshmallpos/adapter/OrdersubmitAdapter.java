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
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UtilMath;
import com.shwm.freshmallpos.value.ValueFuhao;

public class OrdersubmitAdapter extends Adapter<ViewHolder> {
	private List<FoodEntity> listcart;
	private Context context;

	public OrdersubmitAdapter(Context context) {
		this.context = context;
	}

	public void setData(List<FoodEntity> listcart) {
		this.listcart = listcart;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return listcart == null ? 0 : listcart.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.item_ordersubmit, null);
		return new MyViewholder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewholder, int positon) {
		// TODO Auto-generated method stub
		if (viewholder instanceof MyViewholder) {
			MyViewholder mViewholder = (MyViewholder) viewholder;
			FoodEntity food = listcart.get(positon);
			mViewholder.tvFoodName.setText(food.getName());
			String barcode = food.getBarcode();
			if (StringUtil.isEmpty(barcode)) {
				barcode = " - ";
			}
			mViewholder.tvFoodUnit.setText(barcode);
			mViewholder.tvFoodPrice.setText(ValueFuhao.FUHAO_RMB + StringUtil.doubleTrans(food.getPrice()) + " / " + food.getUnit());
			mViewholder.tvFoodNum.setText("x" + (food.isTypeWeight() ? food.getNum() : StringUtil.doubleTrans(food.getNum())));
			double money = UtilMath.mul(food.getPrice(), food.getNum());
			mViewholder.tvFoodMoney.setText(UtilMath.currency(money));
		}
	}

	private class MyViewholder extends ViewHolder {
		TextView tvFoodName;
		TextView tvFoodUnit;
		TextView tvFoodPrice;
		TextView tvFoodNum;
		TextView tvFoodMoney;

		public MyViewholder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
			tvFoodName = (TextView) view.findViewById(R.id.tv_item_ordersumit_foodname);
			tvFoodUnit = (TextView) view.findViewById(R.id.tv_item_ordersumit_foodunit);
			tvFoodPrice = (TextView) view.findViewById(R.id.tv_item_ordersumit_foodprice);
			tvFoodNum = (TextView) view.findViewById(R.id.tv_item_ordersumit_foodnum);
			tvFoodMoney = (TextView) view.findViewById(R.id.tv_item_ordersumit_foodMoney);
		}

	}
}
