package com.example.web.services;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.web.util.StreamTools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtil {
	/**
	 * 获取网路address地址对应的图片,搭配ansycTesk
	 * 
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public static Bitmap getImage(String address) throws Exception {
		// 通过代码模拟浏览器访问图片的流程

		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		// 5秒
		conn.setConnectTimeout(5000);
		// 获取服务器返回的流
		InputStream is = conn.getInputStream();

		byte[] imageBytes = StreamTools.getBytes(is);
		Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0,
				imageBytes.length);

		return bitmap;
	}
}
