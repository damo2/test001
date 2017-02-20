package com.shwm.freshmallpos.util;

import android.content.Context;

import com.shwm.freshmallpos.base.ApplicationMy;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 保存、读取文件
 */
public class FileUtil {
    public FileUtil() {
        super();
    }

    /**
     * 保存文件
     *
     * @param filename 文件名称
     * @param content  文件内容
     */
    public void save(String filename, String content) {
        try {
            // java中的IO技术来保存文件
            // 得到文件的输出流对象
            FileOutputStream outStream;
            outStream = ApplicationMy.getContext().openFileOutput(filename, Context.MODE_PRIVATE);
            // filename不可以带路径
            // mode往文件写的模式：追加、覆盖、针对不同的组的操作权限Context.MODE_PRIVATE私有操作模式，创建出来的文件只能为本应用使用；
            // 采用私有模式创建的文件 写入文件中的内容会覆盖原文件内容，默认会保存在data/data/应用包下/files目录下
            // 将输出流写入文件
            outStream.write(content.getBytes());
            // 写完文件之后要关闭流
            outStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 读取文件内容
     *
     * @param filename 文件名称
     * @return 文件内容
     */
    public String read(String filename) {
        try {
            // 方法openFileInput(filename)会默认从data/当前应用包下/files目录下读取
            FileInputStream inStream = ApplicationMy.getContext().openFileInput(filename);
            // 从内存输出的数据流对象
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            // 从文件中读取数据，先把输入流中的数据读取到一个字节数组中，//输入流中的数据可能很多，应该利用循环实现
            byte[] buffer = new byte[1024];// 定义一个字节数组
            int len = 0;
            // 若未达到文件尾，就一直读
            while ((len = inStream.read(buffer)) != -1) {
                // 得到每次读取到的数据
                outStream.write(buffer, 0, len);
            }
            byte[] data = outStream.toByteArray();
            // data和上面方法content.getBytes()的数据是一致的
            return new String(data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
