package com.shwm.freshmallpos.model.biz;

import java.util.List;

import com.shwm.freshmallpos.been.ClassesEntity;

public interface IClassesListener {
	/** 得到所有分类 */
	void getClassesAll(IRequestListener<List<ClassesEntity>> iRequestListener);
	
	void addClasses(int pid, String className, int classLv, IRequestListener<Boolean> iRequestListener);
	
	void editClasses(int id, int pid, String className, int classLv, IRequestListener<Boolean> iRequestListener);
}
