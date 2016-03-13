package com.wj.security.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;

public class AssetCopyUtil {
	private Context context;

	public AssetCopyUtil(Context context) {
		this.context = context;
	}
	public boolean copyFile(String srcfilename,File file, ProgressDialog pd){
		try{
			AssetManager am = context.getAssets();
			InputStream is = am.open(srcfilename);
			int max = is.available();
			pd.setMax(max);
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			int process = 0;
			while((len = is.read(buffer))!=-1){
				fos.write(buffer,0,len);
				process+=len;
				pd.setProgress(process);
			}
			fos.flush();
			fos.close();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

}
