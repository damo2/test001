package com.shwm.freshmallpos.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.shwm.freshmallpos.util.UL;

public class FileUtil {
	private static final String TAG = FileUtil.class.getSimpleName();

	/** 下载线程是否暂停 */
	private static boolean isPause = false;

	public static void pause(){
		isPause=true;
	}

	/**
	 * 流保存到文件
	 * @param is InputStream 流
	 * @param saveFile File 文件
	 */
	public static void inputStream2File(InputStream is, final File saveFile) {
		isPause=false;
		if (saveFile == null) {
			UL.e(TAG, "saveFile is null");
			return;
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(saveFile, false);
			byte buffer[] = new byte[1024];
			int readsize = 0;
			while ((readsize = is.read(buffer)) != -1) {
				if(!isPause) {
					fos.write(buffer, 0, readsize);
				}
			}
			fos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
			}
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
			}
		}
	}

}
