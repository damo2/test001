package com.shwm.freshmallpos.model.biz;

public interface IRequestListener<T> {

	void onPreExecute(int type);

	void onSuccess(T t);

	void onFail(int statu, Exception exception);

}
