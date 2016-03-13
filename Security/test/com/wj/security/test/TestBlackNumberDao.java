package com.wj.security.test;

import java.util.List;
import java.util.Random;

import com.wj.security.db.dao.BlackNumberDao;
import com.wj.security.domain.BlackNumber;

import android.test.AndroidTestCase;

public class TestBlackNumberDao extends AndroidTestCase{
	
	public void testAdd() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		int number = 100;
		Random random = new Random();
		for(int i=0;i<50;i++){
			int result = (number+i);
			dao.add(result+"", random.nextInt(3)+"");
		}
	}
	
	public void testFindAll() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		List<BlackNumber> numbers = dao.findAll();
		for(BlackNumber number:numbers){
			System.out.println(number.getNumber());
		}
	}

}
