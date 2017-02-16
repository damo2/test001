package com.shwm.freshmallpos.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.net.AppConfig;
import com.shwm.freshmallpos.net.HttpUtil;
//https://github.com/nostra13/Android-Universal-Image-Loader
// "http://site.com/image.png" // from Web
//  "file:///mnt/sdcard/image.png" // from SD card
//  "file:///mnt/sdcard/video.mp4" // from SD card (video thumbnail)
//  "content://media/external/images/media/13" // from content provider
//  "content://media/external/video/media/13" // from content provider (video thumbnail)
//  "assets://image.png" // from assets
//  "drawable://" + R.drawable.img // from drawables (non-9patch images)

public class ImageLoadUtil {
	private static final int imgLoadRes = R.drawable.image_default;
	private static final int imgEmptyRes = R.drawable.image_empty;
	private static final int imgFailRes = R.drawable.image_error;

	private static final int imgUserFailRes = R.drawable.icon_nickname;
	private static final int imgUserEmptyRes = R.drawable.image_empty;

	public static void dispalyImg(ImageView iv, String imgurl, int imgUrlDefault) {
		dispalyImg(iv, imgurl, imgUrlDefault, imgUrlDefault, imgUrlDefault, 0);
	}

	public static void dispalyImg(ImageView iv, String imgurl, int imgLoadRes, int imgEmptyRes, int imgFailRes, int round) {
		if (iv == null)
			return;
		ImageLoader.getInstance().displayImage(imgUrl(imgurl), iv,
				getOptionsImg(imgLoadRes, imgEmptyRes, imgFailRes, round));
	}

	public static void dispalyImgSD(ImageView iv, String imgdir) {
		String imageUri = "file:///" + imgdir;
		ImageLoader.getInstance().displayImage(imageUri, iv);
	}

	public static void dispalyImgBitmap(ImageView iv, String imgUri) {
		String imageUri = "drawable:///" + imgUri;
		ImageLoader.getInstance().displayImage(imageUri, iv);
	}

	public static void displayImageLocal(String uri, ImageView imageView, DisplayImageOptions options) {
		ImageLoader.getInstance().displayImage(uri, imageView, options);
	}

	public static void displayImage(ImageView imageView, String uri, DisplayImageOptions options) {
		if (imageView == null) {
			UL.e("UtilImageLoad", "imageView is null");
			return;
		}
		if (TextUtils.isEmpty(uri)) {
			if (options == getOptionsImgRoundedUser()) {
				imageView.setImageResource(imgUserEmptyRes);
				return;
			}
			imageView.setImageResource(imgUserEmptyRes);
		} else {
			if (!uri.contains("http://")) {
				uri = HttpUtil.getImgURl(uri);
			}
			ImageLoader.getInstance().displayImage(uri, imageView, options);
		}
	}

	/** 图片加载第一次显示监听器 */
	public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// 是否第一次显示
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// 图片淡入效果
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	/** 商品图片 */
	public static DisplayImageOptions getOptionsImgFood() {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(imgLoadRes)// 设置图片下载期间显示的图片
				.showImageForEmptyUri(imgEmptyRes) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(imgFailRes) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(12)) // 设置圆角图片的角度
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}

	/** 显示商品图片的配置(圆角) */
	public static DisplayImageOptions getOptionsImgRounded() {
		return getOptionsImg(imgLoadRes, imgEmptyRes, imgFailRes, 20);
	}

	/** 显示头像的配置(圆形) */
	public static DisplayImageOptions getOptionsImgRoundedUser() {
		return getOptionsImg(imgLoadRes, imgUserEmptyRes, imgUserFailRes, 150);
	}

	/**
	 * 图片类型设置
	 * 
	 * @param imgLoadRes
	 *            加载中
	 * @param imgEmptyRes
	 *            Uri为空
	 * @param imgFailRes
	 *            图片加载或解码错误
	 * @param round
	 *            圆角图片的角度
	 * @return
	 */
	public static DisplayImageOptions getOptionsImg(int imgLoadRes, int imgEmptyRes, int imgFailRes, int round) {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(imgLoadRes)// 设置图片下载期间显示的图片
				.showImageForEmptyUri(imgEmptyRes) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(imgFailRes) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(round)) // 设置圆角图片的角度
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}

	public static void onClearMemoryClick(View view) {
		// Toast.makeText(this, 清除内存缓存成功, Toast.LENGTH_SHORT).show();
		ImageLoader.getInstance().clearMemoryCache(); // 清除内存缓存
	}

	public static void onClearDiskClick(View view) {
		// Toast.makeText(this, 清除本地缓存成功, Toast.LENGTH_SHORT).show();
		ImageLoader.getInstance().clearDiskCache(); // 清除本地缓存
	}

	// data/data/com.shwm.freshmallmanage/...
	public static final String getDirName(Context context, String dirname) {
		return context.getExternalCacheDir().getPath() + "/" + dirname;
	}

	private static String imgUrl(String imgurl) {
		if (!imgurl.contains("http")) {
			imgurl = AppConfig.SERVER_IP + "/" + imgurl;
		}
		return imgurl;
	}
}
