package com.wj.security;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wj.contentview.ContentWidget;
import com.wj.security.service.WatchDogService1;

public class EnterPwdActivity extends Activity {
	
	@ContentWidget(R.id.et_password)
	private EditText et_password;
	@ContentWidget(R.id.tv_enterpwd_name)
	private TextView tv_name;
	@ContentWidget(R.id.iv_enterpwd_icon)
	private ImageView iv_icon;
	
	private Intent serviceIntent;
	private IService iService;
	private MyConn conn;
	private String packname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_pwd);
		Intent intent = getIntent();
		packname = intent.getStringExtra("packname");
		serviceIntent = new Intent(this,WatchDogService1.class);
		conn = new MyConn();
		bindService(serviceIntent,conn,BIND_AUTO_CREATE);
		try{
			PackageInfo info = getPackageManager().getPackageInfo(packname, 0);
			tv_name.setText(info.applicationInfo.loadLabel(getPackageManager()));
			iv_icon.setImageDrawable(info.applicationInfo.loadIcon(getPackageManager()));
		}catch(NameNotFoundException e){
			e.printStackTrace(); }
	}
	
	private class MyConn implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			iService = (IService)service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
		
	}
	
	
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		unbindService(conn);
	}

	public void enterPassword(View view){
		String pwd = et_password.getText().toString().trim();
		if(TextUtils.isEmpty(pwd)){
			Toast.makeText(this, "密码不能为空", 1).show();
			return;
		}
		if("123".equals(pwd)){
			finish();
		}else{
			Toast.makeText(this, "密码不正确", 1).show();
			return;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK)
		{
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	

}
