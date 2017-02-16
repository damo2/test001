package com.shwm.freshmallpos.model.biz;

public interface IDeleteContentListener {
	/** 删除 */
	void deleteContent(int content_type, String ids, IRequestListener iRequestListener);
	void deleteClasses(int content_type, String ids, IRequestListener iRequestListener);
}
