package com.wj.security.receiver;

import com.wj.security.LostProtectedActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OutCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String outnumber = getResultData();
		String enterPhoneBaknumber = "15111980783";
		if(enterPhoneBaknumber.equals(outnumber)){
			Intent lostIntent = new Intent(context,LostProtectedActivity.class);
			lostIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(lostIntent);
			setResultData(null);
		}
	}

}
