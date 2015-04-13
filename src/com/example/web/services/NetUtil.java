package com.example.web.services;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.web.util.StreamTools;

public class NetUtil {
	public static String getHtml(String address) throws Exception {
		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 5秒
		conn.setReadTimeout(5000);
		conn.setRequestMethod("GET");

		int code = conn.getResponseCode();
		if (code == 200) {
			InputStream is = conn.getInputStream();
			byte[] result = StreamTools.getBytes(is);
			// 智能浏览，可处理乱码
			String temp = new String(result);
			if (temp.contains("gb2312")) {
				return new String(result, "gb2312");
			} else if (temp.contains("gbk")) {
				return new String(result, "gbk");
				// 以此类推，添加多种编码支持
			} else {
				return temp;
			}

		} else {
			throw new IllegalStateException("访问网路失败");
		}
	}
}
