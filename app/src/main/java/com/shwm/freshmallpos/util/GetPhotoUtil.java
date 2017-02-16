package com.shwm.freshmallpos.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

public class GetPhotoUtil {
	private String TAG = "GetPhotoUtil";
	// 拍照、从图库获取、剪切返回的Id
	/**
	 * 从图库获取
	 */
	public static final int PHOTO_REQUEST_IMAGE = 1;
	/**
	 * 拍照
	 */
	public static final int PHOTO_REQUEST_CAREMA = 2;
	private static final int PHOTO_REQUEST_CUT = 3;

	private Activity activity;
	private String pathDir;
	private String photoName;
	private File outputImage;// 创建File对象用于存储拍照的图片 SD卡根目录
	private File outputImageCrop;// 创建File对象用于裁剪的图片 SD卡根目录
	private Uri imageUri; // 图片路径
	private Uri imageUriCrop; // 图片路径
	private int size_width;
	private int size_height;
	private boolean isCrop;

	private OnPhotoListener onPhotoListener;

	public GetPhotoUtil(Activity activity) {
		this(activity, 0, 0, false);
	}

	public GetPhotoUtil(Activity activity, int size_width, int size_height) {
		this(activity, size_width, size_height, true);
	}

	/**
	 * @param activity
	 * @param size_width
	 *            裁剪宽
	 * @param size_height
	 *            裁剪高
	 * @param isCrop
	 */
	public GetPhotoUtil(Activity activity, int size_width, int size_height, boolean isCrop) {
		this.activity = activity;
		this.size_height = size_height;
		this.size_width = size_width;
		this.isCrop = isCrop;
		init();
	}

	private void init() {
		pathDir = SDPathUtil.getSDCardPrivateFilesDir(activity.getApplicationContext(), "photo");
	}

	/**
	 * 保存照片目录
	 * 
	 * @return
	 */
	public String getPhotoDir() {
		return pathDir;
	}

	public String getPhotoPathByName(String photoName) {
		return pathDir + "/" + photoName;
	}

	public void setOnPhotoListener(OnPhotoListener onPhotoListener) {
		this.onPhotoListener = onPhotoListener;
	}

	public void getPhoto(int type) {
		this.getPhoto(type, getImgName());
	}

	/**
	 * 从相册获取或拍照获取 type GetPhotoUtil.PHOTO_REQUEST_IMAGE、GetPhotoUtil.PHOTO_REQUEST_CAREMA
	 */
	public void getPhoto(int type, String photoName) {
		this.photoName = photoName;
		outputImage = new File(pathDir + "/" + photoName);
		outputImageCrop = new File(pathDir + "/" + "crop_" + photoName);
		switch (type) {
		case PHOTO_REQUEST_IMAGE:
			image();
			break;
		case PHOTO_REQUEST_CAREMA:
			photo();
			break;
		default:
			break;
		}
	}

	public static String getImgName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String nametime = format.format(new Date());
		String name = nametime + ".jpg";
		return name;
	}

	/*
	 * 从相册获取
	 */
	public void image() {
		// 激活系统图库，选择一张图片
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_IMAGE
		activity.startActivityForResult(intent, PHOTO_REQUEST_IMAGE);
	}

	/** 拍照获取 */
	private void photo() {
		try {
			if (outputImage.exists()) {
				outputImage.delete();
			}
			outputImage.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 将File对象转换为Uri并启动照相程序
		imageUri = Uri.fromFile(outputImage);
		// 激活相机
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
		activity.startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
	}

	/**
	 * 裁切图片 uri为选中图片返回的Uri imageUriCrop为把截取图片写入sd卡的Uri
	 */
	private void crop(Uri uri) {
		imageUriCrop = Uri.fromFile(outputImageCrop);
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例
		intent.putExtra("aspectX", size_width);
		intent.putExtra("aspectY", size_height);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", size_width);
		intent.putExtra("outputY", size_height);
		intent.putExtra("outputFormat", "JPEG");// 图片格式
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriCrop);
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
		activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	public void onPhotoResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// requestCode标示请求的标示 resultCode表示返回标识
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PHOTO_REQUEST_IMAGE) {
				// 从相册返回的数据
				if (data != null) {
					// 得到图片的全路径
					Uri uri = data.getData();
					if (isCrop) {
						crop(uri);
					} else {
						imageUri = uri;
						if (onPhotoListener != null) {
							onPhotoListener.resultBitmapUri(imageUri);
						} else {
							UL.d(TAG, "onPhotoListener=null");
						}
					}
				} else {
					Toast.makeText(activity.getApplicationContext(), "image_data == null", Toast.LENGTH_SHORT).show();
				}
			} else if (requestCode == PHOTO_REQUEST_CAREMA) {
				if (Uri.fromFile(outputImage) != null) {
					imageUri = Uri.fromFile(outputImage);
					if (isCrop) {
						crop(imageUri);
					} else {
						if (onPhotoListener != null) {
							onPhotoListener.resultBitmapUri(imageUri);
						} else {
							UL.d(TAG, "onPhotoListener=null");
						}
					}
				} else {
					Toast.makeText(activity.getApplicationContext(), "没有存储照片", Toast.LENGTH_SHORT).show();
				}
			} else if (requestCode == PHOTO_REQUEST_CUT) {
				// 从剪切图片返回的数据
				if (data != null) {
					// bitmapImg = data.getParcelableExtra("data");
					// Bitmap bitmapImg =
					// BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(imageUri));
					if (onPhotoListener != null) {
						onPhotoListener.resultBitmapUri(imageUriCrop);
					} else {
						UL.d(TAG, "onPhotoListener=null");
					}
				} else {
					Toast.makeText(activity.getApplicationContext(), "crop_data == null", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	public interface OnPhotoListener {
		void resultBitmapUri(Uri bitmapUri);
	}
}
