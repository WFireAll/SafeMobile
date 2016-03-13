package com.wj.security;

import com.wj.security.db.dao.NumberAddressDao;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NumberQueryActivity extends Activity {
	private EditText et_number_query;
	private TextView tv_number_address;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.number_query);
		et_number_query = (EditText)findViewById(R.id.et_number_query);
		tv_number_address = (TextView)findViewById(R.id.tv_number_address);
		
	}
	public void query(View v){
		String number = et_number_query.getText().toString().trim();
		if(TextUtils.isEmpty(number))
		{
			Toast.makeText(this, "号码不能为空", 1).show();
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			et_number_query.startAnimation(shake);
			return;
		}else{
			String address = NumberAddressDao.getAddress(number);
			tv_number_address.setText(address);
		}
	}

}
