package com.shwm.freshmallpos.presenter;

import java.util.HashMap;

import android.net.Uri;
import android.text.TextUtils;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.been.ClassesEntity;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.inter.IAsyncListener;
import com.shwm.freshmallpos.model.biz.IFoodEditListener;
import com.shwm.freshmallpos.model.biz.IFoodListener;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.model.biz.OnFoodEditListener;
import com.shwm.freshmallpos.model.biz.OnFoodListener;
import com.shwm.freshmallpos.net.MyAsyncTaskUtil;
import com.shwm.freshmallpos.request.FoodManageRequests;
import com.shwm.freshmallpos.util.BitmapUtil;
import com.shwm.freshmallpos.util.ExceptionUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;
import com.shwm.freshmallpos.view.IFoodEditView;
import com.shwm.freshmallpos.base.ApplicationMy;

public class MFoodEditPresenter extends MBasePresenter<IFoodEditView> {
	private static final String TAG = "MFoodEditPresenter";
	private IFoodEditView mView;
	private IFoodEditListener iFoodEditListener;
	private IFoodListener iGetClassesAndFoodListener;

	private FoodEntity food;
	private int editType;
	private Uri bitmapUri;
	private int foodId;
	private String name = "", typeTag = "", unit = "", comefrom = "", desc = "", eatIds = "", eatIdsDel = "", barcode = "", img = "";
	private double price = 0, priceMember = 0, numsum = 0;
	private int typeWeight = 0;

	public MFoodEditPresenter(IFoodEditView iView) {
		this.mView = iView;
		iFoodEditListener = new OnFoodEditListener();
		iGetClassesAndFoodListener = new OnFoodListener();
		editType = iView.getEditType();
	}

	public void addFoodRequest() {
		getViewInfo();
		if (isFoodSure()) {
			iFoodEditListener.addFood(name, barcode, typeTag, price, priceMember, unit, numsum, typeWeight, comefrom, desc, eatIds,
					eatIdsDel, new IRequestListener<HashMap<String, Object>>() {
						@Override
						public void onPreExecute(int type) {
							// TODO Auto-generated method stub
							mView.showDialogProgress();
						}

						@Override
						public void onSuccess(HashMap<String, Object> hashmap) {
							// TODO Auto-generated method stub
							mView.dismissDialogProgress();
							int code = StringUtil.getInt(hashmap.get(ValueKey.RESULT_CODE));
							String msg = StringUtil.getString(hashmap.get(ValueKey.RESULT_MSG));
							foodId = StringUtil.getInt(hashmap.get(ValueKey.ItemId));
							if (code == ValueStatu.SUCCESS) {
								bitmapUri = mView.getFoodImgBitmapUri();
								if (bitmapUri != null) {
									upFoodImgRequest(foodId, 1);
								} else {
									food = getFoodNew();
									mView.editSuccess(food);
								}
							} else {
								mView.showFailInfo(code, new ExceptionUtil(msg));
							}
						}

						@Override
						public void onFail(int statu, Exception exception) {
							// TODO Auto-generated method stub
							mView.dismissDialogProgress();
							mView.showFailInfo(statu, exception);
						}
					});
		}
	}

	public void editFoodRequest() {
		food = mView.getFoodEdit();
		if (food != null) {
			foodId = food.getId();
			img = food.getImg();
		}
		getViewInfo();

		if (isFoodSure()) {
			iFoodEditListener.editFood(foodId, name, barcode, typeTag, price, priceMember, unit, numsum, typeWeight, comefrom, desc,
					eatIds, eatIdsDel, new IRequestListener<HashMap<String, Object>>() {
						@Override
						public void onPreExecute(int type) {
							// TODO Auto-generated method stub
							mView.showDialogProgress();
						}

						@Override
						public void onSuccess(HashMap<String, Object> hashmap) {
							// TODO Auto-generated method stub
							mView.dismissDialogProgress();
							int code = StringUtil.getInt(hashmap.get(ValueKey.RESULT_CODE));
							String msg = StringUtil.getString(hashmap.get(ValueKey.RESULT_MSG));
							if (code == ValueStatu.SUCCESS) {
								food = getFoodNew();
								mView.editSuccess(food);
							} else {
								mView.showFailInfo(code, new ExceptionUtil(msg));
							}
						}

						@Override
						public void onFail(int statu, Exception exception) {
							// TODO Auto-generated method stub
							mView.dismissDialogProgress();
							mView.showFailInfo(statu, exception);
						}
					});
		}
	}

