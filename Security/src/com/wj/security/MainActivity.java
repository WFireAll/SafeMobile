package com.wj.security;

import com.wj.security.adapter.MainAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MainActivity extends Activity{
	
	private GridView gv_main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gv_main = (GridView)findViewById(R.id.gv_main);
		gv_main.setAdapter(new MainAdapter(this)); 
		
		gv_main.setOnItemClickListener(new OnItemClickListener() { 

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				switch(position)
				{
				case 0:
					Intent lostprotectedIntent = new Intent(MainActivity.this,LostProtectedActivity.class);
					startActivity(lostprotectedIntent);
					break;
				case 7:
					Intent atoolsIntent = new Intent(MainActivity.this,AtoolsActivity.class);
					startActivity(atoolsIntent);
					break;
				case 8:
					Intent settingIntent = new Intent(MainActivity.this,SettingCenterActivity.class);
					startActivity(settingIntent);
					break;
				}
			}
		});
	}

	
}
