package com.shwm.freshmallpos.presenter;

import java.util.ArrayList;
import java.util.List;

import com.shwm.freshmallpos.been.OrderEntity;
import com.shwm.freshmallpos.model.biz.IOrderManagerListiner;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.model.biz.OnOrderManagerListiner;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.view.IMainOrderView;

public class MMainOrderPresenter extends MBasePresenter<IMainOrderView> {
	private String TAG = "MMainOrderPresenter";
	private IMainOrderView mView;
	private int pageType;
	private int page;
	private int pageThis;
	private String dayNo;
	private List<OrderEntity> listOrder;

	private IOrderManagerListiner iOrderManagerListiner;

	public MMainOrderPresenter(IMainOrderView mView) {
		// TODO Auto-generated constructor stub
		this.mView = mView;
		this.iOrderManagerListiner = new OnOrderManagerListiner();
	}

	/** 取订单 */
	public List<OrderEntity> getListOrder(int dayNearly) {
		pageType = mView.getPageType();
		UL.e(TAG, "pageType=" + pageType);
		if (pageType == ValueType.PAGE_DEFAULT) {
			pageThis = 1;
		}
		if (pageType == ValueType.PAGE_REFRESH) {
			pageThis = 1;
		}
		if (pageType == ValueType.PAGE_LOAD) {
			pageThis = page + 1;
		}

		iOrderManagerListiner.getOrderList(pageThis, dayNo, dayNearly, new IRequestListener<List<OrderEntity>>() {

			@Override
			public void onSuccess(List<OrderEntity> list) {
				// TODO Auto-generated method stub
				if (pageType == ValueType.PAGE_DEFAULT) {
					mView.dismissDialogProgress();
				}
				if (pageType == ValueType.PAGE_REFRESH) {
					mView.refreshCancel();
				}

				if (listOrder == null) {
					listOrder = new ArrayList<OrderEntity>();
				}
				if (list != null) {
					int size = list.size();
					if (size > 0) {
						page = pageThis;
					}
					if (pageType == ValueType.PAGE_DEFAULT || pageType == ValueType.PAGE_REFRESH) {
						listOrder.clear();
					}
					if (pageType == ValueType.PAGE_LOAD) {
						if (size > 0) {
							mView.setLoadType(ValueType.LOAD_OVER);
						} else {
							mView.setLoadType(ValueType.LOAD_OVERALL);
						}
					}
					listOrder.addAll(list);
					mView.showListOrder(listOrder);
					list.clear();
				}
			}

			@Override
			public void onPreExecute(int type) {
				// TODO Auto-generated method stub
				if (pageType == ValueType.PAGE_DEFAULT) {
					// mView.showDialogProgress();
				}
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				if (pageType == ValueType.PAGE_DEFAULT) {
					mView.dismissDialogProgress();
				}
				if (pageType == ValueType.PAGE_REFRESH) {
					mView.refreshCancel();
				}
				if (pageType == ValueType.PAGE_LOAD) {
					mView.setLoadType(ValueType.LOAD_FAIL);
				}
			}
		});
		return listOrder;
	}
}
