package com.shwm.freshmallpos.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.inter.ICashOrderFoodAddSub;
import com.shwm.freshmallpos.inter.IOnItemClickListener;
import com.shwm.freshmallpos.manage.FoodListData;
import com.shwm.freshmallpos.util.CommonUtil;
import com.shwm.freshmallpos.util.ConfigUtil;
import com.shwm.freshmallpos.util.ImageLoadUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueFinal;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.base.ApplicationMy;

public class FoodManageFoodAdapter extends RecyclerView.Adapter<ViewHolder> {
	private String TAG = getClass().getSimpleName();
	private Context context;
	private List<FoodEntity> listFood;
	private IOnItemClickListener iOnItemClickListener;
	private int typeEdit = ValueType.DEFAULT;
	private ICashOrderFoodAddSub iCashOrderFoodAddSub;
	// 设置底部布局
	private static final int TYPE_FOOTER = 0;
	// 设置默认布局
	private static final int TYPE_DEFAULT = 1;
	private static final int TYPE_CART = 2;

	private boolean isShowFoot = false;
	private int loadType = ValueType.LOAD_NO;

	boolean mIsRefreshing = false;

	public FoodManageFoodAdapter(Context context, List<FoodEntity> listFood, int typeEdit) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.listFood = listFood;
		this.typeEdit = typeEdit;
	}

	public void setData(List<FoodEntity> listFood) {
		this.listFood = listFood;
	}

	// 是否是编辑状态
	public void setTypeEdit(int typeEdit) {
		this.typeEdit = typeEdit;
	}

	public void setIOnItemClick(IOnItemClickListener iOnItemClickListener) {
		this.iOnItemClickListener = iOnItemClickListener;
	}

	public void setICashOrderFoodAddSub(ICashOrderFoodAddSub iCashOrderFoodAddSub) {
		this.iCashOrderFoodAddSub = iCashOrderFoodAddSub;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (position + 1 == getItemCount()) {
			isShowFoot = true;
			return TYPE_FOOTER;
		} else {
			if (typeEdit == ValueType.CART) {
				return TYPE_CART;
			} else {
				return TYPE_DEFAULT;
			}
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_DEFAULT) {
			View view = LayoutInflater.from(context).inflate(R.layout.item_cash_foodmanage_food, parent, false);
			return new MyViewHolder(view, iOnItemClickListener, iCashOrderFoodAddSub);
		} else if (viewType == TYPE_CART) {
			View view = LayoutInflater.from(context).inflate(R.layout.item_cash_foodmanage_cart, parent, false);
			return new CartViewHolder(view, iCashOrderFoodAddSub);
		} else {
			View view = LayoutInflater.from(context).inflate(R.layout.view_recycle_bottom, parent, false);
			return new FootViewHolder(view);
		}
	}

	@Override
	public void onBindViewHolder(ViewHolder viewholder, final int position) {
		if (viewholder instanceof MyViewHolder) {
			FoodEntity food = listFood.get(position);
			MyViewHolder holder = (MyViewHolder) viewholder;
			holder.ivSelect.setVisibility(View.GONE);
			holder.viewAddNum.setVisibility(View.GONE);
			holder.viewAddweight.setVisibility(View.GONE);
			holder.btnWeight.setVisibility(View.GONE);
			// holder.ivAddNum.setVisibility(View.GONE);
			// holder.ivImg.setVisibility(View.GONE);
			holder.tvName.setText(food.getName());
			holder.tvPrice.setText(food.getPrice() + "");
//			UL.e(TAG, position + " * | " + food.getName() + " | img=" + food.getImg());
//			UL.e(TAG, holder.tvPrice.getTag() + "Tag| " + holder.tvName.getTag() + " | img=" + holder.ivImg.getTag());
			if (typeEdit == ValueType.CART) {
				holder.ivImg.setVisibility(View.GONE);
			} else if (holder.ivImg.getTag() == null || !holder.ivImg.getTag().equals(food.getImg())) {
				holder.ivImg.setTag(listFood.get(position).getImg());
				holder.tvName.setTag(food.getName());
				holder.tvPrice.setTag(position);
				ImageLoadUtil.displayImage(holder.ivImg, food.getImg(), ImageLoadUtil.getOptionsImgFood());
			}
			if (typeEdit == ValueType.ADD || typeEdit == ValueType.EDIT) {
				holder.ivAddNum.setVisibility(View.GONE);
			}
			// 编辑状态显示选择图标
			if (typeEdit == ValueType.EDIT) {
				holder.ivSelect.setVisibility(View.VISIBLE);
				boolean isExitChoose = FoodListData.isExitListChoose(food);
				if (isExitChoose) {
					holder.ivSelect.setImageResource(R.drawable.ic_select_a);
				} else {
					holder.ivSelect.setImageResource(R.drawable.ic_selectno_a);
				}
			} else {
				double num = FoodListData.getNumCartByFoodId(food.getId());
				if (food.isTypeDefault()) {
					if (num > 0) {
						holder.viewAddNum.setVisibility(View.VISIBLE);
						holder.tvNum.setText(StringUtil.doubleTrans(num));
					}
				}
				if (food.isTypeWeight()) {
					holder.tvPrice.setText(food.getPrice() + " /" + food.getUnit());
					if (food.isAddstatu() || num > 0) {
						holder.btnWeight.setVisibility(View.VISIBLE);
						holder.viewAddweight.setVisibility(View.VISIBLE);
						holder.edtWeight.setText("");
					}
					if (num > 0) {
						holder.edtWeight.setText(StringUtil.doubleTrans(num));
					}
				}
			}

		}
		if (viewholder instanceof CartViewHolder) {
			FoodEntity food = listFood.get(position);
			CartViewHolder holder = (CartViewHolder) viewholder;
			holder.viewAddNum.setVisibility(View.GONE);
			holder.viewAddweight.setVisibility(View.GONE);
			holder.btnWeight.setVisibility(View.GONE);
			// holder.ivAddNum.setVisibility(View.GONE);
			// holder.ivImg.setVisibility(View.GONE);

			holder.tvName.setText(food.getName());
			holder.tvPrice.setText(food.getPrice() + "");
			// 编辑状态显示选择图标
			if (typeEdit == ValueType.ADD) {
				holder.ivAddNum.setVisibility(View.GONE);
			}
			double num = FoodListData.getNumCartByFoodId(food.getId());
			if (food.isTypeDefault()) {
				if (num > 0) {
					holder.viewAddNum.setVisibility(View.VISIBLE);
					holder.tvNum.setText(StringUtil.doubleTrans(num));
				}
			}
			if (food.isTypeWeight()) {
				holder.tvPrice.setText(food.getPrice() + " /" + food.getUnit());
				if (food.isAddstatu() || num > 0) {
					holder.btnWeight.setVisibility(View.VISIBLE);
					holder.viewAddweight.setVisibility(View.VISIBLE);
					holder.edtWeight.setText("");
				}
				if (num > 0) {
					holder.edtWeight.setText(StringUtil.doubleTrans(num));
				}
			}
		}

		if (viewholder instanceof FootViewHolder) {
			FootViewHolder holderFooter = (FootViewHolder) viewholder;
			switch (loadType) {
			case ValueType.LOAD_LOADING:
				holderFooter.tvTag.setVisibility(View.VISIBLE);
				holderFooter.tvTag.setText(ApplicationMy.getContext().getString(R.string.loading));
				break;
			case ValueType.LOAD_OVER:
				holderFooter.tvTag.setVisibility(View.VISIBLE);
				holderFooter.tvTag.setText(ApplicationMy.getContext().getString(R.string.loadover));
				break;
			case ValueType.LOAD_OVERALL:
				holderFooter.tvTag.setVisibility(View.VISIBLE);
				holderFooter.tvTag.setText(ApplicationMy.getContext().getString(R.string.loadoverAll));
				break;
			case ValueType.LOAD_FAIL:
				holderFooter.tvTag.setVisibility(View.VISIBLE);
				holderFooter.tvTag.setText(ApplicationMy.getContext().getString(R.string.loadfail));
				break;
			default:
				holderFooter.tvTag.setVisibility(View.GONE);
				break;
			}
		}
	}

	@Override
	public int getItemCount() {
	//	int temp = isShowFoot ? 1 : 0;
	//	return listFood == null ? temp : listFood.size() + temp;
		return listFood == null ? 1 : listFood.size() + 1;
	}

	/** 是否显示底部 */
	public boolean isShowFooter() {
		return isShowFoot;
	}

	public void setLoadType(int loadType) {
		if (this.loadType != loadType) {
			this.loadType = loadType;
			if (isShowFoot) {
				notifyItemChanged(getItemCount());
			}
		}
	}

	private class MyViewHolder extends ViewHolder implements OnClickListener {
		private TextView tvName;
		private TextView tvPrice;
		private ImageView ivImg;
		private ImageView ivSelect;

		private View viewAddNum;
		private ImageView ivAddNum;
		private ImageView ivSubNum;
		private TextView tvNum;

		private View viewAddweight;
		private EditText edtWeight;
		private ImageView ivWeightDel;
		private Button btnWeight;

		private IOnItemClickListener iOnItemClickListener;
		private ICashOrderFoodAddSub iCashOrderFoodAddSub;

		public MyViewHolder(View view, IOnItemClickListener iOnItemClickListener, final ICashOrderFoodAddSub iCashOrderFoodAddSub) {
			super(view);
			this.iOnItemClickListener = iOnItemClickListener;
			this.iCashOrderFoodAddSub = iCashOrderFoodAddSub;
			tvName = (TextView) view.findViewById(R.id.tv_item_foodmanage_foodname);
			tvPrice = (TextView) view.findViewById(R.id.tv_item_foodmanage_foodprice);
			ivImg = (ImageView) view.findViewById(R.id.iv_item_foodnamage_img);

			ivSelect = (ImageView) view.findViewById(R.id.iv_item_foodmanage_select);

			viewAddNum = view.findViewById(R.id.rl_item_cashOrder_addnum);
			ivAddNum = (ImageView) view.findViewById(R.id.iv_item_cashOrder_addNum);
			ivSubNum = (ImageView) view.findViewById(R.id.iv_item_cashOrder_subNum);
			tvNum = (TextView) view.findViewById(R.id.tv_item_cashOrder_num);

			viewAddweight = view.findViewById(R.id.rl_item_cashOrder_addweight);
			edtWeight = (EditText) view.findViewById(R.id.tv_item_cashOrder_weight);
			ivWeightDel = (ImageView) view.findViewById(R.id.iv_item_cashOrder_del);
			btnWeight = (Button) view.findViewById(R.id.btn_item_foodmanage_weight);

			view.setOnClickListener(this);
			ivAddNum.setOnClickListener(this);
			ivSubNum.setOnClickListener(this);
			ivWeightDel.setOnClickListener(this);
			btnWeight.setOnClickListener(this);
			StringUtil.getIntFomat(edtWeight, ConfigUtil.Weight_Size);
			edtWeight.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					// TODO Auto-generated method stub
				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if (s.length() > 0) {
						if (StringUtil.getDouble(s.toString()) > ValueFinal.MAX_NUM) {
							s.delete(s.length() - 1, s.length());
						}
						if (iCashOrderFoodAddSub != null) {
							double weight = StringUtil.getDouble(s.toString());
							FoodEntity food = listFood.get(getPosition());
							food.setNum(weight);
							iCashOrderFoodAddSub.onAddFoodWeight(getPosition(), food, weight);
						}
					}
				}
			});
		}

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			int position = getPosition();
			FoodEntity food = listFood.get(position);
			switch (view.getId()) {
			case R.id.iv_item_cashOrder_addNum:
				if (listFood.get(position).isTypeWeight()) {
					food.setAddstatu(true);
				}
				if (iCashOrderFoodAddSub != null) {
					iCashOrderFoodAddSub.onAdd(position, food);
				}
				break;
			case R.id.iv_item_cashOrder_subNum:
				if (iCashOrderFoodAddSub != null) {
					iCashOrderFoodAddSub.onSub(position, food);
				}
				break;

			case R.id.iv_item_cashOrder_del:
				if (listFood.get(position).isTypeWeight()) {
					food.setAddstatu(false);
				}
				if (iCashOrderFoodAddSub != null) {
					iCashOrderFoodAddSub.onRemoveFoodWeight(position, food);
				}
				break;
			case R.id.btn_item_foodmanage_weight:
				if (iCashOrderFoodAddSub != null) {
					iCashOrderFoodAddSub.onWeight(position, food);
				}
				break;
			default:
				if (iOnItemClickListener != null) {
					iOnItemClickListener.onItemClick(view, null, position);
				}
			}
		}
	}

	private class CartViewHolder extends ViewHolder implements OnClickListener {
		private TextView tvName;
		private TextView tvPrice;

		private View viewAddNum;
		private ImageView ivAddNum;
		private ImageView ivSubNum;
		private TextView tvNum;

		private View viewAddweight;
		private EditText edtWeight;
		private ImageView ivWeightDel;
		private Button btnWeight;

		public CartViewHolder(View view, final ICashOrderFoodAddSub iCashOrderFoodAddSub) {
			super(view);
			// TODO Auto-generated constructor stub
			tvName = (TextView) view.findViewById(R.id.tv_item_foodmanage_foodname);
			tvPrice = (TextView) view.findViewById(R.id.tv_item_foodmanage_foodprice);

			viewAddNum = view.findViewById(R.id.rl_item_cashOrder_addnum);
			ivAddNum = (ImageView) view.findViewById(R.id.iv_item_cashOrder_addNum);
			ivSubNum = (ImageView) view.findViewById(R.id.iv_item_cashOrder_subNum);
			tvNum = (TextView) view.findViewById(R.id.tv_item_cashOrder_num);

			viewAddweight = view.findViewById(R.id.rl_item_cashOrder_addweight);
			edtWeight = (EditText) view.findViewById(R.id.tv_item_cashOrder_weight);
			ivWeightDel = (ImageView) view.findViewById(R.id.iv_item_cashOrder_del);
			btnWeight = (Button) view.findViewById(R.id.btn_item_foodmanage_weight);

			ivAddNum.setOnClickListener(this);
			ivSubNum.setOnClickListener(this);
			ivWeightDel.setOnClickListener(this);
			btnWeight.setOnClickListener(this);
			StringUtil.getIntFomat(edtWeight, ConfigUtil.Weight_Size);
			edtWeight.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					// TODO Auto-generated method stub
				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if (s.length() > 0) {
						if (StringUtil.getDouble(s.toString()) > ValueFinal.MAX_NUM) {
							s.delete(s.length() - 1, s.length());
						}
						if (iCashOrderFoodAddSub != null) {
							double weight = StringUtil.getDouble(s.toString());
							FoodEntity food = listFood.get(getPosition());
							food.setNum(weight);
							iCashOrderFoodAddSub.onAddFoodWeight(getPosition(), food, weight);
						}
					}
				}
			});
		}

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			if (!CommonUtil.fastClick()) {
				return;
			}
			int position = getPosition();
			FoodEntity food = listFood.get(position);
			switch (view.getId()) {
			case R.id.iv_item_cashOrder_addNum:
				if (listFood.get(position).isTypeWeight()) {
					food.setAddstatu(true);
				}
				if (iCashOrderFoodAddSub != null) {
					iCashOrderFoodAddSub.onAdd(position, food);
				}
				break;
			case R.id.iv_item_cashOrder_subNum:
				if (iCashOrderFoodAddSub != null) {
					iCashOrderFoodAddSub.onSub(position, food);
				}
				break;

			case R.id.iv_item_cashOrder_del:
				if (listFood.get(position).isTypeWeight()) {
					food.setAddstatu(false);
				}
				if (iCashOrderFoodAddSub != null) {
					iCashOrderFoodAddSub.onRemoveFoodWeight(position, food);
				}
				break;
			case R.id.btn_item_foodmanage_weight:
				if (iCashOrderFoodAddSub != null) {
					iCashOrderFoodAddSub.onWeight(position, food);
				}
				break;
			default:
				if (iOnItemClickListener != null) {
					iOnItemClickListener.onItemClick(view, null, position);
				}
			}
		}

	}

	private class FootViewHolder extends ViewHolder {
		private TextView tvTag;

		public FootViewHolder(View view) {
			super(view);
			tvTag = (TextView) view.findViewById(R.id.tv_recycle_bottom_tag);
		}
	}

}