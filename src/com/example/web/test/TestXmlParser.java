package com.example.web.test;

import java.io.InputStream;
import java.util.List;

import com.example.web.domain.Channel;
import com.example.web.services.ChanneUtil;
import com.example.web.services.ChannelService;

import android.test.AndroidTestCase;

public class TestXmlParser extends AndroidTestCase {
	public void testGetChannels() throws Exception {
		InputStream is = ChanneUtil
				.getChanneStream("http://192.168.1.101:8080/WebProject/info.xml");
		List<Channel> channelList = ChannelService.getChannels(is);
		for (Channel channel : channelList) {
			System.out.println(channel.getIcon());
		}
	}

}
