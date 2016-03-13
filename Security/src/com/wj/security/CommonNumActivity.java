package com.wj.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wj.security.db.dao.CommonNumDao;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

public class CommonNumActivity extends Activity {
	protected static final String TAG="CommonNumActivity";
	private ExpandableListView elv_common_num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_num);
		elv_common_num = (ExpandableListView)findViewById(R.id.elv_common_num);
		elv_common_num.setAdapter(new CommonNumberAdapter());
		
		elv_common_num.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				TextView tv = (TextView)v;
				String number = tv.getText().toString().split("\n")[1];
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:"+number));
				startActivity(intent);
				
				return false;
			}
		});
	}
	
	private class CommonNumberAdapter extends BaseExpandableListAdapter{
		
		private List<String> groupNames;
		private Map<Integer,List<String>> childrenCache;
		public CommonNumberAdapter(){
			childrenCache = new HashMap<Integer,List<String>>();
		}
		
		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return CommonNumDao.getGroupCount();
		}
	
		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return CommonNumDao.getChildrenCount(groupPosition);
		}
	
		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}
	
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}
	
		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}
	
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView tv;
			if(convertView ==null){
				tv= new TextView(getApplicationContext());
			}else{
				tv= (TextView)convertView;
			}
			tv.setTextSize(28);
			if(groupNames != null){
				tv.setText("      "+groupNames.get(groupPosition));
			}else{
				groupNames = CommonNumDao.getGroupNames();
				tv.setText("      "+groupNames.get(groupPosition));
			}
			return tv;
		}
	
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView tv;
			if(convertView == null){
				tv = new TextView(getApplicationContext());
			}else{
				tv = (TextView)convertView;
			}
			tv.setTextSize(20);
			String result = null;
			if(childrenCache.containsKey(groupPosition)){
			    result = childrenCache.get(groupPosition).get(childPosition);
			}else{
				List<String> results = CommonNumDao.getChildNameByPosition(groupPosition);
				childrenCache.put(groupPosition, results);
				result = results.get(childPosition);
			}
			tv.setText(result);
			return tv;
		}
	
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}
		

	}
}