	private void getViewInfo() {
		typeWeight = mView.getFoodWeightType();
		editType = mView.getEditType();
		name = mView.getFoodName();
		ClassesEntity classes = mView.getFoodClasses();
		if (classes != null) {
			typeTag = classes.getName();
		}
		unit = mView.getFoodUnit();
		comefrom = mView.getFoodFrom();
		desc = mView.getFoodDesc();
		price = mView.getFoodPrice();
		priceMember = mView.getFoodPriceMember();
		numsum = mView.getFoodNumsum();
		barcode = mView.getFoodCode();
	}

	private FoodEntity getFoodNew() {
		food = new FoodEntity();
		food.setId(foodId);
		food.setName(name);
		food.setImg(img);
		food.setClasses(mView.getFoodClasses());
		food.setPrice(price);
		food.setPriceMember(priceMember);
		food.setUnit(unit);
		food.setNumsum(numsum);
		food.setTypeWeight(typeWeight);
		food.setFrom(comefrom);
		food.setDesc(desc);
		food.setBarcode(barcode);
		return food;
	}

	private boolean isFoodSure() {
		if (TextUtils.isEmpty(name)) {
			mView.toastInfo(ApplicationMy.getContext().getString(R.string.foodedit_toast_noname));
			return false;
		} else if (price <= 0) {
			mView.toastInfo(ApplicationMy.getContext().getString(R.string.foodedit_toast_noprice));
			return false;
		} else if (TextUtils.isEmpty(unit)) {
			mView.toastInfo(ApplicationMy.getContext().getString(R.string.foodedit_toast_nounit));
			return false;
		}
		return true;
	}

	public void upFoodImg() {
		food = mView.getFoodEdit();
		if (food != null) {
			foodId = food.getId();
		}
		bitmapUri = mView.getFoodImgBitmapUri();
		if (bitmapUri != null) {
			upFoodImgRequest(foodId, 1);
		} else {
			UL.e(TAG, "bitmapUri is null");
		}
	}

	/***
	 * 添加商品图片
	 * @param itemId
	 *            返回的商品Id
	 * @param index
	 *            第几张图片 从第1开始 ，默认1
	 * @param bitmap
	 */
	private void upFoodImgRequest(final int itemId, final int index) {
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				mView.dismissDialogProgress();
				getFoodByFoodId(true);
				mView.upFoodImgBitmapSuc();
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				mView.showDialogProgress();
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				mView.dismissDialogProgress();
				mView.showFailInfo(statu, exception);
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return FoodManageRequests.addFoodImg(itemId, index, BitmapUtil.uriToBitmap(bitmapUri));
			}
		}).execute();
	}

	public void getFoodByFoodId(final boolean isGetImg) {
		int foodId = mView.getFoodEditId();
		iGetClassesAndFoodListener.getFoodByFoodId(foodId, new IRequestListener<FoodEntity>() {
			@Override
			public void onPreExecute(int type) {
				// TODO Auto-generated method stub
				mView.showDialogProgress();
			}

			@Override
			public void onSuccess(FoodEntity food) {
				// TODO Auto-generated method stub
				mView.dismissDialogProgress();
				if (!isGetImg) {
					mView.showEditInfo(food);
				}
				if (isGetImg) {
					mView.editSuccess(food);
				}
				MFoodEditPresenter.this.food = food;
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				mView.toastInfo(exception.toString());
				mView.dismissDialogProgress();
			}
		});
	}

}
