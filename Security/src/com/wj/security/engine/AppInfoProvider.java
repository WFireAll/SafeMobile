package com.wj.security.engine;

import java.util.ArrayList;
import java.util.List;

import com.wj.security.domain.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppInfoProvider {
	private PackageManager pm;
	public AppInfoProvider(Context context){
		pm = context.getPackageManager();
	}
	public List<AppInfo> getInstalledApps(){
		List<PackageInfo> packageinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		List<AppInfo> appinfos = new ArrayList<AppInfo>();
		for(PackageInfo info : packageinfos){
			AppInfo appinfo = new AppInfo();
			appinfo.setPackname(info.packageName);
			appinfo.setVersion(info.versionName);
			appinfo.setAppicon(info.applicationInfo.loadIcon(pm));
			appinfo.setAppname(info.applicationInfo.loadLabel(pm).toString());
			appinfo.setUserapp(filterApp(info.applicationInfo));
			appinfos.add(appinfo);
			appinfo = null;
		}
		return appinfos;
	}
	
	public boolean filterApp(ApplicationInfo info){
		if((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)!=0){
			return true;
		}else if((info.flags & ApplicationInfo.FLAG_SYSTEM)==0){
			return true;
		}
		return false;
	}

}
