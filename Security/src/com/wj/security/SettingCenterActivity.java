package com.wj.security;

import com.wj.security.service.ShowCallLocationService;
import com.wj.security.utils.ServiceStatusUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingCenterActivity extends Activity implements OnClickListener {
	// 程序的自动更新
	private SharedPreferences sp;// 用于存储自动更新是否开启的boolean值
	private TextView tv_setting_autoupdate_status;// 自动更新的是否开启对应的TextView控件的显示文字
	private CheckBox cb_setting_autoupdate;// 显示自动更新是否开启的勾选框
	
	private TextView tv_setting_show_location_status;
	private CheckBox cb_setting_show_location;
	private RelativeLayout rl_setting_show_locatioin;
	private Intent showLocationIntent;
	
	private RelativeLayout rl_setting_change_bg;
	private TextView tv_setting_show_bg;
	
	private RelativeLayout rl_setting_change_location;
	private TextView tv_setting_change_location;
	
	

	// 程序锁控件的声明
	private TextView tv_setting_app_lock_status;
	private CheckBox cb_setting_applock;
	private RelativeLayout rl_setting_app_lock;
	private Intent watchDogIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.setting_center);
		super.onCreate(savedInstanceState);
		// 获取Sdcard下的config.xml文件，如果该文件不存在，那么将会自动创建该文件，文件的操作类型为私有类型
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 标记自动更新状态是否开启对应的Checkbox控件
		cb_setting_autoupdate = (CheckBox) findViewById(R.id.cb_setting_autoupdate);
		// 显示当前自动更新是否开启对应的TextView控件
		tv_setting_autoupdate_status = (TextView) findViewById(R.id.tv_setting_autoupdate_status);
		// 初始化自动更新的ui，默认状态下是开启的
		boolean autoupdate = sp.getBoolean("autoupdate", true);
		if (autoupdate) {
			tv_setting_autoupdate_status.setText("自动更新已经开启");
			// 因为autoupdate变量为true，则表示自动更新开启，所以，Checkbox的状态应该是勾选状态的，即为true
			cb_setting_autoupdate.setChecked(true);
		} else {
			tv_setting_autoupdate_status.setText("自动更新已经关闭");
			// 因为autoupdate变量为false，则表示自动更新未开启，所以，Checkbox的状态应该是未勾选状态的，即为false
			cb_setting_autoupdate.setChecked(false);
		}
		/**
		 * 当Checkbox的状态发生改变时执行以下代码
		 */
		cb_setting_autoupdate
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					// 参数一：当前的Checkbox 第二个参数：当前的Checkbox是否处于勾选状态
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// 获取编辑器
						Editor editor = sp.edit();
						// 持久化存储当前Checkbox的状态，当下次进入时，依然可以保存当前设置的状态
						editor.putBoolean("autoupdate", isChecked);
						// 将数据真正提交到sp里面
						editor.commit();
						if (isChecked) {// Checkbox处于选中效果
							// 当Checkbox处于勾选状态时，表示我们的自动更新已经开启，同时修改字体颜色
							tv_setting_autoupdate_status
									.setTextColor(Color.WHITE);
							tv_setting_autoupdate_status.setText("自动更新已经开启");
						} else {// Checkbox处于未勾选状态
								// 当Checkbox未处于勾选状态时，表示我们的自动更新已经开启，同时修改字体颜色
							tv_setting_autoupdate_status
									.setTextColor(Color.RED);
							tv_setting_autoupdate_status.setText("自动更新已经关闭");
						}
					}
				});
		
		tv_setting_show_location_status = (TextView)findViewById(R.id.tv_setting_show_location_status);
		cb_setting_show_location = (CheckBox)findViewById(R.id.cb_setting_show_location);
		rl_setting_show_locatioin = (RelativeLayout)findViewById(R.id.rl_setting_show_location);
		showLocationIntent = new Intent(this,ShowCallLocationService.class);
		rl_setting_show_locatioin.setOnClickListener(this);
		
		rl_setting_change_bg = (RelativeLayout)findViewById(R.id.rl_setting_change_bg);
		tv_setting_show_bg = (TextView)findViewById(R.id.tv_setting_show_bg);
		rl_setting_change_bg.setOnClickListener(this);
		
		rl_setting_change_location = (RelativeLayout)findViewById(R.id.rl_setting_change_location);
		tv_setting_change_location = (TextView)findViewById(R.id.tv_setting_change_location);
		rl_setting_change_location.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.rl_setting_show_location:
			if(cb_setting_show_location.isChecked()){
				tv_setting_show_location_status.setText("来电归属地显示没有开启");
				stopService(showLocationIntent);
				cb_setting_show_location.setChecked(false);
			}else{
				tv_setting_show_location_status.setText("来电归属地显示已经开启");
				startService(showLocationIntent);
				cb_setting_show_location.setChecked(true);
			}
			break;
		case R.id.rl_setting_change_bg:
			showChooseBgDialog();
			break;
		case R.id.rl_setting_change_location:
			Intent intent = new Intent(this,DragViewActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	protected void onResume() {
		if(ServiceStatusUtil.isServiceRunning(this, "com.wj.security.service.ShowCallLocationService")){
			cb_setting_show_location.setChecked(true);
			tv_setting_show_location_status.setText("来电归属地显示已经开启");
		}else{
			cb_setting_show_location.setChecked(false);
			tv_setting_show_location_status.setText("来电归属地显示没有开启");
		}
		super.onResume();
	}
	
	private void showChooseBgDialog(){
		AlertDialog.Builder builder= new Builder(this);
		builder.setIcon(R.drawable.notification);
		builder.setTitle("归属地提示框风格");
		final String[] items = {"半透明","活力橙","卫士蓝","苹果绿","金属灰"};
		int which = sp.getInt("which", 0);
		builder.setSingleChoiceItems(items, which, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Editor editor = sp.edit();
				editor.putInt("which", which);
				editor.commit();
				tv_setting_show_bg.setText(items[which]);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		builder.create().show();
	}

}
