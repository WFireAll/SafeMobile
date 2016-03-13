package com.wj.security;

import java.io.File;

import com.wj.security.utils.AssetCopyUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class AtoolsActivity extends Activity implements OnClickListener{
	protected static final int COPY_SUCCESS = 30;
	protected static final int COPY_FAILED = 31;
	protected static final int COPY_COMMON_NUMBER_SUCCESS = 32;
	private TextView tv_atools_common_num;
	
	private TextView tv_atools_address_query;
	private TextView tv_atools_applock;
	private ProgressDialog pd;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			pd.dismiss();
			switch(msg.what){
			case COPY_SUCCESS:
				loadQueryUI();
				break;
			case COPY_FAILED:
				Toast.makeText(getApplicationContext(), "复制数据失败", 1).show();
				break;
			case COPY_COMMON_NUMBER_SUCCESS:
				loadCommNumUI();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atools);
		tv_atools_address_query = (TextView)findViewById(R.id.tv_atools_address_query);
		tv_atools_address_query.setOnClickListener(this);
		pd = new ProgressDialog(this);
		
		tv_atools_common_num = (TextView)findViewById(R.id.tv_atools_common_num);
		tv_atools_common_num.setOnClickListener(this);
		
		tv_atools_applock = (TextView)findViewById(R.id.tv_atools_applock);
		tv_atools_applock.setOnClickListener(this);
		
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		
	}
	public void onClick(View view){
		switch(view.getId()){
		case R.id.tv_atools_address_query:
			final File file = new File(getFilesDir(),"address.db");
			if(file.exists() && file.length()>0){
				loadQueryUI();
			}else
			{
				pd.show();
				new Thread()
				{
					public void run()
					{
						AssetCopyUtil asu = new AssetCopyUtil(getApplicationContext());
						boolean result = asu.copyFile("naddress.db", file, pd);
						if(result){
							Message msg = Message.obtain();
							msg.what = COPY_SUCCESS;
							handler.sendMessage(msg);
						}else{
							Message msg = Message.obtain();
							msg.what = COPY_FAILED;
							handler.sendMessage(msg);
						}
					};
				}.start();
			}
			break;
		case R.id.tv_atools_common_num:
			final File commonnumberfile = new File(getFilesDir(),"commonnum.db");
			if(commonnumberfile.exists()&& commonnumberfile.length()>0){
				loadCommNumUI();
			}else{
				pd.show();
				new Thread(){
					public void run(){
						AssetCopyUtil asu = new AssetCopyUtil(getApplicationContext());
						boolean result = asu.copyFile("commonnum.db", commonnumberfile, pd);
						if(result){
							Message msg = Message.obtain();
							msg.what = COPY_COMMON_NUMBER_SUCCESS;
							handler.sendMessage(msg);
						}else{
							Message msg = Message.obtain();
							msg.what = COPY_FAILED;
							handler.sendMessage(msg);
						}
					};
				}.start();
			}
		break;
		case R.id.tv_atools_applock:
			Intent applockIntent = new Intent(this, AppLockActivity.class);
			startActivity(applockIntent);
		}
	}
	
	private void loadCommNumUI(){
		Intent intent = new Intent(this, CommonNumActivity.class);
		startActivity(intent);
	}
	private void loadQueryUI(){
		Intent intent = new Intent(this,NumberQueryActivity.class);
		startActivity(intent);
	}
	
}
