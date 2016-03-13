package com.wj.security.engine;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.wj.security.domain.UpdateInfo;

import android.util.Log;
import android.util.Xml;

public class UpdateInfoParser {
	/**
	 * 
	 * 解析xml数据
	 */
	public static UpdateInfo getUpdateInfo(InputStream is) throws XmlPullParserException,IOException
	{
		XmlPullParser parser= Xml.newPullParser();
		parser.setInput(is,"UTF-8");
		UpdateInfo info = new UpdateInfo();
		Log.i("UpdateInfo","UpdateInfo");
		
		int type = parser.getEventType();
		while(type!=XmlPullParser.END_DOCUMENT)
		{
			if(type == XmlPullParser.START_TAG)
			{
				if("version".equals(parser.getName()))
				{
					String version = parser.nextText();
					info.setVersion(version);
				}else if("description".equals(parser.getName()))
				{
					String description = parser.nextText();
					info.setDescription(description);
				}else if("apkurl".equals(parser.getName()))
				{
					String apkurl = parser.nextText();
					info.setApkurl(apkurl);
				}
			}
			type = parser.next();
		}
		return info;
	}

}
