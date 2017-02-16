package com.shwm.freshmallpos.model.biz;

import android.net.Uri;

public interface IBusinessListener {
	/** 改店铺名 */
	void setBusinessName(String busniessname, IRequestListener<String> iRequestListener);

	/** 改店铺地址 */
	void setBusinessAddress(String address, double lat, double lng, IRequestListener<String> iRequestListener);

	/** 改店铺logo */
	void setBusinessIcon(Uri bitmapUri, IRequestListener<String> iRequestListener);

}
