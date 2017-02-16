package com.shwm.freshmallpos.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

import com.shwm.freshmallpos.R;

/**
 * 打印日志到手机文件
 */
public class ULogToDevice {
	private static String writeFile;
	private static FileOutputStream out = null;
	public static boolean hasConfiguration = false;
	private static File writeLogFile = null;
	private static long maxFileSize = 1024 * 1024 * 50;

	public static void initConfiguration() {
		File directory = null;
		String fileName = null;
		String path = null;
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		// System.out.println(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH@mm@ss");
		path = Environment.getExternalStorageDirectory().toString() + "/" + R.string.app_name + "/log/";
		if (path != null) {
			directory = new File(path);
			if (!directory.exists()) {
				directory.mkdirs();
			}
		}
		fileName = sdf.format(date) + ".log";
		File file = new File(directory, fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		writeFile = path + fileName;
		writeLogFile = new File(writeFile);
		if (out != null) {
			closeWriteStream();
		}
		try {
			out = new FileOutputStream(writeFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		hasConfiguration = true;
		writeLog(sdf.format(date) + "\n");
	}

	public static String getSystemDate() {
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss");
		return sdf.format(date);
	}

	/**
	 * 追加文件：使用FileOutputStream，FileOutputStream时，把第二个参数设为true
	 * 
	 * @param fileName
	 * @param content
	 */
	public static void writeLog(String conent) {
		if (!hasConfiguration) {
			return;
		}
		if (writeLogFile.length() >= maxFileSize) {
			initConfiguration();
		}
		String temp = getSystemDate() + ":" + conent + "\n";
		try {
			out.write(temp.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public static void writeLog(String tag, String conent) {
		if (!hasConfiguration) {
			return;
		}
		if (writeLogFile.length() >= maxFileSize) {
			initConfiguration();
		}
		String temp = getSystemDate() + ":" + tag + ": " + conent + "\n";
		// Log.d("tigertiger",tag+":"+conent);
		try {
			if (out != null) {
				out.write(temp.getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public static void closeWriteStream() {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}