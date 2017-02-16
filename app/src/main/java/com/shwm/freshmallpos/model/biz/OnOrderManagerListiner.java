package com.shwm.freshmallpos.model.biz;

import java.util.HashMap;
import java.util.List;

import com.shwm.freshmallpos.been.OrderEntity;
import com.shwm.freshmallpos.inter.IAsyncListener;
import com.shwm.freshmallpos.net.MyAsyncTaskUtil;
import com.shwm.freshmallpos.request.OrderRequest;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueFinal;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

public class OnOrderManagerListiner implements IOrderManagerListiner {

	@Override
	public void getOrderList(final int page, final String dayNo, final int dayNearly,
			final IRequestListener<List<OrderEntity>> iRequestListener) {
		// TODO Auto-generated method stub
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				int code = StringUtil.getInt(hashmap.get(ValueKey.RESULT_CODE));
				if (code == ValueStatu.SUCCESS) {
					List<OrderEntity> list = (List<OrderEntity>) hashmap.get(ValueKey.LISTORDER);
					if (iRequestListener != null) {
						iRequestListener.onSuccess(list);
					}
				} else {
					if (iRequestListener != null) {
						iRequestListener.onFail(ValueStatu.FAIL, ValueFinal.getExceptionFailInfo());
					}
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
				return OrderRequest.getListOrder(page, dayNo, dayNearly);
			}
		}).execute();
	}

	@Override
	public void getOrderDetail(final int orderId, final IRequestListener<HashMap<String, Object>> iRequestListener) {
		// TODO Auto-generated method stub
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				int code = StringUtil.getInt(hashmap.get(ValueKey.RESULT_CODE));
				if (code == ValueStatu.SUCCESS) {
					if (iRequestListener != null) {
						iRequestListener.onSuccess(hashmap);
					}
				} else {
					if (iRequestListener != null) {
						iRequestListener.onFail(ValueStatu.FAIL, ValueFinal.getExceptionFailInfo());
					}
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
				return OrderRequest.getOrderDetail(orderId);
			}
		}).execute();
	}

	@Override
	public void OrderRefund(final String orderNo, final IRequestListener iRequestListener) {
		// TODO Auto-generated method stub
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				int code = StringUtil.getInt(hashmap.get(ValueKey.RESULT_CODE));
				if (code == ValueStatu.SUCCESS) {
					if (iRequestListener != null) {
						iRequestListener.onSuccess(hashmap);
					}
				} else {
					if (iRequestListener != null) {
						iRequestListener.onFail(ValueStatu.FAIL, ValueFinal.getExceptionFailInfo());
					}
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
				return OrderRequest.OrderRefund(orderNo);
			}
		}).execute();
	}

}
