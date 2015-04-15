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
	 * ��ȡ��·address��ַ��Ӧ��ͼƬ,����ansycTesk
	 * 
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public static Bitmap getImage(String address) throws Exception {
		// ͨ������ģ�����������ͼƬ������

		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		// 5��
		conn.setConnectTimeout(5000);
		// ��ȡ���������ص���
		InputStream is = conn.getInputStream();

		byte[] imageBytes = StreamTools.getBytes(is);
		Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0,
				imageBytes.length);

		return bitmap;
	}
}
