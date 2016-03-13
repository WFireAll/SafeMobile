package com.wj.security.test;

import com.wj.security.db.BlackNumberDBOpenHelper;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public class TestCreatDB extends AndroidTestCase{
    public void testCreateDB() throws Exception{
    	BlackNumberDBOpenHelper helper = new BlackNumberDBOpenHelper(getContext());
    	SQLiteDatabase db = helper.getWritableDatabase();
    }
}
