package com.wj.security;

import com.wj.security.receiver.MyAdmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Setup4Activity extends Activity {
	private CheckBox cb_setup4_protect;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup4);
		sp = getSharedPreferences("config",MODE_PRIVATE);
		cb_setup4_protect = (CheckBox)findViewById(R.id.cb_setup4_protect);
		boolean protecting = sp.getBoolean("protecting", false);
		cb_setup4_protect.setChecked(protecting);
		cb_setup4_protect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				Editor editor = sp.edit();
				if(isChecked){
					editor.putBoolean("protecting", true);
					cb_setup4_protect.setText("防盗保护开启");
				}else{
					cb_setup4_protect.setText("防盗保护没有开启");
					editor.putBoolean("protecting", false);
				}
				editor.commit();
			}
		});
		
	}
	public void activeDeviceAdmin(View v){
		ComponentName mAdminName = new ComponentName(this,MyAdmin.class);
		DevicePolicyManager dm = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
		if(!dm.isAdminActive(mAdminName)){
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
			startActivity(intent);
		}
		
	}
	public void next(View v){
		if(!cb_setup4_protect.isChecked())
		{
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("温馨提示");
			builder.setMessage("手机防盗极大的保护u了你的手机安全，强烈建议开启");
			builder.setPositiveButton("开启", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					cb_setup4_protect.setChecked(true);
					Editor editor = sp.edit();
					editor.putBoolean("issetup", true);
					editor.commit();
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
					Editor editor = sp.edit();
					editor.putBoolean("issetup", true);
					editor.commit();
				}
			});
			builder.create().show();
			return;
		}
		Editor editor = sp.edit();
		editor.putBoolean("issetup", true);
		editor.commit();
		Intent intent = new Intent(this,LostProtectedActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}
	public void pre(View v){
		Intent intent = new Intent(this,Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
	}

}
