package com.shwm.freshmallpos.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.FoodManageClassesAdapter.MyViewHolder;
import com.shwm.freshmallpos.been.ClassesEntity;
import com.shwm.freshmallpos.inter.IOnItemClickListener;
import com.shwm.freshmallpos.manage.FoodListData;
import com.shwm.freshmallpos.value.ValueType;

public class FoodManageClassesAdapter extends RecyclerView.Adapter<MyViewHolder> {
	private Context context;
	private List<ClassesEntity> listClasses;
	private IOnItemClickListener iOnItemClickListener;
	private int typeEdit = ValueType.DEFAULT;

	private int type = 0;

	public FoodManageClassesAdapter(Context context, int type) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.type = type;
	}

	public void setData(List<ClassesEntity> listClasses) {
		this.listClasses = listClasses;
	}

	public void setTypeEdit(int typeEdit) {
		this.typeEdit = typeEdit;
	}

	public void setIOnItemClick(IOnItemClickListener iOnItemClickListener) {
		this.iOnItemClickListener = iOnItemClickListener;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cash_foodmanage_class, parent, false),
				iOnItemClickListener);
		return holder;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		ClassesEntity classes = listClasses.get(position);
		if (classes == null)
			return;
		int num = FoodListData.getClassesFoodNumByClassesId(classes.getId());
		if (typeEdit != ValueType.ADD && num > 0) {
			holder.tvNum.setVisibility(View.VISIBLE);
			holder.tvNum.setText(num + "");
		} else {
			holder.tvNum.setVisibility(View.GONE);
		}
		holder.tvText.setText(classes.getName());
		if (classes.isSelected()) {
			holder.tvText.setTextColor(ContextCompat.getColor(context, R.color.text_orange));
			holder.tvText.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
		} else {
			holder.tvText.setTextColor(ContextCompat.getColor(context, R.color.text_default));
			holder.tvText.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_default));
		}
	}

	@Override
	public int getItemCount() {
		return listClasses == null ? 0 : listClasses.size();
	}

	protected class MyViewHolder extends ViewHolder implements OnClickListener {
		private TextView tvText;
		private TextView tvNum;
		private IOnItemClickListener iOnItemClickListener;

		public MyViewHolder(View view, IOnItemClickListener iOnItemClickListener) {
			super(view);
			this.iOnItemClickListener = iOnItemClickListener;
			tvText = (TextView) view.findViewById(R.id.tv_item_foodmanage_classesname);
			tvNum = (TextView) view.findViewById(R.id.tv_item_foodmanage_num);
			view.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			if (iOnItemClickListener != null) {
				iOnItemClickListener.onItemClick(view,null, getPosition());
			}
			// 点击改变选中，改变颜色
			if (listClasses != null && listClasses.size() > 0) {
				int size = listClasses.size();
				for (int i = 0; i < size; i++) {
					if (i == getPosition()) {
						listClasses.get(i).setSelected(true);
					} else {
						listClasses.get(i).setSelected(false);
					}
				}
				notifyDataSetChanged();
			}
		}
	}
}