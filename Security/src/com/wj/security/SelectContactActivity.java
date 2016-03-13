package com.wj.security;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wj.security.domain.ContactInfo;
import com.wj.security.engine.ContactInfoProvider;

public class SelectContactActivity extends Activity {
	private ListView lv_select_contact;
	private ContactInfoProvider provider;
	private List<ContactInfo> infos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_contact);
		lv_select_contact = (ListView)findViewById(R.id.lv_select_contact);
		provider = new ContactInfoProvider(this);
		infos = provider.getContactInfos();
		lv_select_contact.setAdapter(new ContactAdapter());
		lv_select_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ContactInfo info = (ContactInfo) lv_select_contact.getItemAtPosition(position);
				String number = info.getPhone();
				Intent data = new Intent();
				data.putExtra("number", number);
				setResult(0,data);
				finish();
				
			}
		});
	}
	private class ContactAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return infos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ContactInfo info = infos.get(position);
			TextView tv = new TextView(getApplicationContext());
			tv.setTextSize(24);
			tv.setTextColor(Color.WHITE);
			tv.setText(info.getName()+"\n"+info.getPhone());
			return tv;
		}
		
	}

}
