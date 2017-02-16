package com.shwm.freshmallpos.model.biz;

import java.util.HashMap;
import java.util.List;

import com.shwm.freshmallpos.been.ClassesEntity;
import com.shwm.freshmallpos.inter.IAsyncListener;
import com.shwm.freshmallpos.net.MyAsyncTaskUtil;
import com.shwm.freshmallpos.request.ClassesRequest;
import com.shwm.freshmallpos.value.ValueStatu;

public class OnClassesListener implements IClassesListener {

	@Override
	public void getClassesAll(IRequestListener<List<ClassesEntity>> iRequestListener) {
		// TODO Auto-generated method stub
		getClasssesAllRequest(iRequestListener);
	}

	@Override
	public void addClasses(int pid, String className, int classLv, IRequestListener<Boolean> iRequestListener) {
		// TODO Auto-generated method stub
		addClassesRequest(pid, className, classLv, iRequestListener);
	}

	@Override
	public void editClasses(int id, int pid, String className, int classLv, IRequestListener<Boolean> iRequestListener) {
		// TODO Auto-generated method stub
		editClassesRequest(id, pid, className, classLv, iRequestListener);
	}

	private void addClassesRequest(final int pid, final String classesname, final int lv, final IRequestListener<Boolean> iRequestListener) {
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				iRequestListener.onSuccess(true);
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				iRequestListener.onPreExecute(-1);
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				iRequestListener.onFail(statu, exception);
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return ClassesRequest.addClasses(pid, classesname, lv);
			}
		}).execute();
	}

	private void editClassesRequest(final int id, final int pid, final String classesname, final int lv,
			final IRequestListener<Boolean> iRequestListener) {
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				iRequestListener.onSuccess(true);
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				iRequestListener.onPreExecute(-1);
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				iRequestListener.onFail(statu, exception);
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return ClassesRequest.editClasses(id, pid, classesname, lv);
			}
		}).execute();
	}

	private void getClasssesAllRequest(final IRequestListener<List<ClassesEntity>> iRequestListener) {
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				if (hashmap != null) {
					List<ClassesEntity> listClasses = (List<ClassesEntity>) hashmap.get("listClasses");
					iRequestListener.onSuccess(listClasses);
				} else {
					iRequestListener.onFail(ValueStatu.REQUEST_Execute, null);
				}
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				iRequestListener.onPreExecute(0);
			}

			@Override
			public void onFail(int requestStatu, Exception exception) {
				// TODO Auto-generated method stub
				iRequestListener.onFail(requestStatu, exception);
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return ClassesRequest.getClasses();
			}
		}).execute();
	}

}
