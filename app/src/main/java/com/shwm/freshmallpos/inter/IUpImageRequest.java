package com.shwm.freshmallpos.inter;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.net.Uri;

public interface IUpImageRequest {
	public HashMap<String, Object> setImgUploadPost(String url, HashMap<String, Object> hashmap, Bitmap bitmap);

	public HashMap<String, Object> setImgUploadPost(String url, HashMap<String, Object> hashmap, Uri uriBitmap);
}
