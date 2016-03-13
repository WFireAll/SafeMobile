package com.wj.security.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.wj.security.R;
import com.wj.security.engine.GPSInfoProvider;

public class SmsReceiver extends BroadcastReceiver {
	private static final String TAG = "SmsReceiver";
	private SharedPreferences sp;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "短信来了");
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		String safenumber = sp.getString("safenumber", "");
		Object[] objs = (Object[])intent.getExtras().get("pdus");
		DevicePolicyManager dm = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		ComponentName mAdminName = new ComponentName(context,MyAdmin.class);
		for(Object obj:objs){
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
			String sender = smsMessage.getOriginatingAddress();
			String body = smsMessage.getMessageBody();
			if("#*location*#".equals(body)){
				Log.i(TAG, "发送位置信息");
				String lastlocation = GPSInfoProvider.getInstance(context).getLocation();
				if(!TextUtils.isEmpty(lastlocation)){
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(safenumber, null, lastlocation, null, null);
				}
				abortBroadcast();
			}else if("#*alarm*#".equals(body)){
				Log.i(TAG, "播放报警音乐");
				MediaPlayer player = MediaPlayer.create(context,R.raw.ylzs);
				player.start();
				abortBroadcast();
			}else if("#*wipedata*#".equals(body)){
				Log.i(TAG, "清除数据");
				if(dm.isAdminActive(mAdminName)){
					dm.wipeData(0);
				}
				abortBroadcast();
			}else if("#*lockscreen*#".equals(body)){
				Log.i(TAG, "远程锁屏");
				if(dm.isAdminActive(mAdminName)){
				    dm.resetPassword("8421", 0);
				    dm.lockNow();
				}
				abortBroadcast();
			}
		}

	}

}
