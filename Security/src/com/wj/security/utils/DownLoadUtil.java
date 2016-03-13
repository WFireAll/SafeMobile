package com.wj.security.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;

/**
 * 下载工具
 */
public class DownLoadUtil {

	/**
	 * 通过http协议下载apk
	 * @param urlpath   网络路径
	 * @param filepath  文件路径
	 * @param pd        进度条
	 * @return          下载后的APK
	 */
	public static File getFile(String urlpath,String filepath,ProgressDialog pd){
		try{
			URL url = new URL(urlpath);
			File file = new File(filepath);
			FileOutputStream fos = new FileOutputStream(file);
			HttpURLConnection conn= (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(2000);
			int max = conn.getContentLength();
			pd.setMax(max);
			InputStream is = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			int process = 0;
			while((len=is.read(buffer))!=-1){     //缓冲区不为空
				fos.write(buffer,0,len);
				process+=len;
				pd.setProgress(process);
				Thread.sleep(30);
			}
			fos.flush();
			fos.close();
			is.close();
			return file;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getFilename(String urlpath){
		return urlpath.substring(urlpath.lastIndexOf("/")+1,urlpath.length());
	}

}
