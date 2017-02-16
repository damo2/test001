package com.shwm.freshmallpos.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.inter.ICashOrderFoodAddSub;
import com.shwm.freshmallpos.inter.IOnItemClickListener;
import com.shwm.freshmallpos.util.CommonUtil;

/**
 * 
 */
public class CartAdapter extends RecyclerView.Adapter<ViewHolder> {
	// 数据源
	private List<FoodEntity> listFood;
	// 上下文
	private Context mContext;
	private IOnItemClickListener iOnItemClickListener;
	private ICashOrderFoodAddSub iCashOrderFoodAddSub;

	// 构造函数
	public CartAdapter(Context mContext) {
		this.mContext = mContext;
	}

	// 设置数据并刷新
	public void setData(List<FoodEntity> listFood) {
		this.listFood = listFood;
	}

	public void setIOnItemClick(IOnItemClickListener iOnItemClickListener) {
		this.iOnItemClickListener = iOnItemClickListener;
	}

	public void setICashOrderFoodAddSub(ICashOrderFoodAddSub iCashOrderFoodAddSub) {
		this.iCashOrderFoodAddSub = iCashOrderFoodAddSub;
	}

	// 设置布局（相当于设置convertView）
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_food, parent, false);
		MyViewHolder vh = new MyViewHolder(v, iOnItemClickListener, iCashOrderFoodAddSub);
		return vh;
	}

	// 在布局中加载数据（绑定数据）
	@Override
	public void onBindViewHolder(ViewHolder viewholder, int position) {
		// 用来在运行时指出对象是某一个对象
		if (viewholder instanceof MyViewHolder) {
			FoodEntity food = listFood.get(position);
			MyViewHolder holder = (MyViewHolder) viewholder;
			holder.tvName.setText(food.getName());
			holder.tvPrice.setText(food.getPrice() + "");
			holder.tvNum.setText(food.getNum() + "");
		}
	}

	// 获取数据数目
	@Override
	public int getItemCount() {
		return listFood == null ? 0 : listFood.size();
	}

	private class MyViewHolder extends ViewHolder implements OnClickListener {
		private TextView tvName;
		private TextView tvPrice;
		private View viewAddNum;
		private ImageView ivAddNum;
		private ImageView ivSubNum;
		private TextView tvNum;

		private IOnItemClickListener iOnItemClickListener;
		private ICashOrderFoodAddSub iCashOrderFoodAddSub;

		public MyViewHolder(View view, IOnItemClickListener iOnItemClickListener, ICashOrderFoodAddSub iCashOrderFoodAddSub) {
			super(view);
			this.iOnItemClickListener = iOnItemClickListener;
			this.iCashOrderFoodAddSub = iCashOrderFoodAddSub;
			tvName = (TextView) view.findViewById(R.id.tv_item_cart_foodname);
			tvPrice = (TextView) view.findViewById(R.id.tv_item_cart_foodprice);
			ivAddNum = (ImageView) view.findViewById(R.id.iv_item_cart_addNum);
			ivSubNum = (ImageView) view.findViewById(R.id.iv_item_cart_subNum);
			tvNum = (TextView) view.findViewById(R.id.tv_item_cart_num);
			view.setOnClickListener(this);
			ivAddNum.setOnClickListener(this);
			ivSubNum.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			if (!CommonUtil.fastClick()) {
				return;
			}
			int position = getPosition();
			switch (view.getId()) {
			case R.id.iv_item_cart_addNum:
				if (iCashOrderFoodAddSub != null && position < listFood.size()) {
					iCashOrderFoodAddSub.onAdd(position, listFood.get(position));
				}
				break;
			case R.id.iv_item_cart_subNum:

				if (iCashOrderFoodAddSub != null && position < listFood.size()) {
					if (position >= 0 && listFood.size() > position) {
						iCashOrderFoodAddSub.onSub(position, listFood.get(position));
					}
				}
				break;
			default:
				if (iOnItemClickListener != null && position < listFood.size()) {
					iOnItemClickListener.onItemClick(view, null, position);
				}
			}
		}
	}

}
