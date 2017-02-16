package com.shwm.freshmallpos.net;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.net.Uri;

import com.shwm.freshmallpos.inter.IUpImageRequest;
import com.shwm.freshmallpos.util.BitmapUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

public class ImageUploadClientRequest implements IUpImageRequest {
	private static final String TAG = "ImageUploadSerivice";
	public static String result = "";
	public static String InfoException = "";
	public static int statuException = ValueStatu.REQUEST_Execute;
	/**
	 * 上传图片
	 * 
	 * @param httpUrl
	 *            上传图片的地址
	 * @param bitmap
	 *            图片
	 * @return
	 */
	@Override
	public HashMap<String, Object> setImgUploadPost(String httpUrl, HashMap<String, Object> map, Bitmap bitmap) {
		HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
		if (bitmap != null) {
			bitmap = BitmapUtil.comp(bitmap);
		}
		byte[] imgData = Bitmap2Bytes(bitmap);

		if (map != null) {
			Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> entry = iterator.next();
				String key = entry.getKey();
				String value = String.valueOf(entry.getValue());
				httpUrl = httpUrl + "&" + key + "=" + value;
			}
		}
		UL.e(TAG + " /post bitmap", httpUrl);
		HttpPost request = new HttpPost(httpUrl);
		HttpClient httpClient = new DefaultHttpClient();
		// FileEntity entity = new FileEntity(file,"binary/octet-stream");
		ByteArrayEntity byteEntity = new ByteArrayEntity(imgData);
		HttpResponse response = null;
		try {
			byteEntity.setContentType("binary/octet-stream");
			byteEntity.setContentEncoding("binary/octet-stream");
			request.setEntity(byteEntity);
			response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 图片上传成功
				result = EntityUtils.toString(response.getEntity());
				InfoException = "";
				statuException = ValueStatu.REQUEST_SUCCESS;
				UL.e(TAG + "  SUCCESS", result);
			} else {
				result = "";
				statuException = ValueStatu.REQUEST_FAIL;
				InfoException = response.getStatusLine().getStatusCode() + "";
				UL.e(TAG + "  FAIL", response.getStatusLine().getStatusCode() + "");
			}
			hashmapResult.put(ValueKey.HTTP_RESUTL, result);
			hashmapResult.put(ValueKey.HTTP_STATU, statuException);
			hashmapResult.put(ValueKey.HTTP_FAILINFO, InfoException);
			return hashmapResult;
		} catch (Exception e) {
			e.printStackTrace();
			UL.e(TAG + "上传图片", e.getMessage());
			result = "";
			statuException = ValueStatu.REQUEST_Execute;
			InfoException = e.getMessage();
			hashmapResult.put(ValueKey.HTTP_RESUTL, result);
			hashmapResult.put(ValueKey.HTTP_STATU, statuException);
			hashmapResult.put(ValueKey.HTTP_FAILINFO, InfoException);
			// return hashmapResult;
			// 出错不会返回
		}

		return hashmapResult;
	}

	private static byte[] Bitmap2Bytes(Bitmap paramBitmap) {
		if (paramBitmap == null) {
			return new byte[0];
		}
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, localByteArrayOutputStream);
		return localByteArrayOutputStream.toByteArray();
	}

	@Override
	public HashMap<String, Object> setImgUploadPost(String url, HashMap<String, Object> hashmap, Uri uriBitmap) {
		// TODO Auto-generated method stub
		return setImgUploadPost(url, hashmap, BitmapUtil.uriToBitmap(uriBitmap));
	}

}
