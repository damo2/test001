package com.shwm.freshmallpos.model.biz;

import java.util.HashMap;

import com.shwm.freshmallpos.inter.IAsyncListener;
import com.shwm.freshmallpos.net.MyAsyncTaskUtil;
import com.shwm.freshmallpos.request.FoodManageRequests;

public class OnFoodEditListener implements IFoodEditListener {
	@Override
	public void addFood(final String name, final String barcode, final String typeTag, final double price, final double priceMember,
			final String unit, final double numSum, final int type, final String comefrom, final String desc, final String eatIds,
			final String eatIdsDel, final IRequestListener iRequestListener) {
		// TODO Auto-generated method stub
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				if (iRequestListener != null) {
					iRequestListener.onSuccess(hashmap);
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
			public void onFail(int requestStatu, Exception exception) {
				// TODO Auto-generated method stub
				if (iRequestListener != null) {
					iRequestListener.onFail(requestStatu, exception);
				}
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return FoodManageRequests.setAddFood(name, barcode, typeTag, price, priceMember, unit, numSum, type, comefrom, desc,
						eatIds, eatIdsDel);
			}
		}).execute();
	}

	@Override
	public void editFood(final int foodId, final String name, final String barcode, final String typeTag, final double price,
			final double priceMember, final String unit, final double numSum, final int type, final String comefrom, final String desc,
			final String eatIds, final String eatIdsDel, final IRequestListener iRequestListener) {
		// TODO Auto-generated method stub
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				if (iRequestListener != null) {
					iRequestListener.onSuccess(hashmap);
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
			public void onFail(int requestStatu, Exception exception) {
				// TODO Auto-generated method stub
				if (iRequestListener != null) {
					iRequestListener.onFail(requestStatu, exception);
				}
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return FoodManageRequests.setEditFood(foodId, name, barcode, typeTag, price, priceMember, unit, numSum, type, comefrom,
						desc, eatIds, eatIdsDel);
			}
		}).execute();
	}

}
