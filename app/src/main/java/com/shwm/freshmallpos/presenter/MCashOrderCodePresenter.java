package com.shwm.freshmallpos.presenter;

import android.text.TextUtils;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.inter.IOnCartListener;
import com.shwm.freshmallpos.manage.FoodListData;
import com.shwm.freshmallpos.model.biz.IFoodListener;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.model.biz.OnFoodListener;
import com.shwm.freshmallpos.view.ICashOrderCodeView;
import com.shwm.freshmallpos.base.ApplicationMy;

public class MCashOrderCodePresenter extends MBasePresenter<ICashOrderCodeView> {
	private ICashOrderCodeView mView;
	private IFoodListener iGetClassesAndFoodListener;
	private IOnCartListener iOnCartListener;// 通知ParentActivity 更新购物车

	public MCashOrderCodePresenter(ICashOrderCodeView mView, IOnCartListener iOnCartListener) {
		this.mView = mView;
		this.iGetClassesAndFoodListener = new OnFoodListener();
		this.iOnCartListener = iOnCartListener;
	}

	/** 通过条形码查询商品 */
	public void getFoodByCode() {
		final String code = mView.getCode();
		if (TextUtils.isEmpty(code)) {
			mView.toastInfo(ApplicationMy.getStringRes(R.string.cashOrder_code_null));
			return;
		}
		FoodEntity food = FoodListData.getFoodFromCartByBarcode(code);
		if (food != null) {
			String fromat1 = ApplicationMy.getStringRes(R.string.cashOrder_code_existCart);
			mView.toastInfo(String.format(fromat1, food.getName()));
			return;
		}

		iGetClassesAndFoodListener.getFoodByCode(code, new IRequestListener<FoodEntity>() {
			@Override
			public void onSuccess(FoodEntity t) {
				// TODO Auto-generated method stub
				// mView.dismissDialogProgress();
				if (t != null) {
					if (t.isTypeDefault()) {
						t.setNum(1);
					}
					mView.showDilogNum(t);
				}
				mView.showCodeSearch(code);
			}

			@Override
			public void onPreExecute(int type) {
				// TODO Auto-generated method stub
				// mView.showDialogProgress();
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				// mView.dismissDialogProgress();
				mView.showCodeSearch(code);
				mView.showFailInfo(statu, exception);
			}
		});
	}

	/** 添加到购物车 */
	public void addToCart(FoodEntity food) {
		FoodListData.addToCart(food);
		iOnCartListener.changeCartByChooseFood(food);
		String fromat1 = ApplicationMy.getStringRes(R.string.cashOrder_code_addCart);
		mView.toastInfo(String.format(fromat1, food.getName()));
	}

}
