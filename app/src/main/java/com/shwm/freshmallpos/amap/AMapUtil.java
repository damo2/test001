package com.shwm.freshmallpos.amap;

import android.graphics.Bitmap;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

public class AMapUtil {
	public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
		LatLng latLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
		return latLng;
	}

	public static LatLng convertToLatLng(AMapLocation aMapLocation) {
		LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
		return latLng;
	}

	public static LatLonPoint convertToLatLonPoint(LatLng latLng) {
		LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
		return latLonPoint;
	}

	// view 转bitmap
	public static Bitmap convertViewToBitmap(View view) {
		view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}
}
