package com.wj.security.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NumberAddressDao {
	public static String getAddress(String number){
		String address = number;
		String path = "/data/data/com.wj.security/files/address.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		if(db.isOpen()){
			if(number.matches("^1[3458]\\d{9}$")){
				Cursor cursor = db.rawQuery("select city from address_tb where _id = (select outkey from numinfo where mobileprefix =?)", 
						new String[]{number.substring(0,7)});
				if(cursor.moveToFirst()){
					address = cursor.getString(0);
				}
				cursor.close();
			}else{
				Cursor cursor;
				switch(number.length()){
				case 4:
					address ="模拟器";
					break;
				case 7:
					address="本地号码";
					break;
				case 8:
					address ="本地号码";
					break;
				case 10:
					cursor =db.rawQuery("select city from address_tb where area=? limit 1", 
							new String[]{number.substring(0, 3)});
					if(cursor.moveToFirst()){
						address=cursor.getString(0);
					}
					cursor.close();
					break;
				case 12:
					cursor = db.rawQuery("select city from address_tb where area =? limit 1", 
							new String[]{number.substring(0, 4)});
					if(cursor.moveToFirst()){
						address=cursor.getString(0);
					}
					cursor.close();
					break;
				case 11:
					cursor = db.rawQuery("select city from address_tb where area =? limit 1", 
							new String[]{number.substring(0, 3)});
					if(cursor.moveToFirst()){
						address=cursor.getString(0);
					}
					cursor.close();
					cursor = db.rawQuery("select city from address_tb where area =? limit 1", 
							new String[]{number.substring(0,4)});
					if(cursor.moveToFirst()){
						address=cursor.getString(0);
					}
					cursor.close();
					break;
				}
			}
			db.close();
		}
		return address;
	}

}
