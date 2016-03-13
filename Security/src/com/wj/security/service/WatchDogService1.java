package com.wj.security.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.wj.security.EnterPwdActivity;
import com.wj.security.IService;
import com.wj.security.db.dao.AppLockDao;

public class WatchDogService1 extends Service{
	protected static final String TAG="WatchDogService";
	boolean flag;
	private Intent pwdintent;
	private List<String> lockPacknames;
	private AppLockDao dao;
	private List<String> tempStopProtectPacknames;
	private  MyBinder binder;
	private MyObserver observer;
	private LockScreenReceiver receiver;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		binder = new MyBinder();
		return null;
	}
	private class MyBinder extends Binder implements IService{
		public void callTempStopProtect(String packname){
			tempStopProtect(packname);
		}
	}
	
	public void tempStopProtect(String packname){
		tempStopProtectPacknames.add(packname);
	}


	@Override
	public void onCreate() {
		super.onCreate();
		Uri uri = Uri.parse("content://com.wj.applock/");
		observer = new MyObserver(new Handler());
		getContentResolver().registerContentObserver(uri, true, observer);
		IntentFilter filter = new IntentFilter();
		filter.setPriority(1000);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		receiver = new LockScreenReceiver();
		registerReceiver(receiver, filter);
		
		dao = new AppLockDao(this);
		flag = true;
		tempStopProtectPacknames = new ArrayList<String>();
		lockPacknames = dao.findAll();
		pwdintent = new Intent(this,EnterPwdActivity.class);
		pwdintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		new Thread(){
			public void run(){
				while(flag){
					ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
					RunningTaskInfo taskinfo = am.getRunningTasks(1).get(0);
					String packname = taskinfo.topActivity.getPackageName();
					Log.i(TAG, packname);
					if(tempStopProtectPacknames.contains(packname)){
						try{
							Thread.sleep(200);
						}catch(InterruptedException e){
							e.printStackTrace();
						}
						continue;
					}
					pwdintent.putExtra("packname", packname);
					if(lockPacknames.contains(packname)){
						startActivity(pwdintent);
					}
					try{
						Thread.sleep(200);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			};
		}.start();
	}


	@Override
	public void onDestroy() {
		flag = false;
		getContentResolver().unregisterContentObserver(observer);
		observer = null;
		super.onDestroy();
	}
	
	private class MyObserver extends ContentObserver{

		public MyObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			lockPacknames = dao.findAll();
			super.onChange(selfChange);
		}
	}
    private class LockScreenReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "锁屏了");
			tempStopProtectPacknames.clear();
		}
    	
    }
	

}
