package com.shwm.freshmallpos.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.text.TextUtils;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.base.ApplicationMy;
import com.shwm.freshmallpos.been.ClassesEntity;
import com.shwm.freshmallpos.manage.FoodListData;
import com.shwm.freshmallpos.model.biz.IDeleteContentListener;
import com.shwm.freshmallpos.model.biz.IFoodListener;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.model.biz.OnClassesListener;
import com.shwm.freshmallpos.model.biz.OnDeleteContentListenter;
import com.shwm.freshmallpos.model.biz.OnFoodListener;
import com.shwm.freshmallpos.net.ContentTypeUtil;
import com.shwm.freshmallpos.view.IClassesManageView;

/**
 * 分类管理
 * 
 * @author wr 2016-12-13
 */
public class MClassesManagePresenter extends MBasePresenter<IClassesManageView> {
	private IClassesManageView iView;
	private IFoodListener iFoodListener;
	private OnClassesListener classesListener;
	private IDeleteContentListener iDeleteContentListener;
	private List<ClassesEntity> listClasses = new ArrayList<ClassesEntity>();
	private int content_type = ContentTypeUtil.TYPE_Classes;

	public MClassesManagePresenter(IClassesManageView iClassesManageView) {
		this.iView = iClassesManageView;
		this.iFoodListener = new OnFoodListener();
		this.iDeleteContentListener = new OnDeleteContentListenter();
		this.classesListener = new OnClassesListener();
	}

	/** 获取分类 */
	public void getClasses() {
		classesListener.getClassesAll(new IRequestListener<List<ClassesEntity>>() {
			@Override
			public void onSuccess(List<ClassesEntity> listclasses) {
				// TODO Auto-generated method stub
				iView.dismissDialogProgress();
				listClasses = listclasses;
				iView.showClasses(listClasses);
				FoodListData.setListClassesAll(listclasses);
			}

			@Override
			public void onPreExecute(int type) {
				// TODO Auto-generated method stub
				iView.showDialogProgress();
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				iView.dismissDialogProgress();
			}
		});
	}

	public void deleteClasses(String ids) {
		iDeleteContentListener.deleteClasses(content_type, ids, new IRequestListener<HashMap<String, Object>>() {
			@Override
			public void onPreExecute(int type) {
				// TODO Auto-generated method stub
				iView.showDialogProgress();
			}

			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				iView.dismissDialogProgress();
				getClasses();
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				iView.dismissDialogProgress();
				iView.showFailInfo(statu, exception);
			}
		});
	}

	public void addClasses(final String classesname) {
		// classesSup 是父类 classes 没有就是默认的第一级的添加
		final int pid = iView.getSupClassesId();
		final int lv = iView.getClassesLv();
		if (TextUtils.isEmpty(classesname)) {
			iView.toastInfo(ApplicationMy.getContext().getString(R.string.classesmanage_toast_noname));
			return;
		}
		classesListener.addClasses(pid, classesname, lv, new IRequestListener<Boolean>() {
			@Override
			public void onPreExecute(int type) {
				// TODO Auto-generated method stub
				iView.showDialogProgress();
			}

			@Override
			public void onSuccess(Boolean t) {
				// TODO Auto-generated method stub
				iView.dismissDialogProgress();
				iView.toastInfo("编辑成功");
				getClasses();
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				iView.dismissDialogProgress();
				iView.toastInfo("添加失败");
			}
		});

	}

	public void editClasses(final ClassesEntity classes, final String classesnameNew) {
		final int id = classes.getId();
		final int pid = classes.getSupId();
		final int classesLv = classes.getLv();
		if (TextUtils.isEmpty(classesnameNew)) {
			iView.toastInfo(ApplicationMy.getContext().getString(R.string.classesmanage_toast_noname));
			return;
		}
		classesListener.editClasses(id, pid, classesnameNew, classesLv, new IRequestListener<Boolean>() {
			@Override
			public void onPreExecute(int type) {
				// TODO Auto-generated method stub
				iView.showDialogProgress();
			}

			@Override
			public void onSuccess(Boolean t) {
				// TODO Auto-generated method stub
				iView.dismissDialogProgress();
				iView.toastInfo("编辑成功");
				getClasses();
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				iView.dismissDialogProgress();
				iView.toastInfo("编辑失败");
			}
		});
	}
}
