package com.wj.security.engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GPSInfoProvider {
	private static GPSInfoProvider mGPSInfoProvider;
	private static LocationManager lm;
	private static MyListener listener;
	private static SharedPreferences sp;
	private GPSInfoProvider(){}
	
	public synchronized static GPSInfoProvider getInstance(Context context){
		if(mGPSInfoProvider==null){
			mGPSInfoProvider = new GPSInfoProvider();
			lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_HIGH);
			criteria.setAltitudeRequired(true);
			String provider = lm.getBestProvider(criteria, true);
			listener = new GPSInfoProvider().new MyListener();
			lm.requestLocationUpdates(provider, 60000, 100, listener);
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return mGPSInfoProvider;
	}
	public void stopListen(){
		lm.removeUpdates(listener);
		listener = null;
	}
	protected class MyListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
		    String latitude = "latitude is :"+location.getLatitude();
		    String longitude = "longitude is :"+location.getLongitude();
		    String meter = "accuracy is :"+location.getAccuracy();
		    System.out.println(latitude+"-"+longitude+"-"+meter);
		    Editor editor = sp.edit();
		    editor.putString("last_location", latitude+"-"+longitude+"-"+meter);
		    editor.commit();
		    
		    
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			
			
		}

		@Override
		public void onProviderDisabled(String provider) {
		
			
		}
	}
	public String getLocation() {
		return sp.getString("last_location", "");
	}

}
