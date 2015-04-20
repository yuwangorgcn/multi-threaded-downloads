package com.example.web.util;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;

public class NumberParser {
	/**
	 * ��ȡxml���ֻ���������Ϣ
	 * 
	 * @param is
	 * @return
	 */
	public static String getXmlInfo(InputStream is) {

		try {
			// android��ʹ��pull������
			// 1.��ȡpull��������ʵ��
			XmlPullParser parser = Xml.newPullParser();
			// 2.���ý�������һЩ����
			// ����ȷ���ļ���eclipse���ļ���properties����ͬ����
			parser.setInput(is, "utf-8");
			// ��ȡpull���������¼�����
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
		return "���޴˺�";

	}
}
