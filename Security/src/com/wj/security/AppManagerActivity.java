package com.wj.security;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wj.contentview.ContentWidget;
import com.wj.security.domain.AppInfo;
import com.wj.security.engine.AppInfoProvider;
import com.wj.security.utils.ViewUtils;

public class AppManagerActivity extends Activity {
	
	protected static final int LOAD_APP_FINISH = 50;
	private static final String TAG = "AppManagerActivity";
	@ContentWidget(R.id.tv_appmanager_mem_avail)
	private TextView tv_appmanager_mem_avail;
	@ContentWidget(R.id.tv_appmanager_sd_avail)
	private TextView tv_appmanager_sd_avail;
	@ContentWidget(R.id.lv_appmanager)
	private ListView lv_appmanager;
	@ContentWidget(R.id.ll_appmanager_loading)
	private LinearLayout ll_loading;
	private PackageManager pm;
	
	private List<AppInfo> appinfos;
	private List<AppInfo> userappInfos;
	private List<AppInfo> systemappInfos;
	
	private Handler handler = new Handler(){
		public void handlerMessage(android.os.Message msg){
			switch(msg.what){
			case LOAD_APP_FINISH:
				ll_loading.setVisibility(View.VISIBLE);
				lv_appmanager.setAdapter(new AppManagerAdapter());
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.app_manager);
		super.onCreate(savedInstanceState);
		ViewUtils.injectObject(this, this);
		tv_appmanager_sd_avail.setText("SD卡可用" + getAvailSDSize());
		tv_appmanager_mem_avail.setText("内存可用" + getAvailROMSize());
		pm = getPackageManager();
		fillData();
	}


	private void fillData(){
		ll_loading.setVisibility(View.VISIBLE);
		new Thread(){
			public void run(){
				AppInfoProvider provider = new AppInfoProvider(AppManagerActivity.this);
				appinfos = provider.getInstalledApps();
				initAppInfo();
				Message msg = Message.obtain();
				msg.what = LOAD_APP_FINISH;
				handler.sendMessage(msg);
			};
		}.start();
	}
	
	protected void initAppInfo(){
		systemappInfos = new ArrayList<AppInfo>();
		userappInfos = new ArrayList<AppInfo>();
		for(AppInfo appinfo : appinfos){
			if(appinfo.isUserapp()){
				userappInfos.add(appinfo);
				
			}else{
				systemappInfos.add(appinfo);
			}
		}
	}
	
	private class AppManagerAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return userappInfos.size() +1 + systemappInfos.size() +1;
		}

		@Override
		public Object getItem(int position) {
			if(position == 0)
			{
				return position;
			}else if(position <= userappInfos.size()){
				int newposition = position - 1;
				return userappInfos.get(newposition);
			}else if(position == (userappInfos.size() +1)){
			   return position;
			}else{
				int newposition = position - userappInfos.size() - 2;
				return systemappInfos.get(newposition);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(position == 0){
				TextView tv = new TextView(getApplicationContext());
				tv.setTextSize(20);
				tv.setText("用户程序(" + userappInfos.size() + ")");
				return tv;
			}
			else if(position<= userappInfos.size()){
				int newposition = position - 1;
				View view;
				ViewHolder holder;
				if(convertView == null || convertView instanceof TextView){
					view = View.inflate(getApplicationContext(), R.layout.app_manager_item, null);
					holder = new ViewHolder();
					holder.iv_icon = (ImageView)view.findViewById(R.id.iv_appmanager_icon);
					holder.tv_name = (TextView)view.findViewById(R.id.tv_appmanager_appname);
					holder.tv_version = (TextView)view.findViewById(R.id.tv_appmanager_appversion);
					view.setTag(holder);
				}else{
					view = convertView;
					holder = (ViewHolder)view.getTag();
				}
				AppInfo appInfo = userappInfos.get(newposition);
				holder.iv_icon.setImageDrawable(appInfo.getAppicon());
				holder.tv_name.setText(appInfo.getAppname());
				holder.tv_version.setText("版本号："+appInfo.getVersion());
				return view;
			}else if(position == (userappInfos.size() + 1)){
				TextView tv = new TextView(getApplicationContext());
				tv.setTextSize(20);
				tv.setText("系统程序("+systemappInfos.size()+")");
				return tv;
			}else{
				int newposition = position - userappInfos.size() - 2;
				View view;
				ViewHolder holder;
				if(convertView == null || convertView instanceof TextView){
					view = View.inflate(getApplicationContext(), R.layout.app_manager_item, null);
					holder = new ViewHolder();
					holder.iv_icon = (ImageView)view.findViewById(R.id.iv_appmanager_icon);
					holder.tv_name = (TextView)view.findViewById(R.id.tv_appmanager_appname);
					holder.tv_version = (TextView)view.findViewById(R.id.tv_appmanager_appversion);
					view.setTag(holder);
				}else{
					view = convertView;
					holder = (ViewHolder)view.getTag();
				}
				AppInfo appInfo = systemappInfos.get(newposition);
				holder.iv_icon.setImageDrawable(appInfo.getAppicon());
				holder.tv_name.setText(appInfo.getAppname());
				holder.tv_version.setText("版本号："+appInfo.getVersion());
				return view;
			}
		}
		
		@Override
		public boolean isEnabled(int position){
			if(position == 0 || position == (userappInfos.size()+1)){
				return false;
			}
			return super.isEnabled(position);
		}
   }
	
	
	private static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_version;
	}
	
	
	private String getAvailSDSize(){
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long totalBlocks = stat.getBlockCount();
		long availableBlocks = stat.getAvailableBlocks();
		long blockSize = stat.getBlockSize();
		long availSDSize = availableBlocks*blockSize;
		return Formatter.formatFileSize(this, availSDSize);
	}
	
	
	private String getAvailROMSize(){
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return Formatter.formatFileSize(this, availableBlocks*blockSize);
	}
}
