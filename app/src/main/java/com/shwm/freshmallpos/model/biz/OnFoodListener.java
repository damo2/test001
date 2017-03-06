package com.shwm.freshmallpos.model.biz;

import java.util.HashMap;
import java.util.List;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.base.ApplicationMy;
import com.shwm.freshmallpos.been.ClassesEntity;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.inter.IAsyncListener;
import com.shwm.freshmallpos.net.MyAsyncTaskUtil;
import com.shwm.freshmallpos.request.FoodManageRequests;
import com.shwm.freshmallpos.request.FoodRequest;
import com.shwm.freshmallpos.util.ExceptionUtil;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

public class OnFoodListener implements IFoodListener {
	private static final String TAG = "OnGetClassesAndFoodListener";
	boolean done = false;
	MyAsyncTaskUtil mAsyncTaskClassesAll;


	@Override
	public void getFoodByClasses(ClassesEntity classes, int page, int pageType, IRequestListener<List<FoodEntity>> iRequestListener) {
		// TODO Auto-generated method stub
		if (classes == null) {
			return;
		}
		getFoodByClassesRequest(classes, page, pageType, iRequestListener);
	}

	@Override
	public void getFoodByLike(String foodnamelike, int page, int pageType, IRequestListener<List<FoodEntity>> iRequestListener) {
		// TODO Auto-generated method stub
		getFoodByLikeReqest(foodnamelike, page, pageType, iRequestListener);
	}

	@Override
	public void getFoodByCode(String code, IRequestListener<FoodEntity> iRequestListener) {
		// TODO Auto-generated method stub
		getFoodByCodeRequest(code, iRequestListener);
	}


	private void getFoodByClassesRequest(final ClassesEntity classes, final int page, final int pageType,
			final IRequestListener<List<FoodEntity>> iRequestListener) {
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				if (hashmap != null) {
					List<FoodEntity> listFood = (List<FoodEntity>) hashmap.get(ValueKey.LISTFOOD);
					if (iRequestListener != null) {
						iRequestListener.onSuccess(listFood);
					}
				} else {
					iRequestListener.onFail(ValueStatu.REQUEST_Execute, null);
				}
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				if (iRequestListener != null) {
					iRequestListener.onPreExecute(pageType);
				}
			}

			@Override
			public void onFail(int requestStatu, Exception exception) {
				// TODO Auto-generated method stub
				if (iRequestListener != null) {
					iRequestListener.onFail(requestStatu, exception);
				}
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return FoodRequest.getFoodsByClasses(page, classes.getName());
			}
		}).execute();
	}

	@Override
	public void getFoodByFoodId(final int foodId, final IRequestListener<FoodEntity> iRequestListener) {
		// TODO Auto-generated method stub
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				if (hashmap != null) {
					FoodEntity food = (FoodEntity) hashmap.get(ValueKey.FOOD);
					if (iRequestListener != null) {
						iRequestListener.onSuccess(food);
					}
				} else {
					iRequestListener.onFail(ValueStatu.REQUEST_Execute, null);
				}
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				if (iRequestListener != null) {
					iRequestListener.onPreExecute(0);
				}
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				if (iRequestListener != null) {
					iRequestListener.onFail(statu, exception);
				}
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return FoodManageRequests.getFoodDetail(foodId);
			}
		}).execute();
	}

	private void getFoodByLikeReqest(final String foodnameLike, final int page, final int pageType,
			final IRequestListener<List<FoodEntity>> iRequestListener) {
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				if (hashmap != null) {
					List<FoodEntity> listFood = (List<FoodEntity>) hashmap.get(ValueKey.LISTFOOD);
					if (iRequestListener != null) {
						iRequestListener.onSuccess(listFood);
					}
				} else {
					iRequestListener.onFail(ValueStatu.REQUEST_Execute, null);
				}
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				if (iRequestListener != null) {
					iRequestListener.onPreExecute(pageType);
				}
			}

			@Override
			public void onFail(int requestStatu, Exception exception) {
				// TODO Auto-generated method stub
				if (iRequestListener != null) {
					iRequestListener.onFail(requestStatu, exception);
				}
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return FoodManageRequests.getFoodByName(foodnameLike, page);
			}
		}).execute();
	}

	private void getFoodByCodeRequest(final String code, final IRequestListener<FoodEntity> iRequestListener) {
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				int code = (Integer) hashmap.get(ValueKey.RESULT_CODE);
				if (code == ValueStatu.SUCCESS) {
					FoodEntity food = (FoodEntity) hashmap.get(ValueKey.FOOD);
					if (iRequestListener != null) {
						iRequestListener.onSuccess(food);
					}
				} else {
					iRequestListener.onFail(code, new ExceptionUtil(ApplicationMy.getContext().getString(R.string.cashOrder_code_no)));
				}
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				if (iRequestListener != null) {
					iRequestListener.onPreExecute(-1);
				}
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				if (iRequestListener != null) {
					iRequestListener.onFail(statu, exception);
				}
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return FoodManageRequests.getFoodByCode(code);
			}
		}).execute();
	}
}
