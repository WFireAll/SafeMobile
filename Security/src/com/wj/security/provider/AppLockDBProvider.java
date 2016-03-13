package com.wj.security.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.wj.security.db.dao.AppLockDao;

public class AppLockDBProvider extends ContentProvider {
	
	private static final int ADD = 1;
	private AppLockDao dao;
	private static final int DELETE = 2;
	public static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	static{
		matcher.addURI("com.wj.applock", "ADD", ADD);
		matcher.addURI("com.wj.applock", "DELETE", DELETE);
	}

	@Override
	public boolean onCreate() {
		dao = new AppLockDao(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return null;
	}

	@Override
	public String getType(Uri uri) {
		
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int result = matcher.match(uri);
		if(result == ADD){
			String packname = values.getAsString("packname");
			dao.add(packname);
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int result = matcher.match(uri);
		if(result == DELETE){
			dao.delete(selectionArgs[0]);
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
