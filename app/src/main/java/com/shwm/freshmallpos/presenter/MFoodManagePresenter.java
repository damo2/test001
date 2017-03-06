package com.shwm.freshmallpos.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.base.ApplicationMy;
import com.shwm.freshmallpos.been.ClassesEntity;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.inter.IAsyncListener;
import com.shwm.freshmallpos.manage.FoodListData;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.model.biz.OnClassesListener;
import com.shwm.freshmallpos.model.biz.OnFoodListener;
import com.shwm.freshmallpos.net.ContentTypeUtil;
import com.shwm.freshmallpos.net.MyAsyncTaskUtil;
import com.shwm.freshmallpos.request.DeleteRequest;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueFinal;
import com.shwm.freshmallpos.value.ValueFuhao;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.view.IFoodManageView;

/**
 * 商品管理
 * 
 * @author wr 2016-12-1
 */
public class MFoodManagePresenter extends MBasePresenter<IFoodManageView> {
	private static final String TAG = "MFoodManagePresenter";
	private IFoodManageView mView;
	private OnFoodListener onFoodListener;
	private OnClassesListener classesListener;
	/** 当前类型 */
	private int classesCurrentPosition;
	private ClassesEntity classesCurrent;
	private int classesCurrentId;
	/** 当前商品列表 */
	private List<FoodEntity> listFoodCurrent;
	private static List<ClassesEntity> listClassesCurrent;

	private int content_Type = ContentTypeUtil.TYPE_Food;
	private List<FoodEntity> listChoose;
	private String ids;

	public MFoodManagePresenter(IFoodManageView iFoodManageView) {
		// TODO Auto-generated constructor stub
		this.mView = iFoodManageView;
		onFoodListener = new OnFoodListener();
		classesListener = new OnClassesListener();
	}

	public void getClassesListByData() {
		if (FoodListData.getListClassesAllOne() != null) {
			listClassesCurrent = FoodListData.getListClassesAllOne();
			mView.showClasses(listClassesCurrent);
		}
	}

