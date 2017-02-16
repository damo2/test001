package com.shwm.freshmallpos.manage;

import com.shwm.freshmallpos.inter.IUpImageRequest;
import com.shwm.freshmallpos.net.ImageUploadClientRequest;

public class UpImageRequestFactory {
	private static IUpImageRequest upImageRequest;

	private UpImageRequestFactory() {
	}

	/** 上传图片 */
	public static IUpImageRequest getUpImageRequest() {
		if (upImageRequest == null) {
			synchronized (UpImageRequestFactory.class) {
				if (upImageRequest == null) {
					upImageRequest = new ImageUploadClientRequest();
				}
			}
		}
		return upImageRequest;
	}
}
