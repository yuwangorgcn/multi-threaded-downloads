package com.example.web.util;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;

public class NumberParser {
	/**
	 * 获取xml的手机归属地信息
	 * 
	 * @param is
	 * @return
	 */
	public static String getXmlInfo(InputStream is) {

		try {
			// android下使用pull解析器
			// 1.获取pull解析器的实例
			XmlPullParser parser = Xml.newPullParser();
			// 2.设置解析器的一些参数
			// 必须确定文件和eclipse中文件的properties都是同编码
			parser.setInput(is, "utf-8");
			// 获取pull解析器的事件类型
			int type = parser.getEventType();
			while (type != XmlPullParser.END_DOCUMENT) {
				switch (type) {
				case XmlPullParser.START_TAG:
					if ("getMobileCodeInfoResult".equals(parser.getName())) {
						return parser.nextText();
					}
				}

				type = parser.next();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "查无此号";

	}
}