	public void getClassesList() {
		classesListener.getClassesAll(new IRequestListener<List<ClassesEntity>>() {
			@Override
			public void onSuccess(List<ClassesEntity> listClasses) {
				// TODO Auto-generated method stub
				FoodListData.setListClassesAll(listClasses);
				// 先保存类型列表 再通知显示类型
				listClassesCurrent = FoodListData.getListClassesAllOne();
				mView.showClasses(listClassesCurrent);
			}

			@Override
			public void onPreExecute(int type) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				mView.showFailInfo(statu, exception);
			}
		});
	}

	// 如果点击当前类型 都会取当前类型
	public void getFoodListByClasses() {
		int pageCurrent = 0;
		int pageType;
		listFoodCurrent = new ArrayList<FoodEntity>();
		classesCurrentPosition = mView.getClassesPosion();
		pageType = mView.getPageType();// 刷新、加载、default
		if (listClassesCurrent != null && classesCurrentPosition < listClassesCurrent.size()) {
			classesCurrent = listClassesCurrent.get(classesCurrentPosition);
		}
		// 取不到当前类型不执行
		if (classesCurrent == null) {
			return;
		}
		classesCurrentId = classesCurrent.getId();// 选中类型Id
		pageCurrent = FoodListData.getPageByClassesId(classesCurrentId, pageCurrent);
		listFoodCurrent = FoodListData.getListFoodByClassesId(classesCurrentId);
		if (listFoodCurrent == null) {
			listFoodCurrent = new ArrayList<FoodEntity>();
		}
		if (pageType == ValueType.PAGE_DEFAULT) { // 点击类型时 取不到保存的就刷新
			if (listFoodCurrent != null && listFoodCurrent.size() > 0) {
				UL.e(TAG, "listFoodCurrent is exit  return listFoodCurrent");
				for (FoodEntity food : listFoodCurrent) {
					double num = FoodListData.getNumCartByFoodId(food.getId());
					food.setNum(num);
				}
				if (mView != null) {
					mView.showFoods(listFoodCurrent);
				}
				return;
			} else {
				pageCurrent = 1;
			}
		}
		if (pageType == ValueType.PAGE_REFRESH) {
			pageCurrent = 1;
		} else if (pageType == ValueType.PAGE_LOAD) {
			pageCurrent++;
		}
		getFoodListByClassesRequest(pageCurrent, pageType);
	}

	/** 确定删除选中商品吗？ */
	public void sureDelChooseFood() {
		StringBuffer strBuffer = new StringBuffer();
		StringBuffer idsBuffer = new StringBuffer();
		String info = "";
		listChoose = FoodListData.getListChooseAll();
		if (listChoose != null && listChoose.size() > 0) {
			for (FoodEntity food : listChoose) {
				strBuffer.append(food.getName() + ValueFuhao.FUHAO_denghao);
				idsBuffer.append(food.getId() + ValueFuhao.FUHAO_douhao);
			}
			info = strBuffer.toString();
			ids = idsBuffer.toString();
			if (info.length() > 1) {
				info = info.substring(0, info.length() - 1);
			}
			String strFail = ApplicationMy.getContext().getResources().getString(R.string.foodmanage_delsure);
			info = String.format(strFail, info);
			mView.showDialogDel(info);
		} else {
			ids = "";
			mView.toastInfo(ApplicationMy.getContext().getString(R.string.foodmanage_nochoose));
		}
	}

	public void delChooseFood() {
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				int code = (Integer) hashmap.get(ValueKey.RESULT_CODE);
				if (code == ValueStatu.SUCCESS) {
					clearChooseFood();
					mView.delSuccess();
				} else {
					mView.showFailInfo(code, ValueFinal.getExceptionFailInfo());
				}
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				mView.showFailInfo(statu, exception);
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return DeleteRequest.Delete(content_Type, ids);
			}
		}).execute();
	}

	protected void clearChooseFood() {
		Iterator<FoodEntity> iterableFoods = listFoodCurrent.iterator();
		while (iterableFoods.hasNext()) {
			FoodEntity food = iterableFoods.next();
			for (FoodEntity foodEntity : listChoose) {
				if (foodEntity.getId() == food.getId()) {
					iterableFoods.remove();
				}
			}
		}
		mView.showFoods(listFoodCurrent);
		FoodListData.clearChoose();
		FoodListData.clearCartNum();
		FoodListData.clearFoodSparse();
	}

	private void getFoodListByClassesRequest(final int pageCurrent, final int pageType) {
		if (classesCurrentPosition >= listClassesCurrent.size()) {
			return;
		}
		onFoodListener.getFoodByClasses(listClassesCurrent.get(classesCurrentPosition), pageCurrent, pageType,
				new IRequestListener<List<FoodEntity>>() {
					@Override
					public void onPreExecute(int type) {
						// TODO Auto-generated method stub
						if (pageType == ValueType.PAGE_DEFAULT) {
							mView.showDialogProgress();
						}
						if (pageType == ValueType.PAGE_LOAD) {
							mView.setLoadType(ValueType.LOAD_LOADING);
						}
					}

					@Override
					public void onSuccess(List<FoodEntity> listfoods) {
						// TODO Auto-generated method stub
						if (pageType == ValueType.PAGE_DEFAULT) {
							mView.dismissDialogProgress();
						}
						if (pageType == ValueType.PAGE_REFRESH) {
							mView.dismissRefresh();
							listFoodCurrent.clear();
						}
						if (pageType == ValueType.PAGE_LOAD) {
							if (listfoods != null && listfoods.size() > 0) {
								mView.setLoadType(ValueType.LOAD_OVER);
							} else {
								mView.setLoadType(ValueType.LOAD_OVERALL);
							}
						}
						if (listfoods != null && listfoods.size() > 0) {
							for (FoodEntity foods : listfoods) {
								double num = FoodListData.getNumCartByFoodId(foods.getId());
								foods.setNum(num);
								foods.setClasses(classesCurrent);// 给商品添加类型
							}
							if (!(pageType == ValueType.PAGE_LOAD && listfoods.size() == 0)) {
								listFoodCurrent.addAll(listfoods);
							}
							if (pageType == ValueType.PAGE_LOAD && listClassesCurrent.size() > 0) {
								FoodListData.setPageByClassesId(classesCurrentId, pageCurrent);
							}
							if (pageType == ValueType.PAGE_REFRESH || pageType == ValueType.PAGE_DEFAULT) {
								FoodListData.setPageByClassesId(classesCurrentId, 1);
							}
							FoodListData.setListFoodByClassesId(classesCurrentId, listFoodCurrent);
						}
						mView.showFoods(listFoodCurrent);
					}

					@Override
					public void onFail(int statu, Exception exception) {
						// TODO Auto-generated method stub
						// 加载失败提示
						mView.showFailInfo(statu, exception);
						if (pageType == ValueType.PAGE_DEFAULT) {
							mView.dismissDialogProgress();
						}
						if (pageType == ValueType.PAGE_REFRESH) {
							mView.dismissRefresh();
							listFoodCurrent.clear();
						}
						if (pageType == ValueType.PAGE_LOAD) {
							mView.setLoadType(ValueType.LOAD_FAIL);
						}
					}
				});
	}
}
