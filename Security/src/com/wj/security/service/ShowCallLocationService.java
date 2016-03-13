package com.wj.security.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wj.security.R;
import com.wj.security.db.dao.NumberAddressDao;

public class ShowCallLocationService extends Service {

	private TelephonyManager tm;
	private MyPhoneListener listener;
	private WindowManager windowManager;
	private SharedPreferences sp;
	private static final int[] bgs={R.drawable.call_locate_white,R.drawable.call_locate_orange,
		R.drawable.call_locate_blue,R.drawable.call_locate_green,R.drawable.call_locate_green};
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		
		return null;
	}
	
	@Override
	public void onCreate() {
		
		super.onCreate();
		sp = getSharedPreferences("config",MODE_PRIVATE);
		
		listener = new MyPhoneListener();
		tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
	}
	
	private class MyPhoneListener extends PhoneStateListener{
		private View view;

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch(state){
			case TelephonyManager.CALL_STATE_RINGING:
				String address = NumberAddressDao.getAddress(incomingNumber);
				Toast.makeText(getApplicationContext(), "归属地:"+address, 1).show();
				view = View.inflate(getApplicationContext(), R.layout.show_address, null);
				LinearLayout ll = (LinearLayout)view.findViewById(R.id.ll_show_address);
				int which = sp.getInt("which", 0);
				ll.setBackgroundResource(bgs[which]);
				TextView tv = (TextView)view.findViewById(R.id.tv_show_address);
				tv.setText(address);
				final WindowManager.LayoutParams params = new LayoutParams();
				params.gravity = Gravity.LEFT | Gravity.TOP;
				params.x = sp.getInt("lastx", 0);
				params.y = sp.getInt("lasty", 0);
				params.height = WindowManager.LayoutParams.WRAP_CONTENT;
				params.width = WindowManager.LayoutParams.WRAP_CONTENT;
				params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
						  WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
				params.format = PixelFormat.TRANSLUCENT;
				params.type = WindowManager.LayoutParams.TYPE_TOAST;
				windowManager.addView(view, params);
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if(view!=null){
					windowManager.removeView(view);
					view = null;
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				if(view!=null){
					windowManager.removeView(view);
					view=null;
				}
				break;				
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
	}
	

}
