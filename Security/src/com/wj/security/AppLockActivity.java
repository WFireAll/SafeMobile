package com.wj.security;

import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wj.security.db.dao.AppLockDao;
import com.wj.security.domain.AppInfo;
import com.wj.security.engine.AppInfoProvider;

public class AppLockActivity extends Activity {
	
	private ListView lv_applock;
	
	private LinearLayout ll_loading;
	
	private AppInfoProvider provider;
	
	private List<AppInfo> appinfos;
	
	private AppLockDao dao;
	
	private List<String> lockedPacknames;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			ll_loading.setVisibility(View.INVISIBLE);
			lv_applock.setAdapter(new AppLockAdapter());
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.app_lock);
		super.onCreate(savedInstanceState);
		provider = new AppInfoProvider(this);
		
		lv_applock = (ListView)findViewById(R.id.lv_applock);
		ll_loading = (LinearLayout)findViewById(R.id.ll_applock_loading);
		dao = new AppLockDao(this);
		lockedPacknames =  dao.findAll();
		ll_loading.setVisibility(View.VISIBLE);
		new Thread(){
			public void run(){
				appinfos = provider.getInstalledApps();
				handler.sendEmptyMessage(0);
			};
		}.start();
		lv_applock.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AppInfo  appinfo = (AppInfo)lv_applock.getItemAtPosition(position);
				String packname = appinfo.getPackname();
				ImageView iv = (ImageView)view.findViewById(R.id.iv_applock_status);
				TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
						                                       Animation.RELATIVE_TO_SELF,0.2f,
						                                       Animation.RELATIVE_TO_SELF,0,
						                                       Animation.RELATIVE_TO_SELF,0);
				
				ta.setDuration(200);
				if(lockedPacknames.contains(packname)){
					//dao.delete(packname);
					Uri uri = Uri.parse("content://com.wj.applock/DELETE");
				    getContentResolver().delete(uri, null, new String[]{packname});
					iv.setImageResource(R.drawable.unlock);
					lockedPacknames.remove(packname);
				}else{
					//dao.add(packname);
					Uri uri =  Uri.parse("content://com.wj.applock/ADD");
					ContentValues values = new ContentValues();
					values.put("packname", packname);
					getContentResolver().insert(uri, values);
					iv.setImageResource(R.drawable.lock);
					lockedPacknames.add(packname);
				}
				view.startAnimation(ta);
				
			}
			
		});
		
	}
	
	private class AppLockAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return appinfos.size();
		}

		@Override
		public Object getItem(int position) {
			return appinfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if(convertView == null){
				view = View.inflate(getApplicationContext(), R.layout.app_lock_item, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView)view.findViewById(R.id.iv_applock_icon);
				holder.iv_status = (ImageView)view.findViewById(R.id.iv_applock_status);
				holder.tv_name = (TextView)view.findViewById(R.id.tv_applock_appname);
				view.setTag(holder);
			}else{
				view = convertView;
				holder = (ViewHolder)view.getTag();
			}
			
			AppInfo appInfo = appinfos.get(position);
			holder.iv_icon.setImageDrawable(appInfo.getAppicon());
			holder.tv_name.setText(appInfo.getAppname());
			if(lockedPacknames.contains(appInfo.getPackname())){
				holder.iv_status.setImageResource(R.drawable.lock);
//				if(position==1){
//					holder.iv_status.setImageResource(R.drawable.lock);
//				}
			}else{
				holder.iv_status.setImageResource(R.drawable.unlock);
			}
			return view;
		}
		
	}
	
	public static class ViewHolder{
		ImageView iv_icon;
		ImageView iv_status;
		TextView tv_name;
	}

}
