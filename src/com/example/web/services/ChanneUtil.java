package com.example.web.services;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class ChanneUtil {
	/**
	 * ��ȡ������Ƶ����Ϣ���ػ�����������
	 * 
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public static InputStream getChanneStream(String address) throws Exception {
		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("if-modifyed-sice", System.currentTimeMillis()
				+ "");
		int code = conn.getResponseCode();
		if (code == 200) {
			return conn.getInputStream();
		}
		return null;
	}
}
