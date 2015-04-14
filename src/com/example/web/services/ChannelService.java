package com.example.web.services;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.example.web.domain.Channel;

public class ChannelService {
	public static List<Channel> getChannels(InputStream is) throws Exception {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");
		List<Channel> ChannelList = new ArrayList<Channel>();
		int type = parser.getEventType();
		Channel channel = null;

		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("channel".equals(parser.getName())) {

					channel = new Channel();
					String id = parser.getAttributeValue(0);
					channel.setId(id);
				} else if ("name".equals(parser.getName())) {

					String name = parser.nextText();
					channel.setName(name);
				} else if ("time".equals(parser.getName())) {

					String time = parser.nextText();
					channel.setTime(time);

				} else if ("icon".equals(parser.getName())) {

					String icon = parser.nextText();
					channel.setIcon(icon);

				} else if ("count".equals(parser.getName())) {

					int count = Integer.parseInt(parser.nextText());
					channel.setCount(count);

				}
				break;

			case XmlPullParser.END_TAG:
				if ("channel".equals(parser.getName())) {
					ChannelList.add(channel);
					// 防止以后有数据的重复
					channel = null;
				}
				break;
			}
			// 解析下一个节点
			type = parser.next();
		}
		return ChannelList;
	}
}
