package com.wj.security;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wj.contentview.ContentWidget;
import com.wj.security.db.dao.BlackNumberDao;
import com.wj.security.domain.BlackNumber;
import com.wj.security.utils.ViewUtils;

public class CallSmsSafeActivity extends Activity {
	
	protected static final int LOAD_DATA_FINISH = 40;
	public static final String TAG = "CallSmsSafeActivity";
	
	@ContentWidget(R.id.lv_call_sms_safe)
	private ListView lv_call_sms_safe;
	private BlackNumberDao dao;
	private List<BlackNumber> blacknumbers;
	//private BlackNumberAdapter adapter;
	
	@ContentWidget(R.id.ll_call_sms_safe_loading)
	private LinearLayout ll_call_sms_safe_loading;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
            switch(msg.what){
            case LOAD_DATA_FINISH:
            	ll_call_sms_safe_loading.setVisibility(View.INVISIBLE);
            //	adapter = new BlackNumber();
       //     	lv_call_sms_safe.setAdapter(adapter);
            	break;
            }
		};
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_sms_safe);
		ViewUtils.injectObject(this, this);
	//	dao = new BlackNumber(this);
		ll_call_sms_safe_loading.setVisibility(View.VISIBLE);
		new Thread(){
			public void run(){
//				blacknumber = dao.findAll();
				Message msg = Message.obtain();
				msg.what = LOAD_DATA_FINISH;
				handler.sendMessage(msg);
			};
		}.start();
		
	}
	
	private class BlackNumberAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return blacknumbers.size();
		}

		@Override
		public Object getItem(int position) {
			return blacknumbers.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			TextView tv_number;
			TextView tv_mode;
			if(convertView == null){
				view = View.inflate(getApplicationContext(), R.layout.call_sms_item, null);
				tv_number = (TextView)convertView.findViewById(R.id.tv_callsms_item_number);
				tv_mode = (TextView)convertView.findViewById(R.id.tv_callsms_item_mode);
				convertView.setTag(new DataWrapper(tv_number,tv_mode));
			}else{
				view = convertView;
				DataWrapper dataWrapper = (DataWrapper)convertView.getTag();
				tv_number = dataWrapper.tv_number;
				tv_mode = dataWrapper.tv_mode;
			}
			BlackNumber blacknumber = new BlackNumber();
			tv_number.setText(blacknumber.getNumber());
			int mode = blacknumber.getMode();
			if(mode == 0){
				tv_mode.setText("电话拦截");
			}else if(mode == 1){
				tv_mode.setText("短信拦截");
			}else{
				tv_mode.setText("全部拦截");
			}
			
			return view;
		}
		
	}
	
	
	private class DataWrapper{
		TextView tv_number;
		TextView tv_mode;
		public DataWrapper(TextView tv_number, TextView tv_mode) {
			this.tv_number = tv_number;
			this.tv_mode = tv_mode;
		}
		
	}
	

}
