package com.wj.security;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class DragViewActivity extends Activity {
	protected static final String TAG="DragViewActivity";
	private ImageView iv_drag_view;
	private TextView tv_drag_view;
	private int windowHeight;
	private int windowWidth;
	private SharedPreferences sp;
	private long firstclicktime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drag_view);
		
		iv_drag_view = (ImageView)findViewById(R.id.iv_drag_view);
		tv_drag_view = (TextView)findViewById(R.id.tv_drag_view);
		windowHeight = getWindowManager().getDefaultDisplay().getHeight();
		windowWidth = getWindowManager().getDefaultDisplay().getWidth();
		sp = getSharedPreferences("config",MODE_PRIVATE);
		
		RelativeLayout.LayoutParams params = (LayoutParams)iv_drag_view.getLayoutParams();
		params.leftMargin = sp.getInt("lastx", 0);
		params.topMargin = sp.getInt("lasty", 0);
		iv_drag_view.setLayoutParams(params);
		
		iv_drag_view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "被点击");
				if(firstclicktime>0){
					long secondclickTime = System.currentTimeMillis();
					if(secondclickTime - firstclicktime<500){
						Log.i(TAG, "被双击");
						firstclicktime = 0;
						int right = iv_drag_view.getRight();
						int left = iv_drag_view.getLeft();
						int iv_width = right-left;
						int iv_left = windowWidth/2 - iv_width/2;
						int iv_right = windowWidth/2 + iv_width/2;
						iv_drag_view.layout(iv_left, iv_drag_view.getTop(), iv_right, iv_drag_view.getBottom());
						Editor editor = sp.edit();
						int lasty = iv_drag_view.getTop();
						int lastx = iv_drag_view.getLeft();
					    editor.putInt("lastx", lastx);
					    editor.putInt("lasty", lasty);
					    editor.commit();
					}
				}
				firstclicktime = System.currentTimeMillis();
				new Thread(){
					public void run(){
						try{
							Thread.sleep(500);
							firstclicktime = 0;
						}catch(InterruptedException e){
							e.printStackTrace();
						}
					}
				}.start();
			}
		});
		
		iv_drag_view.setOnTouchListener(new OnTouchListener() {
			int startx;
			int starty;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                	Log.i(TAG, "被触摸到");
                	startx = (int)event.getRawX();
                	starty = (int)event.getRawY();
                	break;
                case MotionEvent.ACTION_MOVE:
                	int x = (int)event.getRawX();
                	int y = (int)event.getRawY();
                	int tv_height = tv_drag_view.getBottom() - tv_drag_view.getTop();
                	if(y>(windowHeight/2)){
                		tv_drag_view.layout(tv_drag_view.getLeft(), 60, tv_drag_view.getRight(), 60+tv_height);
                	}else{
                		tv_drag_view.layout(tv_drag_view.getLeft(), windowHeight-20-tv_height, 
                				tv_drag_view.getRight(), windowHeight-20);
                	}
                	
                	int dx = x-startx;
                	int dy = y-starty;
                	
                	int t = iv_drag_view.getTop();
                	int b = iv_drag_view.getBottom();
                	int l = iv_drag_view.getLeft();
                	int r = iv_drag_view.getRight();
                	
                	int newl = l+dx;
                	int newt = t+dy;
                	int newr = r+dx;
                	int newb = b+dy;
                	iv_drag_view.layout(newl, newt, newr, newb);
                	startx = (int)event.getRawX();
                	starty = (int)event.getRawY();
                	Log.i(TAG, "开始移动");
                	break;
                case MotionEvent.ACTION_UP:
                	Log.i(TAG, "触摸结束");
                	Editor editor = sp.edit();
                	int lasty = iv_drag_view.getTop();
                	int lastx = iv_drag_view.getLeft();
                	editor.putInt("lasty", lasty);
                	editor.putInt("lastx", lastx);
                	editor.commit();
                	break;
	           	}
				return false;   
			}
		});
	}
	
}
