package com.shwm.freshmallpos.view;

import java.util.List;

import com.shwm.freshmallpos.been.ClassesEntity;

public interface IClassesManageView extends IBaseView {
	/** 类型 */
	int getEditType();

	void showClasses(List<ClassesEntity> listClassesAll);

	/** 第几级类别 */
	int getClassesLv();

	/** 父类Id */
	int getSupClassesId();
}
