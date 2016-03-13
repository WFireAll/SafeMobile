package com.wj.security.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {
    
	private static final String TAG="BootCompleteReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "手机重启了");
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean protecting = sp.getBoolean("protecting", false);
		if(protecting){
			String safenumber = sp.getString("safenumber", "");
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			String realsim = tm.getSimSerialNumber();
			String saveSim = sp.getString("simserial", "");
			if(!saveSim.equals(realsim)){
				Log.i(TAG, "发送短信");
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(safenumber, null, "sim card changed", null, null);
			}
		}
		

	}

}
